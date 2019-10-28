package com.cjyc.web.api.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cjyc.common.model.dao.ICouponDao;
import com.cjyc.common.model.dao.IOrderCarDao;
import com.cjyc.common.model.dao.IOrderChangeLogDao;
import com.cjyc.common.model.dao.IOrderDao;
import com.cjyc.common.model.dto.web.order.*;
import com.cjyc.common.model.entity.*;
import com.cjyc.common.model.enums.CommonStateEnum;
import com.cjyc.common.model.enums.PayModeEnum;
import com.cjyc.common.model.enums.SendNoTypeEnum;
import com.cjyc.common.model.enums.customer.CustomerTypeEnum;
import com.cjyc.common.model.enums.order.OrderCarStateEnum;
import com.cjyc.common.model.enums.order.OrderChangeTypeEnum;
import com.cjyc.common.model.enums.order.OrderStateEnum;
import com.cjyc.common.model.util.BaseResultUtil;
import com.cjyc.common.model.vo.ListVo;
import com.cjyc.common.model.vo.PageVo;
import com.cjyc.common.model.vo.ResultVo;
import com.cjyc.common.model.vo.web.order.OrderCarWaitDispatchVo;
import com.cjyc.common.model.vo.web.order.OrderVo;
import com.cjyc.web.api.exception.ServerException;
import com.cjyc.web.api.service.*;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 订单表(客户下单) 服务实现类
 * </p>
 *
 * @author JPG
 * @since 2019-10-09
 */
@Service
@Transactional(rollbackFor = ServerException.class)
public class OrderServiceImpl extends ServiceImpl<IOrderDao, Order> implements IOrderService {

    @Resource
    private IOrderDao orderDao;
    @Resource
    private IOrderCarDao orderCarDao;
    @Resource
    private ISendNoService sendNoService;
    @Resource
    private IOrderChangeLogDao orderChangeLogDao;
    @Resource
    private ICustomerService customerService;
    @Resource
    private ICouponSendService couponSendService;

