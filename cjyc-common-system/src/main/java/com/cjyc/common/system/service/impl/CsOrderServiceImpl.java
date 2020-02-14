package com.cjyc.common.system.service.impl;

import com.cjkj.common.redis.lock.RedisDistributedLock;
import com.cjyc.common.model.constant.TimeConstant;
import com.cjyc.common.model.dao.IOrderCarDao;
import com.cjyc.common.model.dao.IOrderDao;
import com.cjyc.common.model.dao.IWaybillCarDao;
import com.cjyc.common.model.dto.web.order.*;
import com.cjyc.common.model.dto.web.waybill.SaveLocalDto;
import com.cjyc.common.model.dto.web.waybill.SaveLocalWaybillDto;
import com.cjyc.common.model.entity.*;
import com.cjyc.common.model.entity.defined.BizScope;
import com.cjyc.common.model.entity.defined.FullCity;
import com.cjyc.common.model.entity.defined.UserInfo;
import com.cjyc.common.model.enums.*;
import com.cjyc.common.model.enums.city.CityLevelEnum;
import com.cjyc.common.model.enums.customer.CustomerTypeEnum;
import com.cjyc.common.model.enums.log.OrderLogEnum;
import com.cjyc.common.model.enums.order.*;
import com.cjyc.common.model.enums.waybill.WaybillCarrierTypeEnum;
import com.cjyc.common.model.enums.waybill.WaybillTypeEnum;
import com.cjyc.common.model.exception.ParameterException;
import com.cjyc.common.model.exception.ServerException;
import com.cjyc.common.model.keys.RedisKeys;
import com.cjyc.common.model.util.BaseResultUtil;
import com.cjyc.common.model.util.MoneyUtil;
import com.cjyc.common.model.vo.ResultVo;
import com.cjyc.common.model.vo.web.order.DispatchAddCarVo;
import com.cjyc.common.model.vo.web.order.OrderVo;
import com.cjyc.common.model.vo.web.waybill.WaybillCarVo;
import com.cjyc.common.system.service.*;
import com.cjyc.common.system.service.sys.ICsSysService;
import com.cjyc.common.system.util.RedisUtils;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.MessageFormat;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 订单公共业务
 *
 * @author JPG
 */
@Service
@Slf4j
@Transactional(rollbackFor = Exception.class)
public class CsOrderServiceImpl implements ICsOrderService {
    @Resource
    private RedisUtils redisUtils;
    @Resource
    private RedisDistributedLock redisLock;
    @Resource
    private IOrderDao orderDao;
    @Resource
    private IOrderCarDao orderCarDao;
    @Resource
    private IWaybillCarDao waybillCarDao;
    @Resource
    private ICsCityService csCityService;
    @Resource
    private ICsLineService csLineService;
    @Resource
    private ICsSysService csSysService;
    @Resource
    private ICsSendNoService csSendNoService;
    @Resource
    private ICsCustomerService csCustomerService;
    @Resource
    private ICsStoreService csStoreService;
    @Resource
    private ICsOrderLogService csOrderLogService;
    @Resource
    private ICsOrderChangeLogService orderChangeLogService;
    @Resource
    private ICsCustomerContactService csCustomerContactService;
    @Resource
    private ICsCustomerLineService csCustomerLineService;
    @Resource
    private ICsWaybillService csWaybillService;
    @Resource
    private ICsAdminService csAdminService;
    @Resource
    private ICsPingPayService csPingPayService;

    @Override
    public ResultVo save(SaveOrderDto paramsDto) {
        //获取参数
        Long orderId = paramsDto.getOrderId();
        long currentTimeMillis = System.currentTimeMillis();

        Order order = null;
        boolean isNewOrder = false;
        //更新订单
        if (orderId != null) {
            order = orderDao.selectById(orderId);
        }
        //新建订单
        if (order == null) {
            isNewOrder = true;
            order = new Order();
        }
        //copy属性
        BeanUtils.copyProperties(paramsDto, order);
        //查询三级城市
        fillOrderCityInfo(order);
        fillOrderStoreInfoForSave(order);
        fillOrderInputStore(order);
        fillOrderLocalCarryTypeInfo(order);
        /**1、组装订单数据
         */
        if (isNewOrder) {
            order.setNo(csSendNoService.getNo(SendNoTypeEnum.ORDER));
            order.setSource(paramsDto.getClientId());
            order.setCreateTime(currentTimeMillis);
        }
        order.setState(paramsDto.getState());
        order.setTotalFee(MoneyUtil.convertYuanToFen(order.getTotalFee()));

        //更新或插入订单
        int row = isNewOrder ? orderDao.insert(order) : orderDao.updateById(order);

        /**2、更新或保存车辆信息*/
        List<SaveOrderCarDto> carDtoList = paramsDto.getOrderCarList();
        if (CollectionUtils.isEmpty(carDtoList)) {
            //没有车辆，结束
            return BaseResultUtil.success("下单成功，订单编号{0}", order.getNo());
        }

        //删除旧的车辆数据
        if (!isNewOrder) {
            orderCarDao.deleteBatchByOrderId(order.getId());
        }
        int noCount = 1;
        Set<String> vinSet = Sets.newHashSet();
        Set<String> plateNoSet = Sets.newHashSet();
        for (SaveOrderCarDto dto : carDtoList) {
            if (dto == null) {
                continue;
            }
            //验证vin码是否重复
            validateOrderCarVinInfo(vinSet, dto.getVin());
            //验证车牌号是否重复
            validateOrderCarPlateNoInfo(plateNoSet, dto.getPlateNo());

            OrderCar orderCar = new OrderCar();
            //copy数据
            BeanUtils.copyProperties(dto, orderCar);
            //填充数据
            orderCar.setOrderNo(order.getNo());
            orderCar.setOrderId(order.getId());
            orderCar.setNo(csSendNoService.formatNo(order.getNo(), noCount, 3));
            orderCar.setState(OrderCarStateEnum.WAIT_ROUTE.code);
            orderCar.setNowAreaCode(order.getStartAreaCode());
            orderCar.setPickType(order.getPickType());
            orderCar.setBackType(order.getBackType());
            orderCar.setNowAreaCode(order.getStartAreaCode());
            orderCar.setPickFee(MoneyUtil.convertYuanToFen(dto.getPickFee()));
            orderCar.setTrunkFee(MoneyUtil.convertYuanToFen(dto.getTrunkFee()));
            orderCar.setBackFee(MoneyUtil.convertYuanToFen(dto.getBackFee()));
            orderCar.setAddInsuranceFee(MoneyUtil.convertYuanToFen(dto.getAddInsuranceFee()));
            orderCarDao.insert(orderCar);
            //统计数量
            noCount++;
        }

        List<OrderCar> orderCarList = orderCarDao.findListByOrderId(order.getId());
        if(!CollectionUtils.isEmpty(orderCarList)){
            //均摊优惠券费用
            shareCouponOffsetFee(order.getCouponOffsetFee(), orderCarList);
            //均摊总费用
            shareTotalFee(order.getTotalFee(), orderCarList);
            //更新车辆信息
            orderCarList.forEach(orderCar -> orderCarDao.updateById(orderCar));

            //合计费用：提、干、送、保险
            //fillOrderFeeInfo(order, orderCarList);
            order.setCarNum(orderCarList.size());
            orderDao.updateById(order);
        }

        if(OrderStateEnum.SUBMITTED.code == paramsDto.getState() && !CollectionUtils.isEmpty(orderCarList)){
            //记录订单日志
            csOrderLogService.asyncSave(order, OrderLogEnum.SUBMIT,
                    new String[]{OrderLogEnum.SUBMIT.getOutterLog(),
                            MessageFormat.format(OrderLogEnum.SUBMIT.getInnerLog(), paramsDto.getLoginName(), paramsDto.getLoginPhone())},
                    new UserInfo(paramsDto.getLoginId(), paramsDto.getLoginName(), paramsDto.getLoginPhone(), UserTypeEnum.valueOf(paramsDto.getLoginType())));
        }

        return BaseResultUtil.success("下单成功，订单编号{0}", order.getNo());
    }

