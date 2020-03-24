package com.cjyc.common.system.service.impl;

import com.alibaba.fastjson.JSON;
import com.cjkj.common.redis.lock.RedisDistributedLock;
import com.cjkj.common.redis.template.StringRedisUtil;
import com.cjkj.log.monitor.LogUtil;
import com.cjyc.common.model.dao.*;
import com.cjyc.common.model.dto.web.waybill.*;
import com.cjyc.common.model.entity.*;
import com.cjyc.common.model.entity.defined.*;
import com.cjyc.common.model.enums.AdminStateEnum;
import com.cjyc.common.model.enums.CommonStateEnum;
import com.cjyc.common.model.enums.SendNoTypeEnum;
import com.cjyc.common.model.enums.UserTypeEnum;
import com.cjyc.common.model.enums.city.CityLevelEnum;
import com.cjyc.common.model.enums.message.PushMsgEnum;
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
import com.cjyc.common.model.vo.driver.mine.BankCardVo;
import com.cjyc.common.system.service.*;
import com.cjyc.common.system.util.RedisUtils;
import com.google.common.base.Joiner;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.text.MessageFormat;
import java.util.*;
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
    private RedisUtils redisUtils;
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
    private IBankCardBindDao bankCardBindDao;
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
    @Resource
    private ICsPushMsgService csPushMsgService;
    @Resource
    private ICsOrderService csOrderService;

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
        Map<Long, Map<Long, PushCustomerInfo>> pushCustomerInfoMap = Maps.newHashMap();
        try {
            List<SaveLocalWaybillDto> list = paramsDto.getList();
            Map<Long, CarrierInfo> carrierMap = Maps.newHashMap();
            Map<Long, Order> orderMap = Maps.newHashMap();
            Map<Long, OrderCar> orderCarMap = Maps.newHashMap();
            for (SaveLocalWaybillDto dto : list) {
                if (dto == null) {
                    continue;
                }
                String orderCarNo = dto.getOrderCarNo();
                Long orderCarId = dto.getOrderCarId();
                Long carrierId = dto.getCarrierId();

                //是否分配司机任务标识
                CarrierInfo carrierInfo;
                if (!carrierMap.containsKey(carrierId)) {
                    carrierInfo = validateLocalCarrierInfo(carrierId, dto.getCarrierName(), dto.getCarrierType(), paramsDto.getType(),
                            new UserInfo(dto.getLoadLinkUserId(), dto.getLoadLinkName(), dto.getLoadLinkPhone()),
                            new UserInfo(dto.getUnloadLinkUserId(), dto.getUnloadLinkName(), dto.getUnloadLinkPhone()),
                            orderCarNo);
                    carrierMap.put(carrierId, carrierInfo);
                }

                /**验证运单车辆信息*/
                //加锁
                String lockKey = RedisKeys.getDispatchLock(orderCarNo);
                log.debug("缓存：key->{}", lockKey);
                if (!redisLock.lock(lockKey, 120000, 1, 100L)) {
                    log.debug("缓存失败：key->{}", lockKey);
                    return BaseResultUtil.fail("订单车辆{0}正在调度，请5秒后重试", orderCarNo);
                }
                lockSet.add(lockKey);
                if (!csStoreService.validateStoreParam(dto.getStartStoreId(), dto.getStartStoreName())) {
                    log.error("业务中心参数错误(saveLocal):" + JSON.toJSONString(paramsDto));
                    return BaseResultUtil.fail("运单中车辆{0}，始发地业务中心参数错误", orderCarNo);
                }
                if (!csStoreService.validateStoreParam(dto.getEndStoreId(), dto.getEndStoreName())) {
                    log.error("业务中心参数错误(saveLocal):" + JSON.toJSONString(paramsDto));
                    return BaseResultUtil.fail("运单中车辆{0}，目的地业务中心参数错误", orderCarNo);
                }
                //【验证】订单车辆状态
                OrderCar orderCar = getOrderCarFromMap(orderCarMap, orderCarId);
                if (orderCar == null) {
                    return BaseResultUtil.fail("车辆{0}，车辆所属订单车辆不存在", orderCarNo);
                }
                if ((paramsDto.getType() == WaybillTypeEnum.PICK.code && orderCar.getPickState() > OrderCarLocalStateEnum.WAIT_DISPATCH.code)
                        || (paramsDto.getType() == WaybillTypeEnum.BACK.code && orderCar.getBackState() > OrderCarLocalStateEnum.WAIT_DISPATCH.code)) {
                    return BaseResultUtil.fail("车辆{0}，当前车辆状态不能提车/配送调度", orderCarNo);
                }
                //【验证】订单状态
                Order order = getOrderFromMap(orderMap, orderCar.getOrderId());
                if (order == null) {
                    return BaseResultUtil.fail("车辆{0}，当前车辆不存在", orderCarNo);
                }
                if (order.getState() == null
                        || order.getState() < OrderStateEnum.CHECKED.code
                        || order.getState() > OrderStateEnum.FINISHED.code) {
                    return BaseResultUtil.fail("车辆{0}，车辆所属订单未确认或已结束不能提车/配送调度", orderCarNo);
                }
                //验证提车联系人
                if (paramsDto.getType() == WaybillTypeEnum.PICK.code) {
                    if (!order.getPickContactPhone().equals(dto.getLoadLinkPhone())) {
                        return BaseResultUtil.fail("车辆{0}，提车人信息与订单不一致", orderCarNo);
                    }
                } else {
                    if (!order.getBackContactPhone().equals(dto.getUnloadLinkPhone())) {
                        return BaseResultUtil.fail("车辆{0}，收车人信息与订单不一致", orderCarNo);
                    }
                }
                //【验证】是否调度过，提送车只能有效执行一次
                if (paramsDto.getType() == WaybillTypeEnum.PICK.code) {
                    int n = waybillDao.countWaybill(orderCarId, WaybillTypeEnum.PICK.code);
                    if (n > 0) {
                        return BaseResultUtil.fail("车辆{0}，已经调度过提车运单", orderCarNo);
                    }
                } else {
                    int n = waybillDao.countWaybill(orderCarId, WaybillTypeEnum.BACK.code);
                    if (n > 0) {
                        return BaseResultUtil.fail("车辆{0}，已经调度过送车运单", orderCarNo);
                    }
                }
                //【验证】配送调度，需验证干线调度是否完成
                if (paramsDto.getType() == WaybillTypeEnum.BACK.code) {
                    WaybillCar waybillCar = waybillCarDao.findLastWaybillCar(orderCarId);
                    String startAreaCode = waybillCar == null ? order.getStartAreaCode() : waybillCar.getEndAreaCode();
                    String startCityCode = waybillCar == null ? order.getStartCityCode() : waybillCar.getEndCityCode();
                    if (!csOrderService.validateIsArriveStoreOrCityRange(startAreaCode, startCityCode, order.getEndStoreId(), order.getEndCityCode())) {
                        return BaseResultUtil.fail("车辆{0}，尚未到达目的地所属业务中心或目的地城市范围内", orderCarNo);
                    }
                }
                //验证是否已被其他人调度
                int n = WaybillTypeEnum.BACK.code == paramsDto.getType() ? waybillCarDao.countActiveWaybill(orderCar.getId(), WaybillTypeEnum.BACK.code) : waybillCarDao.countActiveWaybill(orderCar.getId(), WaybillTypeEnum.PICK.code);
                if (n > 0) {
                    return BaseResultUtil.fail("车辆{0}，已经被其他人调度, 请从订单历史界面重新调度", orderCarNo);
                }

            }

            Long currentMillisTime = System.currentTimeMillis();
            for (SaveLocalWaybillDto dto : list) {
                if (dto == null) {
                    continue;
                }
                CarrierInfo carrierInfo = carrierMap.get(dto.getCarrierId());
                OrderCar orderCar = getOrderCarFromMap(orderCarMap, dto.getOrderCarId());
                Order order = getOrderFromMap(orderMap, orderCar.getOrderId());

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
                waybill.setGuideLine(computeGuideLine(dto.getStartAreaCode(), dto.getEndAreaCode(), null, 1));
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
                fillWaybillCarExpectEndTime(waybillCar, order.getExpectStartDate());
                waybillCar.setReceiptFlag(validateIsArriveDest(waybillCar, order));
                if (waybill.getType() == WaybillTypeEnum.BACK.code && !waybillCar.getReceiptFlag()) {
                    throw new ParameterException("送车调度目前仅支持交付客户");
                }
                //运单车辆状态
                Boolean isInStore = getOrderCarIsInEndStore(orderCar.getNowStoreId(), waybillCar.getStartStoreId());
                waybillCar.setState(getWaybillCarState(waybill, isInStore, carrierInfo));
                if (waybill.getCarrierType() == WaybillCarrierTypeEnum.SELF.code) {
                    waybillCar.setLoadTime(currentMillisTime);
                }
                waybillCar.setCreateTime(currentMillisTime);
                waybillCarDao.insert(waybillCar);

                /**3、添加任务信息*/
                csTaskService.reCreate(waybill, Lists.newArrayList(waybillCar), Lists.newArrayList(waybillCar), carrierInfo);

                /**5、更新订单车辆状态*/
                updateOrderCarForDispatchLocal(orderCar.getId(), waybill, orderCar.getState(), isInStore);

                //推送客户消息
                if (waybill.getType() != null && waybill.getType().equals(WaybillTypeEnum.PICK.code)) {
                    getPushCustomerInfoForPick(pushCustomerInfoMap, order.getCustomerId(), orderCar.getNo(), carrierInfo, waybill.getCarrierType() == WaybillCarrierTypeEnum.LOCAL_CONSIGN.code);
                } else {
                    getPushCustomerInfoForPick(pushCustomerInfoMap, order.getCustomerId(), orderCar.getNo(), carrierInfo, waybill.getCarrierType() == WaybillCarrierTypeEnum.LOCAL_CONSIGN.code);
                }

                sendPushMsgToDriverForDispatch(carrierInfo.getDriverId(), waybill);

            }
            sendPushMsgToCustomerForDispatch(pushCustomerInfoMap);

            return BaseResultUtil.success();
        } finally {
            redisUtils.delayDelete(lockSet);
        }
    }

    @Override
    public Order getOrderFromMap(Map<Long, Order> orderMap, Long orderId) {
        Order order;
        if (orderMap.containsKey(orderId)) {
            order = orderMap.get(orderId);
        } else {
            order = orderDao.selectById(orderId);
            orderMap.put(orderId, order);
        }
        return order;
    }

    @Override
    public OrderCar getOrderCarFromMap(Map<Long, OrderCar> orderCarMap, Long orderCarId) {
        OrderCar orderCar;
        if (orderCarMap.containsKey(orderCarId)) {
            orderCar = orderCarMap.get(orderCarId);
        } else {
            orderCar = orderCarDao.selectById(orderCarId);
            orderCarMap.put(orderCarId, orderCar);
        }
        return orderCar;
    }

    private void sendPushMsgToDriverForDispatch(Long driverId, Waybill waybill) {
        try {
            //推送司机消息
            if (waybill.getCarrierType() != null && waybill.getCarrierType() == WaybillCarrierTypeEnum.LOCAL_ADMIN.code) {
                csPushMsgService.send(driverId, UserTypeEnum.ADMIN, PushMsgEnum.S_NEW_WAYBILL, waybill.getNo(), String.valueOf(waybill.getCarNum()));
            } else {
                csPushMsgService.send(driverId, UserTypeEnum.DRIVER, PushMsgEnum.D_NEW_WAYBILL, waybill.getNo(), String.valueOf(waybill.getCarNum()));
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }

    private Map<Long, Map<Long, PushCustomerInfo>> getPushCustomerInfoForPick(Map<Long, Map<Long, PushCustomerInfo>> pushCustomerInfoMap, Long customerId, String orderCarNo, CarrierInfo carrierInfo, boolean isHasVehicle) {
        if (pushCustomerInfoMap == null) {
            pushCustomerInfoMap = Maps.newHashMap();
        }
        try {
            Long driverId = carrierInfo.getDriverId();
            if (pushCustomerInfoMap.containsKey(customerId)) {
                if (pushCustomerInfoMap.get(customerId).containsKey(driverId)) {
                    pushCustomerInfoMap.get(customerId).get(driverId).getOrderCarNos().add(orderCarNo);
                } else {
                    pushCustomerInfoMap.get(customerId).put(driverId, new PushCustomerInfo(carrierInfo, isHasVehicle ? PushMsgEnum.C_CONSIGN_PICK : PushMsgEnum.C_PILOT_PICK, Lists.newArrayList(orderCarNo)));
                }
            } else {
                HashMap<Long, PushCustomerInfo> tempMap = Maps.newHashMap();
                tempMap.put(driverId, new PushCustomerInfo(carrierInfo, isHasVehicle ? PushMsgEnum.C_CONSIGN_PICK : PushMsgEnum.C_PILOT_PICK, Lists.newArrayList(orderCarNo)));
                pushCustomerInfoMap.put(customerId, tempMap);
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return pushCustomerInfoMap;
    }

    @Override
    public String computeGuideLine(String startAreaCode, String endAreaCode, String defaultGuideLine, Integer carNum) {
        return computeGuideLine(Sets.newHashSet(startAreaCode), Sets.newHashSet(endAreaCode), defaultGuideLine, carNum);
    }

    @Override
    public String computeGuideLine(Set<String> startAreaCodeSet, Set<String> endAreaCodeSet, String defaultGuideLine, Integer carNum) {
        //优先按输入
        if (StringUtils.isNotBlank(defaultGuideLine) && !"-".equals(defaultGuideLine.trim()) && defaultGuideLine.split("-").length >= 2) {
            return defaultGuideLine;
        }
        if (CollectionUtils.isEmpty(startAreaCodeSet)) {
            return null;
        }
        StringBuilder guideLine = new StringBuilder();
        if (carNum != null) {
            if (startAreaCodeSet.size() == 1) {
                FullCity startFullCity = csCityService.findFullCity(startAreaCodeSet.iterator().next(), CityLevelEnum.PROVINCE);
                if (startFullCity == null || startFullCity.getCity() == null) {
                    return null;
                }
                guideLine.append(startFullCity.getCity());
            }
            if (endAreaCodeSet != null && startAreaCodeSet.size() == 1) {
                FullCity endFullCity = csCityService.findFullCity(endAreaCodeSet.iterator().next(), CityLevelEnum.PROVINCE);
                if (endFullCity != null && endFullCity.getCity() != null) {
                    guideLine.append("-");
                    guideLine.append(endFullCity.getCity());
                }
            }
        }
        return guideLine.toString();
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
        //FullWaybill oldFw = getFullWaybill(paramsDto.getCarDto().getWaybillId());

        long currentTimeMillis = System.currentTimeMillis();
        UpdateLocalCarDto dto = paramsDto.getCarDto();
        String orderCarNo = dto.getOrderCarNo();
        Long orderCarId = dto.getOrderCarId();
        Long carrierId = paramsDto.getCarrierId();
        Set<String> lockSet = new HashSet<>();
        try {
            /**1、添加运单信息*/
            Waybill waybill = waybillDao.selectById(paramsDto.getId());
            if (waybill == null) {
                return BaseResultUtil.fail("运单不存在");
            }

            String lockKey = RedisKeys.getDispatchLock(waybill.getNo());
            if (!redisLock.lock(lockKey, 120000, 1, 100L)) {
                return BaseResultUtil.fail("运单，其他人正在调度", waybill.getNo());
            }
            lockSet.add(lockKey);

            //【验证】承运商是否可以运营
            CarrierInfo carrierInfo = validateLocalCarrierInfo(carrierId, paramsDto.getCarrierName(), paramsDto.getCarrierType(), paramsDto.getType(),
                    new UserInfo(dto.getLoadLinkUserId(), dto.getLoadLinkName(), dto.getLoadLinkPhone()),
                    new UserInfo(dto.getUnloadLinkUserId(), dto.getUnloadLinkName(), dto.getUnloadLinkPhone()),
                    orderCarNo);

            validateReAllotCarrier(carrierInfo, waybill.getCarrierId());
            if (waybill.getState() >= WaybillStateEnum.TRANSPORTING.code) {
                return BaseResultUtil.fail("运输中运单不允许修改");
            }

            /**验证运单车辆信息*/
            //加锁
            String lockCarKey = RedisKeys.getDispatchLock(orderCarNo);
            if (!redisLock.lock(lockCarKey, 120000, 1, 150L)) {
                return BaseResultUtil.fail("订单车辆{0}正在调度，请5秒后重试", orderCarNo);
            }
            lockSet.add(lockCarKey);

            if (!csStoreService.validateStoreParam(dto.getStartStoreId(), dto.getStartStoreName())) {
                log.error("业务中心参数错误(updateLocal):" + JSON.toJSONString(paramsDto));
                return BaseResultUtil.fail("运单中车辆{0}，始发地业务中心参数错误", orderCarNo);
            }
            if (!csStoreService.validateStoreParam(dto.getEndStoreId(), dto.getEndStoreName())) {
                log.error("业务中心参数错误(updateLocal):" + JSON.toJSONString(paramsDto));
                return BaseResultUtil.fail("运单中车辆{0}，目的地业务中心参数错误", orderCarNo);
            }
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

            //承运商类型
            waybill.setCarrierId(carrierInfo.getCarrierId());
            waybill.setCarrierName(carrierInfo.getCarrierName());
            waybill.setCarrierType(carrierInfo.getCarryType());
            waybill.setState(getWaybillState(carrierInfo.getCarryType()));
            //提送车费用逻辑，调度时不允许修改提送车费用，需要到订单中修改提送车费用，多则返还，少则后补
            waybill.setFreightFee(getLocalWaybillFreightFee(waybill, orderCar));
            waybill.setRemark(paramsDto.getRemark());
            waybill.setFixedFreightFee(false);
            waybill.setGuideLine(computeGuideLine(dto.getStartAreaCode(), dto.getEndAreaCode(), null, 1));
            waybillDao.updateByIdForNull(waybill);

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
            fillWaybillCarExpectEndTime(waybillCar, order.getExpectStartDate());
            //waybillCar.setReceiptFlag(waybillCar.getUnloadLinkPhone().equals(order.getBackContactPhone()));
            waybillCar.setReceiptFlag(validateIsArriveDest(waybillCar, order));
            //运单车辆状态
            Boolean isInStore = getOrderCarIsInEndStore(orderCar.getNowStoreId(), waybillCar.getStartStoreId());
            waybillCar.setState(getWaybillCarState(waybill, isInStore, carrierInfo));
            if (waybill.getCarrierType() == WaybillCarrierTypeEnum.SELF.code && waybill.getType() == WaybillTypeEnum.PICK.code) {
                waybillCar.setLoadTime(currentTimeMillis);
            }
            waybillCar.setLoadLinkUserId(waybillCar.getLoadLinkUserId());
            waybillCar.setUnloadLinkUserId(waybillCar.getUnloadLinkUserId());
            waybillCarDao.updateByIdForNull(waybillCar);

            //运单车辆状态
            /**3、添加任务信息*/
            csTaskService.reCreate(waybill, Lists.newArrayList(waybillCar), null, carrierInfo);

            /**5、更新订单车辆状态*/
            updateOrderCarForDispatchLocal(orderCar.getId(), waybill, orderCar.getState(), isInStore);

            if (carrierInfo.isReAllotCarrier()) {
                if (waybill.getCarrierType() != null && waybill.getCarrierType() == WaybillCarrierTypeEnum.LOCAL_CONSIGN.code) {
                    csPushMsgService.send(order.getCustomerId(), UserTypeEnum.CUSTOMER, PushMsgEnum.C_CONSIGN_PICK, orderCar.getNo(), carrierInfo.getDriverName(), carrierInfo.getDriverPhone(), carrierInfo.getVehiclePlateNo());
                } else {
                    csPushMsgService.send(order.getCustomerId(), UserTypeEnum.CUSTOMER, PushMsgEnum.C_PILOT_PICK, orderCar.getNo(), carrierInfo.getDriverName(), carrierInfo.getDriverPhone());
                }
                sendPushMsgToDriverForDispatch(carrierInfo.getDriverId(), waybill);
            }
            //记录修改历史
            return BaseResultUtil.success();
        } finally {
            redisUtils.delayDelete(lockSet);
        }
    }

    private Boolean validateIsArriveDest(WaybillCar waybillCar, Order order) {
        String orderPhone = order.getBackContactPhone() == null ? "" : order.getBackContactPhone();
        String orderAreaCode = order.getEndAreaCode() == null ? "" : order.getEndAreaCode();
        String orderAddress = order.getEndAddress() == null ? "" : order.getEndAddress();
        if (orderPhone.equals(waybillCar.getUnloadLinkPhone())
                && orderAreaCode.equals(waybillCar.getEndAreaCode())
                && orderAddress.equals(waybillCar.getEndAddress())) {
            return true;
        }
        return false;
    }

    private boolean validateIsFromOrigin(WaybillCar waybillCar, Order order) {
        String orderPhone = order.getPickContactPhone() == null ? "" : order.getPickContactPhone();
        String orderAreaCode = order.getStartAreaCode() == null ? "" : order.getStartAreaCode();
        String orderAddress = order.getStartAddress() == null ? "" : order.getStartAddress();
        if (orderPhone.equals(waybillCar.getLoadLinkPhone())
                && orderAreaCode.equals(waybillCar.getStartAreaCode())
                && orderAddress.equals(waybillCar.getStartAddress())) {
            return true;
        }
        return false;
    }

    private FullWaybill getFullWaybill(Long waybillId) {
        if (waybillId == null) {
            return null;
        }
        FullWaybill fullWaybill = new FullWaybill();
        Waybill waybill = waybillDao.selectById(waybillId);
        BeanUtils.copyProperties(waybill, fullWaybill);
        List<WaybillCar> list = waybillCarDao.findListByWaybillId(waybillId);
        fullWaybill.setWaybillCarList(list);
        return fullWaybill;
    }

    private Boolean getOrderCarIsInEndStore(Long nowStoreId, Long startStoreId) {
        return startStoreId != null && startStoreId.equals(nowStoreId);
    }

    private void updateOrderCarForDispatchLocal(Long orderCarId, Waybill waybill, Integer orderCarState, Boolean isInStore) {
        OrderCar noc = new OrderCar();
        noc.setId(orderCarId);
        if (waybill.getType() == WaybillTypeEnum.PICK.code) {
            noc.setState(waybill.getCarrierType() == WaybillCarrierTypeEnum.SELF.code ? OrderCarStateEnum.PICKING.code : OrderCarStateEnum.WAIT_PICK.code);
            noc.setPickType(getLocalOrderCarCarryType(waybill.getCarrierType()));
            noc.setPickState(OrderCarLocalStateEnum.DISPATCHED.code);
            orderCarDao.updateById(noc);
        } else {
            //车辆实际运输状态
            if (orderCarState == OrderCarStateEnum.WAIT_BACK_DISPATCH.code) {
                noc.setState(waybill.getCarrierType() == WaybillCarrierTypeEnum.SELF.code ? (isInStore ? OrderCarStateEnum.BACKING.code : OrderCarStateEnum.WAIT_BACK.code) : OrderCarStateEnum.WAIT_BACK.code);
            }
            noc.setBackType(getLocalOrderCarCarryType(waybill.getCarrierType()));
            noc.setBackState(OrderCarLocalStateEnum.DISPATCHED.code);
            orderCarDao.updateById(noc);
        }
    }

    private Integer getWaybillCarState(Waybill waybill, Boolean isInStore, CarrierInfo carrierInfo) {
        if (carrierInfo.getCarryType() == WaybillCarrierTypeEnum.SELF.code) {
            return waybill.getType() == WaybillTypeEnum.PICK.code ? WaybillCarStateEnum.WAIT_UNLOAD_CONFIRM.code : (isInStore ? WaybillCarStateEnum.WAIT_LOAD_CONFIRM.code : WaybillCarStateEnum.WAIT_LOAD.code);
        } else {
            if (carrierInfo.getCarrierType() == CarrierTypeEnum.ENTERPRISE.code) {
                return WaybillCarStateEnum.WAIT_ALLOT.code;
            } else {
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
        if (carrierType == WaybillCarrierTypeEnum.LOCAL_ADMIN.code) {
            Admin admin = adminDao.selectById(newCarrierId);
            if (admin == null || admin.getState() != AdminStateEnum.CHECKED.code) {
                throw new ParameterException("车辆{0}，所选业务员已离职", orderCarNo);
            }
            carrierInfo.setCarrierName(admin.getName());
            carrierInfo.setCarrierType(CarrierTypeEnum.PERSONAL.code);
            carrierInfo.setDriverId(admin.getId());
            carrierInfo.setDriverName(admin.getName());
            carrierInfo.setDriverPhone(admin.getPhone());
        } else if (carrierType == WaybillCarrierTypeEnum.SELF.code) {
            //carrierInfo.setCarrierType(CarrierTypeEnum.PERSONAL.code);
            if (WaybillTypeEnum.PICK.code == waybillType) {
                carrierInfo.setCarrierName(loadUser.getName());
                carrierInfo.setDriverName(loadUser.getName());
                carrierInfo.setDriverPhone(loadUser.getPhone());
            } else {
                carrierInfo.setCarrierName(unloadUser.getName());
                carrierInfo.setDriverName(unloadUser.getName());
                carrierInfo.setDriverPhone(unloadUser.getPhone());
            }
        } else {
            Carrier carrier = carrierDao.selectById(newCarrierId);
            if (carrier.getState() == null || carrier.getState() != CommonStateEnum.CHECKED.code || carrier.getBusinessState() == null || carrier.getBusinessState() != 0) {
                throw new ParameterException("车辆{0}，所选承运商停运中", orderCarNo);
            }
            carrierInfo.setCarrierName(carrier.getName());
            carrierInfo.setCarrierType(carrier.getType());
            //验证司机信息
            if (carrier.getType() == 1) {
                Driver driver = driverDao.findTopByCarrierId(newCarrierId);
                if (driver == null) {
                    throw new ParameterException("车辆{0}，所选承运商没有运营中的司机", orderCarNo);
                }
                List<BankCardVo> binkCardInfoList = bankCardBindDao.findBinkCardInfo(newCarrierId);
                if (CollectionUtils.isEmpty(binkCardInfoList)) {
                    throw new ParameterException("车辆{0}，所选承运商没有绑定或者没有可用的银行卡", orderCarNo);
                }
                carrierInfo.setDriverId(driver.getId());
                carrierInfo.setDriverName(driver.getName());
                carrierInfo.setDriverPhone(driver.getPhone());
                if (carrierType != WaybillCarrierTypeEnum.LOCAL_PILOT.code) {
                    VehicleRunning vr = vehicleRunningDao.findByDriverId(driver.getId());
                    if (vr == null) {
                        throw new ParameterException("运单，所选司机没有绑定车牌号");
                    }
                    carrierInfo.setVehicleId(vr.getId());
                    carrierInfo.setVehiclePlateNo(vr.getPlateNo());
                }
            } else {
                //查询企业联系人司机
                Driver driver = driverDao.findMasterByCarrierId(carrier.getId());
                if (driver != null) {
                    carrierInfo.setDriverPhone(driver.getPhone());
                    carrierInfo.setDriverId(driver.getId());
                    carrierInfo.setDriverName(driver.getName());
                }
            }
        }

        return carrierInfo;
    }

/*    private boolean validateIsArriveEndCity(Order order, WaybillCar waybillCar) {
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

    private WaybillCar fillWaybillCarAdmin(WaybillCar waybillCar, Integer type) {
        if (WaybillTypeEnum.PICK.code != type) {
            if (StringUtils.isBlank(waybillCar.getLoadLinkPhone())) {
                Admin admin = csAdminService.findLoop(waybillCar.getStartStoreId());
                if (admin == null) {
                    throw new ParameterException("业务中心无人员");
                }
                waybillCar.setLoadLinkUserId(admin.getId());
                waybillCar.setLoadLinkName(admin.getName());
                waybillCar.setLoadLinkPhone(admin.getPhone());
            }
        }
        if (WaybillTypeEnum.BACK.code != type) {
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
        if (oldCarrierId.equals(newCarrierId)) {
            carrierInfo.setReAllotCarrier(false);
        } else {
            carrierInfo.setReAllotCarrier(true);
        }
        return carrierInfo;
    }

    /**
     * 计算到达时间
     **/
    private WaybillCar fillWaybillCarExpectEndTime(WaybillCar waybillCar, Long expectStartDate) {
        if (waybillCar.getExpectStartTime() == null || waybillCar.getExpectStartTime() <= 0) {
            waybillCar.setExpectStartTime(System.currentTimeMillis() < expectStartDate ? expectStartDate : System.currentTimeMillis());
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
        Long startBelongStoreId = csStoreService.getBelongStoreId(wc.getStartStoreId(), wc.getStartAreaCode());
        if (startBelongStoreId != null) {
            wc.setStartBelongStoreId(startBelongStoreId);
        }
        Long endBelongStoreId = csStoreService.getBelongStoreId(wc.getEndStoreId(), wc.getEndAreaCode());
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
        log.debug("【干线调度】------------>参数" + JSON.toJSONString(paramsDto));
        Set<String> lockSet = new HashSet<>();
        Map<Long, Map<Long, PushCustomerInfo>> pushCustomerInfoMap = Maps.newHashMap();
        try {
            long currentTimeMillis = System.currentTimeMillis();
            Long carrierId = paramsDto.getCarrierId();
            List<SaveTrunkWaybillCarDto> dtoList = paramsDto.getList();

            //【验证】承运商和司机信息
            CarrierInfo carrierInfo = validateTrunkCarrierInfo(carrierId);

            /**2、运单中车辆循环验证*/
            Set<String> orderCarNoSet = Sets.newHashSet();
            List<WaybillCar> waybillCars = Lists.newArrayList();
            Map<Long, Order> orderMap = Maps.newHashMap();
            Map<Long, OrderCar> orderCarMap = Maps.newHashMap();
            for (SaveTrunkWaybillCarDto dto : dtoList) {
                if (dto == null) {
                    continue;
                }
                String orderCarNo = dto.getOrderCarNo();
                Long orderCarId = dto.getOrderCarId();

                //加锁
                String lockKey = RedisKeys.getDispatchLock(orderCarNo);
                if (!redisLock.lock(lockKey, 120000, 1, 100L)) {
                    return BaseResultUtil.fail("订单车辆{0}正在调度，请5秒后重试", orderCarNo);
                }
                lockSet.add(lockKey);
                if (!csStoreService.validateStoreParam(dto.getStartStoreId(), dto.getStartStoreName())) {
                    log.error("业务中心参数错误(saveTrunk):" + JSON.toJSONString(paramsDto));
                    return BaseResultUtil.fail("运单中车辆{0}，始发地业务中心参数错误", orderCarNo);
                }
                if (!csStoreService.validateStoreParam(dto.getEndStoreId(), dto.getEndStoreName())) {
                    log.error("业务中心参数错误(saveTrunk):" + JSON.toJSONString(paramsDto));
                    return BaseResultUtil.fail("运单中车辆{0}，目的地业务中心参数错误", orderCarNo);
                }

                //验证订单车辆状态
                OrderCar orderCar = getOrderCarFromMap(orderCarMap, orderCarId);
                if (orderCar == null) {
                    return BaseResultUtil.fail("运单车辆{0}，不存在", orderCarNo);
                }
                if (orderCar.getState() == null) {
                    return BaseResultUtil.fail("运单中车辆{0}，无法提车调度", orderCarNo);
                }
                //验证订单状态
                Order order = getOrderFromMap(orderMap, orderCar.getOrderId());
                if (order == null) {
                    return BaseResultUtil.fail("运单中车辆{0}，所属订单车辆不存在", orderCarNo);
                }

                if (order.getState() == null
                        || order.getState() < OrderStateEnum.CHECKED.code
                        || order.getState() > OrderStateEnum.FINISHED.code) {
                    return BaseResultUtil.fail("运单中车辆{0}，所属订单状态无法干线调度", orderCarNo);
                }

                //包板线路不能为空
                Line line = csLineService.getLineByCity(dto.getStartCityCode(), dto.getEndCityCode(), true);
                //validateLine(line, dto.getLineId());
                dto.setLineId(line == null ? null : line.getId());
                if (paramsDto.getFixedFreightFee() && (dto.getLineId() == null || dto.getLineId() <= 0)) {
                    return BaseResultUtil.fail("运单中车辆{0}，线路不能为空", orderCarNo);
                }

                //验证出发地与上一次调度目的地是否一致
                WaybillCar prevWc = waybillCarDao.findLastByOderCarId(orderCarId);
                if (prevWc != null) {
                    if (!prevWc.getEndAreaCode().equals(dto.getStartAreaCode()) || !prevWc.getEndAddress().equals(dto.getStartAddress())) {
                        return BaseResultUtil.fail("运单中车辆{0},已经被其他人调度, 请从订单历史界面重新调度", orderCarNo);
                    }
                }

                //同一订单车辆不能重复
                if (orderCarNoSet.contains(dto.getOrderCarNo())) {
                    return BaseResultUtil.fail("运单中车辆{0}，重复", dto.getOrderCarNo());
                }

            }

            /**1、组装运单信息*/
            Waybill waybill = new Waybill();
            waybill.setNo(sendNoService.getNo(SendNoTypeEnum.WAYBILL));
            waybill.setType(WaybillTypeEnum.TRUNK.code);
            waybill.setSource(WaybillSourceEnum.MANUAL.code);
            waybill.setCarrierId(carrierInfo.getCarrierId());
            waybill.setCarrierName(carrierInfo.getCarrierName());
            waybill.setCarrierType(carrierInfo.getCarryType());
            waybill.setCarNum(dtoList.size());
            waybill.setState(WaybillStateEnum.ALLOT_CONFIRM.code);
            waybill.setFreightFee(MoneyUtil.yuanToFen(paramsDto.getFreightFee()));
            waybill.setRemark(paramsDto.getRemark());
            waybill.setCreateTime(currentTimeMillis);
            waybill.setCreateUser(paramsDto.getLoginName());
            waybill.setCreateUserId(paramsDto.getLoginId());
            waybill.setFixedFreightFee(paramsDto.getFixedFreightFee());
            if (!CollectionUtils.isEmpty(dtoList)) {
                Set<String> startAreaCodeSet = dtoList.stream().map(SaveTrunkWaybillCarDto::getStartAreaCode).collect(Collectors.toSet());
                Set<String> endAreaCodeSet = dtoList.stream().map(SaveTrunkWaybillCarDto::getEndAreaCode).collect(Collectors.toSet());
                waybill.setGuideLine(computeGuideLine(startAreaCodeSet, endAreaCodeSet, paramsDto.getGuideLine(), dtoList.size()));
            }
            //TODO 干线运单所属业务中心
            //waybill.setInputStoreId(paramsDto.);
            waybillDao.insert(waybill);

            for (SaveTrunkWaybillCarDto dto : dtoList) {
                if (dto == null) {
                    continue;
                }
                OrderCar orderCar = getOrderCarFromMap(orderCarMap, dto.getOrderCarId());
                Order order = getOrderFromMap(orderMap, orderCar.getOrderId());
                WaybillCar waybillCar = new WaybillCar();
                BeanUtils.copyProperties(dto, waybillCar);
                waybillCar.setWaybillId(waybill.getId());
                waybillCar.setWaybillNo(waybill.getNo());
                waybillCar.setFreightFee(MoneyUtil.yuanToFen(dto.getFreightFee()));
                waybillCar.setOrderCarId(orderCar.getId());
                //城市信息赋值
                fillWaybillCarCityInfo(waybillCar);
                //业务中心信息赋值
                fillWaybillCarBelongStoreInfo(waybillCar);
                //提送车业务员
                fillWaybillCarAdmin(waybillCar, waybill.getType());
                //计算预计到达时间
                fillWaybillCarExpectEndTime(waybillCar, order.getExpectStartDate());
                //waybillCar.setReceiptFlag(waybillCar.getUnloadLinkPhone().equals(order.getBackContactPhone()));
                waybillCar.setReceiptFlag(validateIsArriveDest(waybillCar, order));
                waybillCar.setState(getTrunkState(carrierInfo));
                waybillCar.setCreateTime(currentTimeMillis);
                waybillCarDao.insert(waybillCar);

                //更新订单车辆状态
                LogUtil.debug("【干线调度】获取车辆状态");
                OrderCar noc = getOrderCarForChangeTrunk(orderCar, waybillCar, order);
                orderCarDao.updateById(noc);

                //提取属性
                orderCarNoSet.add(waybillCar.getOrderCarNo());
                waybillCars.add(waybillCar);

                //推送客户消息
                if (noc.getPickType() != null && OrderPickTypeEnum.WL.code == noc.getPickType()) {
                    getPushCustomerInfoForPick(pushCustomerInfoMap, order.getCustomerId(), orderCar.getNo(), carrierInfo, true);
                }

            }

            //承运商有且仅有一个司机
            /**1+、写入任务表*/
            csTaskService.reCreate(waybill, waybillCars, waybillCars, carrierInfo);

            sendPushMsgToCustomerForDispatch(pushCustomerInfoMap);
            //推送司机消息
            sendPushMsgToDriverForDispatch(carrierInfo.getDriverId(), waybill);

            return BaseResultUtil.success();
        } finally {
            //redisUtil.del(lockSet.toArray(new String[0]));
            redisUtils.delayDelete(lockSet);
        }
    }

    private void validateLine(Line line, Long lineId) {
        Long vlineId = line == null ? 0 : line.getId();
        lineId = lineId == null ? 0 : lineId;
        if (!vlineId.equals(lineId)) {
            if (line != null) {
                throw new ParameterException("传参线路{0}与查询线路{1}({2}-{3})不匹配", lineId, vlineId, line.getFromCity(), line.getToCity());
            } else {
                throw new ParameterException("传参线路{0}与查询线路{1}不匹配", lineId, vlineId);
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
        log.debug("【干线调度修改】------------>参数" + JSON.toJSONString(paramsDto));
        Set<String> lockSet = new HashSet<>();
        Map<Long, Map<Long, PushCustomerInfo>> pushCustomerInfoMap = Maps.newHashMap();
        try {
            List<UpdateTrunkWaybillCarDto> dtoList = paramsDto.getList();
            Long carrierId = paramsDto.getCarrierId();

            Waybill waybill = waybillDao.selectById(paramsDto.getId());
            if (waybill == null) {
                return BaseResultUtil.fail("运单不存在");
            }
            //加锁
            String lockKey = RedisKeys.getDispatchLock(waybill.getNo());
            if (!redisLock.lock(lockKey, 120000, 1, 100L)) {
                return BaseResultUtil.fail("运单{0}正在修改，请5秒后重试", waybill.getNo());
            }
            lockSet.add(lockKey);
            //是否重新分配任务
            CarrierInfo carrierInfo = validateTrunkCarrierInfo(carrierId);
            validateReAllotCarrier(carrierInfo, waybill.getCarrierId());
            if (waybill.getState() >= WaybillStateEnum.TRANSPORTING.code && carrierInfo.isReAllotCarrier()) {
                return BaseResultUtil.fail("运单运输中，不能修改司机，请使用[卸载车辆]功能");
            }

            /**2、运单中车辆循环*/
            Map<Long, Order> orderMap = Maps.newHashMap();
            Map<Long, OrderCar> orderCarMap = Maps.newHashMap();
            for (UpdateTrunkWaybillCarDto dto : dtoList) {
                if (dto == null) {
                    continue;
                }
                String orderCarNo = dto.getOrderCarNo();
                Long orderCarId = dto.getOrderCarId();
                //加锁
                String lockCarKey = RedisKeys.getDispatchLock(orderCarNo);
                if (!redisLock.lock(lockCarKey, 120000, 1, 150L)) {
                    return BaseResultUtil.fail("车辆{0}正在调度，请5秒后重试", orderCarNo);
                }
                lockSet.add(lockCarKey);

                if (!csStoreService.validateStoreParam(dto.getStartStoreId(), dto.getStartStoreName())) {
                    log.error("业务中心参数错误(updateTrunk):" + JSON.toJSONString(paramsDto));
                    return BaseResultUtil.fail("运单中车辆{0}，始发地业务中心参数错误", orderCarNo);
                }
                if (!csStoreService.validateStoreParam(dto.getEndStoreId(), dto.getEndStoreName())) {
                    log.error("业务中心参数错误(updateTrunk):" + JSON.toJSONString(paramsDto));
                    return BaseResultUtil.fail("运单中车辆{0}，目的地业务中心参数错误", orderCarNo);
                }

                WaybillCar waybillCar = null;
                if (dto.getId() != null) {
                    //修改的车辆
                    waybillCar = waybillCarDao.selectById(dto.getId());
                    log.debug("【干线调度修改】已有车辆修改：" + JSON.toJSONString(waybillCar));
                    if (waybillCar.getState() != null && waybillCar.getState() >= WaybillCarStateEnum.LOADED.code && carrierInfo.isReAllotCarrier()) {
                        return BaseResultUtil.fail("运单中车辆{0}，运输中不能修改司机，请使用[卸载车辆]功能", orderCarNo);
                    }
                    if (waybillCar.getState() != null && waybillCar.getState() >= WaybillCarStateEnum.UNLOADED.code) {
                        continue;
                    }
                }
                if (waybillCar == null) {
                    waybillCar = new WaybillCar();
                    log.debug("【干线调度修改】新车辆{}", orderCarNo);
                }

                //验证订单车辆状态
                OrderCar orderCar = getOrderCarFromMap(orderCarMap, orderCarId);
                if (orderCar == null) {
                    return BaseResultUtil.fail("运单中车辆{0}，不存在", orderCarNo);
                }
                if (orderCar.getState() == null) {
                    return BaseResultUtil.fail("运单中车辆{0}，无法提车调度", orderCarNo);
                }
                //验证订单状态
                Order order = getOrderFromMap(orderMap, orderCar.getOrderId());
                if (order == null) {
                    return BaseResultUtil.fail("运单中车辆{0}，所属订单车辆不存在", orderCarNo);
                }
                if (order.getState() == null
                        || order.getState() < OrderStateEnum.CHECKED.code
                        || order.getState() > OrderStateEnum.FINISHED.code) {
                    return BaseResultUtil.fail("运单中车辆{0}，所属订单状态无法干线调度", orderCarNo);
                }

                //包板线路不能为空
                Line line = csLineService.getLineByCity(dto.getStartCityCode(), dto.getEndCityCode(), true);
                //validateLine(line, dto.getLineId());
                dto.setLineId(line == null ? null : line.getId());
                if (paramsDto.getFixedFreightFee() && (dto.getLineId() == null || dto.getLineId() <= 0)) {
                    return BaseResultUtil.fail("运单中车辆{0}，线路不能为空", orderCarNo);
                }
                //验证出发地与上一次调度目的地是否一致
                WaybillCar prevWc = dto.getId() == null ? waybillCarDao.findLastByOderCarId(orderCarId) : waybillCarDao.findLastByOderCarIdAndId(waybillCar.getId(), orderCarId);
                if (prevWc != null && !prevWc.getEndAddress().equals(dto.getStartAddress())) {
                    return BaseResultUtil.fail("运单中车辆{0}，已经被其他人调度, 请从订单历史界面重新调度", orderCarNo, dto.getStartAddress(), prevWc.getWaybillNo(), prevWc.getEndAddress());
                }
            }

            /**1、组装运单信息*/
            waybill.setCarrierId(carrierInfo.getCarrierId());
            waybill.setCarrierName(carrierInfo.getCarrierName());
            waybill.setCarrierType(carrierInfo.getCarryType());
            waybill.setCarNum(dtoList.size());
            waybill.setFreightFee(MoneyUtil.yuanToFen(paramsDto.getFreightFee()));
            waybill.setFixedFreightFee(paramsDto.getFixedFreightFee());
            waybill.setRemark(paramsDto.getRemark());
            if (!CollectionUtils.isEmpty(dtoList)) {
                Set<String> startAreaCodeSet = dtoList.stream().map(UpdateTrunkWaybillCarDto::getStartAreaCode).collect(Collectors.toSet());
                Set<String> endAreaCodeSet = dtoList.stream().map(UpdateTrunkWaybillCarDto::getEndAreaCode).collect(Collectors.toSet());
                waybill.setGuideLine(computeGuideLine(startAreaCodeSet, endAreaCodeSet, paramsDto.getGuideLine(), dtoList.size()));
            }
            waybillDao.updateByIdForNull(waybill);

            //处理车辆
            Set<Long> unCancelWaybillCarIds = Sets.newHashSet();
            List<WaybillCar> newWaybillCars = Lists.newArrayList();
            List<WaybillCar> waybillCars = Lists.newArrayList();
            for (UpdateTrunkWaybillCarDto dto : dtoList) {
                if (dto == null) {
                    continue;
                }
                String orderCarNo = dto.getOrderCarNo();
                Long orderCarId = dto.getOrderCarId();
                boolean isNewWaybillCar = false;
                WaybillCar waybillCar = null;
                if (dto.getId() != null) {
                    //修改的车辆
                    waybillCar = waybillCarDao.selectById(dto.getId());
                    log.debug("【干线调度修改】已有车辆修改：" + JSON.toJSONString(waybillCar));
                    if (waybillCar.getState() != null && waybillCar.getState() >= WaybillCarStateEnum.UNLOADED.code) {
                        continue;
                    }
                }
                if (waybillCar == null) {
                    //新增的车辆
                    isNewWaybillCar = true;
                    waybillCar = new WaybillCar();
                    log.debug("【干线调度修改】新车辆{}", orderCarNo);
                }
                boolean isChangeCarState = false;
                if (waybillCar.getState() == null || (WaybillCarStateEnum.WAIT_LOAD.code >= waybillCar.getState() && carrierInfo.isReAllotCarrier())) {
                    isChangeCarState = true;
                }

                //验证订单车辆状态
                OrderCar orderCar = orderCarDao.selectById(orderCarId);
                //验证订单状态
                Order order = orderDao.selectById(orderCar.getOrderId());
                boolean isChangeAddress = false;
                //验证是否变更地址
                if (!(waybillCar.getEndAreaCode() != null && waybillCar.getEndAreaCode().equals(dto.getEndAreaCode())) || !(waybillCar.getEndAddress() != null && waybillCar.getEndAddress().equals(dto.getEndAddress()))) {
                    isChangeAddress = true;
                }
                //车辆数据
                BeanUtils.copyProperties(dto, waybillCar);
                waybillCar.setWaybillId(waybill.getId());
                waybillCar.setWaybillNo(waybill.getNo());
                waybillCar.setFreightFee(MoneyUtil.yuanToFen(dto.getFreightFee()));
                //城市信息赋值
                fillWaybillCarCityInfo(waybillCar);
                //业务中心信息赋值
                fillWaybillCarBelongStoreInfo(waybillCar);
                //提送车业务员
                fillWaybillCarAdmin(waybillCar, waybill.getType());
                //计算预计到达时间
                fillWaybillCarExpectEndTime(waybillCar, order.getExpectStartDate());
                waybillCar.setReceiptFlag(validateIsArriveDest(waybillCar, order));
                if (isChangeCarState || isNewWaybillCar) {
                    waybillCar.setState(getTrunkState(carrierInfo));
                }
                int row = isNewWaybillCar ? waybillCarDao.insert(waybillCar) : waybillCarDao.updateByIdForNull(waybillCar);

                //变更地址取消后续运单
                if (isChangeAddress) {
                    //取消该车辆所有后续调度
                    cancelAfterDispatch(waybillCar);
                }

                //更新车辆信息
                LogUtil.debug("【干线调度修改】获取车辆状态");
                OrderCar noc = getOrderCarForChangeTrunk(orderCar, waybillCar, order);
                orderCarDao.updateById(noc);
                //提取信息
                unCancelWaybillCarIds.add(waybillCar.getId());
                waybillCars.add(waybillCar);
                if (isNewWaybillCar) {
                    newWaybillCars.add(waybillCar);
                }

                if (carrierInfo.isReAllotCarrier()) {
                    getPushCustomerInfoForPick(pushCustomerInfoMap, order.getCustomerId(), orderCar.getNo(), carrierInfo, true);
                }

            }

            //查询待取消的车辆
            List<WaybillCar> cancelWaybillCars = waybillCarDao.findWaitCancelListByUnCancelIds(unCancelWaybillCarIds, waybill.getId());
            if (!CollectionUtils.isEmpty(cancelWaybillCars)) {
                //取消运单车辆
                cancelWaybillCars.forEach(waybillCar -> cancelWaybillCar(waybill, waybillCar));
            }

            /**承运商有且仅有一个司机*/
            csTaskService.reCreate(waybill, waybillCars, newWaybillCars, carrierInfo);

            //验证运单是否完成
            validateAndFinishWaybill(waybill.getId());
            log.debug("【干线调度修改】------------->修改结束：" + JSON.toJSONString(waybillCars));

            if (carrierInfo.isReAllotCarrier()) {
                sendPushMsgToCustomerForDispatch(pushCustomerInfoMap);
                sendPushMsgToDriverForDispatch(carrierInfo.getDriverId(), waybill);
            }
            return BaseResultUtil.success();
        } finally {
            redisUtils.delayDelete(lockSet);
            //redisUtil.del(lockSet.toArray(new String[0]));
        }
    }

    private void sendPushMsgToCustomerForDispatch(Map<Long, Map<Long, PushCustomerInfo>> pushCustomerInfoMap) {
        pushCustomerInfoMap.forEach((custId, driverMap) -> driverMap.forEach((driverId, pcInfo) -> {
            if (!CollectionUtils.isEmpty(pcInfo.getOrderCarNos())) {
                if (PushMsgEnum.C_CONSIGN_PICK == pcInfo.getPushMsgEnum()) {
                    csPushMsgService.send(custId, UserTypeEnum.CUSTOMER, PushMsgEnum.C_CONSIGN_PICK, Joiner.on(",").join(pcInfo.getOrderCarNos()), pcInfo.getCarrierInfo().getDriverName(), pcInfo.getCarrierInfo().getDriverPhone(), pcInfo.getCarrierInfo().getVehiclePlateNo() == null ? "" : pcInfo.getCarrierInfo().getVehiclePlateNo());
                } else {
                    csPushMsgService.send(custId, UserTypeEnum.CUSTOMER, PushMsgEnum.C_PILOT_PICK, Joiner.on(",").join(pcInfo.getOrderCarNos()), pcInfo.getCarrierInfo().getDriverName(), pcInfo.getCarrierInfo().getDriverPhone());
                }
            }
        }));
    }

    private void cancelAfterDispatch(WaybillCar waybillCar) {
        List<WaybillCar> afterWaybillCars = waybillCarDao.findAfterWaybillCar(waybillCar.getId(), waybillCar.getOrderCarNo());
        if (!CollectionUtils.isEmpty(afterWaybillCars)) {

            List<Long> collect = afterWaybillCars.stream().map(WaybillCar::getId).collect(Collectors.toList());
            waybillCarDao.cancelBatch(collect);

            List<Task> afterTasks = taskDao.findListByWaybillCarIds(collect);
            if (!CollectionUtils.isEmpty(afterTasks)) {
                afterTasks.forEach(task -> {
                    taskDao.updateNum(task.getId());
                    csTaskService.validateAndFinishTask(task);
                });
            }
            List<Waybill> afterWaybills = waybillDao.findListByWaybillCarIds(collect);
            if (!CollectionUtils.isEmpty(afterWaybills)) {
                afterWaybills.forEach(w -> {
                    waybillDao.updateNumAndFreightFee(w.getId());
                    validateAndFinishWaybill(w.getId());
                });
            }
        }
    }

    private Integer getTrunkState(CarrierInfo carrierInfo) {
        return carrierInfo.getCarryType() == CarrierTypeEnum.PERSONAL.code ? WaybillCarStateEnum.WAIT_LOAD.code : WaybillCarStateEnum.WAIT_ALLOT.code;
    }

    private OrderCar getOrderCarForChangeTrunk(OrderCar orderCar, WaybillCar waybillCar, Order order) {
        LogUtil.debug("【计算车辆状态】-------->{}", JSON.toJSONString(orderCar));
        Long orderCarId = orderCar.getId();
        OrderCar noc = new OrderCar();
        noc.setId(orderCarId);

        DispatchNum dsEntity = waybillCarDao.countOrderDispatchState(waybillCar.getOrderCarId());
        if (dsEntity.getTrunkNum() == 0) {
            noc.setPickState(dsEntity.getPickNum() > 0 ? OrderCarLocalStateEnum.DISPATCHED.code : OrderCarLocalStateEnum.WAIT_DISPATCH.code);
        }
        noc.setTrunkState(dsEntity.getTrunkNum() > 0 ? OrderCarTrunkStateEnum.WAIT_NEXT_DISPATCH.code : OrderCarTrunkStateEnum.WAIT_DISPATCH.code);
        noc.setBackState(dsEntity.getBackNum() > 0 ? OrderCarLocalStateEnum.DISPATCHED.code : OrderCarLocalStateEnum.WAIT_DISPATCH.code);
        //第一段干线运单
        int n = waybillCarDao.countPrevTrunk(waybillCar.getId());
        if (n == 0) {
            //提干,APP 自送单起始地是业务中心，无法验证地址，只能验证手机号
            if (validateIsFromOrigin(waybillCar, order)) {
                LogUtil.debug("【计算车辆状态】isFromOrigin");
                noc.setState(OrderCarStateEnum.WAIT_TRUNK.code);
                //验证是否存在提车运单
                int i = waybillCarDao.countActiveWaybill(orderCarId, WaybillTypeEnum.PICK.code);
                if (i <= 0) {
                    LogUtil.debug("【计算车辆状态】isFromOrigin, 不存在提车运单");
                    noc.setPickType(OrderPickTypeEnum.WL.code);
                    noc.setPickState(OrderCarLocalStateEnum.F_WL.code);
                }
            }
        }

        // 最后一段干线运单
        if (validateIsArriveEndStore(order.getEndStoreId(), waybillCar.getEndStoreId())) {
            LogUtil.debug("【计算车辆状态】isArriveEndStore");
            noc.setTrunkState(OrderCarTrunkStateEnum.DISPATCHED.code);
            //如果送车方式是物流上门变更为订单提送车方式
            if (OrderPickTypeEnum.WL.code == orderCar.getBackType()) {
                if (OrderPickTypeEnum.WL.code != order.getBackType()) {
                    LogUtil.debug("【计算车辆状态】isArriveEndStore, 订单是物流上门");
                    noc.setBackType(order.getBackType());
                } else {
                    noc.setBackType(OrderPickTypeEnum.PILOT.code);
                }
            }
        }

        //是否交付客户-干送运单
        if (validateIsArriveDest(waybillCar, order)) {
            LogUtil.debug("【计算车辆状态】isArriveDest, 交付客户-干送运单");
            noc.setTrunkState(OrderCarTrunkStateEnum.DISPATCHED.code);
            //验证是否存在送车运单
            int i = waybillCarDao.countActiveWaybill(orderCarId, WaybillTypeEnum.BACK.code);
            if (i <= 0) {
                noc.setBackState(OrderCarLocalStateEnum.F_WL.code);
                noc.setBackType(OrderPickTypeEnum.WL.code);
            }
        }
        LogUtil.debug("【计算车辆状态】-------->{}", JSON.toJSONString(noc));
        return noc;
    }

    @Override
    public boolean validateIsArriveEndStore(Long orderEndStoreId, Long waybillCarEndStoreId) {
        if (orderEndStoreId == null || orderEndStoreId <= 0) {
            return false;
        }
        if (waybillCarEndStoreId == null || waybillCarEndStoreId <= 0) {
            return false;
        }
        if (!orderEndStoreId.equals(waybillCarEndStoreId)) {
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
            List<BankCardVo> binkCardInfoList = bankCardBindDao.findBinkCardInfo(carrierId);
            if (CollectionUtils.isEmpty(binkCardInfoList)) {
                throw new ParameterException("运单，所选承运商没有绑定或没有可用的银行卡");
            }
            VehicleRunning vr = vehicleRunningDao.findByDriverId(driver.getId());
            if (vr == null) {
                throw new ParameterException("运单，所选司机没有绑定车牌号");
            }
            carrierInfo.setDriverId(driver.getId());
            carrierInfo.setDriverName(driver.getName());
            carrierInfo.setDriverPhone(driver.getPhone());
            carrierInfo.setVehicleId(vr.getId());
            carrierInfo.setVehiclePlateNo(vr.getPlateNo());
        } else {
            //查询企业联系人司机
            Driver driver = driverDao.findMasterByCarrierId(carrier.getId());
            if (driver != null) {
                carrierInfo.setDriverPhone(driver.getPhone());
                carrierInfo.setDriverId(driver.getId());
                carrierInfo.setDriverName(driver.getName());
            }
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
        list.forEach(waybillCar -> cancelWaybillCar(waybill, waybillCar));

        //修改运单主单状态
        waybillDao.updateStateById(WaybillStateEnum.F_CANCEL.code, waybill.getId());

    }

    @Override
    public void cancelWaybillCar(WaybillCar waybillCar) {
        cancelWaybillCar(null, waybillCar);
    }

    @Override
    public WaybillCar getWaybillCarByTaskCarIdFromMap(Map<Long, WaybillCar> waybillCarMap, Long taskCarId) {
        WaybillCar wc;
        if (waybillCarMap.containsKey(taskCarId)) {
            wc = waybillCarMap.get(taskCarId);
        } else {
            wc = waybillCarDao.findByTaskCarId(taskCarId);
            waybillCarMap.put(taskCarId, wc);
        }
        return wc;
    }

    @Override
    public WaybillCar getWaybillCarByIdFromMap(Map<Long, WaybillCar> waybillCarMap, Long waybillCarId) {
        WaybillCar wc;
        if (waybillCarMap.containsKey(waybillCarId)) {
            wc = waybillCarMap.get(waybillCarId);
        } else {
            wc = waybillCarDao.findByTaskCarId(waybillCarId);
            waybillCarMap.put(waybillCarId, wc);
        }
        return wc;
    }

    /**
     * 取消运单车辆
     *
     * @author JPG
     * @since 2019/10/29 9:57
     */
    @Override
    public void cancelWaybillCar(Waybill waybill, WaybillCar waybillCar) {
        if (waybillCar == null) {
            return;
        }
        if (waybill == null) {
            waybill = waybillDao.selectById(waybillCar.getWaybillId());
            ;
        }
        Integer waybillType = waybill.getType();
        if (WaybillCarrierTypeEnum.SELF.code != waybill.getCarrierType() || WaybillTypeEnum.PICK.code != waybill.getType()) {
            //非提车自送的单子
            if (waybillCar.getState() >= WaybillCarStateEnum.LOADED.code) {
                throw new ParameterException("车辆{0}运输中，不允许取消", waybillCar.getOrderCarNo());
            }
        }
        OrderCar orderCar = orderCarDao.selectById(waybillCar.getOrderCarId());
        if (orderCar == null) {
            throw new ParameterException("车辆{0}, 订单信息错误", waybillCar.getOrderCarNo());
        }
        Order order = orderDao.selectById(orderCar.getOrderId());
        if (order == null) {
            throw new ParameterException("车辆{0}, 车辆信息错误", waybillCar.getOrderCarNo());
        }
        //取消运单车辆
        waybillCarDao.updateStateById(waybillCar.getId(), WaybillCarStateEnum.F_CANCEL.code);

        OrderCar noc = new OrderCar();
        noc.setId(orderCar.getId());
        //修改车辆状态
        if (WaybillTypeEnum.PICK.code == waybillType) {
            noc.setPickType(order.getPickType());
            //订单车辆状态
            if (OrderCarStateEnum.WAIT_PICK.code == orderCar.getState()) {
                noc.setState(OrderCarStateEnum.WAIT_PICK_DISPATCH.code);
            }
            //订单车辆提车状态
            noc.setPickState(OrderCarLocalStateEnum.WAIT_DISPATCH.code);

        } else if (WaybillTypeEnum.BACK.code == waybillType) {
            noc.setBackType(order.getBackType());
            //订单车辆状态
            if (OrderCarStateEnum.WAIT_BACK.code == orderCar.getState()) {
                noc.setState(OrderCarStateEnum.WAIT_BACK_DISPATCH.code);
            }
            //订单车辆配送状态
            noc.setBackState(OrderCarLocalStateEnum.WAIT_DISPATCH.code);
        } else if (WaybillTypeEnum.TRUNK.code == waybillType) {
            //取消该车辆所有后续调度
            cancelAfterDispatch(waybillCar);

            //查询是否还有干线调度
            DispatchNum dsEntity = waybillCarDao.countOrderDispatchState(waybillCar.getOrderCarId());
            if (dsEntity.getTrunkNum() == 0) {
                noc.setPickState(dsEntity.getPickNum() > 0 ? OrderCarLocalStateEnum.DISPATCHED.code : OrderCarLocalStateEnum.WAIT_DISPATCH.code);
            }
            noc.setTrunkState(dsEntity.getTrunkNum() > 0 ? OrderCarTrunkStateEnum.WAIT_DISPATCH.code : OrderCarTrunkStateEnum.WAIT_NEXT_DISPATCH.code);
            noc.setBackState(dsEntity.getBackNum() > 0 ? OrderCarLocalStateEnum.DISPATCHED.code : OrderCarLocalStateEnum.WAIT_DISPATCH.code);

        } else {
            throw new ParameterException("运单类型错误");
        }

        //更新运单车辆数
        waybillDao.updateNumAndFreightFee(waybillCar.getWaybillId());
        orderCarDao.updateById(noc);

        //更新任务车辆数
        Task task = taskDao.findByWaybillCarId(waybillCar.getId());
        if (task != null) {
            taskDao.updateNum(task.getId());
            csTaskService.validateAndFinishTask(task);
        }

        validateAndFinishWaybill(waybillCar.getWaybillId());

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
        Set<String> lockSet = Sets.newHashSet();
        try {
            LogUtil.debug("【卸载车辆】-------------------->{}", JSON.toJSONString(paramsDto));
            List<Long> carIdList = paramsDto.getWaybillCarIdList();

            if (!csStoreService.validateStoreParam(paramsDto.getEndStoreId(), paramsDto.getEndStoreName())) {
                log.error("业务中心参数错误(midwayUnload):" + JSON.toJSONString(paramsDto));
                return BaseResultUtil.fail("业务中心参数错误");
            }
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
                String lockKey = RedisKeys.getUnloadLockKey(waybillCarId);
                if (!redisLock.lock(lockKey, 120000, 1, 150L)) {
                    log.debug("缓存失败：key->{}", lockKey);
                    return BaseResultUtil.fail("运单车辆{0}正在卸车，请5秒后重试", waybillCar.getOrderCarNo());
                }
                lockSet.add(lockKey);
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

                boolean isChangeAddress = false;
                //验证是否变更地址
                if (!(waybillCar.getEndAreaCode() != null && waybillCar.getEndAreaCode().equals(paramsDto.getEndAreaCode())) || !(waybillCar.getEndAddress() != null && waybillCar.getEndAddress().equals(paramsDto.getEndAddress()))) {
                    isChangeAddress = true;
                }
                //已装车
                if (waybillCar.getState() >= WaybillCarStateEnum.LOADED.code) {
                    copyWaybillCarEndCity(fullCity, waybillCar);
                    waybillCar.setEndStoreId(paramsDto.getEndStoreId());
                    waybillCar.setEndStoreName(paramsDto.getEndStoreName());
                    waybillCar.setEndAddress(paramsDto.getEndAddress());
                    Long endStoreId = paramsDto.getEndStoreId();
                    boolean isDriectUnload = endStoreId == null || endStoreId <= 0;
                    if (isDriectUnload) {
                        //计算目的地所属业务中心
                        waybillCar.setEndBelongStoreId(waybillCar.getStartBelongStoreId());
                        waybillCar.setState(WaybillCarStateEnum.UNLOADED.code);
                    } else {
                        waybillCar.setEndBelongStoreId(endStoreId);
                        waybillCar.setState(WaybillCarStateEnum.WAIT_UNLOAD_CONFIRM.code);
                    }

                    //waybillCar.setReceiptFlag(waybillCar.getUnloadLinkPhone().equals(order.getBackContactPhone()));
                    waybillCar.setReceiptFlag(validateIsArriveDest(waybillCar, order));
                    Line line = csLineService.getLineByCity(waybillCar.getStartCityCode(), waybillCar.getEndCityCode(), true);
                    waybillCar.setLineId(line == null ? null : line.getId());
                    waybillCar.setUnloadLinkUserId(paramsDto.getUnloadLinkUserId());
                    waybillCar.setUnloadLinkName(paramsDto.getUnloadLinkName());
                    waybillCar.setUnloadLinkPhone(paramsDto.getUnloadLinkPhone());
                    waybillCar.setUnloadTime(paramsDto.getUnloadTime());
                    waybillCarDao.updateByIdForNull(waybillCar);

                    //变更地址取消后续运单
                    if (isChangeAddress) {
                        //取消该车辆所有后续调度
                        cancelAfterDispatch(waybillCar);
                    }

                    //更新车辆状态和所在位置和状态、调度状态
                    LogUtil.debug("【卸载车辆】获取车辆状态");
                    OrderCar noc = getOrderCarForChangeTrunk(orderCar, waybillCar, order);
                    if (validateIsArriveDest(waybillCar, order)) {
                        noc.setState(OrderCarStateEnum.SIGNED.code);
                        throw new ParameterException("卸载车辆暂时不支持直接交付客户");
                        //开发直接交付客户
                    }
                    if (isDriectUnload) {
                        noc.setNowStoreId(waybillCar.getEndStoreId());
                        noc.setNowAreaCode(waybillCar.getEndAreaCode());
                        noc.setNowUpdateTime(System.currentTimeMillis());
                    }
                    orderCarDao.updateById(noc);

                    //提取属性
                    oldTotalFee = oldTotalFee.add(waybillCar.getFreightFee());
                    waybillCars.add(waybillCar);
                } else {
                    //未装车的取消
                    cancelWaybillCar(waybill, waybillCar);
                }
                //验证并完成任务
                Task task = taskDao.findByWaybillCarId(waybillCarId);
                if (task != null) {
                    csTaskService.validateAndFinishTask(task);
                }
            }

            if (!CollectionUtils.isEmpty(waybillCars)) {
                //按比例均摊运费
                shareWaybillCarFreightFee(waybillCars, oldTotalFee, paramsDto.getFreightFee());

                waybillCars.forEach(wc -> waybillCarDao.updateById(wc));
                //更新运单费用
                waybillDao.updateFreightFee(waybill.getId());
            }
            //验证并完成运单
            validateAndFinishWaybill(waybill.getId());
            return BaseResultUtil.success();
        } finally {
            redisUtils.delayDelete(lockSet);
        }
    }

    @Override
    public void validateAndFinishWaybill(Long waybillId) {
        BillCarNum wcNum = waybillCarDao.countUnFinishForState(waybillId);
        if (wcNum.getUnFinishCarNum() > 0) {
            return;
        }
        int newState = wcNum.getTotalCarNum().equals(wcNum.getCancelCarNum()) ? WaybillStateEnum.F_CANCEL.code : WaybillStateEnum.FINISHED.code;
        waybillDao.updateForOver(waybillId, newState);
        if (newState == WaybillStateEnum.FINISHED.code) {
            try {
                //验证是否有不在业务中心中转的车辆，如果有不支付物流费
                int n = waybillDao.countMultiStepUnpassStore(waybillId);
                if (n <= 0) {
                    LogUtil.debug("【完成运单】准备支付运费");
                    csPingPayService.allinpayToCarrier(waybillId);
                } else {
                    LogUtil.debug("【完成运单】运单{}中包含非业务中心中转车辆，业务员确认后支付运费", waybillId);
                }

            } catch (Exception e) {
                log.error("完成运单（ID：{}）支付司机运费失败！", waybillId);
                log.error(e.getMessage(), e);
            }
        }

    }

    private void shareWaybillCarFreightFee(Set<WaybillCar> waybillCars, BigDecimal oldTotalFee, BigDecimal newTotalFee) {
        newTotalFee = MoneyUtil.yuanToFen(MoneyUtil.nullToZero(newTotalFee));
        oldTotalFee = MoneyUtil.nullToZero(oldTotalFee);
        if (CollectionUtils.isEmpty(waybillCars) || newTotalFee.compareTo(oldTotalFee) == 0) {
            return;
        }
        //如果历史数据为零平均分配
        if(oldTotalFee.compareTo(BigDecimal.ZERO) == 0){
            oldTotalFee = new BigDecimal(waybillCars.size());
            waybillCars.forEach(wc -> wc.setFreightFee(BigDecimal.ONE));
        }

        BigDecimal avgTotalFee = BigDecimal.ZERO;
        for (WaybillCar wc : waybillCars) {
            BigDecimal avgCar = wc.getFreightFee().multiply(newTotalFee).divide(oldTotalFee, 0, BigDecimal.ROUND_DOWN);
            wc.setFreightFee(avgCar);
            avgTotalFee = avgTotalFee.add(avgCar);
        }

        BigDecimal cha = newTotalFee.subtract(avgTotalFee);
        if (cha.compareTo(BigDecimal.ZERO) >0) {
            for (WaybillCar wc : waybillCars) {
                if (cha.compareTo(BigDecimal.ZERO) <= 0) {
                    break;
                }
                wc.setFreightFee(wc.getFreightFee().add(BigDecimal.ONE));
                cha = cha.subtract(BigDecimal.ONE);
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
