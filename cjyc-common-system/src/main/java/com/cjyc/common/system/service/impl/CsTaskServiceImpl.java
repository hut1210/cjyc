package com.cjyc.common.system.service.impl;

import com.alibaba.fastjson.JSON;
import com.cjkj.common.redis.lock.RedisDistributedLock;
import com.cjkj.log.monitor.LogUtil;
import com.cjyc.common.model.constant.Constant;
import com.cjyc.common.model.dao.*;
import com.cjyc.common.model.dto.customer.order.ReceiptBatchDto;
import com.cjyc.common.model.dto.driver.task.ReplenishInfoDto;
import com.cjyc.common.model.dto.web.task.*;
import com.cjyc.common.model.entity.*;
import com.cjyc.common.model.entity.defined.CarrierInfo;
import com.cjyc.common.model.entity.defined.UserInfo;
import com.cjyc.common.model.enums.*;
import com.cjyc.common.model.enums.log.OrderCarLogEnum;
import com.cjyc.common.model.enums.log.OrderLogEnum;
import com.cjyc.common.model.enums.order.OrderCarStateEnum;
import com.cjyc.common.model.enums.order.OrderStateEnum;
import com.cjyc.common.model.enums.task.TaskStateEnum;
import com.cjyc.common.model.enums.transport.CarrierTypeEnum;
import com.cjyc.common.model.enums.waybill.WaybillCarStateEnum;
import com.cjyc.common.model.enums.waybill.WaybillCarrierTypeEnum;
import com.cjyc.common.model.enums.waybill.WaybillStateEnum;
import com.cjyc.common.model.enums.waybill.WaybillTypeEnum;
import com.cjyc.common.model.exception.ParameterException;
import com.cjyc.common.model.exception.ServerException;
import com.cjyc.common.model.keys.RedisKeys;
import com.cjyc.common.model.util.BaseResultUtil;
import com.cjyc.common.model.vo.FailResultReasonVo;
import com.cjyc.common.model.vo.ResultReasonVo;
import com.cjyc.common.model.vo.ResultVo;
import com.cjyc.common.model.vo.driver.mine.BankCardVo;
import com.cjyc.common.system.service.*;
import com.cjyc.common.system.util.RedisUtils;
import com.google.common.base.Joiner;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.text.MessageFormat;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author JPG
 */
@Slf4j
@Service
@Transactional(rollbackFor = RuntimeException.class)
public class CsTaskServiceImpl implements ICsTaskService {

    @Resource
    private ITaskDao taskDao;
    @Resource
    private ITaskCarDao taskCarDao;
    @Resource
    private IWaybillDao waybillDao;
    @Resource
    private ICsWaybillService csWaybillService;
    @Resource
    private IWaybillCarDao waybillCarDao;
    @Resource
    private IOrderDao orderDao;
    @Resource
    private IOrderCarDao orderCarDao;
    @Resource
    private ICsOrderCarLogService csOrderCarLogService;
    @Resource
    private IVehicleRunningDao vehicleRunningDao;
    @Resource
    private IBankCardBindDao bankCardBindDao;
    @Resource
    private IUserRoleDeptDao userRoleDeptDao;
    @Resource
    private RedisDistributedLock redisLock;
    @Resource
    private ICsSendNoService sendNoService;
    @Resource
    private RedisUtils redisUtil;
    @Resource
    private ICsDriverService csDriverService;
    @Autowired
    private ICsStorageLogService csStorageLogService;
    @Resource
    private ICsStoreService csStoreService;
    @Resource
    private ICsOrderLogService csOrderLogService;
    @Resource
    private ICsPingPayService csPingPayService;

    /**
     * 获取任务编号
     *
     * @param waybillNo
     * @author JPG
     * @since 2019/12/9 18:10
     */
    @Override
    public String getTaskNo(String waybillNo) {
        String taskNo;
        String lockKey = RedisKeys.getNewTaskNoKey(waybillNo);
        if (!redisLock.lock(lockKey, 30000, 100, 300)) {
            throw new ServerException("获取任务编号失败");
        }
        String maxNo = taskDao.findMaxNo(waybillNo);
        if (maxNo == null) {
            taskNo = sendNoService.formatNo(waybillNo, 1, 3);
        } else {
            String[] split = maxNo.split("-");
            taskNo = sendNoService.formatNo(waybillNo, (Integer.valueOf(split[1]) + 1), 3);
        }
        return taskNo;
    }
    /**
     * 完善提车信息
     *
     * @param reqDto
     * @author JPG
     * @since 2019/12/9 18:10
     */
    @Override
    public ResultVo replenishInfo(ReplenishInfoDto reqDto) {
        WaybillCar waybillCar = waybillCarDao.findByTaskCarId(reqDto.getTaskCarId());
        if (waybillCar == null) {
            return BaseResultUtil.fail("运单车辆不存在");
        }
        Waybill waybill = waybillDao.selectById(waybillCar.getWaybillId());
        if (waybill == null) {
            return BaseResultUtil.fail("运单不存在");
        }
        List<String> loadPhotoImgs = reqDto.getLoadPhotoImgs();
        List<String> unloadPhotoImgs = reqDto.getUnloadPhotoImgs();
        //查询车辆照片是否上传过
        String photo = waybillCarDao.findUploadPhoto(waybillCar.getOrderCarId());
        if(StringUtils.isBlank(photo) || photo.split(",").length < Constant.MIN_LOAD_PHOTO_NUM){
            if((loadPhotoImgs == null || loadPhotoImgs.size() < Constant.MIN_LOAD_PHOTO_NUM) && (unloadPhotoImgs == null || unloadPhotoImgs.size() < Constant.MIN_LOAD_PHOTO_NUM)){
                return BaseResultUtil.fail("第一次完善信息至少上传8张照片");
            }
        }

        if (!CollectionUtils.isEmpty(loadPhotoImgs)) {
            if (loadPhotoImgs.size() > Constant.MAX_LOAD_PHOTO_NUM) {
                return BaseResultUtil.fail("照片数量不能超过20张");
            }
        }
        if (!CollectionUtils.isEmpty(unloadPhotoImgs)) {
            if (unloadPhotoImgs.size() > Constant.MAX_LOAD_PHOTO_NUM) {
                return BaseResultUtil.fail("照片数量不能超过20张");
            }
        }

        //更新车辆信息
        OrderCar orderCar = new OrderCar();
        orderCar.setId(waybillCar.getOrderCarId());
        orderCar.setVin(reqDto.getVin());
        orderCar.setBrand(reqDto.getBrand());
        orderCar.setModel(reqDto.getModel());
        orderCar.setPlateNo(reqDto.getPlateNo());
        orderCarDao.updateById(orderCar);
        //更新运单车辆信息
        if (!CollectionUtils.isEmpty(loadPhotoImgs)) {
            waybillCarDao.updateForLoadReplenishInfo(waybillCar.getId(), Joiner.on(",").join(loadPhotoImgs));
        }
        if (!CollectionUtils.isEmpty(unloadPhotoImgs)){
            waybillCarDao.updateForUnloadReplenishInfo(waybillCar.getId(), Joiner.on(",").join(unloadPhotoImgs));
        }
        return BaseResultUtil.success();
    }

