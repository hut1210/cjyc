package com.cjyc.customer.api.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cjyc.common.model.constant.NoConstant;
import com.cjyc.common.model.dao.IIncrementerDao;
import com.cjyc.common.model.dao.IOrderCarDao;
import com.cjyc.common.model.dao.IOrderDao;
import com.cjyc.common.model.dto.customer.invoice.InvoiceOrderQueryDto;
import com.cjyc.common.model.dto.customer.order.OrderQueryDto;
import com.cjyc.common.model.dto.customer.order.OrderUpdateDto;
import com.cjyc.common.model.entity.CarSeries;
import com.cjyc.common.model.entity.Order;
import com.cjyc.common.model.entity.OrderCar;
import com.cjyc.common.model.enums.order.OrderCarStateEnum;
import com.cjyc.common.model.enums.order.OrderStateEnum;
import com.cjyc.common.model.util.BasePageUtil;
import com.cjyc.common.model.util.BaseResultUtil;
import com.cjyc.common.model.util.LocalDateTimeUtil;
import com.cjyc.common.model.vo.PageVo;
import com.cjyc.common.model.vo.ResultVo;
import com.cjyc.common.model.vo.customer.invoice.InvoiceOrderVo;
import com.cjyc.common.model.vo.customer.order.OrderCarCenterVo;
import com.cjyc.common.model.vo.customer.order.OrderCenterDetailVo;
import com.cjyc.common.model.vo.customer.order.OrderCenterVo;
import com.cjyc.customer.api.dto.OrderCarDto;
import com.cjyc.customer.api.dto.OrderDto;
import com.cjyc.customer.api.service.IOrderService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import javax.validation.constraints.NotEmpty;
import java.util.*;

/**
 * @auther litan
 * @description: com.cjyc.customer.api.system.impl
 * @date:2019/10/8
 */
@Service
@Slf4j
public class OrderServiceImpl extends ServiceImpl<IOrderDao,Order> implements IOrderService{

    @Resource
    private IOrderDao orderDao;

    @Resource
    private IIncrementerDao incrementerDao;

    @Resource
    private IOrderCarDao orderCarDao;

    /**
     * 客户端下单
     * */
    @Override
    public boolean commitOrder(OrderDto orderDto) {

        int isSimple = orderDto.getIsSimple();
        int saveType = orderDto.getSaveType();

        Order order = new Order();
        BeanUtils.copyProperties(orderDto,order);

        //获取订单业务编号
        String orderNo = incrementerDao.getIncrementer(NoConstant.ORDER_PREFIX);
        order.setNo(orderNo);
        //简单
        if(isSimple == 1){

            //详单
        }else if(isSimple == 0){


            //草稿
            if(saveType==0){
                order.setState(0);//待提交
                //正式下单
            }else if(saveType==1){
                order.setState(1);//待分配
            }
        }

        order.setSource(1);
        order.setCarNum(orderDto.getOrderCarDtoList().size());
        order.setCreateTime(System.currentTimeMillis());
        order.setCreateUserName(orderDto.getCustomerName());
        //order.setCreateUserType(0);//创建人类型：0客户，1业务员
        order.setCreateUserId(orderDto.getCustomerId());



        return true;
    }

    /**
     * 客户端修改草稿订单
     * */
    @Override
    public boolean modify(OrderDto orderDto) {
        Order order = orderDao.selectById(orderDto.getOrderId());
        //根据状态判断 只能修改业务员还未确认的订单
        if(order.getState() < 15){
            BeanUtils.copyProperties(orderDto,order);

            //删除之前的
            QueryWrapper<OrderCar> wrapper = new QueryWrapper<>();
            wrapper.eq("order_id",orderDto.getOrderId());
            int delCount = orderCarDao.delete(wrapper);

            //重新保存车辆信息
            List<OrderCarDto> carDtoList =  orderDto.getOrderCarDtoList();
            if(delCount > 0){
                for(OrderCarDto orderCarDto : carDtoList){
                    OrderCar orderCar = new OrderCar();
                    BeanUtils.copyProperties(orderCarDto,orderCar);
                    String carNo = incrementerDao.getIncrementer(NoConstant.CAR_PREFIX);
                    orderCar.setWlPayState(0);
                    orderCar.setOrderNo(order.getNo());
                    orderCar.setOrderId(order.getId());
                    orderCar.setNo(carNo);

                    orderCarDao.insert(orderCar);
                }
            }
        }

        return false;
    }

    @Override
    public ResultVo<PageVo<OrderCenterVo>> getPage(OrderQueryDto dto) {
        BasePageUtil.initPage(dto);
        // 分页
        PageHelper.startPage(dto.getCurrentPage(), dto.getPageSize());
        // 日期格式处理
        String startDate = dto.getStartDate();
        String endDate = dto.getEndDate();
        if (!StringUtils.isBlank(startDate)) {
            Long startLong = LocalDateTimeUtil.convertToLong(startDate, "yyyy/MM/dd");
            dto.setStartDateMS(startLong);
        }
        if (!StringUtils.isBlank(endDate)) {
            Long endLong = LocalDateTimeUtil.convertToLong(endDate, "yyyy/MM/dd");
            dto.setEndDateMS(endLong);
        }

        List<OrderCenterVo> list = orderDao.selectPage(dto);
        return BaseResultUtil.success(new PageInfo<>(list == null ? new ArrayList<>(0) : list));
    }