    @Override
    public ResultVo saveAndUpdate(CommitOrderDto paramsDto) {
        //处理参数
        paramsDto.setWlTotalFee(paramsDto.getWlTotalFee() == null ? BigDecimal.ZERO : paramsDto.getWlTotalFee());
        paramsDto.setRealWlTotalFee(paramsDto.getRealWlTotalFee() == null ? BigDecimal.ZERO : paramsDto.getRealWlTotalFee());
        //获取参数
        Long orderId = paramsDto.getOrderId();
        Long couponSendId = paramsDto.getCouponSendId();

        BigDecimal wlTotalFee = paramsDto.getWlTotalFee();
        BigDecimal realWlTotalFee = paramsDto.getRealWlTotalFee();




        Order order = null;
        Boolean newOrderFlag = false;
        if(orderId != null){
            //更新订单
            order = orderDao.selectById(orderId);
            BeanUtils.copyProperties(paramsDto, order);
        }
        if(order == null){
            //新建订单
            newOrderFlag = true;
            order = new Order();
            BeanUtils.copyProperties(paramsDto, order);
        }


        //创建用户
        Customer customer = new Customer();
        if(paramsDto.getCustomerId() != null){
            customer = customerService.selectById(paramsDto.getCustomerId());
        }
        if(customer == null){
            customer = new Customer();
            if(paramsDto.getCustomerType() == CustomerTypeEnum.INDIVIDUAL.code){
                customer.setName(paramsDto.getCustomerName());
                customer.setContactMan(paramsDto.getCustomerName());
                customer.setContactPhone(paramsDto.getCustomerPhone());
                customer.setType(CustomerTypeEnum.INDIVIDUAL.code);
                //customer.setInitial()
                customer.setState(1);
                customer.setPayMode(PayModeEnum.CURRENT.code);
                customer.setCreateTime(System.currentTimeMillis());
                customer.setCreateUserId(paramsDto.getUserId());
                //添加
                customerService.save(customer);
            }else {
                return BaseResultUtil.fail("企业客户/合伙人不存在");
            }
        }
        /**1、组装订单数据
         *
         */

        if(order.getNo() != null && paramsDto.getSaveType() == 2){
            //订单发号（审核的时候才会发号）
            order.setNo(sendNoService.getNo(SendNoTypeEnum.ORDER));
        }
        order.setState(paramsDto.getState());
        order.setSource(order.getSource() == null ? paramsDto.getClientId() : order.getSource());
        //计算优惠券，抵消金额
        if(couponSendId != null){
            BigDecimal couponAmount = couponSendService.getAmountById(couponSendId, paramsDto.getRealWlTotalFee());
            order.setCouponOffsetFee(couponAmount);
            //TODO 处理优惠券为使用状态，优惠券有且仅能验证一次，修改时怎么保证
        }
        if(paramsDto.getCustomerType() == CustomerTypeEnum.COOPERATOR.code){
            order.setAgencyFee(wlTotalFee.subtract(realWlTotalFee).add(order.getCouponOffsetFee()));
            //合伙人：收车后客户应支付平台的钱->总物流费
            order.setTotalFee(wlTotalFee);
        }else{
            //其他客户：收车后客户应支付平台的钱->总物流费-优惠券
            order.setTotalFee(wlTotalFee.subtract(order.getCouponOffsetFee()));
        }

        order.setCreateTime(System.currentTimeMillis());

        //更新或插入订单
        int row = newOrderFlag ? orderDao.insert(order) : orderDao.updateById(order);


        /**2、更新或保存车辆信息*/
        List<CommitOrderCarDto> carDtoList =  paramsDto.getOrderCarList();
        List<OrderCar> orderCarSavelist = new ArrayList<>();

        //费用统计变量
        int noCount = 1;
        BigDecimal totalAgencyFee = order.getAgencyFee();//均摊
        BigDecimal totalCouponOffsetFee = order.getCouponOffsetFee();//均摊
        BigDecimal totalPickFee = BigDecimal.ZERO;//求和
        BigDecimal totalTrunkFee = BigDecimal.ZERO;//求和
        BigDecimal totalBackFee = BigDecimal.ZERO;//求和
        BigDecimal totalInsuranceFee = BigDecimal.ZERO;//求和
        BigDecimal totalFee = BigDecimal.ZERO;//求和

        for(CommitOrderCarDto dto : carDtoList){
            if(dto == null){
                continue;
            }

            OrderCar orderCar = new OrderCar();
            //复制数据
            BeanUtils.copyProperties(dto,orderCar);
            //填充数据
            orderCar.setOrderNo(order.getNo());
            orderCar.setOrderId(order.getId());
            orderCar.setNo(order.getNo() + "_" + noCount);
            if(paramsDto.getSaveType() != 2) {
                orderCar.setState(OrderCarStateEnum.WAIT_ROUTE.code);
            }else{
                orderCar.setState(OrderCarStateEnum.WAIT_ROUTE.code);
            }
            orderCar.setPickFee(dto.getPickFee() == null ? BigDecimal.ZERO : dto.getPickFee());
            orderCar.setTrunkFee(dto.getTrunkFee() == null ? BigDecimal.ZERO : dto.getTrunkFee());
            orderCar.setBackFee(dto.getBackFee() == null ? BigDecimal.ZERO : dto.getBackFee());
            orderCar.setInsuranceFee(dto.getInsuranceFee() == null ? BigDecimal.ZERO : dto.getInsuranceFee());
            orderCar.setTotalFee(dto.getPickFee()
                    .add(dto.getTrunkFee())
                    .add(dto.getBackFee())
                    .add(dto.getInsuranceFee()));

            orderCarSavelist.add(orderCar);

            //计算统计费用
            totalPickFee = totalPickFee.add(orderCar.getPickFee());
            totalTrunkFee = totalTrunkFee.add(orderCar.getPickFee());
            totalBackFee = totalBackFee.add(orderCar.getBackFee());
            totalInsuranceFee = totalInsuranceFee.add(orderCar.getInsuranceFee());
            totalCouponOffsetFee = totalCouponOffsetFee.add(orderCar.getCouponOffsetFee());
            //统计数量
            noCount++;

        }

        //均摊优惠券费用
        if(totalCouponOffsetFee.compareTo(BigDecimal.ZERO) > 0){
            BigDecimal[] couponOffsetFeeArray = totalCouponOffsetFee.divideAndRemainder(new BigDecimal(noCount));
            BigDecimal couponOffsetFeeAvg = couponOffsetFeeArray[0];
            BigDecimal couponOffsetFeeRemainder = couponOffsetFeeArray[1];
            for (OrderCar orderCar : orderCarSavelist) {
                if(couponOffsetFeeRemainder.compareTo(BigDecimal.ZERO) > 0){
                    orderCar.setCouponOffsetFee(couponOffsetFeeAvg.add(BigDecimal.ONE));
                    couponOffsetFeeRemainder = couponOffsetFeeRemainder.subtract(BigDecimal.ONE);
                }else{
                    orderCar.setCouponOffsetFee(couponOffsetFeeAvg);
                }
                orderCar.setTotalFee(orderCar.getPickFee()
                        .add(orderCar.getTrunkFee())
                        .add(orderCar.getBackFee())
                        .add(order.getInsuranceFee()));
                //不是合伙人
                if(paramsDto.getCustomerType() != CustomerTypeEnum.COOPERATOR.code){
                    orderCar.setTotalFee(orderCar.getTotalFee()
                            .subtract(orderCar.getCouponOffsetFee()));
                }
            }

        }

        //均摊服务费用
        if(totalAgencyFee.compareTo(BigDecimal.ZERO) > 0 && paramsDto.getCustomerType() == CustomerTypeEnum.COOPERATOR.code){
            BigDecimal[] agencyFeeArray = totalAgencyFee.divideAndRemainder(new BigDecimal(noCount));
            BigDecimal agencyFeeAvg = agencyFeeArray[0];
            BigDecimal agencyFeeRemainder = agencyFeeArray[1];
            for (OrderCar orderCar : orderCarSavelist) {
                //合伙人计算均摊服务费
                if(agencyFeeRemainder.compareTo(BigDecimal.ZERO) > 0){
                    orderCar.setAgencyFee(agencyFeeAvg.add(BigDecimal.ONE));
                    agencyFeeRemainder = agencyFeeRemainder.subtract(BigDecimal.ONE);
                }else{
                    orderCar.setAgencyFee(agencyFeeAvg);
                }
                orderCar.setTotalFee(orderCar.getPickFee()
                        .add(orderCar.getTrunkFee())
                        .add(orderCar.getBackFee())
                        .add(order.getInsuranceFee())
                        .add(order.getAgencyFee()));
                
            }

        }
        //删除旧的车辆数据
        if(!newOrderFlag){
            orderCarDao.deleteBatchByOrderId(order.getId());
        }
        //批量保存车辆
        orderCarDao.saveBatch(orderCarSavelist);

        order.setPickFee(totalPickFee);
        order.setTrunkFee(totalTrunkFee);
        order.setBackFee(totalBackFee);
        order.setInsuranceFee(totalInsuranceFee);
        orderDao.updateById(order);

        return BaseResultUtil.success();
    }


