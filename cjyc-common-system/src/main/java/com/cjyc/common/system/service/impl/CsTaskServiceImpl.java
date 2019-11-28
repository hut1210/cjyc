package com.cjyc.common.system.service.impl;

import com.cjkj.common.redis.lock.RedisDistributedLock;
import com.cjyc.common.model.FailResultReasonVo;
import com.cjyc.common.model.dao.*;
import com.cjyc.common.model.dto.driver.task.ReplenishInfoDto;
import com.cjyc.common.model.dto.web.task.*;
import com.cjyc.common.model.entity.*;
import com.cjyc.common.model.enums.CaptchaTypeEnum;
import com.cjyc.common.model.enums.CarStorageTypeEnum;
import com.cjyc.common.model.enums.UserTypeEnum;
import com.cjyc.common.model.enums.order.OrderCarStateEnum;
import com.cjyc.common.model.enums.order.OrderStateEnum;
import com.cjyc.common.model.enums.task.TaskStateEnum;
import com.cjyc.common.model.enums.waybill.WaybillCarStateEnum;
import com.cjyc.common.model.enums.waybill.WaybillStateEnum;
import com.cjyc.common.model.enums.waybill.WaybillTypeEnum;
import com.cjyc.common.model.exception.ServerException;
import com.cjyc.common.model.keys.RedisKeys;
import com.cjyc.common.model.util.BaseResultUtil;
import com.cjyc.common.model.util.StringUtil;
import com.cjyc.common.model.vo.ResultReasonVo;
import com.cjyc.common.model.vo.ResultVo;
import com.cjyc.common.model.vo.web.OrderCarVo;
import com.cjyc.common.system.service.*;
import com.cjyc.common.system.util.RedisUtils;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.*;

/**
 *
 * @author JPG
 */
@Service
public class CsTaskServiceImpl implements ICsTaskService {


    @Resource
    private ITaskDao taskDao;
    @Resource
    private ITaskCarDao taskCarDao;
    @Resource
    private IWaybillDao waybillDao;
    @Resource
    private IWaybillCarDao waybillCarDao;
    @Resource
    private IOrderDao orderDao;
    @Resource
    private IOrderCarDao orderCarDao;
    @Resource
    private IVehicleRunningDao vehicleRunningDao;
    @Resource
    private RedisDistributedLock redisLock;
    @Resource
    private ICsSendNoService sendNoService;
    @Resource
    private RedisUtils redisUtil;
    @Resource
    private ICsDriverService csDriverService;
    @Resource
    private ICsStorageLogService csStorageLogService;
    @Resource
    private ICsSmsService csSmsService;

    @Override
    public String getTaskNo(String waybillNo) {
        String taskNo = null;
        String lockKey = RedisKeys.getNewTaskNoKey(waybillNo);
        if(!redisLock.lock(lockKey, 30000, 100, 300)){
            return null;
        }
        String maxNo = taskDao.findMaxNo(waybillNo);
        if(maxNo == null){
            taskNo = sendNoService.formatNo(waybillNo, 1, 3);
        }else{
            String[] split = maxNo.split("-");
            taskNo = sendNoService.formatNo(waybillNo, (Integer.valueOf(split[1]) + 1), 3);
        }
        return taskNo;
    }

