package com.cjyc.web.api.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cjkj.common.redis.lock.RedisDistributedLock;
import com.cjkj.common.redis.template.StringRedisUtil;
import com.cjyc.common.model.dao.*;
import com.cjyc.common.model.dto.web.waybill.CysWaybillDto;
import com.cjyc.common.model.dto.web.waybill.*;
import com.cjyc.common.model.entity.*;
import com.cjyc.common.model.enums.*;
import com.cjyc.common.model.enums.order.OrderCarLocalStateEnum;
import com.cjyc.common.model.enums.order.OrderCarStateEnum;
import com.cjyc.common.model.enums.order.OrderCarTrunkStateEnum;
import com.cjyc.common.model.enums.order.OrderStateEnum;
import com.cjyc.common.model.enums.task.TaskStateEnum;
import com.cjyc.common.model.enums.waybill.*;
import com.cjyc.common.model.keys.RedisKeys;
import com.cjyc.common.model.util.BaseResultUtil;
import com.cjyc.common.model.vo.PageVo;
import com.cjyc.common.model.vo.ResultVo;
import com.cjyc.common.model.vo.web.city.FullCityVo;
import com.cjyc.common.model.vo.web.waybill.*;
import com.cjyc.web.api.exception.ParameterException;
import com.cjyc.web.api.exception.ServerException;
import com.cjyc.web.api.service.ISendNoService;
import com.cjyc.web.api.service.IWaybillService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * <p>
 * 运单表(业务员调度单) 服务实现类
 * </p>
 *
 * @author JPG
 * @since 2019-10-17
 */
@Service
@Transactional(rollbackFor = RuntimeException.class)
public class WaybillServiceImpl extends ServiceImpl<IWaybillDao, Waybill> implements IWaybillService {

    @Resource
    private IWaybillDao waybillDao;
    @Resource
    private IOrderCarDao orderCarDao;
    @Resource
    private IOrderDao orderDao;
    @Resource
    private StringRedisUtil redisUtil;
    @Resource
    private RedisDistributedLock redisLock;
    @Resource
    private ISendNoService sendNoService;
    @Resource
    private ICarrierDao carrierDao;
    @Resource
    private IAdminDao adminDao;
    @Resource
    private IWaybillCarDao waybillCarDao;
    @Resource
    private IDriverDao driverDao;
    @Resource
    private ITaskDao taskDao;
    @Resource
    private ITaskCarDao taskCarDao;
    @Resource
    private ICityDao cityDao;
    @Resource
    private IVehicleRunningDao vehicleRunningDao;
    @Resource
    private IVehicleDao vehicleDao;
    @Resource
    private IStoreDao storeDao;
    @Resource
    private IStoreCityConDao storeCityConDao;

