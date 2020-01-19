package com.cjyc.common.system.service.impl;

import com.cjkj.common.redis.lock.RedisDistributedLock;
import com.cjkj.common.redis.template.StringRedisUtil;
import com.cjyc.common.model.dao.*;
import com.cjyc.common.model.dto.web.waybill.*;
import com.cjyc.common.model.entity.*;
import com.cjyc.common.model.entity.defined.CarrierInfo;
import com.cjyc.common.model.entity.defined.FullCity;
import com.cjyc.common.model.entity.defined.UserInfo;
import com.cjyc.common.model.enums.AdminStateEnum;
import com.cjyc.common.model.enums.CommonStateEnum;
import com.cjyc.common.model.enums.SendNoTypeEnum;
import com.cjyc.common.model.enums.city.CityLevelEnum;
import com.cjyc.common.model.enums.order.*;
import com.cjyc.common.model.enums.transport.CarrierTypeEnum;
import com.cjyc.common.model.enums.waybill.*;
import com.cjyc.common.model.exception.ParameterException;
import com.cjyc.common.model.exception.ServerException;
import com.cjyc.common.model.keys.RedisKeys;
import com.cjyc.common.model.util.BaseResultUtil;
import com.cjyc.common.model.util.LocalDateTimeUtil;
import com.cjyc.common.model.util.MoneyUtil;
import com.cjyc.common.model.util.TimeStampUtil;
import com.cjyc.common.model.vo.ResultVo;
import com.cjyc.common.system.service.*;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.MessageFormat;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 运单业务类
 *
 * @author JPG
 */
@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
public class CsWaybillServiceImpl implements ICsWaybillService {
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
    private ICsSendNoService sendNoService;
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
    private ICsCityService csCityService;
    @Resource
    private IVehicleRunningDao vehicleRunningDao;
    @Resource
    private ICsStoreService csStoreService;
    @Resource
    private ICsTaskService csTaskService;
    @Resource
    private ICsLineService csLineService;
    @Resource
    private ICsAdminService csAdminService;
    @Resource
    private ICsPingPayService csPingPayService;

    /**
     * 同城调度
     *
     * @param paramsDto
     * @author JPG
     * @since 2019/11/5 17:07
     */
    @Override
    public ResultVo saveLocal(SaveLocalDto paramsDto) {
        Set<String> lockSet = new HashSet<>();
        try {
            Long currentMillisTime = System.currentTimeMillis();
            List<SaveLocalWaybillDto> list = paramsDto.getList();
            for (SaveLocalWaybillDto dto : list) {
                if (dto == null) {
                    continue;
                }
                String orderCarNo = dto.getOrderCarNo();
                Long orderCarId = dto.getOrderCarId();
                Long carrierId = dto.getCarrierId();

                //是否分配司机任务标识
                CarrierInfo carrierInfo = validateLocalCarrierInfo(carrierId, dto.getCarrierName(), dto.getCarrierType(), paramsDto.getType(),
                        new UserInfo(dto.getLoadLinkUserId(), dto.getLoadLinkName(), dto.getLoadLinkPhone()),
                        new UserInfo(dto.getUnloadLinkUserId(), dto.getUnloadLinkName(), dto.getUnloadLinkPhone()),
                        orderCarNo);

                /**验证运单车辆信息*/
                //加锁
                String lockKey = RedisKeys.getDispatchLock(orderCarNo);
                if (!redisLock.lock(lockKey)) {
                    throw new ParameterException("车辆{0}，其他人正在调度", orderCarNo);
                }
                lockSet.add(lockKey);

                //【验证】订单车辆状态
                OrderCar orderCar = orderCarDao.selectById(orderCarId);
                if (orderCar == null) {
                    throw new ParameterException("车辆{0}，车辆所属订单车辆不存在", orderCarNo);
                }

                if ((paramsDto.getType() == WaybillTypeEnum.PICK.code && orderCar.getPickState() > OrderCarLocalStateEnum.WAIT_DISPATCH.code)
                        || (paramsDto.getType() == WaybillTypeEnum.BACK.code && orderCar.getBackState() > OrderCarLocalStateEnum.WAIT_DISPATCH.code)) {
                    throw new ParameterException("车辆{0}，当前车辆状态不能提车/配送调度", orderCarNo);
                }
                //【验证】订单状态
                Order order = orderDao.selectById(orderCar.getOrderId());
                if (order == null) {
                    throw new ParameterException("车辆{0}，当前车辆不存在", orderCarNo);
                }
                if (order.getState() == null
                        || order.getState() < OrderStateEnum.CHECKED.code
                        || order.getState() > OrderStateEnum.FINISHED.code) {
                    throw new ParameterException("车辆{0}，车辆所属订单未确认或已结束不能提车/配送调度", orderCarNo);
                }
                //验证提车联系人
                if (paramsDto.getType() == WaybillTypeEnum.PICK.code) {
                    if (!order.getPickContactPhone().equals(dto.getLoadLinkPhone())) {
                        throw new ParameterException("车辆{0}，提车人信息与订单不一致", orderCarNo);
                    }
                } else {
                    if (!order.getBackContactPhone().equals(dto.getUnloadLinkPhone())) {
                        throw new ParameterException("车辆{0}，收车人信息与订单不一致", orderCarNo);
                    }
                }
                //【验证】是否调度过，提送车只能有效执行一次
                if (paramsDto.getType() == WaybillTypeEnum.PICK.code) {
                    int n = waybillDao.countWaybill(orderCarId, WaybillTypeEnum.PICK.code);
                    if (n > 0) {
                        throw new ParameterException("车辆{0}，已经调度过提车运单", orderCarNo);
                    }
                } else {
                    int n = waybillDao.countWaybill(orderCarId, WaybillTypeEnum.BACK.code);
                    if (n > 0) {
                        throw new ParameterException("车辆{0}，已经调度过送车运单", orderCarNo);
                    }
                }
                //【验证】提车调度，是否已经调度干线
                /*if(paramsDto.getType() == WaybillTypeEnum.PICK.code){
                    WaybillCar waybillCar = waybillCarDao.findFirstTrunkWaybillCar(orderCarId);
                    if(waybillCar != null && !waybillCar.getStartAddress().equals(dto.getEndAddress())){
                        throw new ParameterException("车辆{0}，提车运单交车地址与第一段干线地址不一致", orderCarNo);
                    }
                }*/
                //【验证】配送调度，需验证干线调度是否完成
                if (paramsDto.getType() == WaybillTypeEnum.BACK.code) {
                    WaybillCar waybillCar = waybillCarDao.findLastTrunkWaybillCar(order.getEndCityCode(), orderCarId);
                    if (!validateIsArriveEndCity(order, waybillCar)) {
                        throw new ParameterException("车辆{0}，尚未到达目的地所属业务中心或目的地城市范围内", orderCarNo);
                    }
                }
                if(dto.getStartAddress().equals(dto.getEndAddress()) && dto.getStartAreaCode().equals(dto.getEndAreaCode())){
                    throw new ParameterException("起始地与目的地不能相同", orderCarNo);
                }
                //TODO 验证提车和送车人是否与订单一致

                /**1、添加运单信息*/
                Waybill waybill = new Waybill();
                waybill.setNo(sendNoService.getNo(SendNoTypeEnum.WAYBILL));
                waybill.setType(paramsDto.getType());
                //承运商类型
                waybill.setSource(WaybillSourceEnum.MANUAL.code);
                waybill.setCarrierId(carrierInfo.getCarrierId());
                waybill.setCarrierName(carrierInfo.getCarrierName());
                waybill.setCarrierType(carrierInfo.getCarryType());
                waybill.setCarNum(1);
                waybill.setState(getWaybillState(carrierInfo.getCarryType()));
                //提送车费用逻辑，调度时不允许修改提送车费用，需要到订单中修改提送车费用，多则返还，少则后补
                waybill.setFreightFee(getLocalWaybillFreightFee(waybill, orderCar));
                waybill.setRemark(dto.getRemark());
                waybill.setCreateTime(currentMillisTime);
                waybill.setCreateUser(paramsDto.getLoginName());
                waybill.setCreateUserId(paramsDto.getLoginId());
                waybill.setFixedFreightFee(false);
                waybill.setInputStoreId(getLocalWaybillInputStoreId(waybill.getType(), order));
                //waybill.setGuideLine();
                waybillDao.insert(waybill);

                /**2、添加运单车辆信息*/
                WaybillCar waybillCar = new WaybillCar();
                BeanUtils.copyProperties(dto, waybillCar);
                waybillCar.setWaybillId(waybill.getId());
                waybillCar.setWaybillNo(waybill.getNo());
                waybillCar.setFreightFee(waybill.getFreightFee());
                //城市信息赋值
                fillWaybillCarCityInfo(waybillCar);
                //业务中心信息赋值
                fillWaybillCarBelongStoreInfo(waybillCar);
                //提送车业务员
                fillWaybillCarAdmin(waybillCar, waybill.getType());
                //计算预计到达时间
                fillWaybillCarExpectEndTime(waybillCar);
                waybillCar.setReceiptFlag(waybillCar.getUnloadLinkPhone().equals(order.getBackContactPhone()));
                //运单车辆状态
                waybillCar.setState(getWaybillCarState(waybill, carrierInfo));
                if (waybill.getCarrierType() == WaybillCarrierTypeEnum.SELF.code) {
                    waybillCar.setLoadTime(currentMillisTime);
                }
                waybillCar.setCreateTime(currentMillisTime);
                waybillCarDao.insert(waybillCar);

                /**3、添加任务信息*/
                csTaskService.reCreate(waybill, Lists.newArrayList(waybillCar), Lists.newArrayList(waybillCar), carrierInfo);

                /**5、更新订单车辆状态*/
                updateOrderCarForDispatchLocal(orderCar.getId(), waybill, orderCar.getState());
            }
            return BaseResultUtil.success();
        } finally {
            if (!CollectionUtils.isEmpty(lockSet)) {
                redisUtil.del(lockSet.toArray(new String[0]));
            }
        }
    }


