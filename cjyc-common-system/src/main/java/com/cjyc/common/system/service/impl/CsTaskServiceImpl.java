package com.cjyc.common.system.service.impl;

import com.cjkj.common.redis.lock.RedisDistributedLock;
import com.cjyc.common.model.constant.Constant;
import com.cjyc.common.model.dao.*;
import com.cjyc.common.model.dto.customer.order.ReceiptBatchDto;
import com.cjyc.common.model.dto.driver.task.PickLoadDto;
import com.cjyc.common.model.dto.driver.task.ReplenishInfoDto;
import com.cjyc.common.model.dto.web.task.*;
import com.cjyc.common.model.entity.*;
import com.cjyc.common.model.entity.defined.UserInfo;
import com.cjyc.common.model.enums.BizStateEnum;
import com.cjyc.common.model.enums.CaptchaTypeEnum;
import com.cjyc.common.model.enums.CarStorageTypeEnum;
import com.cjyc.common.model.enums.PayStateEnum;
import com.cjyc.common.model.enums.log.OrderCarLogEnum;
import com.cjyc.common.model.enums.log.OrderLogEnum;
import com.cjyc.common.model.enums.order.OrderCarStateEnum;
import com.cjyc.common.model.enums.order.OrderStateEnum;
import com.cjyc.common.model.enums.task.TaskStateEnum;
import com.cjyc.common.model.enums.waybill.WaybillCarStateEnum;
import com.cjyc.common.model.enums.waybill.WaybillStateEnum;
import com.cjyc.common.model.enums.waybill.WaybillTypeEnum;
import com.cjyc.common.model.exception.ParameterException;
import com.cjyc.common.model.exception.ServerException;
import com.cjyc.common.model.keys.RedisKeys;
import com.cjyc.common.model.util.BaseResultUtil;
import com.cjyc.common.model.vo.FailResultReasonVo;
import com.cjyc.common.model.vo.ResultReasonVo;
import com.cjyc.common.model.vo.ResultVo;
import com.cjyc.common.model.vo.web.OrderCarVo;
import com.cjyc.common.system.service.*;
import com.cjyc.common.system.util.RedisUtils;
import com.google.common.base.Joiner;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.text.MessageFormat;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author JPG
 */