    /**
     * 提送车调度
     *
     * @param paramsDto 参数
     */
    @Override
    public ResultVo localDispatch(LocalDispatchListWaybillDto paramsDto) {
        Long currentMillisTime = System.currentTimeMillis();
        Set<String> lockSet = new HashSet<>();
        try {
            List<LocalDispatchWaybillDto> list = paramsDto.getList();
            for (int i = 0; i < list.size(); i++) {
                int idx = i + 1;
                LocalDispatchWaybillDto dto = list.get(i);
                if (dto == null) {
                    continue;
                }
                String orderCarNo = dto.getOrderCarNo();
                Long orderCarId = dto.getOrderCarId();
                Long carrierId = dto.getCarrierId();

                //是否分配司机任务标识
                boolean isAllotDriver = false;
                /**验证运单信息*/
                //【验证】承运商是否可以运营
                Long driverId = null;
                String driverName = null;
                if (dto.getCarrierType() == WaybillCarrierType.CARRIER.code) {
                    Carrier carrier = carrierDao.selectById(carrierId);
                    if (carrier.getBusinessState() == null || carrier.getBusinessState() != 0) {
                        throw new ParameterException("序号为{0}运单,编号为{1}的车辆，所选承运商停运中", idx, orderCarNo);
                    }
                    //验证司机信息
                    if (carrier.getDriverNum() <= 1) {
                        Driver driver = driverDao.findTopByCarrierId(carrierId);
                        if (driver == null) {
                            throw new ParameterException("序号为{0}运单,编号为{1}的车辆，所选承运商没有运营中的司机", idx, orderCarNo);
                        }
                        isAllotDriver = true;
                        driverId = driver.getId();
                        driverName = driver.getName();
                    }
                } else if (dto.getCarrierType() == WaybillCarrierType.ADMIN.code) {
                    Admin admin = adminDao.selectById(carrierId);
                    if (admin == null || admin.getState() != AdminStateEnum.CHECKED.code) {
                        throw new ParameterException("序号为{0}运单,编号为{1}的车辆，所选业务员已离职", idx, orderCarNo);
                    }
                    isAllotDriver = true;
                    driverId = admin.getId();
                    driverName = admin.getName();
                } else if (dto.getCarrierType() == WaybillCarrierType.SELF.code) {
                    isAllotDriver = true;
                    driverId = dto.getLoadLinkUserId();
                    driverName = dto.getLoadLinkName();
                }

                /**验证运单车辆信息*/
                //加锁
                String lockKey = RedisKeys.getDispatchLock(orderCarNo);
                if (!redisLock.lock(lockKey)) {
                    throw new ParameterException("序号为{0}运单，编号为{1}的车辆，其他人正在调度", idx, orderCarNo);
                }
                lockSet.add(lockKey);

                //【验证】订单车辆状态
                OrderCar orderCar = orderCarDao.selectById(orderCarId);
                if (orderCar == null) {
                    throw new ParameterException("序号为{0}运单,编号为{1}的车辆，车辆所属订单车辆不存在", idx, orderCarNo);
                }
                if (orderCar.getState() == null
                        || (paramsDto.getType() == WaybillTypeEnum.PICK.code && orderCar.getPickState() <= OrderCarLocalStateEnum.WAIT_DISPATCH.code)
                        || (paramsDto.getType() == WaybillTypeEnum.BACK.code && orderCar.getBackState() <= OrderCarLocalStateEnum.WAIT_DISPATCH.code)) {
                    throw new ParameterException("序号为{0}运单,编号为{1}的车辆，当前车辆状态不能提车/配送调度", idx, orderCarNo);
                }
                //【验证】订单状态
                Order order = orderDao.selectById(orderCar.getId());
                if (order == null) {
                    throw new ParameterException("序号为{0}运单,编号为{1}的车辆，当前车辆不存在", idx, orderCarNo);
                }
                if (order.getState() == null
                        || order.getState() < OrderStateEnum.CHECKED.code
                        || order.getState() > OrderStateEnum.FINISHED.code) {
                    throw new ParameterException("序号为{0}运单,编号为{1}的车辆，车辆所属订单状态不能提车/配送调度", idx, orderCarNo);
                }
                //【验证】是否调度过，提送车只能有效执行一次
                if (paramsDto.getType() == WaybillTypeEnum.PICK.code) {
                    int n = waybillDao.countWaybill(orderCarId, WaybillTypeEnum.PICK.code);
                    if (n > 0) {
                        throw new ParameterException("序号为{0}运单,编号为{1}的车辆，已经调度过提车运单", idx, orderCarNo);
                    }
                } else if (paramsDto.getType() == WaybillTypeEnum.BACK.code) {
                    int n = waybillDao.countWaybill(orderCarId, WaybillTypeEnum.BACK.code);
                    if (n > 0) {
                        throw new ParameterException("序号为{0}运单,编号为{1}的车辆，已经调度过送车运单", idx, orderCarNo);
                    }
                } else {
                    throw new ParameterException("序号为{0}运单,编号为{1}的车辆，无法识别的运单类型", idx, orderCarNo);
                }

                //【验证】配送调度，需验证干线调度是否完成
                if (paramsDto.getType() == WaybillTypeEnum.BACK.code) {
                    WaybillCar waybillCar = waybillCarDao.findLastTrunkWaybillCar(order.getEndCityCode(), orderCarId);
                    if (!waybillCar.getEndCityCode().equals(order.getEndCityCode())) {
                        throw new ParameterException("序号为{0}运单,编号为{1}的车辆，干线尚未调度完成", idx, orderCarNo);
                    }
                }

                /**1、添加运单信息*/
                Waybill waybill = new Waybill();
                //发号
                waybill.setNo(sendNoService.getNo(SendNoTypeEnum.ORDER));
                waybill.setType(paramsDto.getType());
                //承运商类型
                waybill.setSource(paramsDto.getUserId().equals(carrierId) ? WaybillSourceEnum.SELF.code : WaybillSourceEnum.MANUAL.code);
                waybill.setCarrierId(carrierId);
                waybill.setCarrierType(dto.getCarrierType());
                waybill.setCarNum(1);
                waybill.setState(WaybillStateEnum.WAIT_ALLOT_CONFIRM.code);
                //提送车费用逻辑，调度时不允许修改提送车费用，需要到订单中修改提送车费用，多则返还，少则后补
                waybill.setFreightFee(orderCar.getPickFee());
                waybill.setRemark(dto.getRemark());
                waybill.setCreateTime(currentMillisTime);
                waybill.setCreateUser(paramsDto.getUserName());
                waybill.setCreateUserId(paramsDto.getUserId());
                waybillDao.insert(waybill);

                /**2、添加运单车辆信息*/
                WaybillCar waybillCar = new WaybillCar();
                //初始copy赋值
                BeanUtils.copyProperties(dto, waybillCar);
                waybillCar.setWaybillId(waybill.getId());
                waybillCar.setWaybillNo(waybill.getNo());
                waybillCar.setFreightFee(paramsDto.getType() == WaybillTypeEnum.PICK.code ? orderCar.getPickFee() : orderCar.getPickFee());
                waybillCar.setSort(1);
                //地址赋值
                FullCityVo startFullCityVo = cityDao.findFullCityVo(dto.getStartAreaCode());
                copyWaybillStartCity(startFullCityVo, waybillCar);
                FullCityVo endFullCityVo = cityDao.findFullCityVo(dto.getEndAreaCode());
                copyWaybillEndCity(endFullCityVo, waybillCar);
                //运单车辆状态
                waybillCar.setState(WaybillCarStateEnum.WAIT_ALLOT.code);
                if (isAllotDriver) {
                    waybillCar.setState(WaybillCarStateEnum.ALLOTED.code);
                }
                waybillCar.setExpectStartTime(dto.getExpectStartTime());
                //TODO 计算预计到达时间，计算线路是否存在
                waybillCar.setCreateTime(currentMillisTime);
                waybillCarDao.insert(waybillCar);

                /**3、添加任务信息*/
                if (isAllotDriver) {
                    //只有一个司机添加任务信息
                    Task task = new Task();
                    task.setNo(sendNoService.getNo(SendNoTypeEnum.TASK));
                    task.setWaybillId(waybill.getId());
                    task.setCarNum(1);
                    task.setState(TaskStateEnum.WAIT_ALLOT_CONFIRM.code);
                    task.setDriverId(driverId);
                    task.setDriverName(driverName);
                    //添加运力信息
                    if (dto.getCarrierType() == WaybillCarrierType.CARRIER.code) {
                        VehicleRunning vehicleRunning = vehicleRunningDao.findByDriverId(driverId);
                        if (vehicleRunning != null) {
                            task.setVehicleRunningId(vehicleRunning.getId());
                            task.setVehiclePlateNo(vehicleRunning.getPlateNo());
                        }
                    }
                    task.setCreateTime(currentMillisTime);
                    task.setCreateUser(paramsDto.getUserName());
                    task.setCreateUserId(paramsDto.getUserId());
                    taskDao.insert(task);

                    /**4、插入任务车辆关联表*/
                    TaskCar taskCar = new TaskCar();
                    taskCar.setTaskId(task.getId());
                    taskCar.setWaybillCarId(waybillCar.getId());
                    taskCarDao.insert(taskCar);

                    /**5、更新订单车辆状态*/
                    orderCarDao.updatePickStateById(
                            paramsDto.getType() == WaybillTypeEnum.PICK.code ? OrderCarLocalStateEnum.DISPATCHED.code : OrderCarLocalStateEnum.DISPATCHED.code,
                            orderCarId);
                }

            }
        } finally {
            for (String key : lockSet) {
                redisLock.releaseLock(key);
            }
        }
        return BaseResultUtil.success();
    }