    /**
     * 修改同城运单
     *
     * @param paramsDto
     * @author JPG
     * @since 2019/11/9 8:59
     */
    @Override
    public ResultVo updateLocal(UpdateLocalDto paramsDto) {
        //历史数据
        //FullWaybill oldFw = waybillDao.findFullWaybillById(paramsDto.getCarDto().getWaybillId());

        Set<String> lockSet = new HashSet<>();
        try {
            long currentTimeMillis = System.currentTimeMillis();
            UpdateLocalCarDto dto = paramsDto.getCarDto();
            String orderCarNo = dto.getOrderCarNo();
            Long orderCarId = dto.getOrderCarId();
            Long carrierId = paramsDto.getCarrierId();

            //【验证】承运商是否可以运营
            CarrierInfo carrierInfo = validateLocalCarrierInfo(carrierId, paramsDto.getCarrierName(), paramsDto.getCarrierType(), paramsDto.getType(),
                    new UserInfo(dto.getLoadLinkUserId(), dto.getLoadLinkName(), dto.getLoadLinkPhone()),
                    new UserInfo(dto.getUnloadLinkUserId(), dto.getUnloadLinkName(), dto.getUnloadLinkPhone()),
                    orderCarNo);
            /**1、添加运单信息*/
            Waybill waybill = waybillDao.selectById(paramsDto.getId());
            if (waybill == null) {
                return BaseResultUtil.fail("运单不存在");
            }
            carrierInfo = validateReAllotCarrier(carrierInfo, waybill.getCarrierId());
            if (waybill.getState() >= WaybillStateEnum.TRANSPORTING.code) {
                return BaseResultUtil.fail("运输中运单不允许修改");
            }

            /**验证运单车辆信息*/
            //加锁
            String lockKey = RedisKeys.getDispatchLock(orderCarNo);
            if (!redisLock.lock(lockKey)) {
                return BaseResultUtil.fail("运单中车辆{0}，其他人正在调度", orderCarNo);
            }
            lockSet.add(lockKey);

            //【验证】订单车辆状态
            OrderCar orderCar = orderCarDao.selectById(orderCarId);
            if (orderCar == null) {
                return BaseResultUtil.fail("运单,车辆{0}，车辆所属订单车辆不存在", orderCarNo);
            }
            //【验证】订单状态
            Order order = orderDao.selectById(orderCar.getOrderId());
            if (order == null) {
                return BaseResultUtil.fail("运单,车辆{0}，当前车辆不存在", orderCarNo);
            }
            if(dto.getStartAddress().equals(dto.getEndAddress()) && dto.getStartAreaCode().equals(dto.getEndAreaCode())){
                throw new ParameterException("起始地与目的地不能相同", orderCarNo);
            }
            //承运商类型
            waybill.setCarrierId(carrierInfo.getCarrierId());
            waybill.setCarrierName(carrierInfo.getCarrierName());
            waybill.setCarrierType(carrierInfo.getCarryType());
            waybill.setState(getWaybillState(carrierInfo.getCarryType()));
            //提送车费用逻辑，调度时不允许修改提送车费用，需要到订单中修改提送车费用，多则返还，少则后补
            waybill.setFreightFee(getLocalWaybillFreightFee(waybill, orderCar));
            waybill.setRemark(paramsDto.getRemark());
            waybill.setFixedFreightFee(false);
            //waybill.setGuideLine();
            waybillDao.updateByIdForNull(waybill);
            ////TODO TODO
            /**2、添加运单车辆信息*/
            WaybillCar waybillCar = waybillCarDao.selectById(dto.getId());
            //初始copy赋值
            BeanUtils.copyProperties(dto, waybillCar);
            waybillCar.setWaybillId(waybill.getId());
            waybillCar.setWaybillNo(waybill.getNo());
            waybillCar.setFreightFee(waybill.getFreightFee());
            //城市信息赋值
            fillWaybillCarCityInfo(waybillCar);
            //业务中心信息赋值
            fillWaybillCarBelongStoreInfo(waybillCar);
            //提送车业务员
            fillWaybillCarAdmin(waybillCar, waybill.getType());
            //计算预计到达时间
            fillWaybillCarExpectEndTime(waybillCar);
            waybillCar.setReceiptFlag(waybillCar.getUnloadLinkPhone().equals(order.getBackContactPhone()));
            //运单车辆状态
            waybillCar.setState(getWaybillCarState(waybill, carrierInfo));
            if (waybill.getCarrierType() == WaybillCarrierTypeEnum.SELF.code) {
                waybillCar.setLoadTime(currentTimeMillis);
            }
            waybillCar.setLoadLinkUserId(waybillCar.getLoadLinkUserId());
            waybillCar.setUnloadLinkUserId(waybillCar.getUnloadLinkUserId());
            waybillCarDao.updateByIdForNull(waybillCar);

            //运单车辆状态
            /**3、添加任务信息*/
            csTaskService.reCreate(waybill, Lists.newArrayList(waybillCar),null, carrierInfo);

            /**5、更新订单车辆状态*/
            updateOrderCarForDispatchLocal(orderCar.getId(), waybill, orderCar.getState());
            return BaseResultUtil.success();
        } finally {
            if (!CollectionUtils.isEmpty(lockSet)) {
                for (String key : lockSet) {
                    redisLock.releaseLock(key);
                }
                //redisUtil.del(lockSet.toArray(new String[0]));
            }
        }
    }

