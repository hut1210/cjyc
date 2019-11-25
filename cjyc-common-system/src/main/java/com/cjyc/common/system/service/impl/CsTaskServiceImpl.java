package com.cjyc.common.system.service.impl;

import com.cjkj.common.redis.lock.RedisDistributedLock;
import com.cjyc.common.model.dao.*;
import com.cjyc.common.model.dto.web.task.AllotTaskDto;
import com.cjyc.common.model.dto.web.task.LoadTaskDto;
import com.cjyc.common.model.dto.web.task.UnLoadTaskDto;
import com.cjyc.common.model.entity.*;
import com.cjyc.common.model.enums.CarStorageTypeEnum;
import com.cjyc.common.model.enums.order.OrderCarStateEnum;
import com.cjyc.common.model.enums.order.OrderStateEnum;
import com.cjyc.common.model.enums.task.TaskStateEnum;
import com.cjyc.common.model.enums.waybill.WaybillCarStateEnum;
import com.cjyc.common.model.enums.waybill.WaybillStateEnum;
import com.cjyc.common.model.enums.waybill.WaybillTypeEnum;
import com.cjyc.common.model.exception.ServerException;
import com.cjyc.common.model.keys.RedisKeys;
import com.cjyc.common.model.util.BaseResultUtil;
import com.cjyc.common.model.vo.ResultVo;
import com.cjyc.common.system.service.ICsDriverService;
import com.cjyc.common.system.service.ICsSendNoService;
import com.cjyc.common.system.service.ICsStorageLogService;
import com.cjyc.common.system.service.ICsTaskService;
import com.cjyc.common.system.util.RedisUtils;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

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
            task.setCarNum(paramsDto.getWaybillCarIdList().size());
            task.setState(TaskStateEnum.WAIT_ALLOT_CONFIRM.code);
            task.setDriverId(driver.getId());
            task.setDriverPhone(driver.getPhone());
            task.setDriverName(driver.getName());
            task.setCreateTime(System.currentTimeMillis());
            task.setCreateUser(paramsDto.getUserName());
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
    public ResultVo load(LoadTaskDto paramsDto) {
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
        Set<CarStorageLog> storageLogSet = Sets.newHashSet();
        for (Long taskCarId : paramsDto.getTaskCarIdList()) {
            WaybillCar waybillCar = waybillCarDao.findByTaskCarId(taskCarId);
            if (waybillCar == null) {
                return BaseResultUtil.fail("ID：{0}运单车辆不存在", taskCarId);
            }
            if (waybillCar.getState() > WaybillCarStateEnum.LOADED.code) {
                return BaseResultUtil.fail("No:{0}运单车辆已经装过车", waybillCar.getOrderCarId());
            }
            //验证车辆当前所在地是否与出发城市匹配
            OrderCar orderCar = orderCarDao.selectById(waybillCar.getOrderCarId());
            if(orderCar == null ){
                return BaseResultUtil.fail("No:{0}订单车辆不存在", waybillCar.getOrderCarId());
            }
            if(!waybillCar.getStartStoreId().equals(orderCar.getNowStoreId())){
                return BaseResultUtil.fail("No:{0}订单车辆尚未到达始发地业务中心范围内", waybillCar.getOrderCarId());
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
            //业务中心装车
            if(waybillCar.getStartStoreId() > 0){
                storageLogSet.add(carStorageLog);
            }
            count++;
        }
        //更新运单车辆状态
        waybillCarDao.updateStateForLoad(waybillCarNewState, waybillCarIdSet);
        //更新空车位数
        vehicleRunningDao.updateOccupiedNumForLoad(paramsDto.getLoginId(), count);
        //更新订单车辆状态
        orderCarDao.updateStateForLoad(orderCarNewState, orderCarIdSet);
        //更新订单状态
        if(!CollectionUtils.isEmpty(orderIdSet)){
            orderDao.updateStateForLoad(OrderStateEnum.TRANSPORTING.code, orderIdSet);
        }
        //添加出库日志
        if(!CollectionUtils.isEmpty(storageLogSet)){
            csStorageLogService.asyncSaveBatch(storageLogSet);
        }

        //TODO 给客户发送消息
        //TODO 写物流轨迹
        return BaseResultUtil.success();
    }

    @Override
    public ResultVo unload(UnLoadTaskDto paramsDto) {
        //返回内容
        Map<String, Object> failCarNoMap = Maps.newHashMap();


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
                failCarNoMap.put(taskCarId.toString(), "运单车辆不存在");
                continue;
            }
            if (waybillCar.getState() >= WaybillCarStateEnum.UNLOADED.code) {
                failCarNoMap.put(waybillCar.getOrderCarNo(), "车辆已经卸过车");
                continue;
            }
            //卸车时间以收车人确认收货为准
            //waybillCar.setUnloadTime(currentTimeMillis);
            waybillCarIdSet.add(waybillCar.getId());

            count++;
        }

        if (CollectionUtils.isEmpty(waybillCarIdSet)) {
            return BaseResultUtil.fail(failCarNoMap);
        }
        //更新运单车辆信息
        if(CollectionUtils.isEmpty(waybillCarIdSet)){
            return BaseResultUtil.fail("没有可以卸车的车辆");
        }
        waybillCarDao.updateInfoBatchForUnload(waybillCarIdSet, WaybillCarStateEnum.UNLOADED.code);
        //更新任务信息
        taskDao.updateForUnload(task.getId(), count);
        //更新实时运力信息
        //TODO 发送收车推送信息

        return BaseResultUtil.success(failCarNoMap);
    }
}