    /**
     * 干线调度
     *
     * @param paramsDto 参数
     * @author JPG
     * @since 2019/10/17 9:16
     */
    @Override
    public ResultVo trunkDispatch(TrunkDispatchListShellWaybillDto paramsDto) {

        Long currentMillisTime = System.currentTimeMillis();
        Long userId = paramsDto.getUserId();
        Set<String> lockSet = new HashSet<>();
        //【验证参数】操作人
        Admin admin = adminDao.findByUserId(userId);
        if (admin == null || admin.getState() != AdminStateEnum.CHECKED.code) {
            return BaseResultUtil.fail("当前业务员，不在职");
        }
        //【验证参数】业务中心ID
        Store store = storeDao.selectById(paramsDto.getStoreId());
        List<String> areaList = storeDao.findAreaBizScope(store.getId());
        // TODO 验证用户角色
        try {
            List<TrunkDispatchListWaybillDto> list = paramsDto.getList();
            //多运单循环
            for (int i = 0; i < list.size(); i++) {
                TrunkDispatchListWaybillDto dtoList = list.get(i);
                if (dtoList == null) {
                    continue;
                }
                List<TrunkDispatchWaybillDto> subList = dtoList.getList();
                if (subList == null || subList.isEmpty()) {
                    continue;
                }
                Long carrierId = dtoList.getCarrierId();

                //标记位置用
                int idx = i + 1;
                //是否分配任务
                boolean isAllotDriver = false;
                //【验证】承运商和司机信息
                Carrier carrier = carrierDao.selectById(carrierId);
                if (carrier.getBusinessState() == null || carrier.getBusinessState() != 0) {
                    throw new ParameterException("序号为{0}运单，所选承运商，停运中", idx);
                }
                Driver driver = new Driver();
                if (carrier.getDriverNum() <= 1) {
                    driver = driverDao.findTopByCarrierId(carrierId);
                    if (driver == null) {
                        throw new ParameterException("序号为{0}运单，所选承运商没有运营中的司机", idx);
                    }
                    isAllotDriver = true;
                }

                /**1、组装运单信息*/
                Waybill waybill = new Waybill();
                waybill.setNo(sendNoService.getNo(SendNoTypeEnum.ORDER));
                waybill.setType(WaybillTypeEnum.PICK.code);
                waybill.setSource(userId.equals(carrierId) ? WaybillSourceEnum.SELF.code : WaybillSourceEnum.MANUAL.code);
                waybill.setGuideLine(dtoList.getGuideLine());
                waybill.setRecommendLine(dtoList.getRecommendLine());
                waybill.setCarrierId(carrierId);
                waybill.setCarrierType(0);
                waybill.setCarNum(1);
                waybill.setState(WaybillStateEnum.WAIT_ALLOT_CONFIRM.code);
                //提送车费用逻辑，调度时不允许修改提送车费用，需要到订单中修改提送车费用，多则返还，少则后补
                waybill.setFreightFee(dtoList.getFreightFee());
                waybill.setCreateTime(currentMillisTime);
                waybill.setCreateUser(admin.getName());
                waybill.setCreateUserId(admin.getId());
                waybillDao.insert(waybill);

                //承运商有且仅有一个司机
                Task task = null;
                if (carrier.getDriverNum() == 1) {
                    /**1+、写入任务表*/
                    task = new Task();
                    task.setNo(sendNoService.getNo(SendNoTypeEnum.TASK));
                    task.setWaybillId(waybill.getId());
                    task.setCarNum(1);
                    task.setState(TaskStateEnum.WAIT_ALLOT_CONFIRM.code);
                    task.setDriverId(driver.getId());
                    task.setDriverName(driver.getName());
                    //查询运力
                    VehicleRunning vehicleRunning = vehicleRunningDao.getVehiRunByDriverId(driver.getId());
                    if (vehicleRunning != null) {
                        task.setVehicleRunningId(vehicleRunning.getId());
                        task.setVehiclePlateNo(vehicleRunning.getPlateNo());
                    }
                    task.setCreateTime(currentMillisTime);
                    task.setCreateUser(admin.getName());
                    task.setCreateUserId(admin.getCreateUserId());
                    taskDao.insert(task);
                }

                /**2、运单，车辆循环*/
                //合计运费
                BigDecimal sumFreightFee = BigDecimal.ZERO;
                for (TrunkDispatchWaybillDto dto : subList) {
                    if (dto == null) {
                        continue;
                    }
                    String orderCarNo = dto.getOrderCarNo();
                    Long orderCarId = dto.getOrderCarId();

                    //加锁
                    String lockKey = RedisKeys.getDispatchLock(orderCarNo);
                    if (!redisLock.lock(lockKey, 20000, 100, 300L)) {
                        throw new ParameterException("序号为{0}运单，编号为{1}的车辆，其他人正在调度", idx, orderCarNo);
                    }
                    lockSet.add(lockKey);

                    //验证订单车辆状态
                    OrderCar orderCar = orderCarDao.selectById(orderCarId);
                    if (orderCar == null) {
                        throw new ParameterException("序号为{0}运单，编号为{1}的车辆，不存在", idx, orderCarNo);
                    }
                    if (orderCar.getState() == null) {
                        throw new ParameterException("序号为{0}运单，编号为{1}的车辆，无法提车调度", idx, orderCarNo);
                    }
                    //验证订单状态
                    Order order = orderDao.selectById(orderCar.getId());
                    if (order == null) {
                        throw new ParameterException("序号为{0}运单，编号为{1}的车辆，所属订单车辆不存在", idx, orderCarNo);
                    }

                    if (order.getState() == null
                            || order.getState() < OrderStateEnum.CHECKED.code
                            || order.getState() > OrderStateEnum.FINISHED.code) {
                        throw new ParameterException("序号为{0}运单，编号为{1}的车辆，所属订单状态无法提车调度", idx, orderCarNo);
                    }

                    //验证是否已经调度过,已经调度的为,暂不支持同城干线调度
                    int n = waybillCarDao.countForValidateRepeatTrunkDisPatch(areaList);
                    if (n > 0) {
                        throw new ParameterException("序号为{0}运单，编号为{1}的车辆，已经调度过", idx, orderCarNo);
                    }

                    //验证出发地与上一次调度目的地是否一致
                    int sort = 1;
                    WaybillCar prevWaybillCar = waybillCarDao.findLastPrevByArea(orderCarId, areaList);
                    if (prevWaybillCar != null) {
                        if (!prevWaybillCar.getEndAddress().equals(dto.getStartAddress())) {
                            throw new ServerException("本次调度出发地址与上次调度结束地址不一致");
                        }
                        sort = prevWaybillCar.getSort() + 1;
                    }

                    WaybillCar waybillCar = new WaybillCar();
                    BeanUtils.copyProperties(dto, waybillCar);
                    waybillCar.setWaybillId(waybill.getId());
                    waybillCar.setWaybillNo(waybill.getNo());
                    waybillCar.setOrderCarId(orderCar.getId());
                    //查询上段sort
                    waybillCar.setSort(sort);
                    //地址赋值
                    FullCityVo sVo = cityDao.findFullCityVo(dto.getStartAreaCode());
                    FullCityVo eVo = cityDao.findFullCityVo(dto.getEndAreaCode());
                    copyWaybillStartCity(sVo, waybillCar);
                    copyWaybillEndCity(eVo, waybillCar);
                    waybillCar.setState(WaybillCarStateEnum.WAIT_ALLOT.code);
                    if (isAllotDriver) {
                        waybillCar.setState(WaybillCarStateEnum.ALLOTED.code);
                    }
                    waybillCar.setTakeType(2);
                    waybillCar.setCreateTime(currentMillisTime);
                    waybillCarDao.insert(waybillCar);

                    sumFreightFee = sumFreightFee.add(dto.getFreightFee());
                    //承运商有且仅有一个司机
                    if (isAllotDriver) {
                        /**2+、组装任务车辆表数据*/
                        TaskCar taskCar = new TaskCar();
                        taskCar.setTaskId(task.getId());
                        taskCar.setWaybillCarId(waybillCar.getId());
                        taskCarDao.insert(taskCar);
                    }

                    //更新订单车辆状态
                    orderCarDao.updateTrunkStateById(orderCar.getId());

                }
            }
        } finally {
            for (String key : lockSet) {
                redisLock.releaseLock(key);
            }
        }
        return BaseResultUtil.success();
    }