    private void updateOrderCarForDispatchLocal(Long orderCarId, Waybill waybill, Integer orderCarState) {
        OrderCar noc = new OrderCar();
        noc.setId(orderCarId);
        if (waybill.getType() == WaybillTypeEnum.PICK.code) {
            noc.setState(waybill.getCarrierType() == WaybillCarrierTypeEnum.SELF.code ? OrderCarStateEnum.PICKING.code : OrderCarStateEnum.WAIT_PICK.code);
            noc.setPickType(getLocalOrderCarCarryType(waybill.getCarrierType()));
            noc.setPickState(OrderCarLocalStateEnum.DISPATCHED.code);
            orderCarDao.updateById(noc);
        } else {
            //车辆实际运输状态
            if (orderCarState == OrderCarStateEnum.WAIT_BACK.code) {
                noc.setState(waybill.getCarrierType() == WaybillCarrierTypeEnum.SELF.code ? OrderCarStateEnum.BACKING.code : OrderCarStateEnum.WAIT_BACK.code);
            }
            noc.setBackType(getLocalOrderCarCarryType(waybill.getCarrierType()));
            noc.setBackState(OrderCarLocalStateEnum.DISPATCHED.code);
            orderCarDao.updateById(noc);
        }
    }

    private Integer getWaybillCarState(Waybill waybill, CarrierInfo carrierInfo) {
        if(carrierInfo.getCarryType() == WaybillCarrierTypeEnum.SELF.code){
            return waybill.getType() == WaybillTypeEnum.PICK.code ? WaybillCarStateEnum.WAIT_UNLOAD_CONFIRM.code : WaybillCarStateEnum.WAIT_LOAD.code;
        }else{
            if(carrierInfo.getCarrierType() == CarrierTypeEnum.ENTERPRISE.code){
                return WaybillCarStateEnum.WAIT_ALLOT.code;
            }else{
                return WaybillCarStateEnum.WAIT_LOAD.code;
            }
        }
    }

    private Long getLocalWaybillInputStoreId(Integer type, Order order) {
        return type == WaybillTypeEnum.PICK.code ? order.getStartBelongStoreId() : order.getEndBelongStoreId();
    }

    private BigDecimal getLocalWaybillFreightFee(Waybill waybill, OrderCar orderCar) {
        return waybill.getCarrierType() == WaybillCarrierTypeEnum.SELF.code ? BigDecimal.ZERO : (waybill.getType() == WaybillTypeEnum.PICK.code ? orderCar.getPickFee() : orderCar.getBackFee());
    }

    private Integer getWaybillState(Integer carryType) {
        return carryType == WaybillCarrierTypeEnum.SELF.code ? WaybillStateEnum.TRANSPORTING.code : WaybillStateEnum.ALLOT_CONFIRM.code;
    }