    @Override
    public ResultVo allot(AllotTaskDto paramsDto) {
        Long waybillId = paramsDto.getWaybillId();
        Long driverId = paramsDto.getDriverId();
        List<Long> waybillCarIdList = paramsDto.getWaybillCarIdList();
        Set<String> lockKeySet = new HashSet<>();
        try {
            //验证运单状态
            Waybill waybill = waybillDao.selectById(waybillId);
            if (waybill == null
                    || waybill.getState() < WaybillStateEnum.WAIT_ALLOT.code
                    || waybill.getState() > WaybillStateEnum.FINISHED.code) {
                return BaseResultUtil.fail("当前运单的状态，无法分配任务");
            }
            List<WaybillCar> list = waybillCarDao.findVoByIds(waybillCarIdList);
            if (list == null || list.isEmpty()) {
                return BaseResultUtil.fail("内容不能为空");
            }
            //验证司机信息
            Driver driver = csDriverService.getById(driverId, true);
            if (driver == null) {
                return BaseResultUtil.fail("司机不在运营中");
            }

            if(StringUtils.isNotBlank(waybill.getGuideLine())){
                if(waybill.getCarNum() != list.size()){
                    return BaseResultUtil.fail("此单不允许拆单分派");
                }
            }

            Task task = new Task();
            //计算任务编号
            task.setNo("");
            task.setWaybillId(waybill.getId());
            task.setWaybillNo(waybill.getNo());
            task.setGuideLine(paramsDto.getGuideLine());
            task.setCarNum(paramsDto.getWaybillCarIdList().size());
            task.setState(TaskStateEnum.WAIT_ALLOT_CONFIRM.code);
            task.setDriverId(driver.getId());
            task.setDriverPhone(driver.getPhone());
            task.setDriverName(driver.getName());
            task.setRemark(paramsDto.getRemark());
            task.setCreateTime(System.currentTimeMillis());
            task.setCreateUser(paramsDto.getLoginName());
            task.setCreateUserId(paramsDto.getLoginId());
            taskDao.insert(task);

            int noCount = 0;
            for (WaybillCar waybillCar : list) {
                if (waybillCar == null) {
                    continue;
                }
                //加锁
                String lockKey = RedisKeys.getAllotTaskKey(waybillCar.getOrderCarNo());
                if (!redisLock.lock(lockKey, 20000, 100, 300)) {
                    throw new ServerException("当前运单的状态，无法分配任务");
                }
                lockKeySet.add(lockKey);

                //验证运单车辆状态
                if (waybillCar.getState() > WaybillCarStateEnum.WAIT_ALLOT.code) {
                    throw new ServerException("当前运单车辆的状态，无法分配任务");
                }

                TaskCar taskCar = new TaskCar();
                taskCar.setTaskId(task.getId());
                taskCar.setWaybillCarId(waybillCar.getId());
                taskCarDao.insert(taskCar);
                noCount++;

                //更新运单车辆状态
                if(waybillCar.getState() < WaybillCarStateEnum.ALLOTED.code){
                    waybillCarDao.updateForAllotDriver(waybillCar.getId());
                }
            }
            if (noCount != task.getCarNum()) {
                task.setCarNum(noCount);
                taskDao.updateById(task);
            }
            return BaseResultUtil.success();
        } finally {
            redisUtil.delete(lockKeySet);
        }

    }

