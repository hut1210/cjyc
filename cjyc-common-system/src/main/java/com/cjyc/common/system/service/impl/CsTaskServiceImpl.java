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
import com.cjyc.common.model.entity.defined.*;
import com.cjyc.common.model.enums.*;
import com.cjyc.common.model.enums.log.OrderCarLogEnum;
import com.cjyc.common.model.enums.log.OrderLogEnum;
import com.cjyc.common.model.enums.message.PushMsgEnum;
import com.cjyc.common.model.enums.order.OrderCarStateEnum;
import com.cjyc.common.model.enums.order.OrderPickTypeEnum;
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
import com.cjyc.common.system.util.MiaoxinSmsUtil;
import com.cjyc.common.system.util.RedisUtils;
import com.google.common.base.Joiner;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
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
import java.util.*;
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
    private ICsOrderService csOrderService;
    @Resource
    private ICsPushMsgService csPushMsgService;
    @Resource
    private RedisUtils redisUtils;
    @Resource
    private ITradeBillDao tradeBillDao;
    @Resource
    private ICsAmqpService csAmqpService;

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
        if (StringUtils.isBlank(photo) || photo.split(",").length < Constant.MIN_LOAD_PHOTO_NUM) {
            if ((loadPhotoImgs == null || loadPhotoImgs.size() < Constant.MIN_LOAD_PHOTO_NUM) && (unloadPhotoImgs == null || unloadPhotoImgs.size() < Constant.MIN_LOAD_PHOTO_NUM)) {
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
        if (!CollectionUtils.isEmpty(unloadPhotoImgs)) {
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
        BaseTaskDto baseTaskDto = new BaseTaskDto();
        BeanUtils.copyProperties(paramsDto, baseTaskDto);
        baseTaskDto.setTaskId(taskCar.getTaskId());
        baseTaskDto.setTaskCarIdList(Lists.newArrayList(taskCar.getId()));

        return load(baseTaskDto);
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
                if (!CollectionUtils.isEmpty(newWaybillCarList)) {
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
        Set<String> lockSet = new HashSet<>();
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
            if (CollectionUtils.isEmpty(carrierIds)) {
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


            for (WaybillCar waybillCar : list) {
                //加锁
                String lockKey = RedisKeys.getAllotTaskKey(waybillCar.getId());
                if (!redisLock.lock(lockKey, 120000, 1, 100L)) {
                    throw new ServerException("运单车辆{0}正在分配, 请5秒后重试，", waybillCar.getOrderCarNo());
                }
                lockSet.add(lockKey);

                //验证运单车辆状态
                if (waybillCar.getState() > WaybillCarStateEnum.WAIT_ALLOT.code) {
                    throw new ServerException("当前运单车辆{0}的状态，无法分配任务", waybillCar.getOrderCarNo());
                }

                //验证是否存在任务明细
                int n = taskDao.countByWaybillCarId(waybillCar.getId());
                if (n > 0) {
                    throw new ServerException("当前运单车辆{0}, 已经分配过", waybillCar.getOrderCarNo());
                }
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
            if (!CollectionUtils.isEmpty(list)) {
                Set<String> startAreaCodeSet = list.stream().map(WaybillCar::getStartAreaCode).collect(Collectors.toSet());
                Set<String> EndAreaCodeSet = list.stream().map(WaybillCar::getEndAreaCode).collect(Collectors.toSet());
                task.setGuideLine(csWaybillService.computeGuideLine(startAreaCodeSet, EndAreaCodeSet, paramsDto.getGuideLine(), list.size()));
            }

            taskDao.insert(task);

            for (WaybillCar waybillCar : list) {

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
            redisUtil.delayDelete(lockSet);
        }

    }

    @Override
    public ResultVo<ResultReasonVo> load(BaseTaskDto paramsDto) {
        Long taskId = paramsDto.getTaskId();
        Set<String> lockSet = Sets.newHashSet();
        try {
            UserInfo userInfo = new UserInfo(paramsDto.getLoginId(), paramsDto.getLoginName(), paramsDto.getLoginPhone(), paramsDto.getLoginType());
            Task task = taskDao.selectById(taskId);
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
            Set<String> vinSet = Sets.newHashSet();
            Set<String> plateNoSet = Sets.newHashSet();
            Map<Long, OrderCar> orderCarMap = Maps.newHashMap();
            Map<Long, WaybillCar> waybillCarMap = Maps.newHashMap();
            Map<Long, Order> orderMap = Maps.newHashMap();
            for (Long taskCarId : paramsDto.getTaskCarIdList()) {

                WaybillCar waybillCar = csWaybillService.getWaybillCarByTaskCarIdFromMap(waybillCarMap, taskCarId);
                if (waybillCar == null) {
                    return BaseResultUtil.fail("运单车辆不存在");
                }
                String lockKey = RedisKeys.getLoadLockKey(taskCarId);
                if (!redisLock.lock(lockKey, 120000, 1, 150L)) {
                    log.debug("缓存失败：key->{}", lockKey);
                    return BaseResultUtil.fail("任务车辆{0}正在提车，请5秒后重试", waybillCar.getOrderCarNo());
                }
                lockSet.add(lockKey);
                String orderCarNo = waybillCar.getOrderCarNo();
                if (waybillCar.getState() >= WaybillCarStateEnum.WAIT_LOAD_CONFIRM.code) {
                    return BaseResultUtil.fail("车辆{0}已经装过车", orderCarNo);
                }
                //验证是否上传过照片
                String photo = waybillCarDao.findUploadPhoto(waybillCar.getOrderCarId());
                if (StringUtils.isBlank(photo) || photo.split(",").length < Constant.MIN_LOAD_PHOTO_NUM) {
                    return BaseResultUtil.fail("车辆{0}尚未上传照片,至少上传8张照片", orderCarNo);
                }
                //验证车辆当前所在地是否与出发区县匹配
                OrderCar orderCar = csWaybillService.getOrderCarFromMap(orderCarMap, waybillCar.getOrderCarId());
                if (orderCar == null) {
                    return BaseResultUtil.fail("订单车辆{0}不存在", orderCarNo);
                }
                Order order = csWaybillService.getOrderFromMap(orderMap, orderCar.getOrderId());
                if (order == null) {
                    return BaseResultUtil.fail("订单{0}不存在", orderCarNo);
                }

                //验证目的地业务中心是否与当前业务中心匹配
                if (waybillCar.getStartStoreId() != null && waybillCar.getStartStoreId() != 0) {
                    if (!waybillCar.getStartStoreId().equals(orderCar.getNowStoreId())) {
                        return BaseResultUtil.fail("订单车辆{0}未入库，请业务员先将车辆入库", orderCarNo);
                    }
                } else {
                    if (!waybillCar.getStartAreaCode().equals(orderCar.getNowAreaCode())) {
                        return BaseResultUtil.fail("订单车辆{0}尚未到达提车地址区县范围内", orderCarNo);
                    }
                }
                //验证运单车辆信息是否完全
                if (!validateOrderCarInfo(orderCar)) {
                    return BaseResultUtil.fail("订单车辆{0}信息不完整", orderCarNo);
                }
                csOrderService.validateOrderCarVinInfo(vinSet, orderCar.getVin());
                csOrderService.validateOrderCarPlateNoInfo(plateNoSet, orderCar.getPlateNo());
            }

            /**写入*/
            int count = 0;
            long currentTimeMillis = System.currentTimeMillis();
            Set<Long> orderIdSet = Sets.newHashSet();
            Set<CarStorageLog> storageLogSet = Sets.newHashSet();
            List<PushInfo> pushCustomerList = Lists.newArrayList();
            Set<String> directLoadCarNoSet = Sets.newHashSet();
            Set<Order> firstLoadOrderSet = Sets.newHashSet();
            for (Long taskCarId : paramsDto.getTaskCarIdList()) {
                WaybillCar waybillCar = csWaybillService.getWaybillCarByTaskCarIdFromMap(waybillCarMap, taskCarId);
                OrderCar orderCar = csWaybillService.getOrderCarFromMap(orderCarMap, waybillCar.getOrderCarId());
                Order order = csWaybillService.getOrderFromMap(orderMap, orderCar.getOrderId());
                boolean isFirstLoad = false;
                if (order.getState() < OrderStateEnum.TRANSPORTING.code) {
                    isFirstLoad = true;
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

                boolean isOutStore = waybillCar.getStartStoreId() != null && waybillCar.getStartStoreId() > 0;
                boolean isDriectLoad = !isOutStore || paramsDto.getLoginType() == UserTypeEnum.ADMIN;
                //运单不经过业务中心,无出入库确认操作
                if (isDriectLoad) {
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

                    if (isOutStore) {
                        // 出库日志
                        CarStorageLog carStorageLog = getCarStorageLog(CarStorageTypeEnum.OUT, waybill, task, waybillCar, orderCar, userInfo);
                        storageLogSet.add(carStorageLog);
                    }

                    //非业务中心提车或者业务员提车，变更订单运输状态
                    orderIdSet.add(orderCar.getOrderId());
                    directLoadCarNoSet.add(orderCar.getNo());
                }

                waybillCarDao.updateForLoad(waybillCar.getId(), waybillCarNewState, currentTimeMillis);

                //订单车辆
                if (orderCar.getState() < orderCarNewState) {
                    orderCarDao.updateStateById(orderCarNewState, orderCar.getId());
                }

                if (isOutStore && paramsDto.getLoginType() == UserTypeEnum.ADMIN) {
                    //出库日志
                    csOrderCarLogService.asyncSave(orderCar, OrderCarLogEnum.C_OUT_STORE,
                            new String[]{MessageFormat.format(OrderCarLogEnum.C_OUT_STORE.getOutterLog(), waybillCar.getStartStoreName()),
                                    MessageFormat.format(OrderCarLogEnum.C_OUT_STORE.getInnerLog(), waybillCar.getStartStoreName(), paramsDto.getLoginName(), paramsDto.getLoginPhone())},
                            userInfo);
                } else {
                    //装车日志
                    csOrderCarLogService.asyncSave(orderCar, OrderCarLogEnum.C_LOAD,
                            new String[]{MessageFormat.format(OrderCarLogEnum.C_LOAD.getOutterLog(), getAddress(waybillCar.getStartProvince(), waybillCar.getStartCity(), waybillCar.getStartArea(), waybillCar.getStartAddress())),
                                    MessageFormat.format(OrderCarLogEnum.C_LOAD.getInnerLog(), getAddress(waybillCar.getStartProvince(), waybillCar.getStartCity(), waybillCar.getStartArea(), waybillCar.getStartAddress()), paramsDto.getLoginName(), paramsDto.getLoginPhone())},
                            userInfo);
                }

                //给客户发送消息
                if (waybillCar.getLoadLinkPhone().equals(order.getPickContactName()) && isFirstLoad) {
                    PushInfo pushInfo = csPushMsgService.getPushInfo(order.getCustomerId(), UserTypeEnum.CUSTOMER, PushMsgEnum.C_TRANSPORT, order.getNo(), order.getStartCity(), order.getEndCity());
                    pushCustomerList.add(pushInfo);
                }
                if (isFirstLoad) {
                    firstLoadOrderSet.add(order);
                }
                count++;
            }
            //更新订单状态
            if (!CollectionUtils.isEmpty(orderIdSet)) {
                orderDao.updateStateForLoad(OrderStateEnum.TRANSPORTING.code, orderIdSet);
                taskDao.updateForOutStore(task.getId());
                taskDao.updateLoadNum(task.getId(), count);
                waybillDao.updateForOutStore(waybill.getId());
                vehicleRunningDao.updateOccupiedNum(task.getVehicleRunningId());
                //添加出库日志
                csStorageLogService.asyncSaveBatch(storageLogSet);
            }

            //给客户发送消息
            csPushMsgService.send(pushCustomerList);

            //给司机发送消息
            if (!CollectionUtils.isEmpty(directLoadCarNoSet)) {
                if (waybill.getCarrierType() == WaybillCarrierTypeEnum.LOCAL_ADMIN.code) {
                    csPushMsgService.getPushInfo(task.getDriverId(), UserTypeEnum.ADMIN, PushMsgEnum.S_LOAD, waybill.getNo(), Joiner.on(",").join(directLoadCarNoSet), directLoadCarNoSet.size());
                } else {
                    csPushMsgService.send(task.getDriverId(), UserTypeEnum.DRIVER, PushMsgEnum.D_LOAD, waybill.getNo(), Joiner.on(",").join(directLoadCarNoSet), directLoadCarNoSet.size());
                }
            }
            firstLoadOrderSet.forEach(o -> o.setState(OrderStateEnum.TRANSPORTING.code));
            csAmqpService.sendOrderState(firstLoadOrderSet);
            return BaseResultUtil.success();
        } finally {
            redisUtils.delayDelete(lockSet);
        }
    }

    private CarStorageLog getCarStorageLog(CarStorageTypeEnum csTypeEnum, Waybill waybill, Task task, WaybillCar waybillCar, OrderCar orderCar, UserInfo userInfo) {
        return CarStorageLog.builder()
                .storeId(CarStorageTypeEnum.IN == csTypeEnum ? waybillCar.getEndStoreId() : waybillCar.getStartStoreId())
                .type(csTypeEnum.code)
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
                .createUserId(userInfo.getId())
                .createUser(userInfo.getName())
                .build();
    }

    private String getAddress(String startProvince, String startCity, String startArea, String startAddress) {
        return (startProvince == null ? "" : startProvince) +
                (startCity == null ? "" : startCity) +
                (startArea == null ? "" : startArea) +
                (startAddress == null ? "" : startAddress);
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
    public ResultVo<ResultReasonVo> unload(BaseTaskDto paramsDto) {
        //返回内容
        ResultReasonVo resultReasonVo = new ResultReasonVo();
        Set<FailResultReasonVo> failCarNoSet = Sets.newHashSet();
        Set<String> successSet = Sets.newHashSet();

        Long taskId = paramsDto.getTaskId();
        Set<String> lockSet = Sets.newHashSet();
        try {

            UserInfo userInfo = new UserInfo(paramsDto.getLoginId(), paramsDto.getLoginName(), paramsDto.getLoginPhone(), paramsDto.getLoginType());
            Task task = taskDao.selectById(taskId);
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

            Map<Long, OrderCar> orderCarMap = Maps.newHashMap();
            Map<Long, Order> orderMap = Maps.newHashMap();
            Map<Long, WaybillCar> waybillCarMap = Maps.newHashMap();
            for (Long taskCarId : paramsDto.getTaskCarIdList()) {
                if (taskCarId == null) {
                    continue;
                }
                WaybillCar waybillCar = csWaybillService.getWaybillCarByTaskCarIdFromMap(waybillCarMap, taskCarId);
                if (waybillCar == null) {
                    return BaseResultUtil.fail("任务ID为{0}对应的运单车辆不存在", taskCarId);
                }
                String lockKey = RedisKeys.getUnloadLockKey(waybillCar.getId());
                if (!redisLock.lock(lockKey, 120000, 1, 150L)) {
                    log.debug("缓存失败：key->{}", lockKey);
                    return BaseResultUtil.fail("任务车辆{0}正在交车，请5秒后重试", waybillCar.getOrderCarNo());
                }
                lockSet.add(lockKey);
                if (waybillCar.getState() <= WaybillCarStateEnum.WAIT_LOAD.code) {
                    return BaseResultUtil.fail("运单车辆{0}尚未装车", waybillCar.getOrderCarNo());
                }
                if (waybillCar.getState() < WaybillCarStateEnum.LOADED.code) {
                    return BaseResultUtil.fail("运单车辆{0}尚未出库，请联系始发地业务中心业务员", waybillCar.getOrderCarNo());
                }
                if (waybillCar.getState() >= WaybillCarStateEnum.WAIT_UNLOAD_CONFIRM.code) {
                    return BaseResultUtil.fail("运单车辆{0}已经卸过车", waybillCar.getOrderCarNo());
                }
                //验证车辆当前所在地是否与出发区县匹配
                OrderCar orderCar = csWaybillService.getOrderCarFromMap(orderCarMap, waybillCar.getOrderCarId());
                if (orderCar == null) {
                    return BaseResultUtil.fail("订单车辆不存在");
                }
                //下一段是否调度
                /*Order order = csWaybillService.getOrderFromMap(orderMap, orderCar.getOrderId());
                if (order == null) {
                    throw new ParameterException("订单不存在", waybillCar.getOrderCarNo());
                } */
            }
            /**写入*/
            int count = 0;
            List<String> directUnloadList = Lists.newArrayList();
            for (Long taskCarId : paramsDto.getTaskCarIdList()) {
                WaybillCar waybillCar = csWaybillService.getWaybillCarByTaskCarIdFromMap(waybillCarMap, taskCarId);
                OrderCar orderCar = csWaybillService.getOrderCarFromMap(orderCarMap, waybillCar.getOrderCarId());

                boolean isInStore = waybillCar.getEndStoreId() != null && waybillCar.getEndStoreId() > 0;
                boolean isDirectUnload = !isInStore || paramsDto.getLoginType() == UserTypeEnum.ADMIN;
                if (isDirectUnload) {

                    waybillCarDao.updateForFinish(waybillCar.getId());
                    OrderCar noc = new OrderCar();
                    noc.setState(computeOrderCarStateForDirectUnload(waybillCar));
                    noc.setId(orderCar.getId());
                    noc.setNowStoreId(waybillCar.getEndStoreId());
                    noc.setNowAreaCode(waybillCar.getEndAreaCode());
                    noc.setNowUpdateTime(System.currentTimeMillis());
                    orderCarDao.updateById(noc);

                    if (isInStore) {
                        //入库日志
                        CarStorageLog carStorageLog = getCarStorageLog(CarStorageTypeEnum.IN, waybill, task, waybillCar, orderCar, userInfo);
                        csStorageLogService.asyncSave(carStorageLog);
                    }

                    directUnloadList.add(orderCar.getNo());

                } else {
                    waybillCarDao.updateStateById(waybillCar.getId(), WaybillCarStateEnum.WAIT_UNLOAD_CONFIRM.code);
                }

                if (isInStore && paramsDto.getLoginType() == UserTypeEnum.ADMIN) {
                    //添加入库物流
                    csOrderCarLogService.asyncSave(orderCar, OrderCarLogEnum.C_IN_STORE,
                            new String[]{MessageFormat.format(OrderCarLogEnum.C_IN_STORE.getOutterLog(), waybillCar.getEndStoreName()),
                                    MessageFormat.format(OrderCarLogEnum.C_IN_STORE.getInnerLog(), waybillCar.getEndStoreName(), paramsDto.getLoginName(), paramsDto.getLoginPhone())},
                            userInfo);
                } else {
                    //添加卸车物流
                    csOrderCarLogService.asyncSave(orderCar, OrderCarLogEnum.C_UNLOAD,
                            new String[]{MessageFormat.format(OrderCarLogEnum.C_UNLOAD.getOutterLog(), getAddress(waybillCar.getEndProvince(), waybillCar.getEndCity(), waybillCar.getEndArea(), waybillCar.getEndAddress())),
                                    MessageFormat.format(OrderCarLogEnum.C_UNLOAD.getInnerLog(), getAddress(waybillCar.getEndProvince(), waybillCar.getEndCity(), waybillCar.getEndArea(), waybillCar.getEndAddress()), paramsDto.getLoginName(), paramsDto.getLoginPhone())},
                            userInfo);
                }

                successSet.add(orderCar.getNo());
                count++;
            }
            if (CollectionUtils.isEmpty(successSet)) {
                return BaseResultUtil.fail("处理失败");
            }
            validateAndFinishTaskWaybill(task);
            //更新任务信息
            taskDao.updateUnloadNum(task.getId(), count);
            //更新实时运力信息
            vehicleRunningDao.updateOccupiedNum(task.getId());

            //给司机交付消息
            if (!CollectionUtils.isEmpty(directUnloadList)) {
                if (waybill.getCarrierType() != null && waybill.getCarrierType() == WaybillCarrierTypeEnum.LOCAL_ADMIN.code && !CollectionUtils.isEmpty(successSet)) {
                    csPushMsgService.send(task.getDriverId(), UserTypeEnum.ADMIN, PushMsgEnum.S_UNLOAD, waybill.getNo(), Joiner.on(",").join(successSet), successSet.size());
                } else {
                    csPushMsgService.send(task.getDriverId(), UserTypeEnum.DRIVER, PushMsgEnum.D_UNLOAD, waybill.getNo(), Joiner.on(",").join(successSet), successSet.size());
                }
            }

            resultReasonVo.setSuccessList(successSet);
            resultReasonVo.setFailList(failCarNoSet);
            return BaseResultUtil.success(resultReasonVo);
        } finally {
            redisUtils.delayDelete(lockSet);
        }
    }

    private Integer computeOrderCarStateForDirectUnload(WaybillCar waybillCar) {
        Order order = orderDao.findByCarId(waybillCar.getOrderCarId());
        //当前状态
        int newState;
        //下一段是否调度
        int m = waybillCarDao.countByStartAddress(waybillCar.getOrderCarId(), waybillCar.getEndAreaCode(), waybillCar.getEndAddress());
        //计算是否到达目的地城市范围
        if (csWaybillService.validateIsArriveEndStore(order.getEndStoreId(), waybillCar.getEndStoreId())) {
            //配送
            newState = m == 0 ? OrderCarStateEnum.WAIT_BACK_DISPATCH.code : OrderCarStateEnum.WAIT_BACK.code;
            //更新自提运单状态
            WaybillCar wc = waybillCarDao.findBackWaybill(waybillCar.getOrderCarId());
            if (wc != null) {
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
        BaseTaskDto inStoreTaskDto = new BaseTaskDto();
        BeanUtils.copyProperties(paramsDto, inStoreTaskDto);
        inStoreTaskDto.setTaskId(taskCar.getTaskId());
        inStoreTaskDto.setTaskCarIdList(Lists.newArrayList(taskCar.getId()));

        return inStore(inStoreTaskDto);
    }

    @Override
    public ResultVo<ResultReasonVo> inStore(BaseTaskDto paramsDto) {
        log.debug("【入库】" + JSON.toJSONString(paramsDto));
        //返回内容
        ResultReasonVo resultReasonVo = new ResultReasonVo();
        Set<FailResultReasonVo> failCarNoSet = Sets.newHashSet();
        Set<String> successSet = Sets.newHashSet();

        Long taskId = paramsDto.getTaskId();
        Set<String> lockSet = Sets.newHashSet();
        try {

            UserInfo userInfo = new UserInfo(paramsDto.getLoginId(), paramsDto.getLoginName(), paramsDto.getLoginPhone(), paramsDto.getLoginType());
            //验证任务
            Task task = taskDao.selectById(taskId);
            if (task == null) {
                return BaseResultUtil.fail("任务不存在");
            }
            //验证运单
            Waybill waybill = waybillDao.selectById(task.getWaybillId());
            if (waybill == null) {
                return BaseResultUtil.fail("运单不存在");
            }
            if (waybill.getState() <= WaybillStateEnum.ALLOT_CONFIRM.code) {
                return BaseResultUtil.fail("运单尚未运输");
            }
            if (waybill.getState() >= WaybillStateEnum.FINISHED.code) {
                return BaseResultUtil.fail("运单未运输或已完结");
            }

            Set<CarStorageLog> storageLogSet = Sets.newHashSet();
            Set<String> vinSet = Sets.newHashSet();
            Set<String> plateNoSet = Sets.newHashSet();
            Map<Long, PushInfo> pushMap = Maps.newHashMap();
            Map<Long, OrderCar> orderCarMap = Maps.newHashMap();
            Map<Long, Order> orderMap = Maps.newHashMap();
            Map<Long, WaybillCar> waybillCarMap = Maps.newHashMap();
            for (Long taskCarId : paramsDto.getTaskCarIdList()) {
                if (taskCarId == null) {
                    continue;
                }
                WaybillCar waybillCar = csWaybillService.getWaybillCarByTaskCarIdFromMap(waybillCarMap, taskCarId);
                if (waybillCar == null) {
                    failCarNoSet.add(new FailResultReasonVo(taskCarId, "任务ID为{0}对应的运单车辆不存在", taskCarId));
                    continue;
                }
                //锁定
                String lockKey = RedisKeys.getInStoreLockKey(taskCarId);
                if (!redisLock.lock(lockKey, 120000, 1, 150L)) {
                    log.debug("缓存失败：key->{}", lockKey);
                    return BaseResultUtil.fail("任务车辆{0}正在入库, 请5秒后重试", waybillCar.getOrderCarNo());
                }
                lockSet.add(lockKey);
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
                OrderCar orderCar = csWaybillService.getOrderCarFromMap(orderCarMap, waybillCar.getOrderCarId());
                if (orderCar == null) {
                    failCarNoSet.add(new FailResultReasonVo(waybillCar.getOrderCarNo(), "车辆不存在"));
                    continue;
                }
                Order order = csWaybillService.getOrderFromMap(orderMap, orderCar.getOrderId());
                if (order == null) {
                    failCarNoSet.add(new FailResultReasonVo(waybillCar.getOrderCarNo(), "订单不存在"));
                }

                csOrderService.validateOrderCarVinInfo(vinSet, orderCar.getVin());
                csOrderService.validateOrderCarPlateNoInfo(plateNoSet, orderCar.getPlateNo());

            }
            //写数据
            for (Long taskCarId : paramsDto.getTaskCarIdList()) {
                //当前状态
                WaybillCar waybillCar = csWaybillService.getWaybillCarByTaskCarIdFromMap(waybillCarMap, taskCarId);
                OrderCar orderCar = csWaybillService.getOrderCarFromMap(orderCarMap, waybillCar.getOrderCarId());
                Order order = csWaybillService.getOrderFromMap(orderMap, orderCar.getOrderId());
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
                CarStorageLog carStorageLog = getCarStorageLog(CarStorageTypeEnum.IN, waybill, task, waybillCar, orderCar, userInfo);

                storageLogSet.add(carStorageLog);

                //添加物流日志
                csOrderCarLogService.asyncSave(orderCar, OrderCarLogEnum.C_IN_STORE,
                        new String[]{MessageFormat.format(OrderCarLogEnum.C_IN_STORE.getOutterLog(), waybillCar.getEndStoreName()),
                                MessageFormat.format(OrderCarLogEnum.C_IN_STORE.getInnerLog(), waybillCar.getEndStoreName(), paramsDto.getLoginName(), paramsDto.getLoginPhone())},
                        userInfo);

                //客户自提推送
                //验证是否订单全部到达业务中心
                if (waybillCar.getEndStoreId().equals(order.getEndStoreId()) && orderCar.getBackType() != null && orderCar.getBackType() == OrderPickTypeEnum.SELF.code) {
                    int i = orderDao.countUnArriveStore(order.getId());
                    if (i <= 0 && !pushMap.containsKey(order.getId())) {
                        Store store = csStoreService.getById(waybillCar.getEndStoreId(), true);
                        PushInfo pushInfo = csPushMsgService.getPushInfo(order.getCustomerId(), UserTypeEnum.CUSTOMER, PushMsgEnum.C_SELF_BACK,
                                order.getNo(), order.getEndStoreName(),
                                getAddress(store.getProvince(), store.getCity(), store.getArea(), store.getDetailAddr()),
                                waybillCar.getUnloadLinkName(), waybillCar.getUnloadLinkPhone());
                        pushMap.put(order.getId(), pushInfo);
                    }
                }
                successSet.add(waybillCar.getOrderCarNo());
            }
            if (CollectionUtils.isEmpty(successSet)) {
                return BaseResultUtil.fail(resultReasonVo);
            }
            //验证任务和运单是否完成
            validateAndFinishTaskWaybill(task);
            //更新实时运力信息
            vehicleRunningDao.updateOccupiedNum(task.getId());
            //写入入库日志
            csStorageLogService.asyncSaveBatch(storageLogSet);

            //客户自提推送
            if (!pushMap.isEmpty()) {
                pushMap.forEach((orderId, p) -> csPushMsgService.send(p));
            }
            //给司机交付消息
            if (waybill.getCarrierType() != null && waybill.getCarrierType() == WaybillCarrierTypeEnum.LOCAL_ADMIN.code && !CollectionUtils.isEmpty(successSet)) {
                csPushMsgService.getPushInfo(task.getDriverId(), UserTypeEnum.ADMIN, PushMsgEnum.S_UNLOAD, waybill.getNo(), Joiner.on(",").join(successSet), successSet.size());
            } else {
                csPushMsgService.send(task.getDriverId(), UserTypeEnum.DRIVER, PushMsgEnum.D_UNLOAD, waybill.getNo(), Joiner.on(",").join(successSet), successSet.size());
            }

            resultReasonVo.setSuccessList(successSet);
            resultReasonVo.setFailList(failCarNoSet);
            return BaseResultUtil.success(resultReasonVo);
        } finally {
            redisUtils.delayDelete(lockSet);
        }
    }

    @Override
    public int validateAndFinishTask(Task task) {

        BillCarNum tcNum = taskCarDao.countUnFinishForState(task.getId());
        if (tcNum.getUnFinishCarNum() > 0) {
            return -1;
        }
        int newState = tcNum.getTotalCarNum().equals(tcNum.getCancelCarNum()) ? WaybillStateEnum.F_CANCEL.code : WaybillStateEnum.FINISHED.code;
        taskDao.updateForOver(task.getId(), newState);
        //任务交付消息
        try {
            Waybill waybill = waybillDao.selectById(task.getWaybillId());
            boolean isAdmin = waybill.getCarrierType() != null && waybill.getCarrierType() == WaybillCarrierTypeEnum.LOCAL_ADMIN.code;
            csPushMsgService.send(task.getDriverId(),
                    isAdmin ? UserTypeEnum.ADMIN : UserTypeEnum.DRIVER,
                    newState == WaybillStateEnum.F_CANCEL.code ? ( isAdmin ? PushMsgEnum.S_CANCEL_WAYBILL : PushMsgEnum.D_CANCEL_WAYBILL) : (isAdmin ? PushMsgEnum.S_FINISHED : PushMsgEnum.D_FINISHED),
                    task.getWaybillNo());
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return newState;
    }

    @Override
    public ResultVo<ResultReasonVo> cancelUnload(BaseTaskDto reqDto) {

        for (Long taskCarId : reqDto.getTaskCarIdList()) {
            WaybillCar waybillCar = waybillCarDao.findByTaskCarId(taskCarId);

        }

        return null;
    }

    @Override
    public int validateAndFinishTaskWaybill(Task task) {
        int state = validateAndFinishTask(task);
        if (state == -1) {
            return -1;
        }
        csWaybillService.validateAndFinishWaybill(task.getWaybillId());
        return state;
    }

    @Override
    public ResultVo<ResultReasonVo> outStore(BaseTaskDto paramsDto) {
        //返回内容
        ResultReasonVo resultReasonVo = new ResultReasonVo();
        Set<FailResultReasonVo> failCarNoSet = Sets.newHashSet();
        Set<String> successSet = Sets.newHashSet();
        UserInfo userInfo = new UserInfo(paramsDto.getLoginId(), paramsDto.getLoginName(), paramsDto.getLoginPhone(), paramsDto.getLoginType());
        Set<String> lockSet = Sets.newHashSet();
        try {
            Long taskId = paramsDto.getTaskId();
            //验证任务
            Task task = taskDao.selectById(taskId);
            if (task == null) {
                return BaseResultUtil.fail("任务不存在");
            }
            //验证运单
            Waybill waybill = waybillDao.selectById(task.getWaybillId());
            if (waybill == null) {
                return BaseResultUtil.fail("运单不存在");
            }
            if (waybill.getState() < WaybillStateEnum.ALLOT_CONFIRM.code) {
                return BaseResultUtil.fail("运单尚未分派司机");
            }
            if (waybill.getState() >= WaybillStateEnum.FINISHED.code) {
                return BaseResultUtil.fail("运单已完结");
            }

            Set<CarStorageLog> storageLogSet = Sets.newHashSet();
            if (CollectionUtils.isEmpty(paramsDto.getTaskCarIdList())) {
                return BaseResultUtil.fail("车辆不能为空");
            }

            Map<Long, OrderCar> orderCarMap = Maps.newHashMap();
            Map<Long, Order> orderMap = Maps.newHashMap();
            Map<Long, WaybillCar> waybillCarMap = Maps.newHashMap();
            for (Long taskCarId : paramsDto.getTaskCarIdList()) {
                WaybillCar waybillCar = csWaybillService.getWaybillCarByTaskCarIdFromMap(waybillCarMap, taskCarId);
                if (waybillCar == null) {
                    return BaseResultUtil.fail("任务ID为{0}对应的运单车辆不存在", taskCarId);
                }
                String lockKey = RedisKeys.getOutStoreLockKey(taskCarId);
                if (!redisLock.lock(lockKey, 120000, 1, 150L)) {
                    log.debug("缓存失败：key->{}", lockKey);
                    return BaseResultUtil.fail("任务车辆{0}正在出库，请5秒后重试", waybillCar.getOrderCarNo());
                }
                lockSet.add(lockKey);
                if (waybillCar.getState() < WaybillCarStateEnum.WAIT_LOAD_CONFIRM.code) {
                    return BaseResultUtil.fail("任务车辆{0}尚未装车，不能出库", waybillCar.getOrderCarNo());
                }
                if (waybillCar.getState() > WaybillCarStateEnum.WAIT_LOAD_CONFIRM.code) {
                    return BaseResultUtil.fail("任务车辆{0}已经出库，不能重复出库", waybillCar.getOrderCarNo());
                }
                //验证车辆当前所在业务中心是否与出发业务中心匹配
                OrderCar orderCar = csWaybillService.getOrderCarFromMap(orderCarMap, waybillCar.getOrderCarId());
                if (orderCar == null) {
                    return BaseResultUtil.fail("任务车辆{0}关联订单车辆不存在", waybillCar.getOrderCarNo());
                }
                Order order = csWaybillService.getOrderFromMap(orderMap, orderCar.getOrderId());
                if (order == null) {
                    return BaseResultUtil.fail("任务车辆{0}关联订单不存在", waybillCar.getOrderCarNo());
                }
            }
            int count = 0;
            Set<Long> orderIdSet = Sets.newHashSet();
            Set<Order> firstLoadOrderSet = Sets.newHashSet();
            for (Long taskCarId : paramsDto.getTaskCarIdList()) {
                WaybillCar waybillCar = csWaybillService.getWaybillCarByTaskCarIdFromMap(waybillCarMap, taskCarId);
                OrderCar orderCar = csWaybillService.getOrderCarFromMap(orderCarMap, waybillCar.getOrderCarId());
                Order order = csWaybillService.getOrderFromMap(orderMap, orderCar.getOrderId());
                if (OrderStateEnum.TRANSPORTING.code > order.getState()) {
                    firstLoadOrderSet.add(order);
                }
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
                CarStorageLog carStorageLog = getCarStorageLog(CarStorageTypeEnum.OUT, waybill, task, waybillCar, orderCar, userInfo);
                storageLogSet.add(carStorageLog);
                //出库日志
                csOrderCarLogService.asyncSave(orderCar, OrderCarLogEnum.C_OUT_STORE,
                        new String[]{MessageFormat.format(OrderCarLogEnum.C_OUT_STORE.getOutterLog(), waybillCar.getStartStoreName()),
                                MessageFormat.format(OrderCarLogEnum.C_OUT_STORE.getInnerLog(), waybillCar.getStartStoreName(), paramsDto.getLoginName(), paramsDto.getLoginPhone())},
                        userInfo);
                //提取数据
                successSet.add(orderCar.getNo());
                orderIdSet.add(orderCar.getOrderId());
                count++;
            }
            //更新订单状态
            if (!CollectionUtils.isEmpty(orderIdSet)) {
                orderDao.updateStateForLoad(OrderStateEnum.TRANSPORTING.code, orderIdSet);
                taskDao.updateForOutStore(task.getId());
                taskDao.updateLoadNum(task.getId(), count);
                waybillDao.updateForOutStore(waybill.getId());
                vehicleRunningDao.updateOccupiedNum(task.getVehicleRunningId());
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

            //给司机发送消息
            if (waybill.getCarrierType() != null && waybill.getCarrierType() == WaybillCarrierTypeEnum.LOCAL_ADMIN.code && !CollectionUtils.isEmpty(successSet)) {
                csPushMsgService.getPushInfo(task.getDriverId(), UserTypeEnum.ADMIN, PushMsgEnum.S_LOAD, waybill.getNo(), Joiner.on(",").join(successSet), successSet.size());
            } else {
                csPushMsgService.send(task.getDriverId(), UserTypeEnum.DRIVER, PushMsgEnum.D_LOAD, waybill.getNo(), Joiner.on(",").join(successSet), successSet.size());
            }

            firstLoadOrderSet.forEach(o -> o.setState(OrderStateEnum.TRANSPORTING.code));
            csAmqpService.sendOrderState(firstLoadOrderSet);
            return BaseResultUtil.success(resultReasonVo);
        } finally {
            redisUtils.delayDelete(lockSet);
        }
    }

  /*  private boolean validateIsArriveEndCity(Order order, WaybillCar waybillCar) {
        if (waybillCar == null) {
            return false;
        }
        //先验证是否到达所属业务中心
        if (order.getEndStoreId() != null) {
            Store store = csStoreService.getBelongByAreaCode(waybillCar.getEndAreaCode());
            if (store != null && store.getId().equals(order.getEndStoreId())) {
                return true;
            }
        }
        //其次验证城市
        if (order.getEndCityCode().equals(waybillCar.getEndCityCode())) {
            return true;
        }
        return false;

    }*/

    @Override
    public ResultVo receipt(ReceiptTaskDto paramsDto) {
        //返回内容
        ResultReasonVo resultReasonVo = new ResultReasonVo();
        Set<FailResultReasonVo> failCarNoSet = Sets.newHashSet();
        Set<String> successSet = Sets.newHashSet();
        Set<String> lockSet = Sets.newHashSet();
        try {
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

            Set<Long> waybillCarIdSet = Sets.newHashSet();
            Set<String> customerPhoneSet = Sets.newHashSet();
            Set<Long> orderSet = Sets.newHashSet();
            List<PushInfo> pushList = Lists.newArrayList();
            Map<Long, List<String>> map = Maps.newHashMap();
            for (Long taskCarId : paramsDto.getTaskCarIdList()) {
                if (taskCarId == null) {
                    continue;
                }
                WaybillCar waybillCar = waybillCarDao.findByTaskCarId(taskCarId);
                if (waybillCar == null) {
                    failCarNoSet.add(new FailResultReasonVo(taskCarId, "任务ID为{0}关联的运单车辆不存在", taskCarId));
                    continue;
                }
                String lockKey = RedisKeys.getReceiptLockKey(waybillCar.getOrderCarNo());
                if (!redisLock.lock(lockKey, 120000, 1, 150L)) {
                    log.debug("缓存失败：key->{}", lockKey);
                    return BaseResultUtil.fail("任务车辆{0}正在交车，请5秒后重试", waybillCar.getOrderCarNo());
                }
                lockSet.add(lockKey);
                if (waybill.getCarrierType() != WaybillCarrierTypeEnum.SELF.code && waybillCar.getState() < WaybillCarStateEnum.LOADED.code) {
                    failCarNoSet.add(new FailResultReasonVo(waybillCar.getOrderCarNo(), "运单车辆还未装车", taskCarId));
                    continue;
                }
                if (waybillCar.getState() >= WaybillCarStateEnum.UNLOADED.code) {
                    failCarNoSet.add(new FailResultReasonVo(waybillCar.getOrderCarNo(), "运单车辆已经签收过", taskCarId));
                    continue;
                }
                Order order = orderDao.findByCarId(waybillCar.getOrderCarId());
                if (waybill.getType() == WaybillTypeEnum.BACK.code && waybill.getCarrierType() == WaybillCarrierTypeEnum.SELF.code) {
                    waybillCarDao.updateSelfCarryForFinish(waybillCar.getId());
                } else {
                    waybillCarDao.updateForFinish(waybillCar.getId());
                }
                orderCarDao.updateForFinish(waybillCar.getOrderCarId(), waybillCar.getEndAreaCode());

                //车辆日志
                OrderCar orderCar = orderCarDao.selectById(waybillCar.getOrderCarId());
                csOrderCarLogService.asyncSave(orderCar, OrderCarLogEnum.C_RECEIPT,
                        new String[]{OrderCarLogEnum.C_RECEIPT.getOutterLog(),
                                MessageFormat.format(OrderCarLogEnum.C_RECEIPT.getInnerLog(), paramsDto.getLoginName(), paramsDto.getLoginPhone())},
                        new UserInfo(paramsDto.getLoginId(), paramsDto.getLoginName(), paramsDto.getLoginPhone(), paramsDto.getLoginType()));

                try {
                    List<String> orderCarNoList = map.get(order.getId());
                    if (orderCarNoList == null) {
                        orderCarNoList = new ArrayList<>();
                    }
                    orderCarNoList.add(orderCar.getNo());
                    map.put(order.getId(), orderCarNoList);

                } catch (Exception e) {
                    log.error("短信信息异常" + e.getMessage(), e);
                }

                PushInfo pushInfo = csPushMsgService.getPushInfo(order.getCustomerId(), UserTypeEnum.CUSTOMER, PushMsgEnum.C_RECEIPT_CAR, orderCar.getNo());
                pushList.add(pushInfo);

                customerPhoneSet.add(order.getBackContactPhone());
                waybillCarIdSet.add(waybillCar.getId());
                orderSet.add(order.getId());
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
            try {
                log.info("客户收车发送短信");
                sendMessage(map);
            } catch (Exception e) {
                log.error("收车短信发送异常" + e.getMessage(), e);
            }

            //验证订单是否完成
            UserInfo userInfo = new UserInfo(paramsDto.getLoginId(), paramsDto.getLoginName(), paramsDto.getLoginPhone(), paramsDto.getLoginType());
            orderSet.forEach(orderId -> validateAndFinishOrder(orderId, userInfo));

            //给客户推送推送
            if (CollectionUtils.isEmpty(pushList)) {
                pushList.forEach(p -> csPushMsgService.send(p));
            }
            resultReasonVo.setSuccessList(successSet);
            resultReasonVo.setFailList(failCarNoSet);
            return BaseResultUtil.success(resultReasonVo);
        } finally {
            redisUtils.delayDelete(lockSet);
        }
    }

    @Override
    public ResultVo<ResultReasonVo> receiptBatch(ReceiptBatchDto paramsDto) {
        //返回内容
        ResultReasonVo resultReasonVo = new ResultReasonVo();
        Set<FailResultReasonVo> failCarNoSet = Sets.newHashSet();
        Set<String> successSet = Sets.newHashSet();
        Set<String> lockSet = Sets.newHashSet();
        try {
            Set<String> orderNos = Sets.newHashSet();
            Set<Long> orderIdSet = Sets.newHashSet();
            Set<Long> waybillCarIdSet = Sets.newHashSet();
            List<OrderCar> list = orderCarDao.findListByNos(paramsDto.getOrderCarNos());
            Order order = null;
            for (OrderCar orderCar : list) {
                if (orderCar == null) {
                    continue;
                }
                String lockKey = RedisKeys.getReceiptLockKey(orderCar.getNo());
                if (!redisLock.lock(lockKey, 120000, 1, 150L)) {
                    log.debug("缓存失败：key->{}", lockKey);
                    return BaseResultUtil.fail("任务车辆{0}正在交车，请5秒后重试", orderCar.getNo());
                }
                lockSet.add(lockKey);
                if (order == null) {
                    order = orderDao.selectById(orderCar.getOrderId());
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
                csOrderCarLogService.asyncSave(orderCar, OrderCarLogEnum.C_IN_STORE,
                        new String[]{MessageFormat.format(OrderCarLogEnum.C_IN_STORE.getInnerLog(), orderCar.getNo(), waybillCar.getEndStoreName()),
                                MessageFormat.format(OrderCarLogEnum.C_IN_STORE.getOutterLog(), orderCar.getNo(), waybillCar.getEndStoreName())},
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

            if (order != null && !CollectionUtils.isEmpty(orderNos)) {
                csPushMsgService.send(order.getCustomerId(), UserTypeEnum.CUSTOMER, PushMsgEnum.C_RECEIPT_CAR, Joiner.on(",").join(orderNos));
            }
            resultReasonVo.setSuccessList(successSet);
            resultReasonVo.setFailList(failCarNoSet);
            LogUtil.debug(MessageFormat.format("订单{0}签收结果：{1}：", JSON.toJSONString(orderNos), JSON.toJSONString(resultReasonVo)));
            return BaseResultUtil.success(resultReasonVo);
        } finally {
            redisUtils.delayDelete(lockSet);
        }
    }

    private void validateAndFinishOrder(Long orderId, UserInfo userInfo) {
        int countUnFinish = orderCarDao.countUnFinishByOrderId(orderId);
        int countUnpay = orderCarDao.countUnPayByOrderId(orderId);
        log.info("validateAndFinishOrder countUnFinish = " + countUnFinish);
        boolean isFinish = countUnFinish <= 0;
        boolean isPaid = countUnpay <= 0;

        if (isFinish) {
            orderDao.updateForFinish(orderId);

            Order order = orderDao.selectById(orderId);
            //订单完成日志
            csOrderLogService.asyncSave(order, OrderLogEnum.FINISH,
                    new String[]{OrderLogEnum.FINISH.getOutterLog(),
                            MessageFormat.format(OrderLogEnum.FINISH.getInnerLog(), userInfo.getName(), userInfo.getPhone())},
                    userInfo);
            //
            csAmqpService.sendOrderState(order);
        }

        if (isPaid) {
            orderDao.updateForPaid(orderId);
        }
    }

    private void sendMessage(Map<Long, List<String>> map) {
        log.info("客户收车发送短信 start");
        for (Long orderId : map.keySet()) {
            Order order = orderDao.selectById(orderId);
            List<String> orderCarNosList = map.get(orderId);
            StringBuilder message = new StringBuilder("【韵车物流】VIN码后六位为");
            List<OrderCar> orderCarList = orderCarDao.findListByNos(orderCarNosList);

            try {
                for (int i = 0; i < orderCarList.size(); i++) {
                    OrderCar orderCar = orderCarList.get(i);
                    if (orderCar != null) {
                        String vin = orderCar.getVin();
                        if (vin != null) {
                            int length = vin.length();
                            if (length > 6) {
                                if (i == orderCarList.size() - 1) {
                                    message.append(vin.substring(length - 6));
                                } else {
                                    message.append(vin.substring(length - 6));
                                    message.append("、");
                                }
                            } else {
                                if (i == orderCarList.size() - 1) {
                                    message.append(vin);
                                } else {
                                    message.append(vin);
                                    message.append("、");
                                }
                            }
                        }
                    }
                }
            } catch (Exception e) {
                log.error("拼接Vin码异常" + e.getMessage(), e);
            }
            message.append("的车辆已完成交车");
            if (order != null && order.getCustomerPhone() != null) {
                try {
                    MiaoxinSmsUtil.send(order.getCustomerPhone(), message.toString());
                } catch (Exception e) {
                    log.error("收车短信发送失败" + e.getMessage(), e);
                }
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

        log.info("updateForCarFinish list =" + list.toString());
        Map<Long, List<String>> pushCustomerInfoMap = Maps.newHashMap();
        Map<Long, PushDriverInfo> pushDriverInfoMap = Maps.newHashMap();
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
                //物流日志
                csOrderCarLogService.asyncSave(orderCar, OrderCarLogEnum.C_RECEIPT,
                        new String[]{OrderCarLogEnum.C_RECEIPT.getOutterLog(),
                                MessageFormat.format(OrderCarLogEnum.C_RECEIPT.getInnerLog(), userInfo.getName(), userInfo.getPhone())},
                        userInfo);
                waybillCarIdSet.add(waybillCar.getId());

                //收款推送
                getPushDriverInfoMapForPay(pushDriverInfoMap, waybillCar);
            } else {
                //更新车辆状态
                orderCarDao.updateForPrePaySuccess(orderCar.getId());
                //出库日志
                csOrderCarLogService.asyncSave(orderCar, OrderCarLogEnum.C_PAID,
                        new String[]{OrderCarLogEnum.C_PAID.getOutterLog(),
                                MessageFormat.format(OrderCarLogEnum.C_PAID.getInnerLog(), userInfo.getName(), userInfo.getPhone())},
                        userInfo);
            }

            //客户支付车辆推送
            try {
                Order order = orderDao.selectById(orderCar.getOrderId());
                if (order != null) {
                    Long customerId = order.getCustomerId();
                    if (pushCustomerInfoMap.containsKey(customerId)) {
                        pushCustomerInfoMap.get(customerId).add(orderCar.getNo());
                    } else {
                        pushCustomerInfoMap.put(customerId, Lists.newArrayList(orderCar.getNo()));
                    }
                }
            } catch (Exception e) {
                log.error(e.getMessage(), e);
            }

            //提取数据
            orderIdSet.add(orderCar.getOrderId());
        }

        //处理订单
        if (!CollectionUtils.isEmpty(orderIdSet)) {
            orderIdSet.forEach(orderId -> validateAndFinishOrder(orderId, userInfo));
        }
        //处理任务
        if (!CollectionUtils.isEmpty(waybillCarIdSet)) {
            List<Task> taskList = taskDao.findListByWaybillCarIds(waybillCarIdSet);
            if (!CollectionUtils.isEmpty(taskList)) {
                taskList.forEach(this::validateAndFinishTaskWaybill);
            }
        }

        try {
            //发送消息
            pushCustomerInfoMap.forEach((customerId, carNoList) -> {
                if (!CollectionUtils.isEmpty(carNoList)) {
                    csPushMsgService.send(customerId, UserTypeEnum.CUSTOMER, PushMsgEnum.C_PAID_CAR, Joiner.on(",").join(carNoList));
                }
            });

            pushDriverInfoMap.forEach((driverId, pdInfo) -> {
                if (!CollectionUtils.isEmpty(pdInfo.getOrderCarNos())) {
                    if (UserTypeEnum.ADMIN == pdInfo.getUserTypeEnum()) {
                        csPushMsgService.send(driverId, UserTypeEnum.ADMIN, PushMsgEnum.S_PAID_CAR, Joiner.on(",").join(pdInfo.getOrderCarNos()));
                    } else {
                        csPushMsgService.send(driverId, UserTypeEnum.DRIVER, PushMsgEnum.D_PAID_CAR, Joiner.on(",").join(pdInfo.getOrderCarNos()));
                    }
                }
            });
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }

    }

    private Map<Long, PushDriverInfo> getPushDriverInfoMapForPay(Map<Long, PushDriverInfo> pushDriverInfoMap, WaybillCar wc) {
        if (pushDriverInfoMap == null) {
            pushDriverInfoMap = Maps.newHashMap();
        }
        try {
            Task task = taskDao.findByWaybillCarId(wc.getId());
            if (task != null) {
                Long driverId = task.getDriverId();
                Waybill waybill = waybillDao.selectById(task.getWaybillId());
                if (waybill.getCarrierId() != null) {
                    boolean isAdmin = waybill.getCarrierType() == WaybillCarrierTypeEnum.LOCAL_ADMIN.code;
                    if (pushDriverInfoMap.containsKey(driverId)) {
                        pushDriverInfoMap.get(driverId).getOrderCarNos().add(wc.getOrderCarNo());
                    } else {
                        PushDriverInfo pdInfo = new PushDriverInfo();
                        pdInfo.setUserTypeEnum(isAdmin ? UserTypeEnum.ADMIN : UserTypeEnum.DRIVER);
                        pdInfo.setOrderCarNos(Lists.newArrayList(wc.getOrderCarNo()));
                        pushDriverInfoMap.put(driverId, pdInfo);
                    }
                }
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return pushDriverInfoMap;
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
            String lockKey = RedisKeys.getWlPayLockKey(orderCar.getNo());
            lockSet.add(lockKey);

            //更新支付车辆状态
            if (payType == ChargeTypeEnum.COLLECT_PAY.getCode()) {
                orderCarDao.updateForPaySuccess(orderCar.getId(), waybillCar.getEndAreaCode());
            } else {
                orderCarDao.updateForPrePaySuccess(orderCar.getId());
            }

            //更新运单车辆状态
            waybillCarDao.updateForFinish(waybillCar.getId());

            csOrderCarLogService.asyncSave(orderCar, OrderCarLogEnum.C_RECEIPT,
                    new String[]{OrderCarLogEnum.C_RECEIPT.getOutterLog(),
                            MessageFormat.format(OrderCarLogEnum.C_RECEIPT.getInnerLog(), userInfo.getName(), userInfo.getPhone())},
                    userInfo);

            taskIdSet.add(taskCar.getTaskId());
            orderIdSet.add(orderCar.getOrderId());
        }
        taskIdSet.forEach(taskId -> {
            log.debug("【支付回调-二维码】完成任务{}", taskId);
            Task task = taskDao.selectById(taskId);
            validateAndFinishTaskWaybill(task);
        });
        orderIdSet.forEach(orderId -> validateAndFinishOrder(orderId, userInfo));

        //删除支付锁
        redisUtil.delete(lockSet);
    }

}