    private void copyWaybillStartCity(FullCityVo vo, WaybillCar waybillCar) {
        if (vo == null) {
            return;
        }
        waybillCar.setStartProvince(vo.getProvince());
        waybillCar.setStartProvinceCode(vo.getProvinceCode());
        waybillCar.setStartCity(vo.getCity());
        waybillCar.setStartCityCode(vo.getCityCode());
        waybillCar.setStartArea(vo.getArea());
        waybillCar.setStartAreaCode(vo.getAreaCode());
    }

    private void copyWaybillEndCity(FullCityVo vo, WaybillCar waybillCar) {
        if (vo == null) {
            return;
        }
        waybillCar.setEndProvince(vo.getProvince());
        waybillCar.setEndProvinceCode(vo.getProvinceCode());
        waybillCar.setEndCity(vo.getCity());
        waybillCar.setEndCityCode(vo.getCityCode());
        waybillCar.setEndArea(vo.getArea());
        waybillCar.setEndAreaCode(vo.getAreaCode());
    }

    @Override
    public ResultVo cancelDispatch(CancelDispatchDto paramsDto) {
        Long userId = paramsDto.getUserId();
        //验证用户
        Admin admin = adminDao.findByUserId(userId);
        if (admin == null || admin.getState() != AdminStateEnum.CHECKED.code) {
            return BaseResultUtil.fail("当前业务员，不在职");
        }
        // TODO 验证用户角色
        //取消运单
        boolean isAllowCancel = true;
        List<Long> cancelWaybillIdList = new ArrayList<>();
        List<String> waybillNoList = paramsDto.getWaybillNoList();
        List<Waybill> waybillList = waybillDao.findListByNos(waybillNoList);
        StringBuilder nos = new StringBuilder();
        for (Waybill waybill : waybillList) {
            if (waybill == null) {
                continue;
            }

            //取消运单
            cancelWaybill(waybill);

        }
        //更新运单主单状态
        waybillDao.updateStateBatchByNos(WaybillStateEnum.F_CANCEL.code, waybillNoList);
        //是否改变任务状态
        taskDao.updateListByWaybillIds(TaskStateEnum.F_CANCEL.code, cancelWaybillIdList);
        return BaseResultUtil.success();
    }