    @Override
    public ResultVo<ResultReasonVo> load(LoadTaskDto paramsDto) {
        ResultReasonVo resultReasonVo = new ResultReasonVo();
        Set<FailResultReasonVo> failCarNoSet = Sets.newHashSet();
        Set<String> successSet = Sets.newHashSet();

        Task task = taskDao.selectById(paramsDto.getTaskId());
        if(task == null){
            return BaseResultUtil.fail("任务不存在");
        }
        if(StringUtils.isBlank(task.getVehiclePlateNo())){
            return BaseResultUtil.fail("请先完善车牌号");
        }
        //验证运单
        Waybill waybill = waybillDao.selectById(task.getWaybillId());
        if(waybill == null){
            return BaseResultUtil.fail("运单不存在");
        }
        if(waybill.getState() >= WaybillStateEnum.FINISHED.code || waybill.getState() <= WaybillStateEnum.WAIT_ALLOT_CONFIRM.code ){
            return BaseResultUtil.fail("运单已完结");
        }

        //组装数据
        int orderCarNewState;
        int waybillCarNewState;
        if (waybill.getType() == WaybillTypeEnum.PICK.code) {
            //提车任务、自送到业务中心
            waybillCarNewState = WaybillCarStateEnum.LOADED.code;
            orderCarNewState = OrderCarStateEnum.PICKING.code;
        } else if (waybill.getType() == WaybillTypeEnum.TRUNK.code) {
            //干线、提干、干送、提干送任务
            waybillCarNewState = WaybillCarStateEnum.WAIT_LOAD_TURN.code;
            orderCarNewState = OrderCarStateEnum.TRUNKING.code;
        } else if (waybill.getType() == WaybillTypeEnum.BACK.code) {
            //送车、自提
            waybillCarNewState = WaybillCarStateEnum.WAIT_LOAD_TURN.code;
            orderCarNewState = OrderCarStateEnum.BACKING.code;
        } else {
            return BaseResultUtil.fail("未识别的任务类型");
        }


        int count = 0;
        long currentTimeMillis = System.currentTimeMillis();
        Set<Long> orderIdSet = Sets.newHashSet();
        Set<Long> orderCarIdSet = Sets.newHashSet();
        Set<Long> waybillCarIdSet = Sets.newHashSet();
        for (Long taskCarId : paramsDto.getTaskCarIdList()) {
            WaybillCar waybillCar = waybillCarDao.findByTaskCarId(taskCarId);
            if (waybillCar == null) {
                failCarNoSet.add(new FailResultReasonVo(taskCarId, "任务ID为{0}对应的运单车辆不存在", taskCarId));
                continue;
            }
            if (waybillCar.getState() >= WaybillCarStateEnum.WAIT_LOAD_TURN.code) {
                failCarNoSet.add(new FailResultReasonVo(waybillCar.getOrderCarId(), "运单车辆已经装过车"));
                continue;
            }
            //验证车辆当前所在地是否与出发城市匹配
            OrderCar orderCar = orderCarDao.selectById(waybillCar.getOrderCarId());
            if(orderCar == null ){
                failCarNoSet.add(new FailResultReasonVo(waybillCar.getOrderCarNo(), "订单车辆不存在"));
                continue;
            }
            if(!waybillCar.getStartStoreId().equals(orderCar.getNowStoreId())){
                failCarNoSet.add(new FailResultReasonVo(waybillCar.getOrderCarNo(), "订单车辆尚未到达始发地业务中心范围内"));
            }

            //验证运单车辆信息是否完全
            if(!validateOrderCarInfo(orderCar)){
                failCarNoSet.add(new FailResultReasonVo(waybillCar.getOrderCarNo(), "订单车辆信息不完整"));
                continue;
            }
            if(!validateWaybillCarInfo(waybillCar)){
                failCarNoSet.add(new FailResultReasonVo(waybillCar.getOrderCarNo(), "运单车辆信息不完整"));
                continue;
            }

            waybillCar.setLoadTime(currentTimeMillis);
            //订单
            orderIdSet.add(orderCar.getOrderId());
            //订单车辆
            if(orderCar.getState() < orderCarNewState){
                orderCarIdSet.add(orderCar.getId());
            }
            //运单车辆
            if (waybillCar.getState() < waybillCarNewState) {
                waybillCarIdSet.add(waybillCar.getId());
            }
            successSet.add(orderCar.getNo());
            count++;
        }
        //更新运单车辆状态
        waybillCarDao.updateBatchForLoad(waybillCarIdSet, waybillCarNewState, System.currentTimeMillis());
        //更新空车位数
        vehicleRunningDao.updateOccupiedNumForLoad(paramsDto.getLoginId(), count);
        //更新订单车辆状态
        orderCarDao.updateStateForLoad(orderCarNewState, orderCarIdSet);
        //更新订单状态
        if(!CollectionUtils.isEmpty(orderIdSet)){
            orderDao.updateStateForLoad(OrderStateEnum.TRANSPORTING.code, orderIdSet);
        }

        //TODO 给客户发送消息
        //TODO 写物流轨迹
        resultReasonVo.setSuccessList(successSet);
        resultReasonVo.setFailList(failCarNoSet);
        return BaseResultUtil.success(resultReasonVo);
    }

    private boolean validateWaybillCarInfo(WaybillCar waybillCar) {
        if(waybillCar == null){
            return false;
        }
        if(StringUtils.isBlank(waybillCar.getLoadPhotoImg())){
            return false;
        }

        if(waybillCar.getLoadPhotoImg().split(",").length < 8){
            return false;
        }
        return true;
    }

    private boolean validateOrderCarInfo(OrderCar orderCar) {
        if(orderCar == null){
            return false;
        }
        if(StringUtils.isBlank(orderCar.getVin())){
            return false;
        }
        if(StringUtils.isBlank(orderCar.getPlateNo())){
            return false;
        }
        return true;
    }