    private CarrierInfo validateLocalCarrierInfo(Long newCarrierId, String newCarrierName, Integer carrierType, Integer waybillType, UserInfo loadUser, UserInfo unloadUser, String orderCarNo) {
        CarrierInfo carrierInfo = new CarrierInfo();
        carrierInfo.setCarrierId(newCarrierId);
        carrierInfo.setCarrierName(newCarrierName);
        carrierInfo.setCarryType(carrierType);
        if(carrierType == WaybillCarrierTypeEnum.LOCAL_ADMIN.code){
            Admin admin = adminDao.selectById(newCarrierId);
            if (admin == null || admin.getState() != AdminStateEnum.CHECKED.code) {
                throw new ParameterException("车辆{0}，所选业务员已离职", orderCarNo);
            }
            carrierInfo.setCarrierType(CarrierTypeEnum.PERSONAL.code);
            carrierInfo.setDriverId(admin.getId());
            carrierInfo.setDriverName(admin.getName());
            carrierInfo.setDriverPhone(admin.getPhone());
        }else if(carrierType == WaybillCarrierTypeEnum.SELF.code){
            carrierInfo.setCarrierType(CarrierTypeEnum.PERSONAL.code);
            if (WaybillTypeEnum.PICK.code == waybillType) {
                carrierInfo.setCarrierName(loadUser.getName());
                carrierInfo.setDriverName(loadUser.getName());
                carrierInfo.setDriverPhone(loadUser.getPhone());
            } else {
                carrierInfo.setCarrierName(unloadUser.getName());
                carrierInfo.setDriverName(unloadUser.getName());
                carrierInfo.setDriverPhone(unloadUser.getPhone());
            }
        }else{
            Carrier carrier = carrierDao.selectById(newCarrierId);
            if (carrier.getState() == null || carrier.getState() != CommonStateEnum.CHECKED.code || carrier.getBusinessState() == null || carrier.getBusinessState() != 0) {
                throw new ParameterException("车辆{0}，所选承运商停运中", orderCarNo);
            }
            carrierInfo.setCarrierType(carrier.getType());
            //验证司机信息
            if (carrier.getType() == 1) {
                Driver driver = driverDao.findTopByCarrierId(newCarrierId);
                if (driver == null) {
                    throw new ParameterException("车辆{0}，所选承运商没有运营中的司机", orderCarNo);
                }
                carrierInfo.setDriverId(driver.getId());
                carrierInfo.setDriverName(driver.getName());
                carrierInfo.setDriverPhone(driver.getPhone());
                if(carrierType != WaybillCarrierTypeEnum.LOCAL_PILOT.code){
                    VehicleRunning vr = vehicleRunningDao.findByDriverId(driver.getId());
                    if(vr == null){
                        throw new ParameterException("运单，所选司机没有绑定车牌号");
                    }
                    carrierInfo.setVehicleId(vr.getId());
                    carrierInfo.setVehiclePlateNo(vr.getPlateNo());
                }
            }
        }

        return carrierInfo;
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

    private WaybillCar fillWaybillCarAdmin(WaybillCar waybillCar, Integer type) {
        if (WaybillTypeEnum.BACK.code == type) {
            if (StringUtils.isBlank(waybillCar.getLoadLinkPhone())) {
                Admin admin = csAdminService.findLoop(waybillCar.getStartStoreId());
                if (admin == null) {
                    throw new ParameterException("业务中心无人员");
                }
                waybillCar.setLoadLinkUserId(admin.getId());
                waybillCar.setLoadLinkName(admin.getName());
                waybillCar.setLoadLinkPhone(admin.getPhone());
            }
        } else if (WaybillTypeEnum.PICK.code == type){

            if (StringUtils.isBlank(waybillCar.getUnloadLinkPhone())) {
                Admin admin = csAdminService.findLoop(waybillCar.getEndStoreId());
                if (admin == null) {
                    throw new ParameterException("业务中心无人员");
                }
                waybillCar.setUnloadLinkUserId(admin.getId());
                waybillCar.setUnloadLinkName(admin.getName());
                waybillCar.setUnloadLinkPhone(admin.getPhone());
            }
        }else{
            if (StringUtils.isBlank(waybillCar.getLoadLinkPhone())) {
                Admin admin = csAdminService.findLoop(waybillCar.getStartStoreId());
                if (admin == null) {
                    throw new ParameterException("业务中心无人员");
                }
                waybillCar.setLoadLinkUserId(admin.getId());
                waybillCar.setLoadLinkName(admin.getName());
                waybillCar.setLoadLinkPhone(admin.getPhone());
            }
            if (StringUtils.isBlank(waybillCar.getUnloadLinkPhone())) {
                Admin admin = csAdminService.findLoop(waybillCar.getEndStoreId());
                if (admin == null) {
                    throw new ParameterException("业务中心无人员");
                }
                waybillCar.setUnloadLinkUserId(admin.getId());
                waybillCar.setUnloadLinkName(admin.getName());
                waybillCar.setUnloadLinkPhone(admin.getPhone());
            }
        }
        return waybillCar;
    }

    private Integer getLocalOrderCarCarryType(Integer carrierType) {
        switch (carrierType) {
            case 3:
                return OrderPickTypeEnum.PILOT.code;
            case 4:
                return OrderPickTypeEnum.PILOT.code;
            case 5:
                return OrderPickTypeEnum.CONSIGN.code;
            case 6:
                return OrderPickTypeEnum.SELF.code;
            default:
                throw new ParameterException("无法识别的提车类型");
        }
    }


    private CarrierInfo validateReAllotCarrier(CarrierInfo carrierInfo, Long oldCarrierId) {
        Long newCarrierId = carrierInfo.getCarrierId();
        oldCarrierId = oldCarrierId == null || oldCarrierId <= 0 ? 0L : oldCarrierId;
        newCarrierId = newCarrierId == null || newCarrierId <= 0 ? 0L : newCarrierId;
        if(oldCarrierId.equals(newCarrierId)){
            carrierInfo.setReAllotCarrier(false);
        }else{
            carrierInfo.setReAllotCarrier(true);
        }
        return carrierInfo;
    }

    /**
     * 计算到达时间
     **/
    private WaybillCar fillWaybillCarExpectEndTime(WaybillCar waybillCar) {
        if (waybillCar.getExpectStartTime() == null) {
            throw new ParameterException("请填写预估提车日期");
        }
        Line line = csLineService.getLineByCity(waybillCar.getStartCityCode(), waybillCar.getEndCityCode(), true);
        if (line != null && line.getDays() != null) {
            waybillCar.setExpectEndTime(TimeStampUtil.addDays(waybillCar.getExpectStartTime(), line.getDays().intValue()));
        }
        return waybillCar;
    }

    /**
     * 计算城市信息
     **/
    private WaybillCar fillWaybillCarCityInfo(WaybillCar wc) {
        FullCity startFullCity = csCityService.findFullCity(wc.getStartAreaCode(), CityLevelEnum.PROVINCE);
        copyWaybillCarStartCity(startFullCity, wc);
        FullCity endFullCity = csCityService.findFullCity(wc.getEndAreaCode(), CityLevelEnum.PROVINCE);
        copyWaybillCarEndCity(endFullCity, wc);
        return wc;
    }

    /**
     * 计算业务中心
     **/
    private WaybillCar fillWaybillCarBelongStoreInfo(WaybillCar wc) {
        Long startBelongStoreId = csStoreService.getBelongStoreId(wc.getStartStoreId(), wc.getStartCityCode());
        if (startBelongStoreId != null) {
            wc.setStartBelongStoreId(startBelongStoreId);
        }
        Long endBelongStoreId = csStoreService.getBelongStoreId(wc.getEndStoreId(), wc.getEndCityCode());
        if (endBelongStoreId != null) {
            wc.setEndBelongStoreId(endBelongStoreId);
        }
        return wc;
    }

    /**
     * 干线调度
     *
     * @param paramsDto
     * @author JPG
     * @since 2019/11/5 17:23
     */
    @Override
    public ResultVo saveTrunk(SaveTrunkWaybillDto paramsDto) {
        Set<String> lockSet = new HashSet<>();
        try {
            long currentTimeMillis = System.currentTimeMillis();
            Long carrierId = paramsDto.getCarrierId();
            List<SaveTrunkWaybillCarDto> dtoList = paramsDto.getList();

            //【验证】承运商和司机信息
            CarrierInfo carrierInfo = validateTrunkCarrierInfo(carrierId);

            /**1、组装运单信息*/
            Waybill waybill = new Waybill();
            waybill.setNo(sendNoService.getNo(SendNoTypeEnum.WAYBILL));
            waybill.setType(WaybillTypeEnum.TRUNK.code);
            waybill.setSource(WaybillSourceEnum.MANUAL.code);
            waybill.setGuideLine(paramsDto.getGuideLine());
            waybill.setCarrierId(carrierInfo.getCarrierId());
            waybill.setCarrierName(carrierInfo.getCarrierName());
            waybill.setCarrierType(carrierInfo.getCarryType());
            waybill.setCarNum(dtoList.size());
            waybill.setState(WaybillStateEnum.ALLOT_CONFIRM.code);
            waybill.setFreightFee(MoneyUtil.convertYuanToFen(paramsDto.getFreightFee()));
            waybill.setRemark(paramsDto.getRemark());
            waybill.setCreateTime(currentTimeMillis);
            waybill.setCreateUser(paramsDto.getLoginName());
            waybill.setCreateUserId(paramsDto.getLoginId());
            waybill.setFixedFreightFee(paramsDto.getFixedFreightFee());
            //TODO 干线运单所属业务中心
            //waybill.setInputStoreId(paramsDto.);
            waybillDao.insert(waybill);

            /**2、运单中车辆循环*/
            Set<String> orderCarNoSet = Sets.newHashSet();
            List<WaybillCar> waybillCars = Lists.newArrayList();
            for (SaveTrunkWaybillCarDto dto : dtoList) {
                if (dto == null) {
                    continue;
                }
                String orderCarNo = dto.getOrderCarNo();
                Long orderCarId = dto.getOrderCarId();

                //加锁
                String lockKey = RedisKeys.getDispatchLock(orderCarNo);
                if (!redisLock.lock(lockKey, 20000, 100, 300L)) {
                    throw new ParameterException("运单中车辆{0}，其他人正在调度", orderCarNo);
                }
                lockSet.add(lockKey);

                //验证订单车辆状态
                OrderCar orderCar = orderCarDao.selectById(orderCarId);
                if (orderCar == null) {
                    throw new ParameterException("运单车辆{0}，不存在", orderCarNo);
                }
                if (orderCar.getState() == null) {
                    throw new ParameterException("运单中车辆{0}，无法提车调度", orderCarNo);
                }
                //验证订单状态
                Order order = orderDao.selectById(orderCar.getOrderId());
                if (order == null) {
                    throw new ParameterException("运单中车辆{0}，所属订单车辆不存在", orderCarNo);
                }

                if (order.getState() == null
                        || order.getState() < OrderStateEnum.CHECKED.code
                        || order.getState() > OrderStateEnum.FINISHED.code) {
                    throw new ParameterException("运单中车辆{0}，所属订单状态无法干线调度", orderCarNo);
                }

                //验证是否已经调度过,已经调度的为
/*                int n = waybillCarDao.countForValidateRepeatTrunkDisPatch(areaList);
                if (n > 0) {
                    throw new ParameterException("运单中车辆{0}，已经调度过", orderCarNos);
                }*/
                if(dto.getStartAddress().equals(dto.getEndAddress()) && dto.getStartAreaCode().equals(dto.getEndAreaCode())){
                    throw new ParameterException("起始地与目的地不能相同", orderCarNo);
                }
                //验证出发地与上一次调度目的地是否一致
                WaybillCar prevWc = waybillCarDao.findLastByOderCarId(orderCarId);
                if (prevWc != null) {
                    if (!prevWc.getEndAddress().equals(dto.getStartAddress())) {
                        throw new ServerException("运单中车辆{0},本次调度出发地址与上次调度结束地址不一致", orderCarNo);
                    }
                }

                //同一订单车辆不能重复
                if (orderCarNoSet.contains(dto.getOrderCarNo())) {
                    throw new ServerException("运单中车辆{0}，重复", dto.getOrderCarNo());
                }

                WaybillCar waybillCar = new WaybillCar();
                BeanUtils.copyProperties(dto, waybillCar);
                waybillCar.setWaybillId(waybill.getId());
                waybillCar.setWaybillNo(waybill.getNo());
                waybillCar.setFreightFee(MoneyUtil.convertYuanToFen(dto.getFreightFee()));
                waybillCar.setOrderCarId(orderCar.getId());
                //城市信息赋值
                fillWaybillCarCityInfo(waybillCar);
                //业务中心信息赋值
                fillWaybillCarBelongStoreInfo(waybillCar);
                //提送车业务员
                fillWaybillCarAdmin(waybillCar, waybill.getType());
                //计算预计到达时间
                fillWaybillCarExpectEndTime(waybillCar);
                waybillCar.setReceiptFlag(waybillCar.getUnloadLinkPhone().equals(order.getBackContactPhone()));
                waybillCar.setState(getTrunkState(carrierInfo));
                waybillCar.setCreateTime(currentTimeMillis);
                waybillCarDao.insert(waybillCar);

                //更新订单车辆状态
                updateOrderCarForDispatchTrunk(orderCar.getId(), waybillCar, order);

                //提取属性
                orderCarNoSet.add(waybillCar.getOrderCarNo());
                waybillCars.add(waybillCar);
            }

            //承运商有且仅有一个司机
            /**1+、写入任务表*/
            csTaskService.reCreate(waybill, waybillCars, waybillCars,carrierInfo);

            return BaseResultUtil.success();
        } finally {
            if (!CollectionUtils.isEmpty(lockSet)) {
                redisUtil.del(lockSet.toArray(new String[0]));
            }
        }
    }

    /**
     * 修改干线运单
     *
     * @param paramsDto
     * @author JPG
     * @since 2019/11/9 8:59
     */
    @Override
    public ResultVo updateTrunk(UpdateTrunkWaybillDto paramsDto) {
        Set<String> lockSet = new HashSet<>();
        try {
            List<UpdateTrunkWaybillCarDto> dtoList = paramsDto.getList();
            Long carrierId = paramsDto.getCarrierId();


            Waybill waybill = waybillDao.selectById(paramsDto.getId());
            if (waybill == null) {
                return BaseResultUtil.fail("运单不存在");
            }

            //加锁
            String lockKey = RedisKeys.getDispatchLock(waybill.getNo());
            if (!redisLock.lock(lockKey, 20000, 100, 300L)) {
                return BaseResultUtil.fail("运单{0}，其他人正在修改", waybill.getNo());
            }
            lockSet.add(lockKey);
            //是否重新分配任务
            CarrierInfo carrierInfo = validateTrunkCarrierInfo(carrierId);
            carrierInfo = validateReAllotCarrier(carrierInfo, waybill.getCarrierId());
            if (waybill.getState() >= WaybillStateEnum.TRANSPORTING.code && carrierInfo.isReAllotCarrier()) {
                return BaseResultUtil.fail("运单运输中，不能修改司机，请使用[卸载车辆]功能");
            }
            /**1、组装运单信息*/
            waybill.setGuideLine(paramsDto.getGuideLine());
            waybill.setCarrierId(carrierInfo.getCarrierId());
            waybill.setCarrierName(carrierInfo.getCarrierName());
            waybill.setCarrierType(carrierInfo.getCarryType());
            waybill.setCarNum(dtoList.size());
            waybill.setFreightFee(MoneyUtil.convertYuanToFen(paramsDto.getFreightFee()));
            waybill.setFixedFreightFee(paramsDto.getFixedFreightFee());
            waybill.setRemark(paramsDto.getRemark());
            waybillDao.updateByIdForNull(waybill);

            /**2、运单中车辆循环*/
            Set<Long> unCancelWaybillCarIds = Sets.newHashSet();
            List<WaybillCar> newWaybillCars = Lists.newArrayList();
            List<WaybillCar> waybillCars = Lists.newArrayList();
            for (UpdateTrunkWaybillCarDto dto : dtoList) {
                if (dto == null) {
                    continue;
                }
                String orderCarNo = dto.getOrderCarNo();
                Long orderCarId = dto.getOrderCarId();
                //加锁
                String lockCarKey = RedisKeys.getDispatchLock(orderCarNo);
                if (!redisLock.lock(lockCarKey, 20000, 100, 300L)) {
                    throw new ParameterException("运单中车辆{0}，其他人正在调度", orderCarNo);
                }
                boolean isNewWaybillCar = false;
                WaybillCar waybillCar = null;
                if (dto.getId() != null) {
                    //修改的车辆
                    waybillCar = waybillCarDao.selectById(dto.getId());
                    if (waybillCar.getState() != null && waybillCar.getState() >= WaybillCarStateEnum.LOADED.code && carrierInfo.isReAllotCarrier()) {
                        throw new ParameterException("运单中车辆{0}，运输中不能修改司机，请使用[卸载车辆]功能", orderCarNo);
                    }
                }
                if (waybillCar == null) {
                    //新增的车辆
                    isNewWaybillCar = true;
                    waybillCar = new WaybillCar();
                }

                //验证订单车辆状态
                OrderCar orderCar = orderCarDao.selectById(orderCarId);
                if (orderCar == null) {
                    throw new ParameterException("运单中车辆{0}，不存在", orderCarNo);
                }
                if (orderCar.getState() == null) {
                    throw new ParameterException("运单中车辆{0}，无法提车调度", orderCarNo);
                }
                //验证订单状态
                Order order = orderDao.selectById(orderCar.getOrderId());
                if (order == null) {
                    throw new ParameterException("运单中车辆{0}，所属订单车辆不存在", orderCarNo);
                }
                if (order.getState() == null
                        || order.getState() < OrderStateEnum.CHECKED.code
                        || order.getState() > OrderStateEnum.FINISHED.code) {
                    throw new ParameterException("运单中车辆{0}，所属订单状态无法干线调度", orderCarNo);
                }

                //包板线路不能为空
                if (waybill.getFixedFreightFee() && waybillCar.getLineId() == null) {
                    throw new ParameterException("运单中车辆{0}，线路不能为空", orderCarNo);
                }
                if(dto.getStartAddress().equals(dto.getEndAddress()) && dto.getStartAreaCode().equals(dto.getEndAreaCode())){
                    throw new ParameterException("起始地与目的地不能相同", orderCarNo);
                }
                lockSet.add(lockCarKey);
                //验证出发地与上一次调度目的地是否一致
                WaybillCar prevWc;
                if (waybillCar.getId() == null) {
                    prevWc = waybillCarDao.findLastByOderCarId(orderCarId);
                } else {
                    prevWc = waybillCarDao.findLastByOderCarIdAndId(waybillCar.getId(), orderCarId);
                }
                if (prevWc != null) {
                    if (!prevWc.getEndAddress().equals(dto.getStartAddress())) {
                        throw new ServerException("运单中车辆{0}，本次调度出发地址与上次调度结束地址不一致", orderCarNo);
                    }
                }

                //车辆数据
                BeanUtils.copyProperties(dto, waybillCar);
                waybillCar.setWaybillId(waybill.getId());
                waybillCar.setWaybillNo(waybill.getNo());
                waybillCar.setFreightFee(MoneyUtil.convertYuanToFen(dto.getFreightFee()));
                //城市信息赋值
                fillWaybillCarCityInfo(waybillCar);
                //业务中心信息赋值
                fillWaybillCarBelongStoreInfo(waybillCar);
                //提送车业务员
                fillWaybillCarAdmin(waybillCar, waybill.getType());
                //计算预计到达时间
                fillWaybillCarExpectEndTime(waybillCar);
                waybillCar.setReceiptFlag(order.getBackContactPhone().equals(waybillCar.getUnloadLinkPhone()));
                if (isNewWaybillCar) {
                    waybillCar.setState(getTrunkState(carrierInfo));
                    waybillCarDao.insert(waybillCar);
                } else {
                    waybillCarDao.updateByIdForNull(waybillCar);
                }

                //更新车辆信息
                updateOrderCarForDispatchTrunk(orderCar.getId(), waybillCar, order);
                //提取信息
                unCancelWaybillCarIds.add(waybillCar.getId());
                waybillCars.add(waybillCar);
                if(isNewWaybillCar){
                    newWaybillCars.add(waybillCar);
                }
            }

            //查询待取消的车辆
            List<WaybillCar> cancelWaybillCars = waybillCarDao.findWaitCancelListByUnCancelIds(unCancelWaybillCarIds, waybill.getId());
            if (!CollectionUtils.isEmpty(cancelWaybillCars)) {
                //取消运单车辆
                cancelWaybillCars.forEach(waybillCar -> cancelWaybillCar(waybill.getType(), waybillCar));
            }

            /**承运商有且仅有一个司机*/
            csTaskService.reCreate(waybill, waybillCars, newWaybillCars, carrierInfo);

            //验证运单是否完成
            validateAndFinishWaybill(waybill.getId());




            return BaseResultUtil.success();
        } finally {
            if (!CollectionUtils.isEmpty(lockSet)) {
                redisUtil.del(lockSet.toArray(new String[0]));
            }
        }
    }

    private Integer getTrunkState(CarrierInfo carrierInfo) {
        return carrierInfo.getCarryType() == CarrierTypeEnum.PERSONAL.code ? WaybillCarStateEnum.WAIT_LOAD.code : WaybillCarStateEnum.WAIT_ALLOT.code;
    }

    private void updateOrderCarForDispatchTrunk(Long orderCarId, WaybillCar waybillCar, Order order) {
        OrderCar noc = new OrderCar();
        noc.setId(orderCarId);
        noc.setTrunkState(OrderCarTrunkStateEnum.WAIT_NEXT_DISPATCH.code);
        int n = waybillCarDao.countPrevTrunk(waybillCar.getId());
        if (n == 0) {
            //提干
            if (order.getStartAddress().equals(waybillCar.getStartAddress())) {
                noc.setPickType(OrderPickTypeEnum.WL.code);
                noc.setPickState(OrderCarLocalStateEnum.F_WL.code);
                noc.setState(OrderCarStateEnum.WAIT_TRUNK.code);
            }
        }
        //干线最后一段
        if(validateIsArriveEndStore(order.getEndStoreId(), waybillCar.getEndStoreId())){
            noc.setTrunkState(OrderCarTrunkStateEnum.DISPATCHED.code);
        }else{
            noc.setTrunkState(OrderCarTrunkStateEnum.WAIT_NEXT_DISPATCH.code);
        }

        //干送
        if (order.getBackContactPhone().equals(waybillCar.getUnloadLinkPhone())) {
            noc.setTrunkState(OrderCarTrunkStateEnum.DISPATCHED.code);
            noc.setBackType(OrderPickTypeEnum.WL.code);
            noc.setBackState(OrderCarLocalStateEnum.F_WL.code);
        }else{
            if (order.getEndStoreId().equals(waybillCar.getEndStoreId())) {
                noc.setTrunkState(OrderCarTrunkStateEnum.DISPATCHED.code);
            }else{
                noc.setTrunkState(OrderCarTrunkStateEnum.WAIT_NEXT_DISPATCH.code);
            }
            noc.setBackType(order.getBackType());
            noc.setBackState(OrderCarLocalStateEnum.WAIT_DISPATCH.code);
        }
        orderCarDao.updateById(noc);
    }

    private boolean validateIsArriveEndStore(Long orderEndStoreId, Long waybillCarEndStoreId) {
        if(orderEndStoreId == null || orderEndStoreId <= 0){
            return false;
        }
        if(waybillCarEndStoreId == null || waybillCarEndStoreId <= 0){
            return false;
        }
        if(!orderEndStoreId.equals(waybillCarEndStoreId)){
            return false;
        }
        return true;
    }

    private CarrierInfo validateTrunkCarrierInfo(Long carrierId) {
        CarrierInfo carrierInfo = new CarrierInfo();
        Carrier carrier = carrierDao.selectById(carrierId);
        if (carrier.getState() == null || carrier.getState() != CommonStateEnum.CHECKED.code || carrier.getBusinessState() == null || carrier.getBusinessState() != 0) {
            throw new ParameterException("运单，所选承运商，停运中");
        }
        carrierInfo.setCarrierType(carrier.getType());
        carrierInfo.setCarrierId(carrierId);
        carrierInfo.setCarryType(carrier.getType());
        carrierInfo.setCarrierName(carrier.getName());
        if (carrier.getType() == 1) {
            Driver driver = driverDao.findTopByCarrierId(carrierId);
            if (driver == null) {
                throw new ParameterException("运单，所选承运商没有运营中的司机");
            }
            VehicleRunning vr = vehicleRunningDao.findByDriverId(driver.getId());
            if(vr == null){
                throw new ParameterException("运单，所选司机没有绑定车牌号");
            }
            carrierInfo.setDriverId(driver.getId());
            carrierInfo.setDriverName(driver.getName());
            carrierInfo.setDriverPhone(driver.getPhone());
            carrierInfo.setVehicleId(vr.getId());
            carrierInfo.setVehiclePlateNo(vr.getPlateNo());
        }
        return carrierInfo;
    }



    /**
     * 取消调度
     *
     * @param paramsDto
     * @author JPG
     * @since 2019/11/5 17:33
     */
    @Override
    public ResultVo cancel(CancelWaybillDto paramsDto) {
        //取消运单
        List<Waybill> waybillList = waybillDao.findListByIds(paramsDto.getWaybillIdList());
        for (Waybill waybill : waybillList) {
            if (waybill == null) {
                continue;
            }
            //取消运单
            cancelWaybill(waybill);
        }
        return BaseResultUtil.success();
    }

    @Override
    public void cancelWaybill(Waybill waybill) {
        //状态不大于待承接
        if ((waybill.getState() >= WaybillStateEnum.TRANSPORTING.code && waybill.getCarrierType() == WaybillCarrierTypeEnum.SELF.code)
                || (waybill.getState() >= WaybillStateEnum.FINISHED.code && waybill.getCarrierType() == WaybillCarrierTypeEnum.SELF.code)) {
            throw new ServerException("运单{0},[{1}]状态不能取消", waybill.getNo(), WaybillStateEnum.valueOf(waybill.getState()).name);
        }
        //修改车辆状态和调度状态
        List<WaybillCar> list = waybillCarDao.findListByWaybillId(waybill.getId());
        if (CollectionUtils.isEmpty(list)) {
            return;
        }
        list.forEach(waybillCar -> cancelWaybillCar(waybill.getType(), waybillCar));

        //修改运单主单状态
        waybillDao.updateStateById(WaybillStateEnum.F_CANCEL.code, waybill.getId());

    }
    @Override
    public void cancelWaybillCar(WaybillCar waybillCar) {
        cancelWaybillCar(null, waybillCar);
    }
    /**
     * 取消运单车辆
     *
     * @author JPG
     * @since 2019/10/29 9:57
     */
    @Override
    public void cancelWaybillCar(Integer waybillType, WaybillCar waybillCar) {
        if(waybillCar == null){
            return;
        }
        if(waybillType == null){
            Waybill waybill = waybillDao.selectById(waybillCar.getWaybillId());
            waybillType = waybill.getType();
        }

        if (waybillCar.getState() >= WaybillCarStateEnum.LOADED.code) {
            throw new ParameterException("车辆{0}运输中，不允许取消", waybillCar.getOrderCarNo());
        }
        OrderCar oc = orderCarDao.selectById(waybillCar.getOrderCarId());
        if (oc == null) {
            throw new ParameterException("车辆{0}, 订单信息错误", waybillCar.getOrderCarNo());
        }
        Order order = orderDao.selectById(oc.getOrderId());
        if (order == null) {
            throw new ParameterException("车辆{0}, 车辆信息错误", waybillCar.getOrderCarNo());
        }

        OrderCar noc = new OrderCar();
        noc.setId(oc.getId());
        //修改车辆状态
        if (WaybillTypeEnum.PICK.code == waybillType) {
            noc.setPickType(order.getPickType());
            //订单车辆状态
            if (OrderCarStateEnum.WAIT_PICK.code == oc.getState()) {
                noc.setState(OrderCarStateEnum.WAIT_PICK_DISPATCH.code);
            }
            //订单车辆提车状态
            noc.setPickState(OrderCarLocalStateEnum.WAIT_DISPATCH.code);

        } else if (WaybillTypeEnum.BACK.code == waybillType) {
            noc.setBackType(order.getBackType());
            //订单车辆状态
            if (OrderCarStateEnum.WAIT_BACK.code == oc.getState()) {
                noc.setState(OrderCarStateEnum.WAIT_BACK_DISPATCH.code);
            }
            //订单车辆配送状态
            noc.setBackState(OrderCarLocalStateEnum.WAIT_DISPATCH.code);
        } else if (WaybillTypeEnum.TRUNK.code == waybillType) {
            //取消该车辆所有后续调度
            List<WaybillCar> afterWaybillCars = waybillCarDao.findAfterWaybillCar(waybillCar.getId(), waybillCar.getOrderCarNo());
            if(!CollectionUtils.isEmpty(afterWaybillCars)){

                List<Long> collect = afterWaybillCars.stream().map(WaybillCar::getId).collect(Collectors.toList());
                waybillCarDao.cancelBatch(collect);
                List<Task> afterTasks = taskDao.findListByWaybillCarIds(collect);
                if(!CollectionUtils.isEmpty(afterTasks)){
                    afterTasks.forEach(task -> {
                        taskDao.updateNum(task.getId());
                        csTaskService.validateAndFinishTask(task.getId());
                    });
                }
                List<Waybill> afterWaybills = waybillDao.findListByWaybillCarIds(collect);
                if(!CollectionUtils.isEmpty(afterWaybills)){
                    afterWaybills.forEach(waybill -> {
                        waybillDao.updateNum(waybill.getId());
                        validateAndFinishWaybill(waybill.getId());
                    });
                }
            }
            //查询是否还有干线调度
            int num = waybillCarDao.countTrunkWaybillCar(waybillCar.getOrderCarNo());
            if (num <= 0) {
                noc.setTrunkState(OrderCarTrunkStateEnum.WAIT_DISPATCH.code);
            } else {
                //订单车辆干线状态
                noc.setTrunkState(OrderCarTrunkStateEnum.WAIT_NEXT_DISPATCH.code);
            }
            //订单车辆状态
            if (OrderCarStateEnum.WAIT_TRUNK.code == oc.getState()) {
                noc.setState(OrderCarStateEnum.WAIT_TRUNK_DISPATCH.code);
            }

            //提干
            if (order.getStartAddress().equals(waybillCar.getStartAddress())) {
                noc.setPickType(order.getPickType());
                noc.setPickState(OrderCarLocalStateEnum.WAIT_DISPATCH.code);
            }
            //干线最后一段
            if (order.getEndCityCode().equals(waybillCar.getEndCityCode())) {
                noc.setTrunkState(OrderCarTrunkStateEnum.WAIT_NEXT_DISPATCH.code);
                if (num <= 0) {
                    noc.setTrunkState(OrderCarTrunkStateEnum.WAIT_DISPATCH.code);
                }
            }
            //干送
            noc.setBackType(order.getBackType());
            noc.setBackState(OrderCarLocalStateEnum.WAIT_DISPATCH.code);
        } else {
            throw new ParameterException("运单类型错误");
        }
        //取消运单车辆
        waybillCarDao.updateStateById(waybillCar.getId(), WaybillCarStateEnum.F_CANCEL.code);
        //更新运单车辆数
        waybillDao.updateNum(waybillCar.getWaybillId());
        orderCarDao.updateById(noc);

        //更新任务车辆数
        Task task = taskDao.findByWaybillCarId(waybillCar.getId());
        if (task != null) {
            taskDao.updateNum(task.getId());
            csTaskService.validateAndFinishTask(task.getId());
        }


    }



    /**
     * 中途强制卸车
     *
     * @param paramsDto
     * @author JPG
     * @since 2019/11/13 10:00
     */
    @Override
    public ResultVo trunkMidwayUnload(TrunkMidwayUnload paramsDto) {
        List<Long> carIdList = paramsDto.getWaybillCarIdList();
        if (CollectionUtils.isEmpty(carIdList)) {
            return BaseResultUtil.fail("车辆不能为空");
        }
        //查询完结城市
        FullCity fullCity = csCityService.findFullCity(paramsDto.getEndAreaCode(), CityLevelEnum.PROVINCE);
        if (fullCity == null) {
            return BaseResultUtil.fail("请填写卸车城市");
        }
        //查询运单
        Waybill waybill = waybillDao.selectById(paramsDto.getWaybillId());
        if (waybill == null) {
            return BaseResultUtil.fail("运单信息错误");
        }
        BigDecimal oldTotalFee = BigDecimal.ZERO;
        Set<WaybillCar> waybillCars = Sets.newHashSet();
        for (Long waybillCarId : carIdList) {
            if (waybillCarId == null) {
                continue;
            }
            WaybillCar waybillCar = waybillCarDao.selectById(waybillCarId);
            if (waybillCar == null) {
                throw new ParameterException("ID为{0}的车辆不存在", waybillCarId);
            }
            if (waybillCar.getState() >= WaybillCarStateEnum.UNLOADED.code) {
                throw new ParameterException("车辆{0}已完结, 不能卸载", waybillCar.getOrderCarNo());
            }
            if (!waybillCar.getWaybillId().equals(waybill.getId())) {
                throw new ParameterException("车辆{0}不属于运单{1}，不能卸载", waybillCar.getOrderCarNo(), waybill.getNo());
            }
            //验证订单车辆状态
            OrderCar orderCar = orderCarDao.selectById(waybillCar.getOrderCarId());
            if (orderCar == null) {
                throw new ParameterException("运单车辆，不存在");
            }
            //验证订单状态
            Order order = orderDao.selectById(orderCar.getOrderId());
            if (order == null) {
                throw new ParameterException("运单中车辆{0}，所属订单车辆不存在", orderCar.getOrderNo());
            }

            //已装车
            if (waybillCar.getState() >= WaybillCarStateEnum.LOADED.code) {
                copyWaybillCarEndCity(fullCity, waybillCar);
                waybillCar.setEndStoreId(paramsDto.getEndStoreId());
                waybillCar.setEndStoreName(paramsDto.getEndStoreName());
                waybillCar.setEndAddress(paramsDto.getEndAddress());
                Long endStoreId = paramsDto.getEndStoreId();
                if (endStoreId == null || endStoreId <= 0) {
                    //计算目的地所属业务中心
                    waybillCar.setEndBelongStoreId(waybillCar.getStartBelongStoreId());
                    waybillCar.setState(WaybillCarStateEnum.UNLOADED.code);
                }else{
                    waybillCar.setEndBelongStoreId(endStoreId);
                    waybillCar.setState(WaybillCarStateEnum.WAIT_UNLOAD_CONFIRM.code);
                }

                waybillCar.setReceiptFlag(waybillCar.getUnloadLinkPhone().equals(order.getBackContactPhone()));
                waybillCar.setUnloadLinkUserId(paramsDto.getUnloadLinkUserId());
                waybillCar.setUnloadLinkName(paramsDto.getUnloadLinkName());
                waybillCar.setUnloadLinkPhone(paramsDto.getUnloadLinkPhone());
                waybillCar.setUnloadTime(paramsDto.getUnloadTime());
                waybillCarDao.updateByIdForNull(waybillCar);

                WaybillCar next = waybillCarDao.findNextWaybillCar(waybillCar.getId(), waybillCar.getOrderCarNo());
                if(next != null){
                    cancelWaybillCar(waybill.getType(), next);
                }
                //提取属性
                oldTotalFee = oldTotalFee.add(waybillCar.getFreightFee());
                waybillCars.add(waybillCar);
            } else {
                //未装车的取消
                cancelWaybillCar(waybill.getType(), waybillCar);
            }
            //验证并完成任务
            Task task = taskDao.findByWaybillCarId(waybillCarId);
            if(task != null){
                csTaskService.validateAndFinishTask(task.getId());
            }
        }

        //按比例均摊运费
        shareWaybillCarFreightFee(waybillCars, oldTotalFee, paramsDto.getFreightFee());

        waybillCars.forEach(wc -> waybillCarDao.updateById(wc));
        //更新运单费用
        waybillDao.updateFreightFee(waybill.getId());
        //验证并完成运单
        validateAndFinishWaybill(waybill.getId());
        return BaseResultUtil.success();
    }

    @Override
    public void validateAndFinishWaybill(Long waybillId) {
        int count = waybillCarDao.countUnFinishByWaybillId(waybillId);
        if(count > 0){
            return;
        }
        waybillDao.updateForFinish(waybillId);
        try {
            csPingPayService.allinpayToCarrier(waybillId);
        } catch (Exception e) {
            log.error("完成运单（ID：{}）支付司机运费失败！", waybillId);
            log.error(e.getMessage(), e);
        }
    }

    private void shareWaybillCarFreightFee(Set<WaybillCar> waybillCars, BigDecimal oldTotalFee, BigDecimal newTotalFee) {
        newTotalFee = MoneyUtil.convertYuanToFen(newTotalFee);
        if(newTotalFee.compareTo(BigDecimal.ZERO) == 0){
            waybillCars.forEach(waybillCar -> {
                waybillCar.setFreightFee(BigDecimal.ZERO);
                waybillCarDao.updateById(waybillCar);
            });
        }else if(oldTotalFee.compareTo(BigDecimal.ZERO) == 0){

            BigDecimal[] bigDecimals = newTotalFee.divideAndRemainder(new BigDecimal(waybillCars.size()));
            BigDecimal rAvg = bigDecimals[0];
            BigDecimal rRemainder = bigDecimals[1];
            for (WaybillCar waybillCar : waybillCars) {
                //运费
                if (rRemainder.compareTo(BigDecimal.ZERO) > 0) {
                    waybillCar.setFreightFee(rAvg.add(BigDecimal.ONE));
                    rRemainder = rRemainder.subtract(BigDecimal.ONE);
                } else {
                    waybillCar.setFreightFee(rAvg);
                }
                waybillCarDao.updateById(waybillCar);
            }
        }else{
            BigDecimal avg = newTotalFee.divide(oldTotalFee, 8, RoundingMode.FLOOR);
            BigDecimal avgTotalFee = BigDecimal.ZERO;
            for (WaybillCar wc : waybillCars) {
                BigDecimal avgCar = (wc.getFreightFee().multiply(avg));
                avgCar = avgCar.setScale(0, BigDecimal.ROUND_HALF_DOWN);
                wc.setFreightFee(avgCar);
                avgTotalFee = avgTotalFee.add(avgCar);
            }

            BigDecimal remainder = newTotalFee.subtract(avgTotalFee);
            if(remainder.compareTo(BigDecimal.ZERO) <= 0){
                return;
            }
            BigDecimal[] bigDecimals = remainder.divideAndRemainder(new BigDecimal(waybillCars.size()));
            BigDecimal rAvg = bigDecimals[0];
            BigDecimal rRemainder = bigDecimals[1];
            for (WaybillCar wc : waybillCars) {
                if (rRemainder.compareTo(BigDecimal.ZERO) > 0) {
                    wc.setFreightFee(wc.getFreightFee().add(rAvg).add(BigDecimal.ONE));
                    rRemainder = rRemainder.subtract(BigDecimal.ONE);
                } else {
                    wc.setFreightFee(wc.getFreightFee().add(rAvg));
                }
            }
        }

    }

    /**
     * @param waybill
     * @param list
     * @param paramsDto
     * @param fullCity
     * @author JPG
     * @since 2019/11/9 11:30
     */
    private Waybill createMidWayWaybill(Waybill waybill, List<WaybillCar> list, UpdateTrunkMidwayFinishDto
            paramsDto, FullCity fullCity) {
        long currentTimeMillis = System.currentTimeMillis();
        Waybill newWaybill = new Waybill();
        BeanUtils.copyProperties(waybill, newWaybill);

        newWaybill.setId(null);
        newWaybill.setNo(sendNoService.getNo(SendNoTypeEnum.WAYBILL));
        newWaybill.setSource(WaybillSourceEnum.MIDWAY_FINISH.code);
        //newWaybill.setGuideLine("");
        //newWaybill.setRecommendLine("");
        newWaybill.setCarrierId(null);
        newWaybill.setCarrierType(null);
        newWaybill.setCarrierName(null);
        newWaybill.setState(WaybillStateEnum.WAIT_ALLOT.code);
        //运费
        BigDecimal subtract = waybill.getFreightFee().subtract(paramsDto.getFreightFee());
        newWaybill.setFreightFee(subtract.compareTo(BigDecimal.ZERO) < 0 ? BigDecimal.ZERO : subtract);
        newWaybill.setFixedFreightFee(false);
        newWaybill.setRemark(MessageFormat.format(WaybillRemarkEnum.MIDWAY_FINISH_CREATED.getMsg(), waybill.getNo()));
        newWaybill.setCreateTime(currentTimeMillis);
        newWaybill.setCreateUser(paramsDto.getLoginName());
        newWaybill.setCreateUserId(paramsDto.getLoginId());
        waybillDao.insert(newWaybill);

        for (WaybillCar waybillCar : list) {
            WaybillCar newWaybillCar = new WaybillCar();
            newWaybillCar.setId(null);
            newWaybillCar.setWaybillId(newWaybill.getId());
            newWaybillCar.setWaybillNo(newWaybill.getNo());
            //newWaybillCar.setFreightFee();
            copyWaybillCarStartCity(fullCity, newWaybillCar);
            newWaybillCar.setStartAddress(paramsDto.getEndAddress());
            if (paramsDto.getEndStoreId() == null) {
                Store store = csStoreService.getOneBelongByAreaCode(paramsDto.getEndAreaCode());
                if (store != null) {
                    newWaybillCar.setStartStoreName(store.getName());
                    newWaybillCar.setStartStoreId(store.getId());
                }
                //车辆运输到中途卸车算那个业务中心的
                waybillCar.setEndBelongStoreId(waybillCar.getStartBelongStoreId());
            } else {
                newWaybillCar.setStartStoreName(paramsDto.getEndStoreName());
                newWaybillCar.setStartStoreId(paramsDto.getEndStoreId());
            }
            newWaybillCar.setState(WaybillCarStateEnum.WAIT_ALLOT.code);
            newWaybillCar.setExpectStartTime(LocalDateTimeUtil.getMillisByLDT(LocalDateTimeUtil.getDayStartByLong(paramsDto.getUnloadTime())));
            newWaybillCar.setExpectEndTime(null);
            newWaybillCar.setLoadLinkName(paramsDto.getLoginName());
            newWaybillCar.setLoadLinkUserId(paramsDto.getLoginId());
            newWaybillCar.setLoadLinkPhone(paramsDto.getUserPhone());
            newWaybillCar.setUnloadLinkName(waybillCar.getUnloadLinkName());
            newWaybillCar.setUnloadLinkPhone(waybillCar.getUnloadLinkPhone());
            newWaybillCar.setUnloadLinkUserId(waybillCar.getUnloadLinkUserId());
            newWaybillCar.setCreateTime(currentTimeMillis);
            waybillCarDao.insert(newWaybillCar);
        }
        return newWaybill;
    }

    private WaybillCar copyWaybillCarStartCity(FullCity fullCity, WaybillCar waybillCar) {
        if (fullCity == null) {
            return waybillCar;
        }
        waybillCar.setStartProvince(fullCity.getProvince());
        waybillCar.setStartProvinceCode(fullCity.getProvinceCode());
        waybillCar.setStartCity(fullCity.getCity());
        waybillCar.setStartCityCode(fullCity.getCityCode());
        waybillCar.setStartArea(fullCity.getArea());
        waybillCar.setStartAreaCode(fullCity.getAreaCode());
        return waybillCar;
    }

    private WaybillCar copyWaybillCarEndCity(FullCity fullCity, WaybillCar waybillCar) {
        if (fullCity == null) {
            return waybillCar;
        }
        waybillCar.setEndProvince(fullCity.getProvince());
        waybillCar.setEndProvinceCode(fullCity.getProvinceCode());
        waybillCar.setEndCity(fullCity.getCity());
        waybillCar.setEndCityCode(fullCity.getCityCode());
        waybillCar.setEndArea(fullCity.getArea());
        waybillCar.setEndAreaCode(fullCity.getAreaCode());
        return waybillCar;
    }

}