    private void cancelWaybill(Waybill waybill) {

        //状态不大于待承接
        if (waybill.getState() > WaybillStateEnum.WAIT_ALLOT_CONFIRM.code) {
            throw new ServerException("运单{0},当前状态不能取消", waybill);
        }
        //修改运单主单状态
        waybill.setState(WaybillStateEnum.F_CANCEL.code);
        waybillDao.updateById(waybill);
        //修改任务主单状态
        taskDao.cancelBywaybillId(TaskStateEnum.F_CANCEL.code, waybill.getId());

        //修改车辆状态
        List<OrderCar> list = orderCarDao.findListByWaybillId(waybill.getId());
        if (list == null || list.isEmpty()) {
            return;
        }
        for (OrderCar orderCar : list) {
            if (orderCar == null) {
                continue;
            }
            updateOrderCarDispatchState(waybill, orderCar);
        }
        //更新车辆下一环节调度单状态
        List<WaybillCar> waybillCarlist = waybillCarDao.findListByWaybillId(waybill.getId());
        for (WaybillCar waybillCar : waybillCarlist) {
            if (waybillCar == null) {
                continue;
            }
            updateNextForCancelDispatch(waybillCar.getOrderCarId(), waybillCar.getEndStoreId());

        }
    }

    /**
     * 取消调度-修改后续调度调度车辆状态
     *
     * @param orderCarId
     * @param endStoreId
     */
    private void updateNextForCancelDispatch(Long orderCarId, Long endStoreId) {
        List<WaybillCar> nextWaybillCarList = waybillCarDao.findNextDispatch(orderCarId, endStoreId);
        if (nextWaybillCarList != null || !nextWaybillCarList.isEmpty()) {
            waybillCarDao.updateForCancelDispatch(orderCarId, endStoreId, WaybillCarStateEnum.CANCEL_DISPATCH.code);
            //TODO 修改运单车辆数
            for (WaybillCar waybillCar : nextWaybillCarList) {
                if (waybillCar == null) {
                    continue;
                }
                updateNextForCancelDispatch(orderCarId, waybillCar.getEndStoreId());
            }
            //TODO 给调度人和承运商发送变更消息
        }

        waybillCarDao.updateForCancelDispatch(orderCarId, endStoreId, WaybillCarStateEnum.CANCEL_DISPATCH.code);
    }