    @Override
    public ResultVo<ResultReasonVo> unload(UnLoadTaskDto paramsDto) {
        ResultReasonVo resultReasonVo = new ResultReasonVo();
        Set<FailResultReasonVo> failCarNoSet = Sets.newHashSet();
        Set<String> successSet = Sets.newHashSet();


        Task task = taskDao.selectById(paramsDto.getTaskId());
        if(task == null){
            return BaseResultUtil.fail("任务不存在");
        }
        //验证运单
        Waybill waybill = waybillDao.selectById(task.getWaybillId());
        if(waybill == null){
            return BaseResultUtil.fail("运单不存在");
        }
        if(waybill.getState() >= WaybillStateEnum.FINISHED.code || waybill.getState() <= WaybillStateEnum.WAIT_ALLOT_CONFIRM.code ){
            return BaseResultUtil.fail("运单已完结");
        }

        int count = 0;
        Set<Long> waybillCarIdSet = Sets.newHashSet();
        for (Long taskCarId : paramsDto.getTaskCarIdList()) {
            if (taskCarId == null) {
                continue;
            }
            WaybillCar waybillCar = waybillCarDao.findByTaskCarId(taskCarId);
            if (waybillCar == null) {
                failCarNoSet.add(new FailResultReasonVo(taskCarId, "任务ID为{0}对应的运单车辆不存在", taskCarId));
                continue;
            }
            if (waybillCar.getState() >= WaybillCarStateEnum.UNLOADED.code) {
                failCarNoSet.add(new FailResultReasonVo(waybillCar.getOrderCarNo(), "运单车辆已经装过车", taskCarId));
                continue;
            }

            //卸车时间以收车人确认收货为准
            //waybillCar.setUnloadTime(currentTimeMillis);
            waybillCarIdSet.add(waybillCar.getId());

            count++;
        }

        if (CollectionUtils.isEmpty(waybillCarIdSet)) {
            return BaseResultUtil.fail("没有可以卸车的运单");
        }
        //更新运单车辆信息
        waybillCarDao.updateBatchForUnload(waybillCarIdSet, WaybillCarStateEnum.UNLOADED.code);
        //更新任务信息
        taskDao.updateForUnload(task.getId(), count);
        //更新实时运力信息
        vehicleRunningDao.updateOccupiedNumForUnload(task.getId(), count);
        //TODO 发送收车推送信息
        resultReasonVo.setSuccessList(successSet);
        resultReasonVo.setFailList(failCarNoSet);
        return BaseResultUtil.success(resultReasonVo);
    }