    @Override
    public ResultVo commit(CommitOrderDto paramsDto) {
        Customer customer = csCustomerService.getByPhone(paramsDto.getCustomerPhone(), true);
        if (customer != null && !customer.getName().equals(paramsDto.getCustomerName())) {
            return BaseResultUtil.fail(ResultEnum.CREATE_NEW_CUSTOMER.getCode(),
                    "客户手机号存在，名称不一致：新名称（{0}）旧名称（{1}），请返回订单重新选择客户",
                    paramsDto.getCustomerName(), customer.getName());
        }
        if (customer == null) {
            if (paramsDto.getCustomerType() == CustomerTypeEnum.INDIVIDUAL.code) {
                if (paramsDto.getCreateCustomerFlag()) {
                    ResultVo<Customer> res = csCustomerService.saveCustomer(paramsDto.getCustomerPhone(), paramsDto.getCustomerName(), paramsDto.getLoginId());
                    if(res.getCode() == ResultEnum.SUCCESS.getCode()){
                        customer = res.getData();
                    }else{
                        return BaseResultUtil.fail(res.getMsg());
                    }
                } else {
                    return BaseResultUtil.getVo(ResultEnum.CREATE_NEW_CUSTOMER.getCode(), ResultEnum.CREATE_NEW_CUSTOMER.getMsg());
                }
            } else {
                return BaseResultUtil.fail("企业客户/合伙人不存在");
            }
        }
        paramsDto.setCustomerId(customer.getId());
        paramsDto.setCustomerType(customer.getType());

        //提交订单
        Order order = commitOrder(paramsDto);
        return BaseResultUtil.success("下单{0}成功", order.getNo());
    }

    private Order commitOrder(CommitOrderDto paramsDto) {
        String lockKey = null;
        boolean isNewOrder = false;
        try {
            //获取参数
            Long orderId = paramsDto.getOrderId();
            long currentTimeMillis = System.currentTimeMillis();

            Order order = null;
            //更新订单
            if (orderId != null) {
                order = orderDao.selectById(orderId);
                if (order != null) {
                    lockKey = RedisKeys.getDispatchLockForOrderUpdate(order.getNo());
                    redisLock.lock(lockKey, 30000, 100, 300);
                }
            }
            //新建订单
            if (order == null) {
                isNewOrder = true;
                order = new Order();
            }
            //copy属性
            BeanUtils.copyProperties(paramsDto, order);
            //城市信息
            fillOrderCityInfo(order);
            //所属业务中心
            fillOrderInputStore(order);
            //业务中心
            fillOrderStoreInfo(order, false);
            //提送车类型
            fillOrderLocalCarryTypeInfo(order);
            //计算预计到达时间
            fillOrderExpectEndTime(order);
            //订单编号
            if (isNewOrder) {
                order.setNo(csSendNoService.getNo(SendNoTypeEnum.ORDER));
                order.setSource(paramsDto.getClientId());
                order.setCreateTime(currentTimeMillis);
            }
            order.setState(OrderStateEnum.SUBMITTED.code);
            order.setTotalFee(MoneyUtil.convertYuanToFen(paramsDto.getTotalFee()));

            //更新或插入订单
            int row = isNewOrder ? orderDao.insert(order) : orderDao.updateById(order);

            /**2、更新或保存车辆信息*/
            List<CommitOrderCarDto> carDtoList = paramsDto.getOrderCarList();
            //删除旧的车辆数据
            if (!isNewOrder) {
                orderCarDao.deleteBatchByOrderId(order.getId());
            }
            int count = 1;//车辆序号
            List<OrderCar> orderCarList = Lists.newArrayList();
            Set<String> vinSet = Sets.newHashSet();
            Set<String> plateNoSet = Sets.newHashSet();
            for (CommitOrderCarDto dto : carDtoList) {
                if (dto == null) {
                    continue;
                }
                //验证vin码是否重复
                validateOrderCarVinInfo(vinSet, dto.getVin());
                //验证车牌号是否重复
                validateOrderCarPlateNoInfo(plateNoSet, dto.getPlateNo());

                OrderCar orderCar = new OrderCar();
                //copy数据
                BeanUtils.copyProperties(dto, orderCar);
                //填充数据
                orderCar.setOrderNo(order.getNo());
                orderCar.setOrderId(order.getId());
                orderCar.setNo(csSendNoService.formatNo(order.getNo(), count, 3));
                orderCar.setPickType(order.getPickType());
                orderCar.setBackType(order.getBackType());
                orderCar.setNowAreaCode(order.getStartAreaCode());
                orderCar.setState(OrderCarStateEnum.WAIT_ROUTE.code);
                orderCar.setPickFee(MoneyUtil.convertYuanToFen(dto.getPickFee()));
                orderCar.setTrunkFee(MoneyUtil.convertYuanToFen(dto.getTrunkFee()));
                orderCar.setBackFee(MoneyUtil.convertYuanToFen(dto.getBackFee()));
                orderCar.setAddInsuranceFee(MoneyUtil.convertYuanToFen(dto.getAddInsuranceFee()));
                orderCarDao.insert(orderCar);

                //提取数据
                orderCarList.add(orderCar);
                count++;
            }
            if (CollectionUtils.isEmpty(orderCarList)) {
                throw new ParameterException("订单至少包含一辆车");
            }

            List<OrderCar> ocList = orderCarDao.findListByOrderId(order.getId());
            //均摊优惠券费用
            shareCouponOffsetFee(order.getCouponOffsetFee(), ocList);
            //均摊总费用
            shareTotalFee(order.getTotalFee(), ocList);
            //更新车辆信息
            ocList.forEach(orderCar -> orderCarDao.updateById(orderCar));

            //合计费用：提、干、送、保险
            //fillOrderFeeInfo(order, ocList);
            order.setCarNum(ocList.size());
            orderDao.updateById(order);

            //记录发车人和收车人
            csCustomerContactService.asyncSave(order);
            //记录订单日志
            csOrderLogService.asyncSave(order, OrderLogEnum.COMMIT,
                    new String[]{OrderLogEnum.COMMIT.getOutterLog(), MessageFormat.format(OrderLogEnum.COMMIT.getInnerLog(), paramsDto.getLoginName(), paramsDto.getLoginPhone())},
                    new UserInfo(paramsDto.getLoginId(), paramsDto.getLoginName(), paramsDto.getLoginPhone(), UserTypeEnum.ADMIN));
            return order;
        } finally {
            if (!isNewOrder) {
                redisLock.releaseLock(lockKey);
            }
        }

    }