    @Override
    public ResultVo<ListVo<Map<String, Object>>> waitDispatchCarCountList() {
        List<Map<String, Object>> list = orderCarDao.countListWaitDispatchCar();
        //查询统计
        Map<String, Object> countInfo = null;
        if (list != null || !list.isEmpty()) {
            countInfo = orderCarDao.countTotalWaitDispatchCar();
        }
        return BaseResultUtil.success(list, countInfo);
    }

    @Override
    public ResultVo<PageVo<OrderCarWaitDispatchVo>> waitDispatchCarList(WaitDispatchListOrderCarDto paramsDto, List<Long> bizScope) {
        PageHelper.startPage(paramsDto.getCurrentPage(), paramsDto.getPageSize(), true);
        List<OrderCarWaitDispatchVo> list = orderCarDao.findWaitDispatchCarList(paramsDto, bizScope);
        PageInfo<OrderCarWaitDispatchVo> pageInfo = new PageInfo<>(list);
        if (paramsDto.getCurrentPage() > pageInfo.getPages()) {
            pageInfo.setList(null);
        }
        return BaseResultUtil.success(pageInfo);
    }

    /**
     * 按线路统计待调度车辆（统计列表）
     *
     * @author JPG
     * @since 2019/10/16 10:04
     */
    @Override
    public ResultVo<ListVo<Map<String, Object>>> lineWaitDispatchCarCountList(LineWaitDispatchCountListOrderCarDto paramsDto, List<Long> bizScopeStoreIds) {
        //查询列表
        List<Map<String, Object>> list = orderCarDao.findlineWaitDispatchCarCountList(paramsDto, bizScopeStoreIds);

        //统计结果
        return null;
    }