    @Override
    public ResultVo<ResultReasonVo> outStore(OutStoreTaskDto paramsDto) {
        ResultReasonVo resultReasonVo = new ResultReasonVo();
        Set<FailResultReasonVo> failCarNoSet = Sets.newHashSet();
        Set<String> successSet = Sets.newHashSet();
        //验证任务
        Task task = taskDao.selectById(paramsDto.getTaskId());
        if(task == null){
            return BaseResultUtil.fail("任务不存在");
        }
        //验证运单
        Waybill waybill = waybillDao.selectById(task.getWaybillId());
        if(waybill == null){
            return BaseResultUtil.fail("运单不存在");
        }
        if(waybill.getState() >= WaybillStateEnum.FINISHED.code || waybill.getState() <= WaybillStateEnum.WAIT_ALLOT_CONFIRM.code ){
            return BaseResultUtil.fail("运单已完结");
        }

        int count = 0;
        long currentTimeMillis = System.currentTimeMillis();
        Set<Long> waybillCarIdSet = Sets.newHashSet();
        Set<CarStorageLog> storageLogSet = Sets.newHashSet();
        if (CollectionUtils.isEmpty(paramsDto.getTaskCarIdList())) {
            return BaseResultUtil.fail("车辆不能为空");
        }
        for (Long taskCarId : paramsDto.getTaskCarIdList()) {
            WaybillCar waybillCar = waybillCarDao.findByTaskCarId(taskCarId);
            if (waybillCar == null) {
                failCarNoSet.add(new FailResultReasonVo(taskCarId, "任务ID为{0}对应的运单车辆不存在", taskCarId));
                continue;
            }
            if (waybillCar.getState() != WaybillCarStateEnum.WAIT_LOAD_TURN.code) {
                failCarNoSet.add(new FailResultReasonVo(waybillCar.getOrderCarNo(), "运单车辆状态不能出库"));
                continue;
            }
            //验证车辆当前所在地是否与出发城市匹配
            OrderCar orderCar = orderCarDao.selectById(waybillCar.getOrderCarId());
            if(orderCar == null ){
                failCarNoSet.add(new FailResultReasonVo(waybillCar.getOrderCarNo(), "订单车辆不存在"));
                continue;
            }
            if(!waybillCar.getStartStoreId().equals(orderCar.getNowStoreId())){
                failCarNoSet.add(new FailResultReasonVo(waybillCar.getOrderCarNo(), "订单车辆尚未到达始发地业务中心范围内"));
            }


            waybillCarIdSet.add(waybillCar.getId());
            successSet.add(orderCar.getNo());
            //业务中心装车
            if(waybillCar.getStartStoreId() > 0){
                //出库日志
                CarStorageLog carStorageLog = CarStorageLog.builder()
                        .type(CarStorageTypeEnum.OUT.code)
                        .orderNo(orderCar.getOrderNo())
                        .orderCarNo(orderCar.getNo())
                        .vin(orderCar.getVin())
                        .brand(orderCar.getBrand())
                        .model(orderCar.getModel())
                        .freight(waybillCar.getFreightFee())
                        .carryType(waybill.getCarrierType())
                        .carrierId(waybill.getCarrierId())
                        .carrier(waybill.getCarrierName())
                        .driver(task.getDriverName())
                        .driverId(task.getDriverId())
                        .drvierPhone(task.getDriverPhone())
                        .vehiclePlateNo(task.getVehiclePlateNo())
                        .createTime(currentTimeMillis)
                        .createUserId(paramsDto.getLoginId())
                        .createUser(paramsDto.getLoginName())
                        .build();
                storageLogSet.add(carStorageLog);
            }
            //更新车辆状态
            count++;
        }

        //修改运单车辆
        if(CollectionUtils.isEmpty(waybillCarIdSet)){
            return BaseResultUtil.fail("没有可以出库的车辆");
        }
        waybillCarDao.updateStateBatchByIds(waybillCarIdSet, WaybillCarStateEnum.LOADED.code);
        //添加出库日志
        if(!CollectionUtils.isEmpty(storageLogSet)){
            csStorageLogService.asyncSaveBatch(storageLogSet);
        }
        //TODO 添加物流日志
        //推送消息
        resultReasonVo.setFailList(failCarNoSet);
        resultReasonVo.setSuccessList(successSet);
        return BaseResultUtil.success(resultReasonVo);
    }