    private Order fillOrderExpectEndTime(Order order) {
        if(order.getExpectStartDate() != null && order.getExpectEndDate() == null){
            Line line = csLineService.getLineByCity(order.getStartCityCode(), order.getEndCityCode(), true);
            if(line == null){
                throw new ParameterException("线路不存在");
            }
            if(line.getDays() != null){
                order.setExpectEndDate(order.getExpectStartDate() + (86400000 * line.getDays().intValue()));
            }
        }
        return order;
    }

    @Override
    public ResultVo simpleCommitAndCheck(CheckOrderDto paramsDto) {
        Order order = orderDao.selectById(paramsDto.getOrderId());
 /*     if(order == null || order.getStartStoreId() == null || order.getStartStoreId() < 0 ){

        }
        if(order.getEndStoreId() == null || order.getEndStoreId() < 0 ){
            return BaseResultUtil.fail("目的地业务中心未处理，请点击订单进入[下单详情]中修改并确认下单");
        }*/

        Customer customer = csCustomerService.getByPhone(order.getCustomerPhone(), true);
        if (customer != null && !customer.getName().equals(order.getCustomerName())) {
            return BaseResultUtil.fail(ResultEnum.CREATE_NEW_CUSTOMER.getCode(),
                    "客户手机号存在，名称不一致：新名称（{0}）旧名称（{1}），请返回订单重新选择客户",
                    order.getCustomerName(), customer.getName());
        }
        if (customer == null) {
            if (order.getCustomerType() == CustomerTypeEnum.INDIVIDUAL.code) {
                ResultVo<Customer> res = csCustomerService.saveCustomer(order.getCustomerPhone(), order.getCustomerName(), paramsDto.getLoginId());
                if(res.getCode() == ResultEnum.SUCCESS.getCode()){
                    customer = res.getData();
                }else{
                    return BaseResultUtil.fail(res.getMsg());
                }
            } else {
                return BaseResultUtil.fail("企业客户/合伙人不存在");
            }
        }
        order.setCustomerId(customer.getId());
        order.setCustomerType(customer.getType());

        List<OrderCar> orderCarList = orderCarDao.findListByOrderId(order.getId());
        if(orderCarList == null || orderCarList.size() <= 0){
            return BaseResultUtil.fail("实际车辆数不能小于1, 请点击订单进入[下单详情]中修改");
        }
        //均摊优惠券费用
        shareCouponOffsetFee(order.getCouponOffsetFee(), orderCarList);
        //均摊总费用
        shareTotalFee(order.getTotalFee(), orderCarList);
        //更新车辆信息
        orderCarList.forEach(orderCar -> orderCarDao.updateById(orderCar));

        //合计费用：提、干、送、保险
        //fillOrderFeeInfo(order, orderCarList);
        order.setCarNum(orderCarList.size());
        orderDao.updateById(order);
        //记录发车人和收车人
        csCustomerContactService.asyncSave(order);
        //记录订单日志
        csOrderLogService.asyncSave(order, OrderLogEnum.COMMIT,
                new String[]{OrderLogEnum.COMMIT.getOutterLog(),
                        MessageFormat.format(OrderLogEnum.COMMIT.getInnerLog(), paramsDto.getLoginName(), paramsDto.getLoginPhone())},
                new UserInfo(paramsDto.getLoginId(), paramsDto.getLoginName(), paramsDto.getLoginPhone(), UserTypeEnum.ADMIN));

        check(paramsDto);

        return BaseResultUtil.success();
    }


    @Override
    public ResultVo changeOrderCarCarryType(ChangeCarryTypeDto  paramsDto) {
        List<Long> orderCarIdList = paramsDto.getOrderCarIdList();
        if(paramsDto.getPickType() == null && paramsDto.getBackType() == null){
            return BaseResultUtil.fail("至少更改一种承运方式");
        }
        //验证
        for (Long orderCarId : orderCarIdList) {
            OrderCar orderCar = orderCarDao.selectById(orderCarId);
            if(orderCar.getState() >= OrderCarStateEnum.SIGNED.code){
                return BaseResultUtil.fail("车辆{0}，已经完结无法修改", orderCar.getNo());
            }
            //验证车辆提车是否结束
            if(paramsDto.getPickType() != null){
                if(orderCar.getPickState() >= OrderCarLocalStateEnum.DISPATCHED.code){
                    return BaseResultUtil.fail("车辆{0}，提车已经调度完成，无法变更承运方式", orderCar.getNo());
                }
                if(orderCar.getState() >= OrderCarStateEnum.WAIT_PICK.code){
                    return BaseResultUtil.fail("车辆{0}，提车调度已经结束，无法变更承运方式", orderCar.getNo());
                }
            }
            if(paramsDto.getBackType() != null){
                if(orderCar.getBackState() >= OrderCarLocalStateEnum.DISPATCHED.code){
                    return BaseResultUtil.fail("车辆{0}，配送已经调度完成，无法变更承运方式", orderCar.getNo());
                }
                if(orderCar.getState() >= OrderCarStateEnum.WAIT_BACK.code){
                    return BaseResultUtil.fail("车辆{0}，配送调度已经结束，无法变更承运方式", orderCar.getNo());
                }
            }
        }
        //更新
        orderCarIdList.forEach(orderCarId ->{
            OrderCar noc = new OrderCar();
            noc.setId(orderCarId);
            noc.setPickType(paramsDto.getPickType());
            noc.setBackType(paramsDto.getBackType());
            orderCarDao.updateById(noc);
        });

        return BaseResultUtil.success();
    }

    /**
     * 审核订单
     *
     * @param paramsDto
     * @author JPG
     * @since 2019/11/5 15:03
     */
    @Override
    public ResultVo check(CheckOrderDto paramsDto) {
        @NotNull Long orderId = paramsDto.getOrderId();
        long currentTimeMillis = System.currentTimeMillis();

        Order order = orderDao.selectById(orderId);
        if(order.getSource() != null && ClientEnum.APP_CUSTOMER.code == order.getSource() || ClientEnum.APPLET_CUSTOMER.code == order.getSource()){
            fillOrderStoreInfo(order, true);
        }
        if(order.getStartStoreId() == null && order.getEndStoreId() == null){
            fillOrderStoreInfo(order, true);
        }
        //验证必要信息是否完全
        validateOrderFeild(order);

        List<OrderCar> orderCarList = orderCarDao.findListByOrderId(order.getId());
        if (CollectionUtils.isEmpty(orderCarList)) {
            throw new ParameterException("订单车辆列表不能为空，请先通过[修改订单]完善信息");
        }

        //根据到付和预付置不同状态
        if (PayModeEnum.PREPAY.code == order.getPayType()) {
            order.setState(OrderStateEnum.WAIT_PREPAY.code);
            //TODO 支付通知
        } else {
            order.setState(OrderStateEnum.CHECKED.code);
        }
        order.setCheckTime(currentTimeMillis);
        order.setCheckUserName(paramsDto.getLoginName());
        order.setCheckUserId(paramsDto.getLoginId());
        orderDao.updateById(order);

        //记录订单日志
        csOrderLogService.asyncSave(order, OrderLogEnum.CHECK,
                new String[]{OrderLogEnum.CHECK.getOutterLog(),
                        MessageFormat.format(OrderLogEnum.CHECK.getInnerLog(), paramsDto.getLoginName(), paramsDto.getLoginPhone())},
                new UserInfo(paramsDto.getLoginId(), paramsDto.getLoginName(), paramsDto.getLoginPhone(), UserTypeEnum.ADMIN));
        //TODO 处理优惠券为使用状态，优惠券有且仅能验证一次

        return BaseResultUtil.success();
    }