    /**
     * 修改订单车辆调度状态
     *
     * @param waybill
     * @param orderCar
     * @author JPG
     * @since 2019/10/29 9:57
     */
    private void updateOrderCarDispatchState(Waybill waybill, OrderCar orderCar) {
        //查询订单是否已经完结
        Order order = orderDao.selectById(orderCar.getOrderId());
        if (order == null || order.getState() >= OrderStateEnum.FINISHED.code) {
            return;
        }
        //修改车辆状态
        if (waybill.getType() == WaybillTypeEnum.PICK.code) {
            //订单车辆状态
            if (orderCar.getState() == OrderCarStateEnum.WAIT_PICK.code) {
                orderCar.setState(OrderCarStateEnum.WAIT_PICK_DISPATCH.code);
            }
            //订单车辆提车状态
            orderCar.setPickState(OrderCarLocalStateEnum.WAIT_DISPATCH.code);
        }
        if (waybill.getType() == WaybillTypeEnum.BACK.code) {
            //订单车辆状态
            if (orderCar.getState() == OrderCarStateEnum.WAIT_BACK.code) {
                orderCar.setState(OrderCarStateEnum.WAIT_BACK_DISPATCH.code);
            }
            //订单车辆配送状态
            orderCar.setBackState(OrderCarLocalStateEnum.WAIT_DISPATCH.code);
        }
        if (waybill.getType() == WaybillTypeEnum.BACK.code) {
            int trunkState = OrderCarLocalStateEnum.WAIT_DISPATCH.code;
            //根据订单车辆所有运单状态判断订单车辆干线状态
            List<Integer> waybillStateList = waybillDao.findTrunkStateListByOrderCarId(orderCar.getId());
            if (waybillStateList != null || !waybillStateList.isEmpty()) {
                if (waybillStateList.contains(WaybillStateEnum.FINISHED.code)) {
                    trunkState = OrderCarTrunkStateEnum.WAIT_NEXT_DISPATCH.code;
                    if (orderCar.getNowStoreId().equals(order.getEndStoreId())
                            || orderCar.getNowAreaCode().equals(order.getEndAreaCode())) {
                        trunkState = OrderCarTrunkStateEnum.DISPATCHED.code;
                    }
                }
                if (waybillStateList.contains(WaybillStateEnum.WAIT_ALLOT_CONFIRM.code)
                        || waybillStateList.contains(WaybillStateEnum.ALLOT_CONFIRM.code)) {
                    trunkState = OrderCarTrunkStateEnum.WAIT_NEXT_DISPATCH.code;
                }
                if (waybillStateList.contains(WaybillStateEnum.TRANSPORTING.code)) {
                    trunkState = OrderCarTrunkStateEnum.WAIT_NEXT_DISPATCH.code;
                }
            }

            //订单车辆状态
            if (orderCar.getState() == OrderCarStateEnum.WAIT_TRUNK.code) {
                orderCar.setState(OrderCarStateEnum.WAIT_TRUNK_DISPATCH.code);
            }
            //订单车辆干线状态
            orderCar.setTrunkState(trunkState);

        }
        orderCarDao.updateById(orderCar);
    }