    @Override
    public OrderVo getVoById(Long orderId) {
        OrderVo orderVo = orderDao.findVoById(orderId);
        List<OrderCar> list = orderCarDao.findByOrderId(orderId);
        orderVo.setOrderCarList(list);
        return orderVo;
    }

    @Override
    public ResultVo allot(AllotOrderDto orderAllotDto) {
        Order order = orderDao.selectById(orderAllotDto.getOrderId());
        if(order == null || order.getState() >= OrderStateEnum.WAIT_RECHECK.code){
            BaseResultUtil.fail("订单不允许修改");
        }
        order.setCheckUserId(orderAllotDto.getToUserId());
        order.setCheckUserName(orderAllotDto.getToUserName());
        orderDao.updateById(order);
        return BaseResultUtil.success();
    }

    @Override
    public ResultVo<PageVo<Order>> list(ListOrderDto paramsDto) {
        PageHelper.offsetPage(paramsDto.getCurrentPage(), paramsDto.getPageSize(), true);
        List<Order> list =  orderDao.findListSelective(paramsDto);
        PageInfo<Order> pageInfo = new PageInfo<>(list);
        if(paramsDto.getCurrentPage() > pageInfo.getPages()){
            pageInfo.setList(null);
        }

        return BaseResultUtil.success(pageInfo);
    }

    @Override
    public ResultVo<PageVo<OrderCar>> carlist(ListOrderCarDto paramsDto) {
        PageHelper.offsetPage(paramsDto.getCurrentPage(), paramsDto.getPageSize(), true);
        List<OrderCar> list =  orderCarDao.findListSelective(paramsDto);
        PageInfo<OrderCar> pageInfo = new PageInfo<>(list);
        if(paramsDto.getCurrentPage() > pageInfo.getPages()){
            pageInfo.setList(null);
        }

        return BaseResultUtil.success(pageInfo);
    }

    @Override
    public ResultVo cancel(CancelOrderDto paramsDto) {
        //取消订单
        Order order = orderDao.selectById(paramsDto.getOrderId());
        if(order == null){
            BaseResultUtil.fail("订单不存在");
        }
        if(order.getState() > OrderStateEnum.CHECKED.code){
            BaseResultUtil.fail("当前订单状态不允许取消");
        }
        Integer oldState = order.getState();
        order.setState(OrderStateEnum.F_CANCEL.code);
        orderDao.updateById(order);

        //添加操作日志
        OrderChangeLog orderChangeLog = new OrderChangeLog();
        orderChangeLog.setOrderId(order.getId());
        orderChangeLog.setOrderNo(order.getNo());
        orderChangeLog.setName(OrderChangeTypeEnum.CANCEL.name);
        orderChangeLog.setType(OrderChangeTypeEnum.CANCEL.code);
        orderChangeLog.setOldContent(oldState.toString());
        orderChangeLog.setNewContent(order.getState().toString());
        orderChangeLog.setReason(paramsDto.getReason());
        orderChangeLog.setState(CommonStateEnum.CHECKED.code);
        orderChangeLog.setCreateTime(System.currentTimeMillis());
        orderChangeLog.setCreateUser(paramsDto.getUserName());
        orderChangeLog.setCreateUserId(paramsDto.getUserId());
        orderChangeLogDao.insert(orderChangeLog);
        return BaseResultUtil.success();
    }