    private Order fillOrderStoreInfoForSave(Order order) {
        Long inputStoreId = order.getInputStoreId();
        order.setInputStoreId(inputStoreId == null || inputStoreId == -5 ? null : inputStoreId);
        Long startStoreId = order.getStartStoreId();
        order.setStartStoreId(startStoreId == null || startStoreId == -5 ? null : startStoreId);
        Long endStoreId = order.getEndStoreId();
        order.setEndStoreId(endStoreId == null || endStoreId == -5 ? null : endStoreId);
        return order;
    }

    private Order fillOrderInputStore(Order order) {
        if (order.getInputStoreId() != null && order.getInputStoreId() != -5) {
            if(StringUtils.isBlank(order.getInputStoreName())){
                Store store = csStoreService.getById(order.getInputStoreId(), true);
                if(store != null){
                    order.setInputStoreName(store.getName());
                }
            }
            return order;
        }
        //根据业务范围查询业务中心
        Store store = null;
        if (StringUtils.isNotBlank(order.getStartAreaCode())) {
            store = csStoreService.getOneBelongByAreaCode(order.getStartAreaCode());
        }
        //根据城市查询业务中心
        if (store == null && StringUtils.isNotBlank(order.getStartCityCode())) {
            store = csStoreService.getOneBelongByCityCode(order.getStartCityCode());
        }
        if (store != null) {
            order.setInputStoreId(store.getId());
            order.setInputStoreName(store.getName());
        } else {
            order.setInputStoreId(0L);
        }
        return order;
    }

    private Order fillOrderLocalCarryTypeInfo(Order order) {
        if (order.getPickType() == null || order.getPickType() <= 0) {
            order.setPickType(null);
        }
        if (order.getBackType() == null || order.getBackType() <= 0) {
            order.setBackType(null);
        }
        return order;
    }

    private void validateOrderCarPlateNoInfo(Set<String> plateNoSet, String plateNo) {
        //验证车牌号是否重复
        if (StringUtils.isNotBlank(plateNo)) {
            if (plateNoSet.contains(plateNo)) {
                throw new ParameterException("车牌号码：{0}重复", plateNo);
            }
            plateNoSet.add(plateNo);
        }
    }

    private void validateOrderCarVinInfo(Set<String> vinSet, String vin) {
        //验证vin码是否重复
        if (StringUtils.isNotBlank(vin)) {
            if (vinSet.contains(vin)) {
                throw new ParameterException("vin码：{0}重复", vin);
            }
            vinSet.add(vin);
        }
    }

    /**
     * 城市信息赋值
     *
     * @param order
     * @author JPG
     * @since 2019/12/12 13:11
     */
    private Order fillOrderCityInfo(Order order) {
        if (StringUtils.isNotBlank(order.getStartAreaCode())) {
            FullCity startFullCity = csCityService.findFullCity(order.getStartAreaCode(), CityLevelEnum.PROVINCE);
            copyOrderStartCity(startFullCity, order);
        }
        if (StringUtils.isNotBlank(order.getEndAreaCode())) {
            FullCity endFullCity = csCityService.findFullCity(order.getEndAreaCode(), CityLevelEnum.PROVINCE);
            copyOrderEndCity(endFullCity, order);
        }
        return order;
    }

    /**
     * 业务中心信息赋值
     *
     * @param order
     * @author JPG
     * @since 2019/12/12 11:55
     */
    private Order fillOrderStoreInfo(Order order, boolean isForceUpdate) {
        if (order.getStartStoreId() != null && order.getStartStoreId() > 0) {
            if(StringUtils.isBlank(order.getStartStoreName())){
                Store store = csStoreService.getById(order.getStartStoreId(), true);
                if(store != null){
                    order.setStartStoreName(store.getName());
                }
            }
            order.setStartBelongStoreId(order.getStartStoreId());
        } else {
            //查询地址所属业务中心
            Store store = csStoreService.getOneBelongByCityCode(order.getStartCityCode());
            if (store != null) {
                if (order.getStartStoreId() == null || order.getStartStoreId() == -5 || isForceUpdate) {
                    //无主观操作
                    order.setStartStoreId(store.getId());
                    order.setStartStoreName(store.getName());
                }
                order.setStartBelongStoreId(store.getId());
            } else {
                order.setStartStoreId(0L);
                order.setStartBelongStoreId(0L);
            }
        }
        if (order.getEndStoreId() != null && order.getEndStoreId() > 0) {
            order.setEndBelongStoreId(order.getEndStoreId());
            if(StringUtils.isBlank(order.getEndStoreName())){
                Store store = csStoreService.getById(order.getEndStoreId(), true);
                if(store != null){
                    order.setStartStoreName(store.getName());
                }
            }
            order.setStartBelongStoreId(order.getStartStoreId());
        } else {
            //查询地址所属业务中心
            Store store = csStoreService.getOneBelongByCityCode(order.getEndCityCode());
            if (store != null) {
                if (order.getEndStoreId() == null || order.getEndStoreId() == -5 || isForceUpdate) {
                    //无主观操作
                    order.setEndStoreId(store.getId());
                    order.setEndStoreName(store.getName());
                }
                order.setEndBelongStoreId(store.getId());
            } else {
                order.setEndStoreId(0L);
                order.setEndBelongStoreId(0L);
            }
        }
        return order;
    }

    /**
     * 费用信息赋值
     *
     * @param order
     * @param orderCarList
     * @author JPG
     * @since 2019/12/12 14:41
     */
    /*private Order fillOrderFeeInfo(Order order, List<OrderCar> orderCarList) {
        BigDecimal pickFee = BigDecimal.ZERO;
        BigDecimal trunkFee = BigDecimal.ZERO;
        BigDecimal backFee = BigDecimal.ZERO;
        BigDecimal addInsuranceFee = BigDecimal.ZERO;
        for (OrderCar orderCar : orderCarList) {
            pickFee = pickFee.add(orderCar.getPickFee() == null ? BigDecimal.ZERO : orderCar.getPickFee());
            trunkFee = trunkFee.add(orderCar.getTrunkFee() == null ? BigDecimal.ZERO : orderCar.getTrunkFee());
            backFee = backFee.add(orderCar.getBackFee() == null ? BigDecimal.ZERO : orderCar.getBackFee());
            addInsuranceFee = addInsuranceFee.add(orderCar.getAddInsuranceFee() == null ? BigDecimal.ZERO : orderCar.getAddInsuranceFee());
        }
        order.setPickFee(pickFee);
        order.setTrunkFee(trunkFee);
        order.setBackFee(backFee);
        order.setAddInsuranceFee(addInsuranceFee);
        return order;
    }*/