@Service
@Transactional(rollbackFor = Exception.class)
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
    private ICsOrderLogService orderLogService;
    @Resource
    private ICsOrderCarLogService csOrderCarLogService;
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
    @Resource
    private ICsUserService csUserService;

    /**
     * 获取任务编号
     *
     * @param waybillNo
     * @author JPG
     * @since 2019/12/9 18:10
     */
    @Override
    public String getTaskNo(String waybillNo) {
        String taskNo = null;
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
        WaybillCar waybillCar = waybillCarDao.selectById(reqDto.getWaybillCarId());
        if (waybillCar == null) {
            return BaseResultUtil.fail("运单车辆不存在");
        }
        List<String> loadPhotoImgs = reqDto.getLoadPhotoImgs();
        if (loadPhotoImgs.size() < Constant.MIN_LOAD_PHOTO_NUM) {
            return BaseResultUtil.fail("照片数量不足8张");
        }
        if (loadPhotoImgs.size() > Constant.MAX_LOAD_PHOTO_NUM) {
            return BaseResultUtil.fail("照片数量不能超过20张");
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
        waybillCarDao.updateForReplenishInfo(waybillCar.getId(), Joiner.on(",").join(loadPhotoImgs));
        return BaseResultUtil.success();
    }

    @Override
    public ResultVo<ResultReasonVo>  loadForLocal(PickLoadDto reqDto) {
        Long taskCarId = reqDto.getTaskCarId();
        WaybillCar waybillCar = waybillCarDao.findByTaskCarId(taskCarId);
        if (waybillCar == null) {
            return BaseResultUtil.fail("运单车辆不存在");
        }
        List<String> loadPhotoImgs = reqDto.getLoadPhotoImgs();
        if (loadPhotoImgs.size() < Constant.MIN_LOAD_PHOTO_NUM) {
            return BaseResultUtil.fail("照片数量不足8张");
        }
        if (loadPhotoImgs.size() > Constant.MAX_LOAD_PHOTO_NUM) {
            return BaseResultUtil.fail("照片数量不能超过20张");
        }
        //更新车辆信息
        OrderCar oc = new OrderCar();
        oc.setId(waybillCar.getOrderCarId());
        oc.setVin(reqDto.getVin());
        oc.setBrand(reqDto.getBrand());
        oc.setModel(reqDto.getModel());
        oc.setPlateNo(reqDto.getPlateNo());
        orderCarDao.updateById(oc);
        //更新运单车辆信息
        waybillCarDao.updateForReplenishInfo(waybillCar.getId(), Joiner.on(",").join(loadPhotoImgs));

        TaskCar taskCar = taskCarDao.selectById(taskCarId);
        LoadTaskDto loadTaskDto = new LoadTaskDto();
        loadTaskDto.setLoginId(reqDto.getLoginId());
        loadTaskDto.setLoginName(reqDto.getLoginName());
        loadTaskDto.setLoginPhone(reqDto.getLoginPhone());
        loadTaskDto.setLoginType(reqDto.getLoginType());
        loadTaskDto.setTaskId(taskCar.getTaskId());
        loadTaskDto.setTaskCarIdList(Lists.newArrayList(taskCarId));

        return load(loadTaskDto);

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
                return BaseResultUtil.fail("司机不存在");
            }
            if (driver.getBusinessState() != BizStateEnum.BUSINESS.code) {
                return BaseResultUtil.fail("司机不在运营中");
            }

            Task task = new Task();
            //计算任务编号
            task.setNo(getTaskNo(waybill.getNo()));
            task.setWaybillId(waybill.getId());
            task.setWaybillNo(waybill.getNo());
            task.setGuideLine(paramsDto.getGuideLine());
            task.setCarNum(paramsDto.getWaybillCarIdList().size());
            task.setState(TaskStateEnum.WAIT_LOAD.code);
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
                String lockKey = RedisKeys.getAllotTaskKey(waybillCar.getId());
                if (!redisLock.lock(lockKey, 20000, 100, 300)) {
                    throw new ServerException("当前运单车辆{}其他人正在分配，", waybillCar.getOrderCarNo());
                }
                lockKeySet.add(lockKey);

                //验证运单车辆状态
                if (waybillCar.getState() > WaybillCarStateEnum.WAIT_ALLOT.code) {
                    throw new ServerException("当前运单车辆{}的状态，无法分配任务", waybillCar.getOrderCarNo());
                }

                //验证是否存在任务明细
                int n = taskDao.countByTaskIdAndWaybillCarId(task.getId(), waybillCar.getId());
                if (n > 0) {
                    throw new ServerException("当前运单车辆{}, 已经分配过", waybillCar.getOrderCarNo());
                }

                TaskCar taskCar = new TaskCar();
                taskCar.setTaskId(task.getId());
                taskCar.setWaybillCarId(waybillCar.getId());
                taskCarDao.insert(taskCar);
                noCount++;

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
        //返回内容
        ResultReasonVo resultReasonVo = new ResultReasonVo();
        Set<FailResultReasonVo> failCarNoSet = Sets.newHashSet();
        Set<String> successSet = Sets.newHashSet();

        Task task = taskDao.selectById(paramsDto.getTaskId());
        if (task == null) {
            return BaseResultUtil.fail("任务不存在");
        }
        if (StringUtils.isBlank(task.getVehiclePlateNo())) {
            return BaseResultUtil.fail("请先完善运力车牌号");
        }
        //验证运单
        Waybill waybill = waybillDao.selectById(task.getWaybillId());
        if (waybill == null) {
            return BaseResultUtil.fail("运单不存在");
        }
        if (waybill.getState() >= WaybillStateEnum.FINISHED.code || waybill.getState() <= WaybillStateEnum.ALLOT_CONFIRM.code) {
            return BaseResultUtil.fail("运单已完结");
        }

        //批量处理数据
        int count = 0;
        long currentTimeMillis = System.currentTimeMillis();
        Set<Long> orderIdSet = Sets.newHashSet();
        for (Long taskCarId : paramsDto.getTaskCarIdList()) {
            WaybillCar waybillCar = waybillCarDao.findByTaskCarId(taskCarId);
            if (waybillCar == null) {
                failCarNoSet.add(new FailResultReasonVo(taskCarId, "运单车辆不存在"));
                continue;
            }
            if (waybillCar.getState() >= WaybillCarStateEnum.WAIT_LOAD_CONFIRM.code) {
                failCarNoSet.add(new FailResultReasonVo(waybillCar.getOrderCarId(), "运单车辆已经装过车"));
                continue;
            }
            //验证车辆当前所在地是否与出发区县匹配
            OrderCar orderCar = orderCarDao.selectById(waybillCar.getOrderCarId());
            if (orderCar == null) {
                failCarNoSet.add(new FailResultReasonVo(waybillCar.getOrderCarNo(), "订单车辆不存在"));
                continue;
            }
            if (!waybillCar.getStartAreaCode().equals(orderCar.getNowAreaCode())) {
                failCarNoSet.add(new FailResultReasonVo(waybillCar.getOrderCarNo(), "订单车辆区县范围内"));
            }

            //验证运单车辆信息是否完全
            if (!validateOrderCarInfo(orderCar)) {
                failCarNoSet.add(new FailResultReasonVo(waybillCar.getOrderCarNo(), "订单车辆信息不完整"));
                continue;
            }
            if (!validateWaybillCarInfo(waybillCar)) {
                failCarNoSet.add(new FailResultReasonVo(waybillCar.getOrderCarNo(), "运单车辆信息不完整"));
                continue;
            }

            //运单和车辆状态
            int waybillCarNewState = WaybillCarStateEnum.WAIT_LOAD_CONFIRM.code;
            int orderCarNewState = orderCar.getState();
            //运单不经过业务中心,无出入库确认操作
            if (waybillCar.getStartStoreId() == null || waybillCar.getStartStoreId() <= 0) {
                if (waybill.getType() == WaybillTypeEnum.PICK.code) {
                    //提车任务、自送到业务中心
                    waybillCarNewState = WaybillCarStateEnum.LOADED.code;
                    orderCarNewState = OrderCarStateEnum.PICKING.code;
                } else if (waybill.getType() == WaybillTypeEnum.TRUNK.code) {
                    //干线、提干、干送、提干送任务
                    waybillCarNewState = WaybillCarStateEnum.LOADED.code;
                    orderCarNewState = OrderCarStateEnum.TRUNKING.code;
                } else if (waybill.getType() == WaybillTypeEnum.BACK.code) {
                    //送车、自提
                    waybillCarNewState = WaybillCarStateEnum.LOADED.code;
                    orderCarNewState = OrderCarStateEnum.BACKING.code;
                }
            }

            waybillCarDao.updateForLoad(waybillCar.getId(), waybillCarNewState, currentTimeMillis);
            //订单车辆
            if (orderCar.getState() < orderCarNewState) {
                orderCarDao.updateStateById(orderCarNewState, orderCar.getId());
                //订单
                orderIdSet.add(orderCar.getOrderId());
            }

            //更新空车位数
            vehicleRunningDao.updateOccupiedNumForLoad(paramsDto.getLoginId(), count);
            //更新订单状态
            if (!CollectionUtils.isEmpty(orderIdSet)) {
                orderDao.updateStateForLoad(OrderStateEnum.TRANSPORTING.code, orderIdSet);
            }
            successSet.add(orderCar.getNo());
            count++;
        }
        resultReasonVo.setSuccessList(successSet);
        resultReasonVo.setFailList(failCarNoSet);
        return BaseResultUtil.success(resultReasonVo);
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
        if (waybill.getState() >= WaybillStateEnum.FINISHED.code || waybill.getState() <= WaybillStateEnum.ALLOT_CONFIRM.code) {
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
            if (waybillCar.getState() < WaybillCarStateEnum.LOADED.code) {
                failCarNoSet.add(new FailResultReasonVo(waybillCar.getOrderCarNo(), "运单车辆尚未装车", taskCarId));
                continue;
            }
            if (waybillCar.getState() >= WaybillCarStateEnum.UNLOADED.code) {
                failCarNoSet.add(new FailResultReasonVo(waybillCar.getOrderCarNo(), "运单车辆已经卸过车", taskCarId));
                continue;
            }
            //卸车时间以收车人确认收货为准
            //waybillCar.setUnloadTime(currentTimeMillis);
            waybillCarIdSet.add(waybillCar.getId());
            count++;
        }

        if (!CollectionUtils.isEmpty(waybillCarIdSet)) {
            //更新运单车辆信息
            waybillCarDao.updateBatchForUnload(waybillCarIdSet, WaybillCarStateEnum.WAIT_UNLOAD_CONFIRM.code);
            //更新任务信息
            taskDao.updateForUnload(task.getId(), count);
            //更新实时运力信息
            vehicleRunningDao.updateOccupiedNumForUnload(task.getId(), count);
            //TODO 发送收车推送信息
        }
        resultReasonVo.setSuccessList(successSet);
        resultReasonVo.setFailList(failCarNoSet);
        return BaseResultUtil.success(resultReasonVo);
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
        if (waybill.getState() >= WaybillStateEnum.FINISHED.code || waybill.getState() <= WaybillStateEnum.ALLOT_CONFIRM.code) {
            return BaseResultUtil.fail("运单已完结");
        }

        long currentTimeMillis = System.currentTimeMillis();
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
            if (!waybillCar.getStartStoreId().equals(orderCar.getNowStoreId())) {
                failCarNoSet.add(new FailResultReasonVo(waybillCar.getOrderCarNo(), "订单车辆尚未到达始发地业务中心范围内"));
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
            orderCarDao.updateStateById(orderCarNewState, orderCar.getId());
            //提取数据
            successSet.add(orderCar.getNo());
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
            //出库日志
            csOrderCarLogService.asyncSave(orderCar, OrderCarLogEnum.OUT_STORE,
                    new String[]{MessageFormat.format(OrderCarLogEnum.OUT_STORE.getInnerLog(), orderCar.getNo(), waybillCar.getEndStoreName()),
                            MessageFormat.format(OrderCarLogEnum.OUT_STORE.getOutterLog(), orderCar.getNo(), waybillCar.getEndStoreName())},
                    userInfo);
        }
        //添加出库日志
        csStorageLogService.asyncSaveBatch(storageLogSet);
        //推送消息
        resultReasonVo.setFailList(failCarNoSet);
        resultReasonVo.setSuccessList(successSet);
        return BaseResultUtil.success(resultReasonVo);
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
            return BaseResultUtil.fail("运单已完结");
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
            if (waybillCar.getEndStoreId() == null) {
                failCarNoSet.add(new FailResultReasonVo(waybillCar.getOrderCarNo(), "车辆目的地不在业务中心, 不能入库"));
                continue;
            }

            //计算车辆状态
            OrderCarVo orderCarVo = orderCarDao.findExtraById(waybillCar.getOrderCarId());
            if (orderCarVo == null) {
                failCarNoSet.add(new FailResultReasonVo(waybillCar.getOrderCarNo(), "车辆不存在"));
                continue;
            }
            //当前状态
            int newState;
            //下一段是否调度
            int m = waybillCarDao.countByStartCityAndOrderCar(waybillCar.getEndCityCode(), waybillCar.getEndStoreId());
            //计算是否到达目的地城市范围
            if (waybillCar.getEndCityCode().equals(orderCarVo.getEndCityCode())) {
                //配送
                newState = m == 0 ? OrderCarStateEnum.WAIT_BACK_DISPATCH.code : OrderCarStateEnum.WAIT_BACK.code;
            } else {
                //干线
                newState = m == 0 ? OrderCarStateEnum.WAIT_TRUNK_DISPATCH.code : OrderCarStateEnum.WAIT_TRUNK.code;
            }

            //更新运单车辆状态
            waybillCarDao.updateStateById(waybillCar.getId(), WaybillCarStateEnum.UNLOADED.code);

            //更新车辆状态和所在位置
            OrderCar orderCar = new OrderCar();
            orderCar.setId(waybillCar.getOrderCarId());
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

            //添加物流日志
            csOrderCarLogService.asyncSave(orderCar, OrderCarLogEnum.IN_STORE,
                    new String[]{MessageFormat.format(OrderCarLogEnum.IN_STORE.getInnerLog(), orderCar.getNo(), waybillCar.getEndStoreName()),
                            MessageFormat.format(OrderCarLogEnum.IN_STORE.getOutterLog(), orderCar.getNo(), waybillCar.getEndStoreName())},
                    new UserInfo(paramsDto.getLoginId(), paramsDto.getLoginName(), paramsDto.getLoginPhone(), paramsDto.getLoginType()));
        }

        //验证任务是否完成
        int row = taskCarDao.countUnFinishByTaskId(paramsDto.getTaskId());
        if (row == 0) {
            //更新任务状态
            taskDao.updateStateById(task.getId(), TaskStateEnum.FINISHED.code);
            //验证运单是否完成
            int n = waybillCarDao.countUnFinishByWaybillId(task.getWaybillId());
            if (n == 0) {
                //更新运单状态
                waybillDao.updateStateById(WaybillStateEnum.FINISHED.code, waybill.getId());
            }
        }

        //写入入库日志
        csStorageLogService.asyncSaveBatch(storageLogSet);

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
            if (waybillCar.getState() < WaybillCarStateEnum.LOADED.code) {
                failCarNoSet.add(new FailResultReasonVo(waybillCar.getOrderCarNo(), "运单车辆还未装车", taskCarId));
                continue;
            }
            if (waybillCar.getState() >= WaybillCarStateEnum.UNLOADED.code) {
                failCarNoSet.add(new FailResultReasonVo(waybillCar.getOrderCarNo(), "运单车辆已经签收过", taskCarId));
                continue;
            }
            Order order = orderDao.findByCarId(waybillCar.getOrderCarId());
            customerPhoneSet.add(order.getBackContactPhone());

            waybillCarIdSet.add(waybillCar.getId());
            orderSet.add(order.getId());
            count++;
        }
        if (customerPhoneSet.size() > 1) {
            return BaseResultUtil.fail("批量收车不能同时包含多个收车人订单");
        }
        boolean flag = csSmsService.validateCaptcha(customerPhoneSet.iterator().next(), paramsDto.getCaptcha(), CaptchaTypeEnum.CONFIRM_RECEIPT, paramsDto.getClientEnum());
        if (!flag) {
            return BaseResultUtil.fail("收车码错误");
        }
        if (CollectionUtils.isEmpty(waybillCarIdSet)) {
            return BaseResultUtil.fail("没有可以卸车的车辆");
        }
        //验证任务是否完成
        int row = taskCarDao.countUnFinishByTaskId(paramsDto.getTaskId());
        if (row == 0) {
            //更新任务状态
            taskDao.updateStateById(task.getId(), TaskStateEnum.FINISHED.code);
            //验证运单是否完成
            int n = waybillCarDao.countUnFinishByWaybillId(task.getWaybillId());
            if (n == 0) {
                //更新运单状态
                waybillDao.updateForReceipt(task.getWaybillId(), System.currentTimeMillis());
            }
        }

        //验证订单是否完成
        orderSet.forEach(orderId -> {
            int rows = orderCarDao.countUnFinishByOrderId(orderId);
            if (rows <= 0) {
                //更新订单状态
                orderDao.updateForReceipt(orderId, System.currentTimeMillis());
            }
        });

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

        long currentTimeMillis = System.currentTimeMillis();
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
            if (PayStateEnum.PAID.code == orderCar.getWlPayState() || BigDecimal.ZERO.compareTo(orderCar.getTotalFee()) == 0) {
                failCarNoSet.add(new FailResultReasonVo(orderCarNo, "车辆，尚未支付"));
                continue;
            }
            //处理车辆相关运单和任务
            WaybillCar waybillCar = waybillCarDao.findWaitReceiptWaybill(orderCar.getId());
            if (waybillCar == null) {
                failCarNoSet.add(new FailResultReasonVo(orderCarNo, "车辆，尚未开始配送"));
                continue;
            }
            //更新车辆状态
            orderCarDao.updateForReceipt(orderCar.getId());
            //处理车辆相关运单车辆
            waybillCarDao.updateForReceipt(waybillCar.getId());

            //添加日志
            csOrderCarLogService.asyncSave(orderCar, OrderCarLogEnum.IN_STORE,
                    new String[]{MessageFormat.format(OrderCarLogEnum.IN_STORE.getInnerLog(), orderCar.getNo(), waybillCar.getEndStoreName()),
                            MessageFormat.format(OrderCarLogEnum.IN_STORE.getOutterLog(), orderCar.getNo(), waybillCar.getEndStoreName())},
                    new UserInfo(paramsDto.getLoginId(), paramsDto.getLoginName(), paramsDto.getLoginPhone(), paramsDto.getLoginType()));
            //提取数据
            orderIdSet.add(orderCar.getOrderId());
            waybillCarIdSet.add(waybillCar.getId());
            successSet.add(orderCar.getNo());

        }
        if (orderIdSet.size() > 1) {
            throw new ParameterException("暂不支持跨订单批量签收");
        }
        //用户信息
        UserInfo userInfo = new UserInfo(paramsDto.getLoginId(), paramsDto.getLoginName(), paramsDto.getLoginPhone(), paramsDto.getLoginType());
        if (CollectionUtils.isEmpty(orderIdSet)) {
            //处理订单
            orderIdSet.forEach(orderId -> {
                int count = orderCarDao.countUnFinishByOrderId(orderId);
                if (count <= 0) {
                    Order order = orderDao.selectById(orderId);
                    orderDao.updateForReceipt(orderId, currentTimeMillis);

                    //订单完成日志
                    orderLogService.asyncSave(order, OrderLogEnum.RECEIPT,
                            new String[]{MessageFormat.format(OrderLogEnum.RECEIPT.getInnerLog(), order.getNo()),
                                    MessageFormat.format(OrderLogEnum.RECEIPT.getOutterLog(), order.getNo())},
                            userInfo);
                }
            });

        }

        //是否处理任务
        if (!CollectionUtils.isEmpty(waybillCarIdSet)) {
            //处理任务
            List<Task> taskList = taskDao.findListByWaybillCarIds(waybillCarIdSet);
            taskList.forEach(task -> {
                int count = taskCarDao.countUnFinishByTaskId(task.getId());
                if (count == 0) {
                    //处理运单
                    int countw = waybillCarDao.countUnFinishByWaybillId(task.getWaybillId());
                    if (countw == 0) {
                        waybillDao.updateForReceipt(task.getWaybillId(), currentTimeMillis);
                    }
                }
            });
        }

        resultReasonVo.setSuccessList(successSet);
        resultReasonVo.setFailList(failCarNoSet);
        return BaseResultUtil.success(resultReasonVo);
    }

    /**
     * 车辆完成更新订单、运单、任务状态
     *
     * @param orderCarNoList 车辆编号列表
     * @param userInfo
     * @return
     * @author JPG
     * @date 2019/12/8 12:06
     */
    @Override
    public void updateForCarFinish(List<String> orderCarNoList, UserInfo userInfo) {

        //返回内容
        long currentTimeMillis = System.currentTimeMillis();
        Set<Long> orderIdSet = Sets.newHashSet();
        Set<Long> waybillCarIdSet = Sets.newHashSet();
        List<OrderCar> orderCarList = Lists.newArrayList();
        List<OrderCar> list = orderCarDao.findListByNos(orderCarNoList);
        for (OrderCar orderCar : list) {
            if (orderCar.getState() >= OrderCarStateEnum.SIGNED.code) {
                continue;
            }
            //更新车辆状态
            orderCarDao.updateForReceipt(orderCar.getId());
            //处理车辆相关运单车辆
            WaybillCar waybillCar = waybillCarDao.findWaitReceiptWaybill(orderCar.getId());
            if (waybillCar == null) {
                continue;
            }
            waybillCarDao.updateForReceipt(waybillCar.getId());

            //添加日志
            //            csOrderCarLogService.asyncSave(orderCar, OrderCarLogEnum.IN_STORE,
            //                    new String[]{MessageFormat.format(OrderCarLogEnum.IN_STORE.getInnerLog(), orderCar.getNo(), waybillCar.getEndStoreName()),
            //                            MessageFormat.format(OrderCarLogEnum.IN_STORE.getOutterLog(), orderCar.getNo(), waybillCar.getEndStoreName())},
            //                    userInfo);
            //            //提取数据
            orderIdSet.add(orderCar.getOrderId());
            orderCarList.add(orderCar);
            waybillCarIdSet.add(waybillCar.getId());
        }
        //处理订单
        if (!CollectionUtils.isEmpty(orderIdSet)) {
            for (Long orderId : orderIdSet) {
                int count = orderCarDao.countUnFinishByOrderId(orderId);
                if (count <= 0) {
                    Order order = orderDao.selectById(orderId);
                    orderDao.updateForReceipt(orderId, currentTimeMillis);

                    //订单完成日志
                    orderLogService.asyncSave(order, OrderLogEnum.RECEIPT,
                            new String[]{MessageFormat.format(OrderLogEnum.RECEIPT.getInnerLog(), order.getNo()),
                                    MessageFormat.format(OrderLogEnum.RECEIPT.getOutterLog(), order.getNo())},
                            userInfo);
                }
            }
        }
        //处理任务
        List<Task> taskList = taskDao.findListByWaybillCarIds(waybillCarIdSet);
        if (!CollectionUtils.isEmpty(taskList)) {
            taskList.forEach(task -> {
                int count = taskCarDao.countUnFinishByTaskId(task.getId());
                if (count == 0) {
                    //处理运单
                    int countw = waybillCarDao.countUnFinishByWaybillId(task.getWaybillId());
                    if (countw == 0) {
                        waybillDao.updateForReceipt(task.getWaybillId(), System.currentTimeMillis());
                    }
                }
            });
        }

    }

}