    @Override
    public ResultVo<Map<String, Object>> getOrderCount(Long userId) {
        Map<String,Object> map = new HashMap<>(4);
        map.put("waitConfirm",0);
        map.put("inTransitCount",0);
        map.put("payCount",0);
        map.put("allCount",0);
        // 查询待确认订单数量
        LambdaQueryWrapper<Order> queryWrapper = new QueryWrapper<Order>().lambda()
                .eq(Order::getCustomerId,userId)
                .le(Order::getState, OrderStateEnum.CHECKED.code);
        Integer waitConfirmCount = orderDao.selectCount(queryWrapper);
        if (!Objects.isNull(waitConfirmCount)) {
            map.put("waitConfirmCount",waitConfirmCount);
        }

        // 查询运输中订单数量
        queryWrapper = new QueryWrapper<Order>().lambda()
                .eq(Order::getCustomerId,userId)
                .gt(Order::getState,OrderStateEnum.CHECKED.code)
                .lt(Order::getState,OrderStateEnum.FINISHED.code);
        Integer inTransitCount = orderDao.selectCount(queryWrapper);
        if (!Objects.isNull(inTransitCount)) {
            map.put("inTransitCount",inTransitCount);
        }

        // 查询已交付订单数量
        queryWrapper = new QueryWrapper<Order>().lambda()
                .eq(Order::getCustomerId,userId).eq(Order::getState,OrderStateEnum.FINISHED.code);
        Integer payCount = orderDao.selectCount(queryWrapper);
        if (!Objects.isNull(payCount)) {
            map.put("payCount",payCount);
        }

        // 查询所有订单数量
        queryWrapper = new QueryWrapper<Order>().lambda()
                .eq(Order::getCustomerId,userId)
                .le(Order::getState,OrderStateEnum.FINISHED.code).or().eq(Order::getState,OrderStateEnum.F_CANCEL.code);
        Integer allCount = orderDao.selectCount(queryWrapper);
        if (!Objects.isNull(allCount)) {
            map.put("allCount",allCount);
        }
        return BaseResultUtil.success(map);
    }

    @Override
    public ResultVo<OrderCenterDetailVo> getDetail(OrderUpdateDto dto) {
        OrderCenterDetailVo detailVo = new OrderCenterDetailVo();
        // 查询订单信息
        LambdaQueryWrapper<Order> queryOrderWrapper = new QueryWrapper<Order>().lambda()
                .eq(Order::getCustomerId,dto.getUserId()).eq(Order::getNo,dto.getOrderNo());
        Order order = super.getOne(queryOrderWrapper);
        BeanUtils.copyProperties(order,detailVo);
        // 查询车辆信息
        LambdaQueryWrapper<OrderCar> queryCarWrapper = new QueryWrapper<OrderCar>().lambda().eq(OrderCar::getOrderNo,dto.getOrderNo());
        List<OrderCar> orderCarList = orderCarDao.selectList(queryCarWrapper);
        List<OrderCarCenterVo> orderCarCenterVoList = new ArrayList<>(10);
        List<OrderCarCenterVo> orderCarFinishPayList = new ArrayList<>(10);
        if (!CollectionUtils.isEmpty(orderCarList)) {
            for (OrderCar orderCar : orderCarList) {
                OrderCarCenterVo orderCarCenter = new OrderCarCenterVo();
                BeanUtils.copyProperties(orderCar,orderCarCenter);
                if (OrderCarStateEnum.SIGNED.code == orderCar.getState()) {
                    // 运输中订单车辆信息
                    orderCarFinishPayList.add(orderCarCenter);
                } else {
                    // 待确认，已交付，运输中，全部订单车辆信息
                    orderCarCenterVoList.add(orderCarCenter);
                }
            }
        }
        detailVo.setOrderCarCenterVoList(orderCarCenterVoList);
        detailVo.setOrderCarFinishPayList(orderCarFinishPayList);
        return BaseResultUtil.success(detailVo);
    }

    @Override
    public ResultVo confirmPickCar(OrderUpdateDto dto) {
        // 修改车辆状态
        OrderCar orderCar = new OrderCar();
        orderCar.setState(OrderCarStateEnum.SIGNED.code);
        for (Long id : dto.getCarIdList()) {
            orderCar.setId(id);
            int i = orderCarDao.updateById(orderCar);
            if (i == 0) {
                return BaseResultUtil.fail();
            }
        }

        // 修改订单状态
        LambdaQueryWrapper<OrderCar> queryWrapper = new QueryWrapper<OrderCar>().lambda()
                .eq(OrderCar::getOrderNo, dto.getOrderNo()).ne(OrderCar::getState,OrderCarStateEnum.SIGNED.code);
        if (CollectionUtils.isEmpty(orderCarDao.selectList(queryWrapper))) {
            // 说明已经全部确认收车，更新总订单状态为已交付
            LambdaUpdateWrapper<Order> updateWrapper = new UpdateWrapper<Order>().lambda().set(Order::getState, OrderStateEnum.FINISHED.code)
                    .eq(Order::getNo, dto.getOrderNo()).eq(Order::getCustomerId, dto.getUserId());
            boolean result = super.update(updateWrapper);
            return result ? BaseResultUtil.success() : BaseResultUtil.fail();
        }
        return BaseResultUtil.success();
    }

    @Override
    public ResultVo getUnInvoiceOrderList(InvoiceOrderQueryDto dto) {
        BasePageUtil.initPage(dto);
        PageHelper.startPage(dto.getCurrentPage(),dto.getPageSize());
        List<InvoiceOrderVo> list = orderCarDao.selectUnInvoiceOrderList(dto.getUserId());
        PageInfo<InvoiceOrderVo> pageInfo = new PageInfo<>(list);
        return BaseResultUtil.success(pageInfo);
    }
}