    /**
     * 提交并审核
     *
     * @param paramsDto
     * @return
     */
    @Override
    public ResultVo commitAndCheck(CommitOrderDto paramsDto) {
        //验证客户
        Customer customer = csCustomerService.getByPhone(paramsDto.getCustomerPhone(), true);
        if (customer != null && !customer.getName().equals(paramsDto.getCustomerName())) {
            return BaseResultUtil.fail(ResultEnum.CREATE_NEW_CUSTOMER.getCode(),
                    "客户手机号存在，名称不一致：新名称（{0}）旧名称（{1}），请返回订单重新选择客户",
                    paramsDto.getCustomerName(), customer.getName());
        }
        if (customer == null) {
            if (paramsDto.getCustomerType() == CustomerTypeEnum.INDIVIDUAL.code) {

                if (paramsDto.getCreateCustomerFlag()) {
                    ResultVo<Customer> res = csCustomerService.saveCustomer(paramsDto.getCustomerPhone(), paramsDto.getCustomerName(), paramsDto.getLoginId());
                    if(res.getCode() == ResultEnum.SUCCESS.getCode()){
                        customer = res.getData();
                    }else{
                        return BaseResultUtil.fail(res.getMsg());
                    }
                } else {
                    return BaseResultUtil.getVo(ResultEnum.CREATE_NEW_CUSTOMER.getCode(), ResultEnum.CREATE_NEW_CUSTOMER.getMsg());
                }
            } else {
                return BaseResultUtil.fail("企业客户/合伙人不存在");
            }
        }
        paramsDto.setCustomerId(customer.getId());
        paramsDto.setCustomerType(customer.getType());
        //提交订单
        Order order = commitOrder(paramsDto);
        //审核订单
        check(new CheckOrderDto(paramsDto.getLoginId(), paramsDto.getLoginName(), paramsDto.getLoginPhone(), order.getId()));
        //调度
        if(paramsDto.isDispatch()){
            SaveLocalDto slDto = getPickSaveLocalDto(order, new UserInfo(paramsDto.getLoginId(), paramsDto.getLoginName(), paramsDto.getLoginPhone()));
            csWaybillService.saveLocal(slDto);
        }
        return BaseResultUtil.success("下单{0}成功", order.getNo());
    }

    private SaveLocalDto getPickSaveLocalDto(Order order, UserInfo userInfo) {
        SaveLocalDto slDto = new SaveLocalDto();
        slDto.setLoginId(userInfo.getId());
        slDto.setLoginName(userInfo.getName());
        slDto.setType(WaybillTypeEnum.PICK.code);


        if(order.getStartStoreId() == null || order.getStartStoreId() <= 0){
            throw new ParameterException("提车业务中心为空，不能设置业务员提车");
        }
        Store startStore = csStoreService.getById(order.getStartStoreId(), true);
        if(startStore == null){
            throw new ParameterException("提车业务中心不存在或者已停用，不能设置业务员提车");
        }
        List<OrderCar> orderCars = orderCarDao.findListByOrderId(order.getId());
        if(CollectionUtils.isEmpty(orderCars)){
            throw new ParameterException("车辆列表为空，不能设置业务员提车");
        }
        List<SaveLocalWaybillDto> list = Lists.newArrayList();
        for (OrderCar orderCar : orderCars) {
            if(orderCar == null){
                continue;
            }
            SaveLocalWaybillDto wDto = new SaveLocalWaybillDto();
            wDto.setCarrierId(userInfo.getId());
            wDto.setCarrierType(WaybillCarrierTypeEnum.LOCAL_ADMIN.code);
            wDto.setCarrierName(userInfo.getName());
            wDto.setRemark("确认订单的业务员，自己提车");
            wDto.setOrderCarId(orderCar.getId());
            wDto.setOrderCarNo(orderCar.getNo());
            wDto.setStartAreaCode(order.getStartAreaCode());
            wDto.setStartAddress(order.getStartAddress());
            wDto.setStartStoreId(0L);
            wDto.setEndAreaCode(startStore.getAreaCode());
            wDto.setEndAddress(startStore.getDetailAddr());
            wDto.setEndStoreName(startStore.getName());
            wDto.setEndStoreId(startStore.getId());
            wDto.setExpectStartTime(order.getExpectStartDate() == null || order.getExpectStartDate() < System.currentTimeMillis() ? System.currentTimeMillis() : order.getExpectStartDate());
            wDto.setExpectEndTime(wDto.getExpectStartTime() + TimeConstant.MILLS_OF_ONE_DAY);
            wDto.setLoadLinkName(order.getPickContactName());
            wDto.setLoadLinkPhone(order.getPickContactPhone());
            wDto.setUnloadLinkName(userInfo.getName());
            wDto.setUnloadLinkUserId(userInfo.getId());
            wDto.setUnloadLinkPhone(userInfo.getPhone());

            list.add(wDto);
        }
        if(CollectionUtils.isEmpty(list)){
            throw new ParameterException("车辆列表为空，不能设置业务员提车");
        }
        slDto.setList(list);
        return slDto;
    }

    /**
     * 验证订单必要信息
     *
     * @param order
     * @author JPG
     * @since 2019/11/6 19:45
     */
    private void validateOrderFeild(Order order) {
        if (order == null) {
            throw new ParameterException("订单不存在");
        }

        if (order.getState() >= OrderStateEnum.CHECKED.code) {
            throw new ParameterException("订单已经审核过，无法审核");
        }
        if (order.getId() == null || order.getNo() == null) {
            throw new ParameterException("订单编号不能为空");
        }
        if (order.getCustomerId() == null) {
            throw new ParameterException("订单客户不存在");
        }
        if (order.getStartProvinceCode() == null
                || order.getStartCityCode() == null
                || order.getStartAreaCode() == null
                || order.getStartAddress() == null
                || order.getEndProvinceCode() == null
                || order.getEndCityCode() == null
                || order.getEndAreaCode() == null
                || order.getEndAddress() == null) {
            throw new ParameterException("地址信息不完整，请先通过[修改订单]完善信息");
        }
        if (order.getCarNum() == null || order.getCarNum() <= 0) {
            throw new ParameterException("车辆数不能小于1，请先通过[修改订单]完善信息");
        }
        if (order.getPickType() == null
                || order.getPickContactPhone() == null) {
            throw new ParameterException("提车联系人/提车方式不能为空，请先通过[修改订单]完善信息");
        }
        if (order.getBackType() == null
                || order.getBackContactPhone() == null) {
            throw new ParameterException("收车联系人/收车方式不能为空，请先通过[修改订单]完善信息");
        }
        if (order.getStartStoreId() == null || order.getStartStoreId() == -5
                || order.getEndStoreId() == null || order.getEndStoreId() == -5
                || order.getInputStoreId() == null || order.getInputStoreId() == -5) {
            throw new ParameterException("业务中心信息不能为空，请先通过[修改订单]完善信息");
        }
    }