    @Override
    public ResultVo<ResultReasonVo> inStore(InStoreTaskDto paramsDto) {

        ResultReasonVo resultReasonVo = new ResultReasonVo();
        Set<FailResultReasonVo> failCarNoSet = Sets.newHashSet();
        Set<String> successSet = Sets.newHashSet();

        //验证任务
        Task task = taskDao.selectById(paramsDto.getTaskId());
        if(task == null){
            return BaseResultUtil.fail("任务不存在");
        }
        //验证运单
        Waybill waybill = waybillDao.selectById(task.getWaybillId());
        if(waybill == null){
            return BaseResultUtil.fail("运单不存在");
        }
        if(waybill.getState() >= WaybillStateEnum.FINISHED.code || waybill.getState() <= WaybillStateEnum.WAIT_ALLOT_CONFIRM.code ){
            return BaseResultUtil.fail("运单已完结");
        }

        int count = 0;
        long currentTimeMillis = System.currentTimeMillis();
        Set<Long> waybillCarIdSet = Sets.newHashSet();
        Set<CarStorageLog> storageLogSet = Sets.newHashSet();
        if (CollectionUtils.isEmpty(paramsDto.getTaskCarIdList())) {
            return BaseResultUtil.fail("车辆不能为空");
        }
        for (Long taskCarId : paramsDto.getTaskCarIdList()) {
            if (taskCarId == null) {
                continue;
            }
            WaybillCar waybillCar = waybillCarDao.findByTaskCarId(taskCarId);
            if (waybillCar == null) {
                failCarNoSet.add(new FailResultReasonVo(taskCarId, "任务ID为{0}对应的运单车辆不存在", taskCarId));
                continue;
            }
            if (waybillCar.getState() > WaybillCarStateEnum.UNLOADED.code) {
                failCarNoSet.add(new FailResultReasonVo(waybillCar.getOrderCarNo(), "车辆已经卸过车"));
                continue;
            }
            String endAddress = getFullAddress(waybillCar.getEndProvince(), waybillCar.getEndCity(), waybillCar.getEndArea(), waybillCar.getEndAddress());
            //验证卸车目的地
            if (waybillCar.getEndStoreId() == null) {
                failCarNoSet.add(new FailResultReasonVo(waybillCar.getOrderCarNo(), "车辆目的地不在业务中心", endAddress));
                continue;
            }

            //验证车辆是否到达目的地
            Long orderCarId = waybillCar.getOrderCarId();
            OrderCarVo orderCarVo = orderCarDao.findExtraById(orderCarId);
            if (orderCarVo == null) {
                failCarNoSet.add(new FailResultReasonVo(waybillCar.getOrderCarNo(), "车辆不存在"));
                continue;
            }
            //当前状态
            int state = orderCarVo.getState();
            int newState = 0;
            //下一段是否调度
            int m = waybillCarDao.countByStartCityAndOrderCar(waybillCar.getEndCityCode(), waybillCar.getEndStoreId());
            //计算是否到达目的地城市范围
            if (waybillCar.getEndCityCode().equals(orderCarVo.getEndCityCode())) {
                //配送
                newState = OrderCarStateEnum.WAIT_BACK_DISPATCH.code;
                if (m > 0) {
                    //配送已调度
                    newState = OrderCarStateEnum.WAIT_BACK.code;
                }
            } else {
                //干线
                newState = OrderCarStateEnum.WAIT_TRUNK_DISPATCH.code;
                if (m > 0) {
                    //干线已调度
                    newState = OrderCarStateEnum.WAIT_TRUNK.code;
                }
            }

            //更新运单车辆状态
            waybillCarDao.updateStateById(waybillCar.getId(), WaybillCarStateEnum.CONFIRMED.code);

            //更新车辆状态和所在位置
            OrderCar orderCar = new OrderCar();
            orderCar.setId(orderCarId);
            orderCar.setState(newState);
            orderCar.setNowStoreId(waybillCar.getEndStoreId());
            orderCar.setNowAreaCode(waybillCar.getEndAreaCode());
            orderCarDao.updateById(orderCar);


            //添加入库日志
            CarStorageLog carStorageLog = CarStorageLog.builder()
                    .type(CarStorageTypeEnum.OUT.code)
                    .orderNo(orderCar.getOrderNo())
                    .orderCarNo(orderCar.getNo())
                    .vin(orderCar.getVin())
                    .brand(orderCar.getBrand())
                    .model(orderCar.getModel())
                    .freight(waybillCar.getFreightFee())
                    .carryType(waybill.getCarrierType())
                    .carrierId(waybill.getCarrierId())
                    .carrier(waybill.getCarrierName())
                    .driver(task.getDriverName())
                    .driverId(task.getDriverId())
                    .drvierPhone(task.getDriverPhone())
                    .vehiclePlateNo(task.getVehiclePlateNo())
                    .createTime(currentTimeMillis)
                    .createUserId(paramsDto.getLoginId())
                    .createUser(paramsDto.getLoginName())
                    .build();
            storageLogSet.add(carStorageLog);

            count++;
            //TODO 添加物流日志
            //推送消息
        }

        //验证任务是否完成
        int row = taskCarDao.countUnFinishByTaskId(paramsDto.getTaskId());
        if(row == 0){
            //更新任务状态
            taskDao.updateStateById(task.getId(), TaskStateEnum.FINISHED.code);
            //验证运单是否完成
            int n = waybillCarDao.countUnFinishByWaybillId(task.getWaybillId());
            if(n == 0){
                //更新运单状态
                waybillDao.updateStateById(WaybillStateEnum.FINISHED.code, waybill.getId());
            }
        }

        //写入入库日志
        if(!CollectionUtils.isEmpty(storageLogSet)){
            csStorageLogService.asyncSaveBatch(storageLogSet);
        }
        resultReasonVo.setSuccessList(successSet);
        resultReasonVo.setFailList(failCarNoSet);
        return BaseResultUtil.success(resultReasonVo);
    }