    @Override
    public ResultVo obsolete(CancelOrderDto paramsDto) {
        //作废订单
        Order order = orderDao.selectById(paramsDto.getOrderId());
        if(order == null){
            BaseResultUtil.fail("订单不存在");
        }
        if(order.getState() > OrderStateEnum.CHECKED.code){
            BaseResultUtil.fail("当前订单状态不允许作废");
        }
        Integer oldState = order.getState();
        order.setState(OrderStateEnum.F_OBSOLETE.code);
        orderDao.updateById(order);

        //添加操作日志
        OrderChangeLog orderChangeLog = new OrderChangeLog();
        orderChangeLog.setOrderId(order.getId());
        orderChangeLog.setOrderNo(order.getNo());
        orderChangeLog.setName(OrderChangeTypeEnum.OBSOLETE.name);
        orderChangeLog.setType(OrderChangeTypeEnum.OBSOLETE.code);
        orderChangeLog.setOldContent(oldState.toString());
        orderChangeLog.setNewContent(order.getState().toString());
        orderChangeLog.setReason(paramsDto.getReason());
        orderChangeLog.setState(CommonStateEnum.CHECKED.code);
        orderChangeLog.setCreateTime(System.currentTimeMillis());
        orderChangeLog.setCreateUser(paramsDto.getUserName());
        orderChangeLog.setCreateUserId(paramsDto.getUserId());
        orderChangeLogDao.insert(orderChangeLog);
        return BaseResultUtil.success();
    }
    @Override
    public ResultVo changePrice(ChangePriceOrderDto paramsDto) {
        //处理参数
        paramsDto.setWlTotalFee(paramsDto.getWlTotalFee() == null ? BigDecimal.ZERO : paramsDto.getWlTotalFee());
        paramsDto.setRealWlTotalFee(paramsDto.getRealWlTotalFee() == null ? BigDecimal.ZERO : paramsDto.getRealWlTotalFee());
        paramsDto.setCouponOffsetFee(paramsDto.getCouponOffsetFee() == null ? BigDecimal.ZERO : paramsDto.getCouponOffsetFee());
        //获取参数
        Long orderId = paramsDto.getOrderId();
        BigDecimal wlTotalFee = paramsDto.getWlTotalFee();
        BigDecimal couponOffsetFee = paramsDto.getCouponOffsetFee();
        BigDecimal realWlTotalFee = paramsDto.getRealWlTotalFee();


        Order order = orderDao.selectById(orderId);

        /**1、组装订单数据*/
        //查询客户类型
        Customer customer = customerService.selectById(order.getCustomerId());
        int customerType = CustomerTypeEnum.INDIVIDUAL.code;
        if(customer == null){
            customerType = customer.getType();
        }
        if(customerType == CustomerTypeEnum.COOPERATOR.code){
            order.setAgencyFee(wlTotalFee.subtract(realWlTotalFee).add(couponOffsetFee));
            //合伙人：收车后客户应支付平台的钱->总物流费
            order.setTotalFee(wlTotalFee);
        }else{
            //其他客户：收车后客户应支付平台的钱->总物流费-优惠券
            order.setTotalFee(wlTotalFee.subtract(couponOffsetFee));
        }
        order.setCreateTime(System.currentTimeMillis());

        //更新或插入订单
        orderDao.updateById(order);


        /**2、更新或保存车辆信息*/
        List<ChangePriceOrderCarDto> orderCarList = paramsDto.getOrderCarList();
        List<OrderCar> orderCarUpdatelist = new ArrayList<>();

        //费用统计变量
        int noCount = 1;
        BigDecimal totalAgencyFee = order.getAgencyFee();//均摊
        BigDecimal totalCouponOffsetFee = order.getCouponOffsetFee();//均摊
        BigDecimal totalPickFee = BigDecimal.ZERO;//求和
        BigDecimal totalTrunkFee = BigDecimal.ZERO;//求和
        BigDecimal totalBackFee = BigDecimal.ZERO;//求和
        BigDecimal totalInsuranceFee = BigDecimal.ZERO;//求和
        BigDecimal totalFee = BigDecimal.ZERO;//求和

        for (ChangePriceOrderCarDto dto : orderCarList) {
            if(dto == null){
                continue;
            }
            OrderCar orderCar = new OrderCar();
            //填充数据
            orderCar.setPickFee(dto.getPickFee() == null ? BigDecimal.ZERO : dto.getPickFee());
            orderCar.setTrunkFee(dto.getTrunkFee() == null ? BigDecimal.ZERO : dto.getTrunkFee());
            orderCar.setBackFee(dto.getBackFee() == null ? BigDecimal.ZERO : dto.getBackFee());
            orderCar.setInsuranceFee(dto.getInsuranceFee() == null ? BigDecimal.ZERO : dto.getInsuranceFee());
            orderCar.setTotalFee(dto.getPickFee()
                    .add(dto.getTrunkFee())
                    .add(dto.getBackFee())
                    .add(dto.getInsuranceFee()));

            orderCarUpdatelist.add(orderCar);

            //计算统计费用
            totalPickFee = totalPickFee.add(orderCar.getPickFee());
            totalTrunkFee = totalTrunkFee.add(orderCar.getPickFee());
            totalBackFee = totalBackFee.add(orderCar.getBackFee());
            totalInsuranceFee = totalInsuranceFee.add(orderCar.getInsuranceFee());
            totalCouponOffsetFee = totalCouponOffsetFee.add(orderCar.getCouponOffsetFee());
            //统计数量
            noCount++;

        }

        //均摊优惠券费用
        if(totalCouponOffsetFee.compareTo(BigDecimal.ZERO) > 0){
            BigDecimal[] couponOffsetFeeArray = totalCouponOffsetFee.divideAndRemainder(new BigDecimal(noCount));
            BigDecimal couponOffsetFeeAvg = couponOffsetFeeArray[0];
            BigDecimal couponOffsetFeeRemainder = couponOffsetFeeArray[1];
            for (OrderCar orderCar : orderCarUpdatelist) {
                if(couponOffsetFeeRemainder.compareTo(BigDecimal.ZERO) > 0){
                    orderCar.setCouponOffsetFee(couponOffsetFeeAvg.add(BigDecimal.ONE));
                    couponOffsetFeeRemainder = couponOffsetFeeRemainder.subtract(BigDecimal.ONE);
                }else{
                    orderCar.setCouponOffsetFee(couponOffsetFeeAvg);
                }

                orderCar.setTotalFee(orderCar.getPickFee()
                        .add(orderCar.getTrunkFee())
                        .add(orderCar.getBackFee())
                        .add(order.getInsuranceFee()));
                //不是合伙人，总物流费=总物流费-减去优惠券
                if(customerType != CustomerTypeEnum.COOPERATOR.code){
                    orderCar.setTotalFee(orderCar.getTotalFee()
                            .subtract(orderCar.getCouponOffsetFee()));
                }
            }

        }

        //均摊服务费用
        if(totalAgencyFee.compareTo(BigDecimal.ZERO) > 0 && customerType == CustomerTypeEnum.COOPERATOR.code){
            BigDecimal[] agencyFeeArray = totalAgencyFee.divideAndRemainder(new BigDecimal(noCount));
            BigDecimal agencyFeeAvg = agencyFeeArray[0];
            BigDecimal agencyFeeRemainder = agencyFeeArray[1];
            for (OrderCar orderCar : orderCarUpdatelist) {
                //合伙人计算均摊服务费
                if(agencyFeeRemainder.compareTo(BigDecimal.ZERO) > 0){
                    orderCar.setAgencyFee(agencyFeeAvg.add(BigDecimal.ONE));
                    agencyFeeRemainder = agencyFeeRemainder.subtract(BigDecimal.ONE);
                }else{
                    orderCar.setAgencyFee(agencyFeeAvg);
                }
                orderCar.setTotalFee(orderCar.getPickFee()
                        .add(orderCar.getTrunkFee())
                        .add(orderCar.getBackFee())
                        .add(order.getInsuranceFee())
                        .add(order.getAgencyFee()));
            }
        }
        for (OrderCar orderCar : orderCarUpdatelist) {
            orderCarDao.updateById(orderCar);
        }
        return BaseResultUtil.success();
    }

    @Override
    public ResultVo replenishInfo(ReplenishOrderDto paramsDto) {
        Order order = orderDao.selectById(paramsDto.getUserId());
        if(order == null || order.getState() > OrderStateEnum.CHECKED.code){
            return BaseResultUtil.fail("订单不允许修改");
        }
        List<ReplenishOrderCarDto> list = paramsDto.getOrderCarList();
        for (ReplenishOrderCarDto dto : list) {
            OrderCar orderCar = orderCarDao.selectById(dto.getId());
            if(orderCar == null || orderCar.getState() > OrderStateEnum.WAIT_CHECK.code){
                throw new ServerException("当前订单车辆不允许修改");
            }
            orderCar.setBrand(dto.getBrand());
            orderCar.setModel(dto.getModel());
            orderCar.setPlateNo(dto.getPlateNo());
            orderCar.setVin(dto.getVin());
            orderCarDao.updateById(orderCar);
        }

        return BaseResultUtil.success();
    }



}