    /**
     * 分配订单
     *
     * @param paramsDto
     * @author JPG
     * @since 2019/11/5 16:05
     */
    @Override
    public ResultVo allot(AllotOrderDto paramsDto) {
        Order o = new Order();
        o.setId(paramsDto.getOrderId());
        o.setAllotToUserId(paramsDto.getToAdminId());
        o.setAllotToUserName(paramsDto.getToAdminName());
        orderDao.updateById(o);
        return BaseResultUtil.success();
    }

    /**
     * 驳回订单
     *
     * @param paramsDto
     * @author JPG
     * @since 2019/11/5 16:07
     */
    @Override
    public ResultVo reject(RejectOrderDto paramsDto) {
        Order order = orderDao.selectById(paramsDto.getOrderId());
        if (order == null) {
            return BaseResultUtil.fail("订单不存在");
        }
        Integer oldState = order.getState();
        if (oldState <= OrderStateEnum.WAIT_SUBMIT.code) {
            return BaseResultUtil.fail("订单未提交，无法驳回");
        }
        if (oldState > OrderStateEnum.CHECKED.code) {
            return BaseResultUtil.fail("订单运输中，无法驳回");
        }
        orderDao.updateStateById(OrderStateEnum.WAIT_SUBMIT.code, order.getId());
        //添加操作日志
        orderChangeLogService.asyncSave(order, OrderChangeTypeEnum.REJECT,
                new Object[]{oldState, order.getState(), paramsDto.getReason()},
                new Object[]{paramsDto.getLoginId(), paramsDto.getLoginName()});

        //TODO 发送消息给创建人
        return BaseResultUtil.success();
    }

    /**
     * 取消订单
     *
     * @param paramsDto
     * @author JPG
     * @since 2019/11/5 16:32
     */
    @Override
    public ResultVo cancel(CancelOrderDto paramsDto) {
        //取消订单
        Order order = orderDao.selectById(paramsDto.getOrderId());
        if (order == null) {
            return BaseResultUtil.fail("订单不存在");
        }
        if (order.getState() >= OrderStateEnum.TRANSPORTING.code) {
            return BaseResultUtil.fail("订单运输中，不允许取消");
        }
        String oldStateName = OrderStateEnum.valueOf(order.getState()).name;

        order.setState(OrderStateEnum.F_CANCEL.code);
        orderDao.updateById(order);

        //取消所有调度
        List<OrderCar> orderCars = orderCarDao.findListByOrderId(order.getId());
        if(!CollectionUtils.isEmpty(orderCars)){
            List<Long> collect = orderCars.stream().map(OrderCar::getId).collect(Collectors.toList());
            List<WaybillCar> waybillCars = waybillCarDao.findListByOrderCarIds(collect);
            waybillCars.forEach(wc -> csWaybillService.cancelWaybillCar(wc));
        }
        //退款
        csPingPayService.cancelOrderRefund(order.getId());

        //添加操作日志
        orderChangeLogService.asyncSave(order, OrderChangeTypeEnum.CANCEL,
                new Object[]{oldStateName, OrderStateEnum.F_CANCEL.name, paramsDto.getReason()},
                new Object[]{paramsDto.getLoginId(), paramsDto.getLoginName()});

        //记录订单日志
        csOrderLogService.asyncSave(order, OrderLogEnum.CANCEL,
                new String[]{OrderLogEnum.CANCEL.getOutterLog(),
                        MessageFormat.format(OrderLogEnum.CANCEL.getInnerLog(), paramsDto.getLoginName(), paramsDto.getLoginPhone())},
                new UserInfo(paramsDto.getLoginId(), paramsDto.getLoginName(), paramsDto.getLoginPhone(), paramsDto.getLoginType()));
        //TODO 发送消息
        return BaseResultUtil.success();
    }

    @Override
    public ResultVo obsolete(CancelOrderDto paramsDto) {
        //作废订单
        Order order = orderDao.selectById(paramsDto.getOrderId());
        if (order == null) {
            return BaseResultUtil.fail("订单不存在");
        }
        if (order.getState() > OrderStateEnum.CHECKED.code) {
            return BaseResultUtil.fail("订单运输中，不允许作废");
        }
        String oldStateName = OrderStateEnum.valueOf(order.getState()).name;

        order.setState(OrderStateEnum.F_OBSOLETE.code);
        orderDao.updateById(order);

        //添加操作日志
        orderChangeLogService.asyncSave(order, OrderChangeTypeEnum.OBSOLETE,
                new Object[]{oldStateName, OrderStateEnum.F_OBSOLETE.name, paramsDto.getReason()},
                new Object[]{paramsDto.getLoginId(), paramsDto.getLoginName()});

        return BaseResultUtil.success();
    }

    @Override
    public ResultVo changePrice(ChangePriceOrderDto paramsDto) {
        //参数
        Long orderId = paramsDto.getOrderId();
        Order order = orderDao.selectById(orderId);
        //记录历史数据
        OrderVo oldOrderVo = getFullOrderVo(order, new OrderVo());

        /**2、更新或保存车辆信息*/
        List<ChangePriceOrderCarDto> carDtoList = paramsDto.getOrderCarList();
        //费用统计变量
        for (ChangePriceOrderCarDto dto : carDtoList) {
            if (dto == null) {
                continue;
            }
            OrderCar orderCar = orderCarDao.selectById(dto.getId());
            if (orderCar == null) {
                throw new ServerException("ID为{}的车辆，不存在", dto.getId());
            }
            OrderCar noc = new OrderCar();
            noc.setId(orderCar.getId());
            noc.setPickFee(MoneyUtil.convertYuanToFen(dto.getPickFee()));
            noc.setTrunkFee(MoneyUtil.convertYuanToFen(dto.getTrunkFee()));
            noc.setBackFee(MoneyUtil.convertYuanToFen(dto.getBackFee()));
            noc.setAddInsuranceFee(MoneyUtil.convertYuanToFen(dto.getAddInsuranceFee()));
            noc.setAddInsuranceAmount(dto.getAddInsuranceAmount());
            orderCarDao.updateById(noc);
        }
        //新数据
        List<OrderCar> orderCarList = orderCarDao.findListByOrderId(orderId);

        //均摊优惠券费用
        shareCouponOffsetFee(order.getCouponOffsetFee(), orderCarList);
        //均摊总费用
        shareTotalFee(order.getTotalFee(), orderCarList);
        //更新车辆信息
        orderCarList.forEach(orderCar -> orderCarDao.updateById(orderCar));

        //合计费用：提、干、送、保险
        //fillOrderFeeInfo(order, orderCarList);
        order.setTotalFee(MoneyUtil.convertYuanToFen(paramsDto.getTotalFee()));
        orderDao.updateById(order);

        //变更记录
        OrderVo newOrderVo = new OrderVo();
        BeanUtils.copyProperties(order, newOrderVo);
        newOrderVo.setOrderCarList(orderCarList);
        orderChangeLogService.asyncSave(order, OrderChangeTypeEnum.CHANGE_FEE,
                new Object[]{oldOrderVo, newOrderVo, ""},
                new Object[]{paramsDto.getLoginId(), paramsDto.getLoginName()});
        return BaseResultUtil.success();
    }