    @Override
    public ResultVo receipt(ReceiptTaskDto paramsDto) {
        //返回内容
        ResultReasonVo resultReasonVo = new ResultReasonVo();
        Set<FailResultReasonVo> failCarNoSet = Sets.newHashSet();
        Set<String> successSet = Sets.newHashSet();


        Task task = taskDao.selectById(paramsDto.getTaskId());
        if(task == null){
            return BaseResultUtil.fail("任务不存在");
        }
        //验证运单
        Waybill waybill = waybillDao.selectById(task.getWaybillId());
        if(waybill == null){
            return BaseResultUtil.fail("运单不存在");
        }
        if(waybill.getState() >= WaybillStateEnum.FINISHED.code || waybill.getState() <= WaybillStateEnum.WAIT_ALLOT_CONFIRM.code ){
            return BaseResultUtil.fail("运单已完结");
        }

        int count = 0;
        Set<Long> waybillCarIdSet = Sets.newHashSet();
        List<String> customerPhoneList = Lists.newArrayList();
        Set<Long> orderSet = Sets.newHashSet();
        for (Long taskCarId : paramsDto.getTaskCarIdList()) {
            if (taskCarId == null) {
                continue;
            }
            WaybillCar waybillCar = waybillCarDao.findByTaskCarId(taskCarId);
            if (waybillCar == null) {
                failCarNoSet.add(new FailResultReasonVo(taskCarId, "任务ID为{0}对应的运单车辆不存在", taskCarId));
                continue;
            }
            if (waybillCar.getState() < WaybillCarStateEnum.LOADED.code) {
                failCarNoSet.add(new FailResultReasonVo(waybillCar.getOrderCarNo(), "运单车辆还未装车", taskCarId));
                continue;
            }
            if (waybillCar.getState() >= WaybillCarStateEnum.CONFIRMED.code) {
                failCarNoSet.add(new FailResultReasonVo(waybillCar.getOrderCarNo(), "运单车辆已经签收过", taskCarId));
                continue;
            }
            Order order = orderDao.findByCarId(waybillCar.getOrderCarId());
            if(order != null && StringUtils.isNotBlank(order.getBackContactPhone()) && !customerPhoneList.contains(order.getBackContactPhone())){
                customerPhoneList.add(order.getBackContactPhone());
            }


            waybillCarIdSet.add(waybillCar.getId());
            orderSet.add(order.getId());
            count++;
        }
        if(customerPhoneList.size() > 1){
            return BaseResultUtil.fail("批量收车不能同时包含多个收车人订单");
        }

        boolean flag = csSmsService.validateCaptcha(customerPhoneList.get(0), paramsDto.getCaptcha(), CaptchaTypeEnum.CONFIRM_RECEIPT, paramsDto.getClientEnum());
        if(!flag){
            return BaseResultUtil.fail("收车码错误");
        }

        if (CollectionUtils.isEmpty(waybillCarIdSet)) {
            return BaseResultUtil.fail("没有可以卸车的车辆");
        }
        //验证任务是否完成
        int row = taskCarDao.countUnFinishByTaskId(paramsDto.getTaskId());
        if(row == 0){
            //更新任务状态
            taskDao.updateStateById(task.getId(), TaskStateEnum.FINISHED.code);
            //验证运单是否完成
            int n = waybillCarDao.countUnFinishByWaybillId(task.getWaybillId());
            if(n == 0){
                //更新运单状态
                waybillDao.updateStateById(WaybillStateEnum.FINISHED.code, waybill.getId());
            }
        }

        //验证订单是否完成
        orderSet.forEach(orderId -> {
            int rows = orderCarDao.countUnFinishByOrderId(orderId);
            if(rows <= 0){
                //更新订单状态
                orderDao.updateForReceipt(orderId, System.currentTimeMillis());
            }
        });


        //TODO 发送收车推送信息
        resultReasonVo.setSuccessList(successSet);
        resultReasonVo.setFailList(failCarNoSet);
        return BaseResultUtil.success(resultReasonVo);
    }

    private String getFullAddress(String endProvince, String endCity, String endArea, String endAddress) {
       return  (endProvince == null ? "" : endProvince)
                + (endCity == null ? "" : endCity)
                + (endArea == null ? "" : endArea)
                + (endAddress == null ? "" : endAddress);
    }
}
