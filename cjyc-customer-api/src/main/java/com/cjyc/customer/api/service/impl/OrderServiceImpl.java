package com.cjyc.customer.api.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cjyc.common.model.dao.IOrderCarDao;
import com.cjyc.common.model.dao.IOrderDao;
import com.cjyc.common.model.dto.customer.invoice.InvoiceApplyQueryDto;
import com.cjyc.common.model.dto.customer.order.OrderQueryDto;
import com.cjyc.common.model.dto.customer.order.OrderUpdateDto;
import com.cjyc.common.model.dto.web.order.CommitOrderDto;
import com.cjyc.common.model.dto.web.order.SaveOrderDto;
import com.cjyc.common.model.entity.Order;
import com.cjyc.common.model.entity.OrderCar;
import com.cjyc.common.model.enums.order.OrderCarStateEnum;
import com.cjyc.common.model.enums.order.OrderStateEnum;
import com.cjyc.common.model.util.BaseResultUtil;
import com.cjyc.common.model.vo.PageVo;
import com.cjyc.common.model.vo.ResultVo;
import com.cjyc.common.model.vo.customer.invoice.InvoiceOrderVo;
import com.cjyc.common.model.vo.customer.order.OrderCarCenterVo;
import com.cjyc.common.model.vo.customer.order.OrderCenterDetailVo;
import com.cjyc.common.model.vo.customer.order.OrderCenterVo;
import com.cjyc.common.system.service.ICsOrderService;
import com.cjyc.customer.api.service.IOrderService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
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
    private ICsOrderService comOrderService;
    @Resource
    private IOrderCarDao orderCarDao;


    /**
     * 保存订单
     *
     * @param reqDto
     * @author JPG
     * @since 2019/11/5 8:39
     */
    @Override
    public ResultVo save(SaveOrderDto reqDto) {
        return comOrderService.save(reqDto, OrderStateEnum.WAIT_SUBMIT);
    }

    @Override
    public ResultVo submit(SaveOrderDto reqDto) {
        return comOrderService.save(reqDto, OrderStateEnum.SUBMITTED);
    }

    /**
     * 提交订单
     *
     * @param reqDto
     * @author JPG
     * @since 2019/11/5 8:46
     */
    @Override
    public ResultVo commit(CommitOrderDto reqDto) {
        return comOrderService.commit(reqDto);
    }

    @Override
    public ResultVo<PageVo<OrderCenterVo>> getPage(OrderQueryDto dto) {
        // 分页
        PageHelper.startPage(dto.getCurrentPage(), dto.getPageSize());
        List<OrderCenterVo> list = orderDao.selectPage(dto);
        PageInfo<OrderCenterVo> pageInfo = new PageInfo<>(list == null ? new ArrayList<>(0) : list);
        return BaseResultUtil.success(pageInfo);
    }

    @Override
    public ResultVo<Map<String, Object>> getOrderCount(Long loginId) {
        Map<String,Object> map = new HashMap<>(4);
        map.put("waitConfirm",0);
        map.put("inTransitCount",0);
        map.put("payCount",0);
        map.put("allCount",0);
        // 查询待确认订单数量
        LambdaQueryWrapper<Order> queryWrapper = new QueryWrapper<Order>().lambda()
                .eq(Order::getCustomerId,loginId)
                .le(Order::getState, OrderStateEnum.CHECKED.code);
        Integer waitConfirmCount = orderDao.selectCount(queryWrapper);
        if (!Objects.isNull(waitConfirmCount)) {
            map.put("waitConfirmCount",waitConfirmCount);
        }

        // 查询运输中订单数量
        queryWrapper = new QueryWrapper<Order>().lambda()
                .eq(Order::getCustomerId,loginId)
                .gt(Order::getState,OrderStateEnum.CHECKED.code)
                .lt(Order::getState,OrderStateEnum.FINISHED.code);
        Integer inTransitCount = orderDao.selectCount(queryWrapper);
        if (!Objects.isNull(inTransitCount)) {
            map.put("inTransitCount",inTransitCount);
        }

        // 查询已交付订单数量
        queryWrapper = new QueryWrapper<Order>().lambda()
                .eq(Order::getCustomerId,loginId).eq(Order::getState,OrderStateEnum.FINISHED.code);
        Integer payCount = orderDao.selectCount(queryWrapper);
        if (!Objects.isNull(payCount)) {
            map.put("payCount",payCount);
        }

        // 查询所有订单数量
        queryWrapper = new QueryWrapper<Order>().lambda()
                .eq(Order::getCustomerId,loginId)
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
                .eq(Order::getCustomerId,dto.getLoginId()).eq(Order::getNo,dto.getOrderNo());
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
                    .eq(Order::getNo, dto.getOrderNo()).eq(Order::getCustomerId, dto.getLoginId());
            boolean result = super.update(updateWrapper);
            return result ? BaseResultUtil.success() : BaseResultUtil.fail();
        }
        return BaseResultUtil.success();
    }

    @Override
    public ResultVo getUnInvoicePage(InvoiceApplyQueryDto dto) {
        PageHelper.startPage(dto.getCurrentPage(),dto.getPageSize());
        List<InvoiceOrderVo> list = orderCarDao.selectUnInvoiceOrderList(dto.getLoginId());
        PageInfo<InvoiceOrderVo> pageInfo = new PageInfo<>(list);
        return BaseResultUtil.success(pageInfo);
    }

    @Override
    public ResultVo getInvoiceApplyOrderPage(InvoiceApplyQueryDto dto) {
        PageHelper.startPage(dto.getCurrentPage(),dto.getPageSize());
        List<InvoiceOrderVo> list = orderCarDao.selectInvoiceOrderList(dto);
        PageInfo<InvoiceOrderVo> pageInfo = new PageInfo<>(list);
        return BaseResultUtil.success(pageInfo);
    }


}