    @Override
    public ResultVo<ResultReasonVo> loadForLocal(ReplenishInfoDto paramsDto) {
        //提车上传信息
        ResultVo resultVo = replenishInfo(paramsDto);
        if (ResultEnum.SUCCESS.getCode() != resultVo.getCode()) {
            return BaseResultUtil.fail(resultVo.getMsg());
        }
        //提车
        TaskCar taskCar = taskCarDao.selectById(paramsDto.getTaskCarId());
        LoadTaskDto loadTaskDto = new LoadTaskDto();
        BeanUtils.copyProperties(paramsDto, loadTaskDto);
        loadTaskDto.setTaskId(taskCar.getTaskId());
        loadTaskDto.setTaskCarIdList(Lists.newArrayList(taskCar.getId()));

        return load(loadTaskDto);
    }

    @Override
    public void reCreate(Waybill waybill, List<WaybillCar> waybillCarList, List<WaybillCar> newWaybillCarList, CarrierInfo carrierInfo) {
        /**4、插入任务车辆关联表*/
        Set<Long> waybillCarIds = waybillCarList.stream().map(WaybillCar::getId).collect(Collectors.toSet());
        Task task;
        if (carrierInfo.isReAllotCarrier()) {
            //删除历史任务
            taskDao.cancelBywaybillId(waybill.getId());
            if (carrierInfo.getCarrierType() != CarrierTypeEnum.ENTERPRISE.code) {
                task = new Task();
                //只有一个司机添加任务信息
                //获取编号
                task.setNo(getTaskNo(waybill.getNo()));
                task.setWaybillId(waybill.getId());
                task.setWaybillNo(waybill.getNo());
                task.setGuideLine(waybill.getGuideLine());
                task.setCarNum(waybillCarList.size());
                task.setState(waybill.getCarrierType() == WaybillCarrierTypeEnum.SELF.code ? TaskStateEnum.TRANSPORTING.code : TaskStateEnum.WAIT_LOAD.code);
                task.setDriverId(carrierInfo.getDriverId());
                task.setDriverName(carrierInfo.getCarrierName());
                task.setDriverPhone(carrierInfo.getDriverPhone());
                //添加运力信息
                task.setVehicleRunningId(carrierInfo.getVehicleId());
                task.setVehiclePlateNo(carrierInfo.getVehiclePlateNo());
                task.setCreateTime(System.currentTimeMillis());
                task.setCreateUser(waybill.getCreateUser());
                task.setCreateUserId(waybill.getCreateUserId());
                taskDao.insert(task);
                //添加任务车辆信息
                taskCarDao.saveBatchByTaskIdAndWaybillCarIds(task.getId(), waybillCarIds);
            }
        } else {
            if (carrierInfo.getCarrierType() != CarrierTypeEnum.ENTERPRISE.code) {
                task = taskDao.findTopByWaybillId(waybill.getId());
                Task nt = new Task();
                nt.setId(task.getId());
                nt.setCarNum(waybillCarList.size());
                taskDao.updateById(nt);
                //添加任务车辆信息
                if(!CollectionUtils.isEmpty(newWaybillCarList)){
                    List<Long> collect = newWaybillCarList.stream().map(WaybillCar::getId).collect(Collectors.toList());
                    taskCarDao.saveBatchByTaskIdAndWaybillCarIds(task.getId(), collect);
                }

            }
        }
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
            List<WaybillCar> list = null;
            if (CollectionUtils.isEmpty(waybillCarIdList)) {
                //如果未指定车辆列表，则查询所有未分配的车辆
                list = waybillCarDao.findUnAllotListByWaybillId(waybillId);
            } else {

                list = waybillCarDao.findVoByIds(waybillCarIdList);
            }
            if (CollectionUtils.isEmpty(list)) {
                return BaseResultUtil.fail("内容不能为空");
            }
            //验证司机信息
            Driver driver = csDriverService.getById(driverId, true);
            if (driver == null) {
                return BaseResultUtil.fail("司机不存在");
            }
            if (driver.getBusinessState() != BizStateEnum.BUSINESS.code) {
                return BaseResultUtil.fail("司机不在运营中");
            }
            //判断司机绑定银行卡
            List<Long> carrierIds = userRoleDeptDao.findDeptIds(driverId);
            if (CollectionUtils.isEmpty(carrierIds)){
                return BaseResultUtil.fail("数据有误,司机不存在");
            }
            List<BankCardVo> binkCardInfoList = bankCardBindDao.findBinkCardInfo(carrierIds.get(0));
            if (CollectionUtils.isEmpty(binkCardInfoList)) {
                return BaseResultUtil.fail("所选承运商没有绑定或者没有可用的银行卡");
            }
            //验证司机运力信息
            VehicleRunning vr = vehicleRunningDao.findByDriverId(driverId);
            if (vr == null || vr.getState() == null || vr.getState() != 1) {
                return BaseResultUtil.fail("司机尚未绑定车牌号");
            }

            Task task = new Task();
            //计算任务编号
            task.setNo(getTaskNo(waybill.getNo()));
            task.setWaybillId(waybill.getId());
            task.setWaybillNo(waybill.getNo());
            task.setGuideLine(paramsDto.getGuideLine());
            task.setCarNum(list.size());
            task.setState(TaskStateEnum.WAIT_LOAD.code);
            task.setDriverId(driver.getId());
            task.setDriverPhone(driver.getPhone());
            task.setDriverName(driver.getName());
            //查询实时运力
            task.setVehiclePlateNo(vr.getPlateNo());
            task.setVehicleRunningId(vr.getId());
            task.setRemark(paramsDto.getRemark());
            task.setCreateTime(System.currentTimeMillis());
            task.setCreateUser(paramsDto.getLoginName());
            task.setCreateUserId(paramsDto.getLoginId());
            if(!CollectionUtils.isEmpty(list)){
                task.setGuideLine(csWaybillService.computeGuideLine(list.get(0).getStartAreaCode(), list.get(0).getEndAreaCode(), paramsDto.getGuideLine(), list.size()));
            }

            taskDao.insert(task);

            for (WaybillCar waybillCar : list) {
                if (waybillCar == null) {
                    continue;
                }
                //加锁
                String lockKey = RedisKeys.getAllotTaskKey(waybillCar.getId());
                if (!redisLock.lock(lockKey, 20000, 100, 300)) {
                    throw new ServerException("当前运单车辆{0}其他人正在分配，", waybillCar.getOrderCarNo());
                }
                lockKeySet.add(lockKey);

                //验证运单车辆状态
                if (waybillCar.getState() > WaybillCarStateEnum.WAIT_ALLOT.code) {
                    throw new ServerException("当前运单车辆{0}的状态，无法分配任务", waybillCar.getOrderCarNo());
                }

                //验证是否存在任务明细
                int n = taskDao.countByTaskIdAndWaybillCarId(task.getId(), waybillCar.getId());
                if (n > 0) {
                    throw new ServerException("当前运单车辆{0}, 已经分配过", waybillCar.getOrderCarNo());
                }

                TaskCar taskCar = new TaskCar();
                taskCar.setTaskId(task.getId());
                taskCar.setWaybillCarId(waybillCar.getId());
                taskCarDao.insert(taskCar);

                //更新运单车辆状态
                if (waybillCar.getState() < WaybillCarStateEnum.WAIT_LOAD.code) {
                    waybillCarDao.updateForAllotDriver(waybillCar.getId());
                }
            }
            return BaseResultUtil.success();
        } finally {
            redisUtil.delete(lockKeySet);
        }

    }

    @Override
    public ResultVo<ResultReasonVo> load(LoadTaskDto paramsDto) {
        Task task = taskDao.selectById(paramsDto.getTaskId());
        if (task == null) {
            return BaseResultUtil.fail("任务不存在");
        }
        //验证司机是否匹配
        if (!paramsDto.getLoginId().equals(task.getDriverId())) {
            return BaseResultUtil.fail("不是自己的任务");
        }
        //验证运单
        Waybill waybill = waybillDao.selectById(task.getWaybillId());
        if (waybill == null) {
            return BaseResultUtil.fail("运单不存在");
        }
        if (waybill.getState() >= WaybillStateEnum.FINISHED.code || waybill.getState() < WaybillStateEnum.ALLOT_CONFIRM.code) {
            return BaseResultUtil.fail("运单已完结");
        }

        //批量处理数据
        int count = 0;
        long currentTimeMillis = System.currentTimeMillis();
        Set<Long> orderIdSet = Sets.newHashSet();
        Set<CarStorageLog> storageLogSet = Sets.newHashSet();
        for (Long taskCarId : paramsDto.getTaskCarIdList()) {
            WaybillCar waybillCar = waybillCarDao.findByTaskCarId(taskCarId);
            if (waybillCar == null) {
                throw new ParameterException("运单车辆不存在");
            }
            String orderCarNo = waybillCar.getOrderCarNo();
            if (waybillCar.getState() >= WaybillCarStateEnum.WAIT_LOAD_CONFIRM.code) {
                throw new ParameterException("车辆{0}已经装过车", orderCarNo);
            }
            //验证是否上传过照片
            String photo = waybillCarDao.findUploadPhoto(waybillCar.getOrderCarId());
            if(StringUtils.isBlank(photo) || photo.split(",").length < Constant.MIN_LOAD_PHOTO_NUM){
                throw new ParameterException("车辆{0}尚未上传照片,至少上传8张照片", orderCarNo);
            }
            //验证车辆当前所在地是否与出发区县匹配
            OrderCar orderCar = orderCarDao.selectById(waybillCar.getOrderCarId());
            if (orderCar == null) {
                throw new ParameterException("订单车辆{0}不存在", orderCarNo);
            }
            //验证目的地业务中心是否与当前业务中心匹配
            if (waybillCar.getStartStoreId() != null && waybillCar.getStartStoreId() != 0) {
                if (!waybillCar.getStartStoreId().equals(orderCar.getNowStoreId())) {
                    throw new ParameterException("订单车辆{0}未入库，请业务员先将车辆入库", orderCarNo);
                }
            } else {
                if (!waybillCar.getStartAreaCode().equals(orderCar.getNowAreaCode())) {
                    throw new ParameterException("订单车辆{0}尚未到达提车地址区县范围内", orderCarNo);
                }
            }
            //验证运单车辆信息是否完全
            if (!validateOrderCarInfo(orderCar)) {
                throw new ParameterException("订单车辆{0}信息不完整", orderCarNo);
            }

            //运单和车辆状态
            int waybillCarNewState = WaybillCarStateEnum.WAIT_LOAD_CONFIRM.code;
            int orderCarNewState = orderCar.getState();
            if (waybill.getType() == WaybillTypeEnum.PICK.code) {
                //提车任务、自送到业务中心
                orderCarNewState = OrderCarStateEnum.WAIT_PICK.code;
            } else if (waybill.getType() == WaybillTypeEnum.TRUNK.code) {
                //干线、提干、干送、提干送任务
                orderCarNewState = OrderCarStateEnum.WAIT_TRUNK.code;
            } else if (waybill.getType() == WaybillTypeEnum.BACK.code) {
                //送车、自提
                orderCarNewState = OrderCarStateEnum.WAIT_BACK.code;
            }

            boolean isNotFromStore = waybillCar.getStartStoreId() == null || waybillCar.getStartStoreId() <= 0;
            //运单不经过业务中心,无出入库确认操作
            if (isNotFromStore || paramsDto.getLoginType() == UserTypeEnum.ADMIN) {
                waybillCarNewState = WaybillCarStateEnum.LOADED.code;
                if (waybill.getType() == WaybillTypeEnum.PICK.code) {
                    //提车任务、自送到业务中心
                    orderCarNewState = OrderCarStateEnum.PICKING.code;
                } else if (waybill.getType() == WaybillTypeEnum.TRUNK.code) {
                    //干线、提干、干送、提干送任务
                    orderCarNewState = OrderCarStateEnum.TRUNKING.code;
                } else if (waybill.getType() == WaybillTypeEnum.BACK.code) {
                    //送车、自提
                    orderCarNewState = OrderCarStateEnum.BACKING.code;
                }

                if(isNotFromStore){
                    // 出库记录
                    CarStorageLog carStorageLog = CarStorageLog.builder()
                            .storeId(waybillCar.getStartStoreId())
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

                    //非业务中心提车，变更订单运输状态
                    orderIdSet.add(orderCar.getOrderId());
                }
            }

            waybillCarDao.updateForLoad(waybillCar.getId(), waybillCarNewState, currentTimeMillis);
            //订单车辆
            if (orderCar.getState() < orderCarNewState) {
                orderCarDao.updateStateById(orderCarNewState, orderCar.getId());
            }

            if(isNotFromStore){
                //装车日志
                csOrderCarLogService.asyncSave(orderCar, OrderCarLogEnum.LOAD,
                        new String[]{MessageFormat.format(OrderCarLogEnum.LOAD.getOutterLog(), getAddress(waybillCar.getStartProvince(), waybillCar.getStartCity(), waybillCar.getStartArea(), waybillCar.getStartAddress())),
                                MessageFormat.format(OrderCarLogEnum.LOAD.getInnerLog(), getAddress(waybillCar.getStartProvince(), waybillCar.getStartCity(), waybillCar.getStartArea(), waybillCar.getStartAddress())), paramsDto.getLoginName(), paramsDto.getLoginPhone()},
                        new UserInfo(paramsDto.getLoginId(), paramsDto.getLoginName(), paramsDto.getLoginPhone(), paramsDto.getLoginType()));
            }else{
                //出库日志
                csOrderCarLogService.asyncSave(orderCar, OrderCarLogEnum.OUT_STORE,
                        new String[]{MessageFormat.format(OrderCarLogEnum.OUT_STORE.getOutterLog(), waybillCar.getStartStoreName()),
                                MessageFormat.format(OrderCarLogEnum.OUT_STORE.getInnerLog(), waybillCar.getStartStoreName(), paramsDto.getLoginName(), paramsDto.getLoginPhone())},
                        new UserInfo(paramsDto.getLoginId(), paramsDto.getLoginName(), paramsDto.getLoginPhone(), paramsDto.getLoginType()));
            }
            count++;
        }
        //更新订单状态
        if (!CollectionUtils.isEmpty(orderIdSet)) {
            orderDao.updateStateForLoad(OrderStateEnum.TRANSPORTING.code, orderIdSet);
            taskDao.updateForLoad(task.getId());
            taskDao.updateLoadNum(task.getId(), count);
            waybillDao.updateForLoad(waybill.getId());
            vehicleRunningDao.updateOccupiedNum(task.getVehicleRunningId());
            //添加出库日志
            csStorageLogService.asyncSaveBatch(storageLogSet);
        }

        return BaseResultUtil.success();
    }

    private String getAddress(String startProvince, String startCity, String startArea, String startAddress) {
        return (startProvince == null ? "" : startProvince) +
                (startCity == null ? "" : startCity) +
                (startArea == null ? "" : startArea) +
                (startAddress == null ? "" : startAddress);
    }

    private boolean validateWaybillCarInfo(WaybillCar waybillCar) {
        if (waybillCar == null) {
            return false;
        }
        if (StringUtils.isBlank(waybillCar.getLoadPhotoImg())) {
            return false;
        }
        return true;
    }

    private boolean validateOrderCarInfo(OrderCar orderCar) {
        if (orderCar == null) {
            return false;
        }
        if (StringUtils.isBlank(orderCar.getVin())) {
            return false;
        }
        return true;
    }

    @Override
    public ResultVo<ResultReasonVo> unload(UnLoadTaskDto paramsDto) {
        //返回内容
        ResultReasonVo resultReasonVo = new ResultReasonVo();
        Set<FailResultReasonVo> failCarNoSet = Sets.newHashSet();
        Set<String> successSet = Sets.newHashSet();

        Task task = taskDao.selectById(paramsDto.getTaskId());
        if (task == null) {
            return BaseResultUtil.fail("任务不存在");
        }
        //验证运单
        Waybill waybill = waybillDao.selectById(task.getWaybillId());
        if (waybill == null) {
            return BaseResultUtil.fail("运单不存在");
        }
        if (waybill.getState() >= WaybillStateEnum.FINISHED.code || waybill.getState() < WaybillStateEnum.ALLOT_CONFIRM.code) {
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
                throw new ParameterException("任务ID为{0}对应的运单车辆不存在", taskCarId);
            }
            if (waybillCar.getState() <= WaybillCarStateEnum.WAIT_LOAD.code) {
                throw new ParameterException("运单车辆{0}尚未装车", waybillCar.getOrderCarNo());
            }
            if (waybillCar.getState() < WaybillCarStateEnum.LOADED.code) {
                throw new ParameterException("运单车辆{0}尚未出库，请联系始发地业务中心业务员", waybillCar.getOrderCarNo());
            }
            if (waybillCar.getState() >= WaybillCarStateEnum.UNLOADED.code) {
                throw new ParameterException("运单车辆{0}已经卸过车", waybillCar.getOrderCarNo());
            }
            //验证车辆当前所在地是否与出发区县匹配
            OrderCar orderCar = orderCarDao.selectById(waybillCar.getOrderCarId());
            if (orderCar == null) {
                throw new ParameterException("订单车辆不存在");
            }
            //下一段是否调度
            Order order = orderDao.selectById(orderCar.getOrderId());
            if (order == null) {
                failCarNoSet.add(new FailResultReasonVo(waybillCar.getOrderCarNo(), "订单不存在"));
                continue;
            }
            boolean isInStore = false;
            if (waybillCar.getEndStoreId() == null || waybillCar.getEndStoreId() <= 0) {
                waybillCarDao.updateForFinish(waybillCar.getId());
                validateAndFinishTask(task.getId());
                csWaybillService.validateAndFinishWaybill(waybillCar.getWaybillId());
                OrderCar noc = new OrderCar();
                noc.setState(computeOrderCarStateForDirectUnload(waybillCar));
                noc.setId(orderCar.getId());
                noc.setNowStoreId(waybillCar.getEndStoreId());
                noc.setNowAreaCode(waybillCar.getEndAreaCode());
                noc.setNowUpdateTime(System.currentTimeMillis());
                orderCarDao.updateById(noc);
            } else if (paramsDto.getLoginType() == UserTypeEnum.ADMIN){

                waybillCarDao.updateForFinish(waybillCar.getId());
                validateAndFinishTask(task.getId());
                csWaybillService.validateAndFinishWaybill(waybillCar.getWaybillId());
                OrderCar noc = new OrderCar();
                noc.setState(computeOrderCarStateForDirectUnload(waybillCar));
                noc.setId(orderCar.getId());
                noc.setNowStoreId(waybillCar.getEndStoreId());
                noc.setNowAreaCode(waybillCar.getEndAreaCode());
                noc.setNowUpdateTime(System.currentTimeMillis());
                orderCarDao.updateById(noc);

                if(waybillCar.getEndStoreId() != null && waybillCar.getEndStoreId() > 0){
                    CarStorageLog carStorageLog = CarStorageLog.builder()
                            .storeId(waybillCar.getEndStoreId())
                            .type(CarStorageTypeEnum.IN.code)
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
                            .createTime(System.currentTimeMillis())
                            .createUserId(paramsDto.getLoginId())
                            .createUser(paramsDto.getLoginName())
                            .build();
                    csStorageLogService.asyncSave(carStorageLog);

                    isInStore = true;
                }

            } else {
                waybillCarDao.updateStateById(waybillCar.getId(), WaybillCarStateEnum.WAIT_UNLOAD_CONFIRM.code);
            }

            if(isInStore){
                //添加物流日志
                csOrderCarLogService.asyncSave(orderCar, OrderCarLogEnum.IN_STORE,
                        new String[]{MessageFormat.format(OrderCarLogEnum.IN_STORE.getOutterLog(), waybillCar.getEndStoreName()),
                                MessageFormat.format(OrderCarLogEnum.IN_STORE.getInnerLog(), waybillCar.getEndStoreName(), paramsDto.getLoginName(), paramsDto.getLoginPhone())},
                        new UserInfo(paramsDto.getLoginId(), paramsDto.getLoginName(), paramsDto.getLoginPhone(), paramsDto.getLoginType()));
            }else{
                csOrderCarLogService.asyncSave(orderCar, OrderCarLogEnum.UNLOAD,
                        new String[]{MessageFormat.format(OrderCarLogEnum.UNLOAD.getInnerLog(), getAddress(waybillCar.getStartProvince(), waybillCar.getStartCity(), waybillCar.getStartArea(), waybillCar.getStartAddress())),
                                MessageFormat.format(OrderCarLogEnum.UNLOAD.getOutterLog(), getAddress(waybillCar.getStartProvince(), waybillCar.getStartCity(), waybillCar.getStartArea(), waybillCar.getStartAddress()), paramsDto.getLoginName(), paramsDto.getLoginPhone())},
                        new UserInfo(paramsDto.getLoginId(), paramsDto.getLoginName(), paramsDto.getLoginPhone(), paramsDto.getLoginType()));
            }

            waybillCarIdSet.add(waybillCar.getId());
            count++;
        }




        //更新任务信息
        taskDao.updateUnloadNum(task.getId(), count);
        //更新实时运力信息
        vehicleRunningDao.updateOccupiedNum(task.getId());

        //TODO 发送收车推送信息
        resultReasonVo.setSuccessList(successSet);
        resultReasonVo.setFailList(failCarNoSet);
        return BaseResultUtil.success(resultReasonVo);
    }

    private Integer computeOrderCarStateForDirectUnload(WaybillCar waybillCar) {
        Order order = orderDao.findByCarId(waybillCar.getOrderCarId());
        //当前状态
        int newState;
        //下一段是否调度
        int m = waybillCarDao.countByStartAddress(waybillCar.getOrderCarId(), waybillCar.getEndAreaCode(),waybillCar.getEndAddress());
        //计算是否到达目的地城市范围
        if (order.getEndStoreId() != null && order.getEndStoreId().equals(waybillCar.getEndStoreId())) {
            //配送
            newState = m == 0 ? OrderCarStateEnum.WAIT_BACK_DISPATCH.code : OrderCarStateEnum.WAIT_BACK.code;
            //更新自提运单状态
            WaybillCar wc = waybillCarDao.findBackWaybill(waybillCar.getOrderCarId());
            if(wc != null){
                waybillCarDao.updateStateById(wc.getId(), WaybillCarStateEnum.WAIT_LOAD_CONFIRM.code);
            }
        } else {
            //干线
            newState = m == 0 ? OrderCarStateEnum.WAIT_TRUNK_DISPATCH.code : OrderCarStateEnum.WAIT_TRUNK.code;
        }
        return newState;
    }

    @Override
    public ResultVo inStoreForLocal(ReplenishInfoDto paramsDto) {
        //提车上传信息
        ResultVo resultVo = replenishInfo(paramsDto);
        if (ResultEnum.SUCCESS.getCode() != resultVo.getCode()) {
            return BaseResultUtil.fail(resultVo.getMsg());
        }
        //提车
        TaskCar taskCar = taskCarDao.selectById(paramsDto.getTaskCarId());
        InStoreTaskDto inStoreTaskDto = new InStoreTaskDto();
        BeanUtils.copyProperties(paramsDto, inStoreTaskDto);
        inStoreTaskDto.setTaskId(taskCar.getTaskId());
        inStoreTaskDto.setTaskCarIdList(Lists.newArrayList(taskCar.getId()));

        return inStore(inStoreTaskDto);
    }


    @Override
    public ResultVo<ResultReasonVo> inStore(InStoreTaskDto paramsDto) {
        //返回内容
        ResultReasonVo resultReasonVo = new ResultReasonVo();
        Set<FailResultReasonVo> failCarNoSet = Sets.newHashSet();
        Set<String> successSet = Sets.newHashSet();

        //验证任务
        Task task = taskDao.selectById(paramsDto.getTaskId());
        if (task == null) {
            return BaseResultUtil.fail("任务不存在");
        }
        //验证运单
        Waybill waybill = waybillDao.selectById(task.getWaybillId());
        if (waybill == null) {
            return BaseResultUtil.fail("运单不存在");
        }
        if (waybill.getState() >= WaybillStateEnum.FINISHED.code || waybill.getState() <= WaybillStateEnum.ALLOT_CONFIRM.code) {
            return BaseResultUtil.fail("运单未运输或已完结");
        }

        long currentTimeMillis = System.currentTimeMillis();
        Set<CarStorageLog> storageLogSet = Sets.newHashSet();
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
            //验证卸车目的地
            if (waybillCar.getEndStoreId() == null || waybillCar.getEndStoreId() <= 0) {
                failCarNoSet.add(new FailResultReasonVo(waybillCar.getOrderCarNo(), "车辆目的地不在业务中心, 不能入库"));
                continue;
            }

            //计算车辆状态
            OrderCar orderCar = orderCarDao.selectById(waybillCar.getOrderCarId());
            if (orderCar == null) {
                failCarNoSet.add(new FailResultReasonVo(waybillCar.getOrderCarNo(), "车辆不存在"));
                continue;
            }
            //当前状态
            //更新运单车辆状态
            waybillCarDao.updateForInStore(waybillCar.getId());
            Integer newState = computeOrderCarStateForDirectUnload(waybillCar);
            //更新车辆状态和所在位置
            OrderCar noc = new OrderCar();
            noc.setId(orderCar.getId());
            noc.setState(newState);
            noc.setNowStoreId(waybillCar.getEndStoreId());
            noc.setNowAreaCode(waybillCar.getEndAreaCode());
            noc.setNowUpdateTime(System.currentTimeMillis());
            orderCarDao.updateById(noc);

            //添加入库日志
            CarStorageLog carStorageLog = CarStorageLog.builder()
                    .storeId(waybillCar.getEndStoreId())
                    .type(CarStorageTypeEnum.IN.code)
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

            //添加物流日志
            csOrderCarLogService.asyncSave(orderCar, OrderCarLogEnum.IN_STORE,
                    new String[]{MessageFormat.format(OrderCarLogEnum.IN_STORE.getOutterLog(), waybillCar.getEndStoreName()),
                            MessageFormat.format(OrderCarLogEnum.IN_STORE.getInnerLog(), waybillCar.getEndStoreName(), paramsDto.getLoginName(), paramsDto.getLoginPhone())},
                    new UserInfo(paramsDto.getLoginId(), paramsDto.getLoginName(), paramsDto.getLoginPhone(), paramsDto.getLoginType()));
            successSet.add(waybillCar.getOrderCarNo());
        }
        resultReasonVo.setSuccessList(successSet);
        resultReasonVo.setFailList(failCarNoSet);
        if (CollectionUtils.isEmpty(successSet)) {
            BaseResultUtil.fail(resultReasonVo);
        }

        //验证任务和运单是否完成
        validateAndFinishTaskWaybill(task);

        //更新实时运力信息
        vehicleRunningDao.updateOccupiedNum(task.getId());

        //写入入库日志
        csStorageLogService.asyncSaveBatch(storageLogSet);

        return BaseResultUtil.success(resultReasonVo);
    }

    @Override
    public void validateAndFinishTask(Long taskId) {
        int count = taskCarDao.countUnFinishByTaskId(taskId);
        if (count > 0) {
            return;
        }
        taskDao.updateForFinish(taskId);
    }

    @Override
    public void validateAndFinishTaskWaybill(Task task) {
        if (task == null) {
            return;
        }
        int count = taskCarDao.countUnFinishByTaskId(task.getId());
        if (count <= 0) {
            taskDao.updateForFinish(task.getId());
            csWaybillService.validateAndFinishWaybill(task.getWaybillId());
        }
    }

    @Override
    public ResultVo<ResultReasonVo> outStore(OutStoreTaskDto paramsDto) {
        //返回内容
        ResultReasonVo resultReasonVo = new ResultReasonVo();
        Set<FailResultReasonVo> failCarNoSet = Sets.newHashSet();
        Set<String> successSet = Sets.newHashSet();
        UserInfo userInfo = new UserInfo(paramsDto.getLoginId(), paramsDto.getLoginName(), paramsDto.getLoginPhone(), paramsDto.getLoginType());

        //验证任务
        Task task = taskDao.selectById(paramsDto.getTaskId());
        if (task == null) {
            return BaseResultUtil.fail("任务不存在");
        }
        //验证运单
        Waybill waybill = waybillDao.selectById(task.getWaybillId());
        if (waybill == null) {
            return BaseResultUtil.fail("运单不存在");
        }
        if(waybill.getState() < WaybillStateEnum.ALLOT_CONFIRM.code){
            return BaseResultUtil.fail("运单尚未承接");
        }
        if (waybill.getState() >= WaybillStateEnum.FINISHED.code) {
            return BaseResultUtil.fail("运单已完结");
        }

        long currentTimeMillis = System.currentTimeMillis();
        Set<CarStorageLog> storageLogSet = Sets.newHashSet();
        if (CollectionUtils.isEmpty(paramsDto.getTaskCarIdList())) {
            return BaseResultUtil.fail("车辆不能为空");
        }
        Set<Long> orderIdSet = Sets.newHashSet();
        for (Long taskCarId : paramsDto.getTaskCarIdList()) {
            WaybillCar waybillCar = waybillCarDao.findByTaskCarId(taskCarId);
            if (waybillCar == null) {
                failCarNoSet.add(new FailResultReasonVo(taskCarId, "任务ID为{0}对应的运单车辆不存在", taskCarId));
                continue;
            }
            if (waybillCar.getState() != WaybillCarStateEnum.WAIT_LOAD_CONFIRM.code) {
                failCarNoSet.add(new FailResultReasonVo(waybillCar.getOrderCarNo(), "运单车辆状态不能出库"));
                continue;
            }
            //验证车辆当前所在业务中心是否与出发业务中心匹配
            OrderCar orderCar = orderCarDao.selectById(waybillCar.getOrderCarId());
            if (orderCar == null) {
                failCarNoSet.add(new FailResultReasonVo(waybillCar.getOrderCarNo(), "订单车辆不存在"));
                continue;
            }
/*            if (!waybillCar.getStartStoreId().equals(orderCar.getNowStoreId())) {
                failCarNoSet.add(new FailResultReasonVo(waybillCar.getOrderCarNo(), "订单车辆尚未到达始发地业务中心范围内"));
                continue;
            }*/
            //更新运单车辆状态
            waybillCarDao.updateStateById(waybillCar.getId(), WaybillCarStateEnum.LOADED.code);
            //更新车辆信息
            int orderCarNewState = orderCar.getState();
            if (waybill.getType() == WaybillTypeEnum.PICK.code) {
                //提车任务、自送到业务中心
                orderCarNewState = OrderCarStateEnum.PICKING.code;
            } else if (waybill.getType() == WaybillTypeEnum.TRUNK.code) {
                //干线、提干、干送、提干送任务
                orderCarNewState = OrderCarStateEnum.TRUNKING.code;
            } else if (waybill.getType() == WaybillTypeEnum.BACK.code) {
                //送车、自提
                orderCarNewState = OrderCarStateEnum.BACKING.code;
            }

            //更新车辆状态和所在位置
            OrderCar noc = new OrderCar();
            noc.setId(orderCar.getId());
            noc.setState(orderCarNewState);
            noc.setNowStoreId(0L);
            noc.setNowUpdateTime(System.currentTimeMillis());
            orderCarDao.updateById(noc);

            //出库日志
            CarStorageLog carStorageLog = CarStorageLog.builder()
                    .storeId(waybillCar.getStartStoreId())
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
            //出库日志
            csOrderCarLogService.asyncSave(orderCar, OrderCarLogEnum.OUT_STORE,
                    new String[]{MessageFormat.format(OrderCarLogEnum.OUT_STORE.getOutterLog(), waybillCar.getStartStoreName()),
                            MessageFormat.format(OrderCarLogEnum.OUT_STORE.getInnerLog(), waybillCar.getStartStoreName(), paramsDto.getLoginName(), paramsDto.getLoginPhone())},
                    new UserInfo(paramsDto.getLoginId(), paramsDto.getLoginName(), paramsDto.getLoginPhone(), paramsDto.getLoginType()));
            //提取数据
            successSet.add(orderCar.getNo());
            orderIdSet.add(orderCar.getOrderId());
        }
        //更新订单状态
        if (!CollectionUtils.isEmpty(orderIdSet)) {
            orderDao.updateStateForLoad(OrderStateEnum.TRANSPORTING.code, orderIdSet);
        }

        //更新实时运力信息
        vehicleRunningDao.updateOccupiedNum(task.getId());

        resultReasonVo.setFailList(failCarNoSet);
        resultReasonVo.setSuccessList(successSet);
        if (CollectionUtils.isEmpty(successSet)) {
            return BaseResultUtil.fail(resultReasonVo);
        }
        //添加出库日志
        csStorageLogService.asyncSaveBatch(storageLogSet);
        return BaseResultUtil.success(resultReasonVo);
    }

    private boolean validateIsArriveEndCity(Order order, WaybillCar waybillCar) {
        if (waybillCar == null) {
            return false;
        }
        //先验证是否到达所属业务中心
        if (order.getEndStoreId() != null) {
            List<Store> storeList = csStoreService.getBelongByAreaCode(waybillCar.getEndAreaCode());
            if (!CollectionUtils.isEmpty(storeList) && storeList.stream().map(Store::getId).collect(Collectors.toList()).contains(order.getEndStoreId())) {
                return true;
            }
        }
        //其次验证城市
        if (order.getEndCityCode().equals(waybillCar.getEndCityCode())) {
            return true;
        }
        return false;

    }

    @Override
    public ResultVo receipt(ReceiptTaskDto paramsDto) {
        //返回内容
        ResultReasonVo resultReasonVo = new ResultReasonVo();
        Set<FailResultReasonVo> failCarNoSet = Sets.newHashSet();
        Set<String> successSet = Sets.newHashSet();

        Task task = taskDao.selectById(paramsDto.getTaskId());
        if (task == null) {
            return BaseResultUtil.fail("任务不存在");
        }
        //验证运单
        Waybill waybill = waybillDao.selectById(task.getWaybillId());
        if (waybill == null) {
            return BaseResultUtil.fail("运单不存在");
        }
        if (waybill.getState() >= WaybillStateEnum.FINISHED.code || waybill.getState() <= WaybillStateEnum.ALLOT_CONFIRM.code) {
            return BaseResultUtil.fail("运单已完结");
        }

        int count = 0;
        Set<Long> waybillCarIdSet = Sets.newHashSet();
        Set<String> customerPhoneSet = Sets.newHashSet();
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
            if (waybill.getCarrierType() != WaybillCarrierTypeEnum.SELF.code && waybillCar.getState() < WaybillCarStateEnum.LOADED.code) {
                failCarNoSet.add(new FailResultReasonVo(waybillCar.getOrderCarNo(), "运单车辆还未装车", taskCarId));
                continue;
            }
            if (waybillCar.getState() >= WaybillCarStateEnum.UNLOADED.code) {
                failCarNoSet.add(new FailResultReasonVo(waybillCar.getOrderCarNo(), "运单车辆已经签收过", taskCarId));
                continue;
            }
            Order order = orderDao.findByCarId(waybillCar.getOrderCarId());
            if(waybill.getType() == WaybillTypeEnum.BACK.code && waybill.getCarrierType() == WaybillCarrierTypeEnum.SELF.code){
                waybillCarDao.updateSelfCarryForFinish(waybillCar.getId());
            }else{
                waybillCarDao.updateForFinish(waybillCar.getId());
            }
            orderCarDao.updateForFinish(waybillCar.getOrderCarId(), waybillCar.getEndAreaCode());

            //车辆日志
            OrderCar orderCar = orderCarDao.selectById(waybillCar.getOrderCarId());
            csOrderCarLogService.asyncSave(orderCar, OrderCarLogEnum.RECEIPT,
                    new String[]{OrderCarLogEnum.RECEIPT.getOutterLog(),
                            MessageFormat.format(OrderCarLogEnum.RECEIPT.getInnerLog(), paramsDto.getLoginName(), paramsDto.getLoginPhone())},
                    new UserInfo(paramsDto.getLoginId(), paramsDto.getLoginName(), paramsDto.getLoginPhone(), paramsDto.getLoginType()));

            customerPhoneSet.add(order.getBackContactPhone());
            waybillCarIdSet.add(waybillCar.getId());
            orderSet.add(order.getId());
            count++;
        }
        if (customerPhoneSet.size() > 1) {
            return BaseResultUtil.fail("批量收车不能同时包含多个收车人订单");
        }
        if (CollectionUtils.isEmpty(waybillCarIdSet)) {
            resultReasonVo.setSuccessList(successSet);
            resultReasonVo.setFailList(failCarNoSet);
            return BaseResultUtil.fail(resultReasonVo);
        }
        //验证任务是否完成
        validateAndFinishTaskWaybill(task);

        //验证订单是否完成
        UserInfo userInfo = new UserInfo(paramsDto.getLoginId(), paramsDto.getLoginName(), paramsDto.getLoginPhone(), paramsDto.getLoginType());
        orderSet.forEach(orderId -> validateAndFinishOrder(orderId, userInfo));

        //TODO 发送收车推送信息
        resultReasonVo.setSuccessList(successSet);
        resultReasonVo.setFailList(failCarNoSet);
        return BaseResultUtil.success(resultReasonVo);
    }

    @Override
    public ResultVo<ResultReasonVo> receiptBatch(ReceiptBatchDto paramsDto) {
        //返回内容
        ResultReasonVo resultReasonVo = new ResultReasonVo();
        Set<FailResultReasonVo> failCarNoSet = Sets.newHashSet();
        Set<String> successSet = Sets.newHashSet();

        Set<String> orderNos = Sets.newHashSet();
        Set<Long> orderIdSet = Sets.newHashSet();
        Set<Long> waybillCarIdSet = Sets.newHashSet();
        List<OrderCar> list = orderCarDao.findListByNos(paramsDto.getOrderCarNos());
        for (OrderCar orderCar : list) {
            if (orderCar == null) {
                continue;
            }
            String orderCarNo = orderCar.getNo();
            if (orderCar.getState() >= OrderCarStateEnum.SIGNED.code) {
                failCarNoSet.add(new FailResultReasonVo(orderCarNo, "车辆，已被签收"));
                continue;
            }
            if (PayStateEnum.PAID.code != orderCar.getWlPayState() && BigDecimal.ZERO.compareTo(orderCar.getTotalFee()) == 0) {
                failCarNoSet.add(new FailResultReasonVo(orderCarNo, "车辆，尚未支付"));
                continue;
            }
            //处理车辆相关运单和任务
            WaybillCar waybillCar = waybillCarDao.findWaitReceiptWaybill(orderCar.getId());
            if (waybillCar == null) {
                failCarNoSet.add(new FailResultReasonVo(orderCarNo, "车辆，尚未开始配送"));
                continue;
            }
            //处理车辆相关运单车辆
            waybillCarDao.updateForFinish(waybillCar.getId());
            //更新车辆状态
            orderCarDao.updateForFinish(orderCar.getId(), waybillCar.getEndAreaCode());

            //添加日志
            csOrderCarLogService.asyncSave(orderCar, OrderCarLogEnum.IN_STORE,
                    new String[]{MessageFormat.format(OrderCarLogEnum.IN_STORE.getInnerLog(), orderCar.getNo(), waybillCar.getEndStoreName()),
                            MessageFormat.format(OrderCarLogEnum.IN_STORE.getOutterLog(), orderCar.getNo(), waybillCar.getEndStoreName())},
                    new UserInfo(paramsDto.getLoginId(), paramsDto.getLoginName(), paramsDto.getLoginPhone(), paramsDto.getLoginType()));
            //提取数据
            orderIdSet.add(orderCar.getOrderId());
            waybillCarIdSet.add(waybillCar.getId());
            successSet.add(orderCar.getNo());
            orderNos.add(orderCar.getOrderNo());
        }
        if (orderIdSet.size() > 1) {
            throw new ParameterException("暂不支持跨订单批量签收");
        }
        //处理订单
        if (!CollectionUtils.isEmpty(orderIdSet)) {
            UserInfo userInfo = new UserInfo(paramsDto.getLoginId(), paramsDto.getLoginName(), paramsDto.getLoginPhone(), paramsDto.getLoginType());
            orderIdSet.forEach(orderId -> validateAndFinishOrder(orderId, userInfo));
        }

        //是否处理任务
        if (!CollectionUtils.isEmpty(waybillCarIdSet)) {
            List<Task> taskList = taskDao.findListByWaybillCarIds(waybillCarIdSet);
            taskList.forEach(this::validateAndFinishTaskWaybill);
        }

        resultReasonVo.setSuccessList(successSet);
        resultReasonVo.setFailList(failCarNoSet);
        LogUtil.debug(MessageFormat.format("订单{0}签收结果：{1}：", JSON.toJSONString(orderNos), JSON.toJSONString(resultReasonVo)));
        return BaseResultUtil.success(resultReasonVo);
    }

    private void validateAndFinishOrder(Long orderId, UserInfo userInfo) {
        int count = orderCarDao.countUnFinishByOrderId(orderId);
        log.info("validateAndFinishOrder count = "+count);
        if (count <= 0) {
            Order order = orderDao.selectById(orderId);
            orderDao.updateForFinish(orderId);
            //订单完成日志
            csOrderLogService.asyncSave(order, OrderLogEnum.FINISH,
                    new String[]{OrderLogEnum.FINISH.getOutterLog(),
                            MessageFormat.format(OrderLogEnum.FINISH.getInnerLog(), userInfo.getName(), userInfo.getPhone())},
                    userInfo);
            //支付合伙人服务费

            try {
                csPingPayService.allinpayToCooperator(order.getId());
            } catch (Exception e) {
                log.error("支付合伙人{}（ID{}）服务费失败", order.getCustomerName(), order.getCustomerId());
                log.error(e.getMessage(), e);
            }
        }
    }

    /**
     * 车辆完成更新订单、运单、任务状态
     *
     * @param orderCarNoList 车辆编号列表
     * @param userInfo
     * @author JPG
     * @date 2019/12/8 12:06
     */
    @Override
    public void updateForCarFinish(List<String> orderCarNoList, UserInfo userInfo) {
        log.info("车辆支付完成回调修改状态：{},操作人信息{}", JSON.toJSONString(orderCarNoList), JSON.toJSONString(userInfo));
        //返回内容
        Set<Long> orderIdSet = Sets.newHashSet();
        Set<Long> waybillCarIdSet = Sets.newHashSet();
        List<OrderCar> list = orderCarDao.findListByNos(orderCarNoList);

        log.info("updateForCarFinish list ="+list.toString());
        for (OrderCar orderCar : list) {
            if (orderCar.getState() >= OrderCarStateEnum.SIGNED.code) {
                continue;
            }

            //处理车辆相关运单车辆
            WaybillCar waybillCar = waybillCarDao.findWaitReceiptWaybill(orderCar.getId());
            if (waybillCar != null) {
                //更新车辆状态
                orderCarDao.updateForPaySuccess(orderCar.getId(), waybillCar.getEndAreaCode());

                waybillCarDao.updateForFinish(waybillCar.getId());
                //出库日志
                csOrderCarLogService.asyncSave(orderCar, OrderCarLogEnum.RECEIPT,
                        new String[]{OrderCarLogEnum.RECEIPT.getOutterLog(),
                                MessageFormat.format(OrderCarLogEnum.RECEIPT.getInnerLog(), userInfo.getName(), userInfo.getPhone())},
                        userInfo);
                waybillCarIdSet.add(waybillCar.getId());
            }else{
                //更新车辆状态
                orderCarDao.updateForPrePaySuccess(orderCar.getId());
            }
            //提取数据
            orderIdSet.add(orderCar.getOrderId());
        }

        //处理订单
        if (!CollectionUtils.isEmpty(orderIdSet)) {
            orderIdSet.forEach(orderId -> validateAndFinishOrder(orderId, userInfo));
        }
        //处理任务
        if(!CollectionUtils.isEmpty(waybillCarIdSet)){
            List<Task> taskList = taskDao.findListByWaybillCarIds(waybillCarIdSet);
            if (!CollectionUtils.isEmpty(taskList)) {
                taskList.forEach(this::validateAndFinishTaskWaybill);
            }
        }

    }

    @Override
    public void updateForTaskCarFinish(List<String> taskCarIdList, int payType, UserInfo userInfo) {

        Set<Long> taskIdSet = Sets.newHashSet();
        Set<Long> orderIdSet = Sets.newHashSet();
        Set<String> lockSet = Sets.newHashSet();
        for (String taskIdStr : taskCarIdList) {
            TaskCar taskCar = taskCarDao.selectById(Long.valueOf(taskIdStr));
            if (taskCar == null) {
                continue;
            }
            WaybillCar waybillCar = waybillCarDao.selectById(taskCar.getWaybillCarId());
            if (waybillCar == null) {
                continue;
            }
            OrderCar orderCar = orderCarDao.selectById(waybillCar.getOrderCarId());
            if (orderCar == null) {
                continue;
            }
            String lockKey = RedisKeys.getWlCollectPayLockKey(orderCar.getNo());
            lockSet.add(lockKey);

            //更新支付车辆状态
            if(payType == ChargeTypeEnum.COLLECT_PAY.getCode()){
                orderCarDao.updateForPaySuccess(orderCar.getId(), waybillCar.getEndAreaCode());
            }else{
                orderCarDao.updateForPrePaySuccess(orderCar.getId());
            }

            //更新运单车辆状态
            waybillCarDao.updateForFinish(waybillCar.getId());

            taskIdSet.add(taskCar.getTaskId());
            orderIdSet.add(orderCar.getOrderId());
        }
        taskIdSet.forEach(taskId -> {
            Task task = taskDao.selectById(taskId);
            validateAndFinishTaskWaybill(task);
        });
        orderIdSet.forEach(orderId -> validateAndFinishOrder(orderId, userInfo));

        //删除支付锁
        redisUtil.delete(lockSet);
    }

}