    /**
     * 完善订单信息
     *
     * @param paramsDto
     * @author JPG
     * @since 2019/11/5 16:51
     */
    @Override
    public ResultVo replenishInfo(ReplenishOrderDto paramsDto) {
        Long orderId = paramsDto.getOrderId();
        Order order = orderDao.selectById(orderId);
        //记录历史数据
        OrderVo oldOrderVo = getFullOrderVo(order, new OrderVo());

        List<ReplenishOrderCarDto> list = paramsDto.getOrderCarList();
        for (ReplenishOrderCarDto dto : list) {
            if (dto == null) {
                continue;
            }
            OrderCar noc = new OrderCar();
            noc.setId(dto.getId());
            noc.setBrand(dto.getBrand());
            noc.setModel(dto.getModel());
            noc.setPlateNo(dto.getPlateNo());
            noc.setVin(dto.getVin());
            orderCarDao.updateById(noc);
        }

        OrderVo newOrderVo = getFullOrderVo(order, new OrderVo());

        orderChangeLogService.asyncSave(order, OrderChangeTypeEnum.CHANGE_ORDER,
                new Object[]{oldOrderVo, newOrderVo, ""},
                new Object[]{paramsDto.getLoginId(), paramsDto.getLoginName()});
        return BaseResultUtil.success();
    }

    private OrderVo getFullOrderVo(Order order, OrderVo orderVo) {
        BeanUtils.copyProperties(order, orderVo);
        orderVo.setOrderCarList(orderCarDao.findListByOrderId(order.getId()));
        return orderVo;
    }

    /**
     * 计算车辆起始目的地
     *
     * @param paramsDto
     * @author JPG
     * @since 2019/12/11 13:43
     */
    @Override
    public ResultVo<DispatchAddCarVo> computerCarEndpoint(ComputeCarEndpointDto paramsDto) {
         DispatchAddCarVo dispatchAddCarVo = new DispatchAddCarVo();
        //查询角色业务中心范围
        BizScope bizScope = null;
        if(paramsDto.getRoleId() == null){
            bizScope = csSysService.getBizScopeByLoginIdNew(paramsDto.getLoginId(), true);
            if (bizScope == null || bizScope.getCode() == BizScopeEnum.NONE.code) {
                return BaseResultUtil.fail("没有数据权限");
            }
        }else{
            bizScope = csSysService.getBizScopeBySysRoleIdNew(paramsDto.getLoginId(), paramsDto.getRoleId(), true);
            if (bizScope == null || bizScope.getCode() == BizScopeEnum.NONE.code) {
                return BaseResultUtil.fail("没有数据权限");
            }
        }

        Set<Long> storeIds = bizScope.getStoreIds();
        List<Long> orderCarIdList = paramsDto.getOrderCarIdList();
        List<WaybillCarVo> list = null;
        if (paramsDto.getDispatchType() == WaybillTypeEnum.PICK.code) {
            /**提车*/
            list = orderCarDao.findPickCarEndpoint(orderCarIdList);
            for (WaybillCarVo vo : list) {
                //验证状态
                if(vo.getPickState() >= OrderCarLocalStateEnum.DISPATCHED.code){
                    return BaseResultUtil.fail("车辆{0},提车已经调度完成", vo.getOrderCarNo());
                }
                if(vo.getOrderCarState() >= OrderCarStateEnum.WAIT_PICK.code){
                    return BaseResultUtil.fail("车辆{0},提车调度已经结束", vo.getOrderCarNo());
                }
                if(vo.getEndStoreId() == null || vo.getEndStoreId() <= 0){
                    return BaseResultUtil.fail("车辆{0},没有业务中心，无法提送车调度", vo.getOrderCarNo());
                }
                //验证数据范围
                if(bizScope.getCode() != BizScopeEnum.CHINA.code){
                    if (vo.getStartBelongStoreId() == null || !storeIds.contains(vo.getStartBelongStoreId())) {
                        Store store = csStoreService.getById(vo.getStartBelongStoreId(), true);
                        String city = store == null ? "总部" : store.getCity();
                        return BaseResultUtil.fail("车辆{0},不在调度权限范围内，请联系{1}业务员", vo.getOrderCarNo(), city);
                    }
                }
            }

        } else if (paramsDto.getDispatchType() == WaybillTypeEnum.BACK.code) {
            /**送车*/
            list = orderCarDao.findBackCarEndpoint(orderCarIdList);
            for (WaybillCarVo vo : list) {
                //验证状态
                if(vo.getBackState() >= OrderCarLocalStateEnum.DISPATCHED.code){
                    return BaseResultUtil.fail("车辆{0},配送已经调度完成", vo.getOrderCarNo());
                }
                if(vo.getOrderCarState() >= OrderCarStateEnum.WAIT_BACK.code){
                    return BaseResultUtil.fail("车辆{0},配送调度已经结束", vo.getOrderCarNo());
                }
                if(vo.getStartStoreId() == null || vo.getStartStoreId() <= 0){
                    return BaseResultUtil.fail("车辆{0},没有业务中心，无法配送调度", vo.getOrderCarNo());
                }
                if(vo.getOrderEndCityCode() != null && vo.getOrderEndCityCode() != null && !vo.getOrderEndCityCode().equals(vo.getStartCityCode())){
                    return BaseResultUtil.fail("车辆{0},干线尚未调度到订单目的地城市范围内，不能送车调度", vo.getOrderCarNo());
                }
                //验证数据范围
                if(bizScope.getCode() != BizScopeEnum.CHINA.code){
                    if (vo.getStartBelongStoreId() == null || !storeIds.contains(vo.getStartBelongStoreId())) {
                        Store store = csStoreService.getById(vo.getStartBelongStoreId(), true);
                        String city = store == null ? "总部" : store.getCity();
                        return BaseResultUtil.fail("车辆{0},不在调度权限范围内，请联系{1}业务员", vo.getOrderCarNo(), city);
                    }
                }
            }
        } else {
            /**干线*/
            list = orderCarDao.findTrunkCarEndpoint(orderCarIdList);
            for (WaybillCarVo vo : list) {
                //验证状态
                if(vo.getTrunkState() >= OrderCarTrunkStateEnum.DISPATCHED.code){
                    return BaseResultUtil.fail("车辆{0},干线已经调度完成", vo.getOrderCarNo());
                }
                if(vo.getOrderCarState() >= OrderCarStateEnum.WAIT_BACK_DISPATCH.code){
                    return BaseResultUtil.fail("车辆{0},干线调度已经结束", vo.getOrderCarNo());
                }

                //验证数据范围
                if(bizScope.getCode() != BizScopeEnum.CHINA.code){
                    if(vo.getStartBelongStoreId() == null || !storeIds.contains(vo.getStartBelongStoreId())){
                        Store store = csStoreService.getById(vo.getStartBelongStoreId(), true);
                        String city = store == null ? "总部" : store.getCity();
                        return BaseResultUtil.fail("车辆{0},不在调度权限范围内，请联系{1}业务员", vo.getOrderCarNo(), city);
                    }
                }
            }

        }

        //计算推荐线路
        /*List<String> guideLines = csLineNodeService.getGuideLine(citySet, store.getCity());
        dispatchAddCarVo.setGuideLine(guideLines == null ? store.getCity() : guideLines.get(0));*/
        for (WaybillCarVo waybillCarVo : list) {
            Admin admin = csAdminService.findLoop(waybillCarVo.getEndStoreId());
            if(admin != null){
                waybillCarVo.setUnloadLinkUserId(admin.getId());
                waybillCarVo.setUnloadLinkName(admin.getName());
                waybillCarVo.setUnloadLinkPhone(admin.getPhone());
            }
        }
        dispatchAddCarVo.setList(list);
        return BaseResultUtil.success(dispatchAddCarVo);
    }