    @Override
    public ResultVo<List<HistoryListWaybillVo>> historyList(HistoryListWaybillDto paramsDto) {
        List<HistoryListWaybillVo> list = waybillDao.findHistoryList(paramsDto);
        return BaseResultUtil.success(list);
    }

    @Override
    public ResultVo<List<CysWaybillVo>> cysList(CysWaybillDto reqDto) {

        return null;
    }

    @Override
    public ResultVo<PageVo<LocalListWaybillCarVo>> Locallist(LocalListWaybillCarDto paramsDto) {
        PageHelper.startPage(paramsDto.getCurrentPage(), paramsDto.getPageSize(), true);
        List<LocalListWaybillCarVo> list = waybillCarDao.findListLocal(paramsDto);
        PageInfo<LocalListWaybillCarVo> pageInfo = new PageInfo<>(list);
        if(paramsDto.getCurrentPage() > pageInfo.getPages()){
            pageInfo.setList(null);
        }
        return BaseResultUtil.success(pageInfo);
    }

    @Override
    public ResultVo<PageVo<TrunkListWaybillVo>> trunklist(TrunkListWaybillDto paramsDto) {

        PageHelper.startPage(paramsDto.getCurrentPage(), paramsDto.getPageSize(), true);
        List<TrunkListWaybillVo> list = waybillDao.findListTrunk(paramsDto);
        PageInfo<TrunkListWaybillVo> pageInfo = new PageInfo<>(list);
        if(paramsDto.getCurrentPage() > pageInfo.getPages()){
            pageInfo.setList(null);
        }
        return BaseResultUtil.success(pageInfo);
    }

    @Override
    public ResultVo<PageVo<TrunkListWaybillCarVo>> trunkCarlist(TrunkListWaybillCarDto paramsDto) {
        PageHelper.startPage(paramsDto.getCurrentPage(), paramsDto.getPageSize(), true);
        List<TrunkListWaybillCarVo> list = waybillCarDao.findListTrunk(paramsDto);
        PageInfo<TrunkListWaybillCarVo> pageInfo = new PageInfo<>(list);
        if(paramsDto.getCurrentPage() > pageInfo.getPages()){
            pageInfo.setList(null);
        }
        return BaseResultUtil.success(pageInfo);
    }

    @Override
    public ResultVo<GetWaybillVo> get(Long id) {

        Waybill waybill = waybillDao.selectById(id);
        List<WaybillCar> listCar = waybillCarDao.findListByWaybillId(id);
        return null;
    }

    @Override
    public ResultVo<List<GetWaybillCarVo> > getCarByType(Long orderCarId, Integer waybillType) {
       List<GetWaybillCarVo> list =waybillCarDao.findVoByType(orderCarId, waybillType);

        return BaseResultUtil.success(list);
    }

}
