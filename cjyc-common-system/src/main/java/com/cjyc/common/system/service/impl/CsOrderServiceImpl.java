package com.cjyc.common.system.service.impl;

import com.alibaba.fastjson.JSON;
import com.cjkj.common.redis.lock.RedisDistributedLock;
import com.cjyc.common.model.dao.IOrderCarDao;
import com.cjyc.common.model.dao.IOrderDao;
import com.cjyc.common.model.dao.IWaybillCarDao;
import com.cjyc.common.model.dto.web.order.*;
import com.cjyc.common.model.entity.*;
import com.cjyc.common.model.entity.defined.FullCity;
import com.cjyc.common.model.entity.defined.UserInfo;
import com.cjyc.common.model.enums.PayModeEnum;
import com.cjyc.common.model.enums.ResultEnum;
import com.cjyc.common.model.enums.SendNoTypeEnum;
import com.cjyc.common.model.enums.UserTypeEnum;
import com.cjyc.common.model.enums.city.CityLevelEnum;
import com.cjyc.common.model.enums.customer.CustomerTypeEnum;
import com.cjyc.common.model.enums.log.OrderLogEnum;
import com.cjyc.common.model.enums.order.OrderCarStateEnum;
import com.cjyc.common.model.enums.order.OrderChangeTypeEnum;
import com.cjyc.common.model.enums.order.OrderStateEnum;
import com.cjyc.common.model.exception.ParameterException;
import com.cjyc.common.model.exception.ServerException;
import com.cjyc.common.model.keys.RedisKeys;
import com.cjyc.common.model.util.BaseResultUtil;
import com.cjyc.common.model.vo.ResultVo;
import com.cjyc.common.model.vo.web.OrderCarVo;
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
import java.math.BigDecimal;
import java.text.MessageFormat;
import java.util.List;
import java.util.Set;

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
    private ICsPingxxService csPingxxService;
    @Resource
    private ICsSysService csSysService;
    @Resource
    private ICsCityService csCityService;
    @Resource
    private ICsLineService csLineService;
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

    @Override
    public ResultVo save(SaveOrderDto paramsDto, OrderStateEnum stateEnum) {
        //获取参数
        Long orderId = paramsDto.getOrderId();

        Order order = null;
        boolean newOrderFlag = false;
        if (orderId != null) {
            //更新订单
            order = orderDao.selectById(orderId);
        }
        if (order == null) {
            //新建订单
            newOrderFlag = true;
            order = new Order();

        }
        BeanUtils.copyProperties(paramsDto, order);
        //查询三级城市
        fillOrderCityInfo(order);

        /**1、组装订单数据
         */
        if (newOrderFlag) {
            order.setNo(csSendNoService.getNo(SendNoTypeEnum.ORDER));
        }
        order.setState(stateEnum.code);
        order.setSource(order.getSource() == null ? paramsDto.getClientId() : order.getSource());
        order.setCreateTime(System.currentTimeMillis());
        order.setTotalFee(convertYuanToFen(order.getTotalFee()));

        //更新或插入订单
        int row = newOrderFlag ? orderDao.insert(order) : orderDao.updateById(order);

        /**2、更新或保存车辆信息*/
        List<SaveOrderCarDto> carDtoList = paramsDto.getOrderCarList();
        if (carDtoList == null || carDtoList.isEmpty()) {
            //没有车辆，结束
            return BaseResultUtil.success();
        }

        //费用统计变量
        //删除旧的车辆数据
        if (!newOrderFlag) {
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
            validateOrderCarPlateNoInfo(plateNoSet, dto.getPlateNo());

            OrderCar orderCar = new OrderCar();
            //复制数据
            BeanUtils.copyProperties(dto, orderCar);
            //填充数据
            orderCar.setOrderNo(order.getNo());
            orderCar.setOrderId(order.getId());
            orderCar.setNo(csSendNoService.formatNo(order.getNo(), noCount, 3));
            orderCar.setState(OrderCarStateEnum.WAIT_ROUTE.code);
            orderCar.setPickFee(convertYuanToFen(dto.getPickFee()));
            orderCar.setTrunkFee(convertYuanToFen(dto.getTrunkFee()));
            orderCar.setBackFee(convertYuanToFen(dto.getBackFee()));
            orderCar.setAddInsuranceFee(convertYuanToFen(dto.getAddInsuranceFee()));
            orderCarDao.insert(orderCar);
            //统计数量
            noCount++;
        }
        return BaseResultUtil.success("下单成功，订单编号{0}", order.getNo());
    }

    @Override
    public ResultVo commit(CommitOrderDto paramsDto) {
        //验证客户
        ResultVo<Customer> validateCustomerResult = validateCustomer(paramsDto);
        if (ResultEnum.SUCCESS.getCode() != validateCustomerResult.getCode()) {
            return validateCustomerResult;
        }
        Customer customer = validateCustomerResult.getData();
        paramsDto.setCustomerId(customer.getId());
        //提交订单
        Order order = commitOrder(paramsDto);

        return BaseResultUtil.success("下单{0}成功", order.getNo());

    }

    private Order commitOrder(CommitOrderDto paramsDto) {
        //获取参数
        Long orderId = paramsDto.getOrderId();

        Order order = null;
        boolean newOrderFlag = false;
        String lockKey = null;
        try {
            if (orderId != null) {
                //更新订单
                order = orderDao.selectById(orderId);
                if (order != null) {
                    lockKey = RedisKeys.getDispatchLockForOrderUpdate(order.getNo());
                    redisLock.lock(lockKey, 30000, 100, 300);
                }
            }
            if (order == null) {
                //新建订单
                newOrderFlag = true;
                order = new Order();
            }
            BeanUtils.copyProperties(paramsDto, order);
            //城市信息
            fillOrderCityInfo(order);
            //业务中心信息
            fillOrderStoreInfo(order);
            //订单编号
            if (newOrderFlag) {
                order.setNo(csSendNoService.getNo(SendNoTypeEnum.ORDER));
            }
            //TODO 验证物流券费用
            //TODO 校验总费用

            order.setState(OrderStateEnum.SUBMITTED.code);
            if (order.getSource() == null) {
                order.setSource(paramsDto.getClientId());
            }
            order.setCreateTime(System.currentTimeMillis());
            order.setTotalFee(convertYuanToFen(paramsDto.getTotalFee()));

            //更新或插入订单
            int row = newOrderFlag ? orderDao.insert(order) : orderDao.updateById(order);

            /**2、更新或保存车辆信息*/
            List<CommitOrderCarDto> carDtoList = paramsDto.getOrderCarList();
            //删除旧的车辆数据
            if (!newOrderFlag) {
                orderCarDao.deleteBatchByOrderId(order.getId());
            }
            //费用统计变量
            int count = 0;
            List<OrderCar> orderCarList = Lists.newArrayList();
            Set<String> vinSet = Sets.newHashSet();
            Set<String> plateNoSet = Sets.newHashSet();
            for (CommitOrderCarDto dto : carDtoList) {
                if (dto == null) {
                    continue;
                }
                //编号
                count++;
                //验证vin码是否重复
                validateOrderCarVinInfo(vinSet, dto.getVin());
                //验证车牌号是否重复
                validateOrderCarPlateNoInfo(plateNoSet, dto.getPlateNo());
                //复制数据
                OrderCar orderCar = new OrderCar();
                BeanUtils.copyProperties(dto, orderCar);
                //填充数据
                orderCar.setOrderNo(order.getNo());
                orderCar.setOrderId(order.getId());
                orderCar.setPickType(order.getPickType());
                orderCar.setBackType(order.getBackType());
                orderCar.setNowAreaCode(order.getStartAreaCode());
                orderCar.setNo(csSendNoService.formatNo(order.getNo(), count, 3));
                orderCar.setState(OrderCarStateEnum.WAIT_ROUTE.code);
                orderCar.setPickFee(convertYuanToFen(dto.getPickFee()));
                orderCar.setTrunkFee(convertYuanToFen(dto.getTrunkFee()));
                orderCar.setBackFee(convertYuanToFen(dto.getBackFee()));
                orderCar.setAddInsuranceFee(convertYuanToFen(dto.getAddInsuranceFee()));
                orderCarDao.insert(orderCar);

                //提取数据
                orderCarList.add(orderCar);
            }
            if (CollectionUtils.isEmpty(orderCarList)) {
                throw new ParameterException("订单至少包含一辆车");
            }

            //均摊优惠券费用
            if (order.getCouponOffsetFee() != null && order.getCouponOffsetFee().compareTo(BigDecimal.ZERO) > 0) {
                shareCouponOffsetFee(order, orderCarList);
            }
            //均摊总费用
            if (order.getTotalFee() != null && order.getTotalFee().compareTo(BigDecimal.ZERO) > 0) {
                shareTotalFee(order, orderCarList);
            }
            //更新车辆信息
            orderCarList.forEach(orderCar -> orderCarDao.updateById(orderCar));

            //合计费用：提、干、送、保险
            fillOrderFeeInfo(order, orderCarList);
            order.setCarNum(orderCarList.size());
            orderDao.updateById(order);

            //记录发车人和收车人
            csCustomerContactService.asyncSaveByOrder(order);
            //记录客户历史线路
            if (newOrderFlag) {
                csCustomerLineService.asyncSave(order);
            }
            //记录订单日志
            csOrderLogService.asyncSave(order, OrderLogEnum.COMMITTED,
                    new String[]{MessageFormat.format(OrderLogEnum.COMMITTED.getInnerLog(), order.getNo()),
                            MessageFormat.format(OrderLogEnum.COMMITTED.getInnerLog(), order.getNo())},
                    new UserInfo(paramsDto.getLoginId(), paramsDto.getLoginName(), paramsDto.getLoginPhone(), UserTypeEnum.ADMIN));
        } finally {
            if (!newOrderFlag) {
                redisLock.releaseLock(lockKey);
            }
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
    private void fillOrderCityInfo(Order order) {
        if (StringUtils.isNotBlank(order.getStartAreaCode())) {
            FullCity startFullCity = csCityService.findFullCity(order.getStartAreaCode(), CityLevelEnum.PROVINCE);
            copyOrderStartCity(startFullCity, order);
        }
        if (StringUtils.isNotBlank(order.getEndAreaCode())) {
            FullCity endFullCity = csCityService.findFullCity(order.getEndAreaCode(), CityLevelEnum.PROVINCE);
            copyOrderEndCity(endFullCity, order);
        }
    }

    /**
     * 业务中心信息赋值
     *
     * @param order
     * @author JPG
     * @since 2019/12/12 11:55
     */
    private void fillOrderStoreInfo(Order order) {
        if (order.getStartStoreId() != null && order.getStartStoreId() > 0) {
            order.setStartBelongStoreId(order.getStartStoreId());
        } else {
            //查询地址所属业务中心
            Store store = csStoreService.getOneBelongByCityCode(order.getStartCityCode());
            if (store != null) {
                if (order.getStartStoreId() == -5) {
                    //无主观操作
                    order.setStartStoreId(store.getId());
                }
                order.setStartBelongStoreId(store.getId());
            }
        }
        if (order.getEndStoreId() != null && order.getEndStoreId() > 0) {
            order.setEndBelongStoreId(order.getEndStoreId());
        } else {
            //查询地址所属业务中心
            Store store = csStoreService.getOneBelongByCityCode(order.getEndCityCode());
            if (store != null) {
                if (order.getEndStoreId() == -5) {
                    //无主观操作
                    order.setEndStoreId(store.getId());
                }
                order.setEndBelongStoreId(store.getId());
            }
        }
    }

    /**
     * 下单验证客户
     *
     * @param paramsDto
     * @author JPG
     * @since 2019/11/27 14:05
     */
    private ResultVo<Customer> validateCustomer(CommitOrderDto paramsDto) {
        Customer customer = null;
        if (paramsDto.getCustomerId() != null) {
            customer = csCustomerService.getById(paramsDto.getCustomerId(), true);
            if (customer != null && !customer.getName().equals(paramsDto.getCustomerName())) {
                return BaseResultUtil.fail(ResultEnum.CREATE_NEW_CUSTOMER.getCode(),
                        "客户手机号存在，名称不一致：新名称（{0}）旧名称（{1}），请返回订单重新选择客户",
                        paramsDto.getCustomerName(), customer.getName());
            }
        }
        if (customer == null) {
            customer = csCustomerService.getByPhone(paramsDto.getCustomerPhone(), true);
            if (customer != null && !customer.getName().equals(paramsDto.getCustomerName())) {
                return BaseResultUtil.fail(ResultEnum.CREATE_NEW_CUSTOMER.getCode(),
                        "客户手机号存在，名称不一致：新名称（{0}）旧名称（{1}），请返回订单重新选择客户",
                        paramsDto.getCustomerName(), customer.getName());
            }
        }
        if (customer == null) {
            customer = new Customer();
            if (paramsDto.getCustomerType() == CustomerTypeEnum.INDIVIDUAL.code) {
                if (paramsDto.getCreateCustomerFlag()) {
                    customer.setName(paramsDto.getCustomerName());
                    customer.setContactMan(paramsDto.getCustomerName());
                    customer.setContactPhone(paramsDto.getCustomerPhone());
                    customer.setType(CustomerTypeEnum.INDIVIDUAL.code);
                    //customer.setInitial()
                    customer.setState(1);
                    customer.setPayMode(PayModeEnum.COLLECT.code);
                    customer.setCreateTime(System.currentTimeMillis());
                    customer.setCreateUserId(paramsDto.getLoginId());
                    //添加
                    csCustomerService.save(customer);
                } else {
                    return BaseResultUtil.getVo(ResultEnum.CREATE_NEW_CUSTOMER.getCode(), ResultEnum.CREATE_NEW_CUSTOMER.getMsg());
                }
            } else {
                return BaseResultUtil.fail("企业客户/合伙人不存在");
            }
        }
        return BaseResultUtil.success(customer);
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
        Order order = orderDao.selectById(paramsDto.getOrderId());
        //验证必要信息是否完全
        validateOrderFeild(order);

        List<OrderCar> orderCarList = orderCarDao.findListByOrderId(order.getId());
        if (CollectionUtils.isEmpty(orderCarList)) {
            throw new ParameterException("[订单车辆]-为空");
        }

        //根据到付和预付置不同状态
        if (PayModeEnum.PREPAY.code == order.getPayType()) {
            order.setState(OrderStateEnum.WAIT_PREPAY.code);
            //TODO 支付通知
        } else {
            order.setState(OrderStateEnum.CHECKED.code);
        }
        order.setCheckTime(System.currentTimeMillis());
        order.setCheckUserName(paramsDto.getLoginName());
        order.setCheckUserId(paramsDto.getLoginId());
        orderDao.updateById(order);

        //记录订单日志
        UserInfo userInfo = new UserInfo(paramsDto.getLoginId(), paramsDto.getLoginName(), paramsDto.getLoginPhone(), UserTypeEnum.ADMIN);
        csOrderLogService.asyncSave(order, OrderLogEnum.CHECKED,
                new String[]{MessageFormat.format(OrderLogEnum.CHECKED.getInnerLog(), order.getNo()),
                        MessageFormat.format(OrderLogEnum.CHECKED.getInnerLog(), order.getNo())},
                userInfo);
        //TODO 处理优惠券为使用状态，优惠券有且仅能验证一次，修改时怎么保证

        //TODO 路由轨迹

        return BaseResultUtil.success();
    }

    /**
     * 费用信息赋值
     *
     * @param order
     * @param orderCarList
     * @author JPG
     * @since 2019/12/12 14:41
     */
    private void fillOrderFeeInfo(Order order, List<OrderCar> orderCarList) {
        BigDecimal pickFee = BigDecimal.ZERO;
        BigDecimal trunkFee = BigDecimal.ZERO;
        BigDecimal backFee = BigDecimal.ZERO;
        BigDecimal addInsuranceFee = BigDecimal.ZERO;
        for (OrderCar orderCar : orderCarList) {
            pickFee = pickFee.add(orderCar.getPickFee());
            trunkFee = trunkFee.add(orderCar.getTrunkFee());
            backFee = backFee.add(orderCar.getBackFee());
            addInsuranceFee = addInsuranceFee.add(orderCar.getAddInsuranceFee());
        }
        order.setPickFee(pickFee);
        order.setTrunkFee(trunkFee);
        order.setBackFee(backFee);
        order.setAddInsuranceFee(addInsuranceFee);
    }

    /**
     * 提交并审核
     *
     * @param paramsDto
     * @return
     */
    @Override
    public ResultVo commitAndCheck(CommitOrderDto paramsDto) {
        //验证客户
        ResultVo<Customer> validateCustomerResult = validateCustomer(paramsDto);
        if (ResultEnum.SUCCESS.getCode() != validateCustomerResult.getCode()) {
            return validateCustomerResult;
        }
        Customer customer = validateCustomerResult.getData();
        paramsDto.setCustomerId(customer.getId());
        //提交订单
        Order order = commitOrder(paramsDto);
        //审核订单
        check(new CheckOrderDto(paramsDto.getLoginId(), paramsDto.getLoginName(), paramsDto.getLoginPhone(), order.getId()));
        return BaseResultUtil.success("下单{0}成功", order.getNo());
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
            throw new ParameterException("[订单]-不存在");
        }
        if (order.getState() <= OrderStateEnum.WAIT_SUBMIT.code) {
            throw new ParameterException("[订单]-未提交，无法审核");
        }
        if (order.getState() >= OrderStateEnum.CHECKED.code) {
            throw new ParameterException("[订单]-已经审核过，无法审核");
        }
        if (order.getId() == null || order.getNo() == null) {
            throw new ParameterException("[订单]-订单编号不能为空");
        }
        if (order.getCustomerId() == null) {
            throw new ParameterException("[订单]-客户不存在");
        }
        if (order.getStartProvinceCode() == null
                || order.getStartCityCode() == null
                || order.getStartAreaCode() == null
                || order.getStartAddress() == null
                || order.getEndProvinceCode() == null
                || order.getEndCityCode() == null
                || order.getEndAreaCode() == null
                || order.getEndAddress() == null) {
            throw new ParameterException("[订单]-地址不完整");
        }
        if (order.getCarNum() == null || order.getCarNum() <= 0) {
            throw new ParameterException("[订单]-车辆数不能小于一辆");
        }
        if (order.getPickType() == null
                || order.getPickContactPhone() == null) {
            throw new ParameterException("[订单]-提车联系人不能为空");
        }
        if (order.getBackType() == null
                || order.getBackContactPhone() == null) {
            throw new ParameterException("收车联系人不能为空");
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
        Order order = orderDao.selectById(paramsDto.getOrderId());
        if (order == null || order.getState() >= OrderStateEnum.WAIT_RECHECK.code) {
            return BaseResultUtil.fail("订单不允许修改");
        }
        order.setAllotToUserId(paramsDto.getToAdminId());
        order.setAllotToUserName(paramsDto.getToAdminName());
        orderDao.updateById(order);
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
            return BaseResultUtil.fail("[订单]-不存在");
        }
        Integer oldState = order.getState();
        if (oldState <= OrderStateEnum.WAIT_SUBMIT.code) {
            return BaseResultUtil.fail("[订单]-未提交，无法驳回");
        }
        if (oldState > OrderStateEnum.CHECKED.code) {
            return BaseResultUtil.fail("[订单]-已经运输无法驳回");
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
            return BaseResultUtil.fail("当前订单状态不允许取消");
        }
        Integer oldState = order.getState();
        order.setState(OrderStateEnum.F_CANCEL.code);
        orderDao.updateById(order);

        //添加操作日志
        orderChangeLogService.asyncSave(order, OrderChangeTypeEnum.CANCEL,
                new Object[]{oldState, order.getState(), paramsDto.getReason()},
                new Object[]{paramsDto.getLoginId(), paramsDto.getLoginName()});
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
            return BaseResultUtil.fail("当前订单状态不允许作废");
        }
        Integer oldState = order.getState();
        order.setState(OrderStateEnum.F_OBSOLETE.code);
        orderDao.updateById(order);

        //添加操作日志
        orderChangeLogService.asyncSave(order, OrderChangeTypeEnum.OBSOLETE,
                new Object[]{oldState, OrderStateEnum.F_OBSOLETE.code, paramsDto.getReason()},
                new Object[]{paramsDto.getLoginId(), paramsDto.getLoginName()});

        return BaseResultUtil.success();
    }

    @Override
    public ResultVo changePrice(ChangePriceOrderDto paramsDto) {
        //参数
        Long orderId = paramsDto.getOrderId();
        Order order = orderDao.selectById(orderId);
        //记录历史数据
        OrderVo oldOrderVo = new OrderVo();
        BeanUtils.copyProperties(order, oldOrderVo);
        oldOrderVo.setOrderCarList(orderCarDao.findListByOrderId(orderId));

        /**2、更新或保存车辆信息*/
        List<ChangePriceOrderCarDto> carDtoList = paramsDto.getOrderCarList();
        //费用统计变量
        for (ChangePriceOrderCarDto dto : carDtoList) {
            if (dto == null) {
                continue;
            }
            //统计数量
            OrderCar oc = orderCarDao.selectById(dto.getId());
            if (oc == null) {
                throw new ServerException("ID为{}的车辆，不存在", dto.getId());
            }
            OrderCar orderCar = new OrderCar();
            //填充数据
            orderCar.setId(oc.getOrderId());
            orderCar.setPickFee(convertYuanToFen(dto.getPickFee()));
            orderCar.setTrunkFee(convertYuanToFen(dto.getTrunkFee()));
            orderCar.setBackFee(convertYuanToFen(dto.getBackFee()));
            orderCar.setAddInsuranceFee(convertYuanToFen(dto.getAddInsuranceFee()));
            orderCar.setAddInsuranceAmount(dto.getAddInsuranceAmount() == null ? 0 : dto.getAddInsuranceAmount());
            orderCarDao.updateById(orderCar);
        }
        //新数据
        List<OrderCar> orderCarList = orderCarDao.findListByOrderId(orderId);
        //均摊优惠券费用
        if (order.getCouponOffsetFee() != null && order.getCouponOffsetFee().compareTo(BigDecimal.ZERO) > 0) {
            shareCouponOffsetFee(order, orderCarList);
        }
        //均摊总费用
        if (order.getTotalFee() != null && order.getTotalFee().compareTo(BigDecimal.ZERO) > 0) {
            shareTotalFee(order, orderCarList);
        }
        //更新车辆信息
        orderCarList.forEach(orderCar -> orderCarDao.updateById(orderCar));

        //合计费用：提、干、送、保险
        fillOrderFeeInfo(order, orderCarList);
        order.setCarNum(orderCarList.size());
        order.setTotalFee(convertYuanToFen(paramsDto.getTotalFee()));
        orderDao.updateById(order);

        //TODO 日志
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
        Order order = orderDao.selectById(paramsDto.getOrderId());
        if (order == null || order.getState() >= OrderStateEnum.TRANSPORTING.code) {
            return BaseResultUtil.fail("订单不允许修改");
        }
        List<ReplenishOrderCarDto> list = paramsDto.getOrderCarList();
        for (ReplenishOrderCarDto dto : list) {
            OrderCar orderCar = orderCarDao.selectById(dto.getId());
            orderCar.setBrand(dto.getBrand());
            orderCar.setModel(dto.getModel());
            orderCar.setPlateNo(dto.getPlateNo());
            orderCar.setVin(dto.getVin());
            orderCarDao.updateById(orderCar);
        }
        // TODO 日志
        return BaseResultUtil.success();
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

        //业务范围
        /*BizScope bizScope = csSysService.getBizScopeByLoginId(paramsDto.getLoginId(), true);
        if(BizScopeEnum.NONE.code == bizScope.getCode()){
            return BaseResultUtil.fail("无数据权限");
        }
        paramsDto.setBizScope(bizScope.getCode() == 0 ? null : bizScope.getStoreIds());*/

        //查询车辆信息
        List<WaybillCarVo> childList = waybillCarDao.findCarEndpoint(paramsDto.getOrderCarIdList());
        //计算推荐线路
        /*List<String> guideLines = csLineNodeService.getGuideLine(citySet, store.getCity());
        dispatchAddCarVo.setGuideLine(guideLines == null ? store.getCity() : guideLines.get(0));*/

        dispatchAddCarVo.setList(childList);
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
     * @param order
     * @param orderCarSavelist
     * @author JPG
     * @since 2019/10/29 8:30
     */
    private void shareTotalFee(Order order, List<OrderCar> orderCarSavelist) {
        BigDecimal totalFee = order.getTotalFee() == null ? BigDecimal.ZERO : order.getTotalFee();
        BigDecimal[] totalFeeArray = totalFee.divideAndRemainder(new BigDecimal(order.getCarNum()));
        BigDecimal totalFeeAvg = totalFeeArray[0];
        BigDecimal totalFeeRemainder = totalFeeArray[1];
        for (OrderCar orderCar : orderCarSavelist) {
            //合伙人计算均摊服务费
            if (totalFeeRemainder.compareTo(BigDecimal.ZERO) > 0) {
                orderCar.setTotalFee(totalFeeAvg.add(BigDecimal.ONE));
                totalFeeRemainder = totalFeeRemainder.subtract(BigDecimal.ONE);
            } else {
                orderCar.setTotalFee(totalFeeAvg);
            }
        }
    }

    /**
     * 均摊优惠券
     *
     * @param order
     * @param orderCarSavelist
     * @author JPG
     * @since 2019/10/29 8:27
     */
    private void shareCouponOffsetFee(Order order, List<OrderCar> orderCarSavelist) {
        BigDecimal couponOffsetFee = order.getCouponOffsetFee() == null ? BigDecimal.ZERO : order.getCouponOffsetFee();
        BigDecimal[] couponOffsetFeeArray = couponOffsetFee.divideAndRemainder(new BigDecimal(order.getCarNum()));
        BigDecimal couponOffsetFeeAvg = couponOffsetFeeArray[0];
        BigDecimal couponOffsetFeeRemainder = couponOffsetFeeArray[1];
        for (OrderCar orderCar : orderCarSavelist) {
            if (couponOffsetFeeRemainder.compareTo(BigDecimal.ZERO) > 0) {
                orderCar.setCouponOffsetFee(couponOffsetFeeAvg.add(BigDecimal.ONE));
                couponOffsetFeeRemainder = couponOffsetFeeRemainder.subtract(BigDecimal.ONE);
            } else {
                orderCar.setCouponOffsetFee(couponOffsetFeeAvg);
            }
        }
    }

    private BigDecimal convertYuanToFen(BigDecimal fee) {
        return fee == null ? null : fee.multiply(new BigDecimal(100));
    }

}