    /**
     * 拷贝订单开始城市
     *
     * @param vo
     * @param order
     * @author JPG
     * @since 2019/11/5 9:06
     */
    private void copyOrderStartCity(FullCity vo, Order order) {
        if (vo == null) {
            return;
        }
        order.setStartProvince(vo.getProvince());
        order.setStartProvinceCode(vo.getProvinceCode());
        order.setStartCity(vo.getCity());
        order.setStartCityCode(vo.getCityCode());
        order.setStartArea(vo.getArea());
        order.setStartAreaCode(vo.getAreaCode());
    }

    /**
     * 拷贝订单结束城市
     *
     * @param vo
     * @param order
     * @author JPG
     * @since 2019/11/5 9:06
     */
    private void copyOrderEndCity(FullCity vo, Order order) {
        if (vo == null) {
            return;
        }
        order.setEndProvince(vo.getProvince());
        order.setEndProvinceCode(vo.getProvinceCode());
        order.setEndCity(vo.getCity());
        order.setEndCityCode(vo.getCityCode());
        order.setEndArea(vo.getArea());
        order.setEndAreaCode(vo.getAreaCode());
    }

    /**
     * 均摊服务费
     *
     * @author JPG
     * @since 2019/10/29 8:30
     */
    private void shareTotalFee(BigDecimal totalFee, List<OrderCar> orderCarlist) {
        if (totalFee == null || totalFee.compareTo(BigDecimal.ZERO) <= 0) {
            return;
        }
        if (CollectionUtils.isEmpty(orderCarlist)) {
            return;
        }
        BigDecimal carTotalFee = BigDecimal.ZERO;
        for (OrderCar oc : orderCarlist) {
            carTotalFee = carTotalFee.add(oc.getPickFee() == null ? BigDecimal.ZERO : oc.getPickFee())
                    .add(oc.getTrunkFee() == null ? BigDecimal.ZERO : oc.getTrunkFee())
                    .add(oc.getBackFee() == null ? BigDecimal.ZERO : oc.getBackFee())
                    .add(oc.getAddInsuranceFee() == null ? BigDecimal.ZERO : oc.getAddInsuranceFee());
        }
        if (carTotalFee.compareTo(BigDecimal.ZERO) <= 0) {
            return;
        }

        BigDecimal avg = totalFee.divide(carTotalFee, 8, RoundingMode.FLOOR);
        BigDecimal avgTotalFee = BigDecimal.ZERO;
        for (OrderCar oc : orderCarlist) {
            BigDecimal avgCar = (oc.getPickFee()
                    .add(oc.getTrunkFee() == null ? BigDecimal.ZERO : oc.getTrunkFee())
                    .add(oc.getBackFee() == null ? BigDecimal.ZERO : oc.getBackFee())
                    .add(oc.getAddInsuranceFee() == null ? BigDecimal.ZERO : oc.getAddInsuranceFee()))
                    .multiply(avg);
            avgCar = avgCar.setScale(0, BigDecimal.ROUND_HALF_DOWN);
            //赋值
            oc.setTotalFee(avgCar);
            //统计
            avgTotalFee = avgTotalFee.add(avgCar);
        }

        BigDecimal remainder = totalFee.subtract(avgTotalFee);
        if (remainder.compareTo(BigDecimal.ZERO) <= 0) {
            return;
        }

        BigDecimal[] bigDecimals = remainder.divideAndRemainder(new BigDecimal(orderCarlist.size()));
        BigDecimal rAvg = bigDecimals[0];
        BigDecimal rRemainder = bigDecimals[1];
        for (OrderCar oc : orderCarlist) {
            if (rRemainder.compareTo(BigDecimal.ZERO) > 0) {
                oc.setTotalFee(oc.getTotalFee().add(rAvg).add(BigDecimal.ONE));
                rRemainder = rRemainder.subtract(BigDecimal.ONE);
            } else {
                oc.setTotalFee(oc.getTotalFee().add(rAvg));
            }
        }

    }

    /**
     * 均摊优惠券
     *
     * @param orderCarlist
     * @author JPG
     * @since 2019/10/29 8:27
     */
    private void shareCouponOffsetFee(BigDecimal couponOffsetFee, List<OrderCar> orderCarlist) {
        if (couponOffsetFee == null || couponOffsetFee.compareTo(BigDecimal.ZERO) <= 0) {
            return;
        }
        if (CollectionUtils.isEmpty(orderCarlist)) {
            return;
        }

        BigDecimal carTotalFee = BigDecimal.ZERO;
        for (OrderCar oc : orderCarlist) {
            carTotalFee = carTotalFee.add(oc.getPickFee()).add(oc.getTrunkFee()).add(oc.getBackFee()).add(oc.getAddInsuranceFee());
        }
        if (carTotalFee.compareTo(BigDecimal.ZERO) <= 0) {
            return;
        }

        BigDecimal avg = couponOffsetFee.divide(carTotalFee, 8, RoundingMode.FLOOR);
        BigDecimal avgTotalFee = BigDecimal.ZERO;
        for (OrderCar oc : orderCarlist) {
            BigDecimal avgCar = (oc.getPickFee().add(oc.getTrunkFee()).add(oc.getBackFee()).add(oc.getAddInsuranceFee())).multiply(avg);
            avgCar = avgCar.setScale(0, BigDecimal.ROUND_HALF_DOWN);
            oc.setTotalFee(avgCar);
            avgTotalFee = avgTotalFee.add(avgCar);
        }

        BigDecimal remainder = couponOffsetFee.subtract(avgTotalFee);
        if (remainder.compareTo(BigDecimal.ZERO) <= 0) {
            return;
        }
        BigDecimal[] bigDecimals = remainder.divideAndRemainder(new BigDecimal(orderCarlist.size()));
        BigDecimal rAvg = bigDecimals[0];
        BigDecimal rRemainder = bigDecimals[1];
        for (OrderCar oc : orderCarlist) {
            if (rRemainder.compareTo(BigDecimal.ZERO) > 0) {
                oc.setCouponOffsetFee(oc.getTotalFee().add(rAvg).add(BigDecimal.ONE));
                rRemainder = rRemainder.subtract(BigDecimal.ONE);
            } else {
                oc.setCouponOffsetFee(oc.getTotalFee().add(rAvg));
            }
        }
    }

}
