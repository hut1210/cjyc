package com.cjyc.customer.api.service;

import com.cjyc.common.model.dto.BasePageDto;
import com.cjyc.common.model.dto.customer.OrderConditionDto;
import com.cjyc.common.model.vo.PageVo;
import com.cjyc.common.model.vo.ResultVo;
import com.cjyc.common.model.vo.customer.OrderCenterVo;
import com.cjyc.common.model.vo.customer.OrderDetailVo;
import com.cjyc.customer.api.dto.OrderDto;
import com.github.pagehelper.PageInfo;

import java.util.Map;

/**
 * @auther litan
 * @description: com.cjyc.customer.api.system
 * @date:2019/10/8
 */
public interface IOrderService {

    /**
     * 客户下单
     * @return
     */
    boolean commitOrder(OrderDto orderDto);

    /**
     * 客户修改订单
     * @return
     */
    boolean modify(OrderDto orderDto);

    /**
     * 获取待确认订单
     * @return
     */
    PageInfo<OrderCenterVo> getWaitConFirmOrders(BasePageDto dto);

    /**
     * 获取运输中订单
     * @param dto
     * @return
     */
    PageInfo<OrderCenterVo> getTransOrders(BasePageDto dto);

    /**
     * 获取已支付订单
     * @param dto
     * @return
     */
    PageInfo<OrderCenterVo> getPaidOrders(BasePageDto dto);

    /**
     * 获取所有订单
     * @param dto
     * @return
     */
    PageInfo<OrderCenterVo> getAllOrders(BasePageDto dto);

    /**
     * 根据订单编号查看订单详情
     * @param orderNo
     * @return
     */
    OrderDetailVo getOrderDetailByNo(String orderNo);

    /**
     * 根据条件进行筛选查询待确认订单列表
     * @param dto
     * @return
     */
    PageInfo<OrderCenterVo> getConFirmOrdsByTerm(OrderConditionDto dto);

    /**
     * 根据条件进行查询运输中订单列表
     * @param dto
     * @return
     */
    PageInfo<OrderCenterVo> getTransOrdsByTerm(OrderConditionDto dto);

    /**
     * 根据条件进行查询已支付订单列表
     * @param dto
     * @return
     */
    PageInfo<OrderCenterVo> getPaidOrdsByTerm(OrderConditionDto dto);

    /**
     * 根据条件进行查询全部订单列表
     * @param dto
     * @return
     */
    PageInfo<OrderCenterVo> getAllOrdsByTerm(OrderConditionDto dto);

    /**
     * 根据条件查询订单信息
     * @param dto
     * @return
     */
    ResultVo<PageVo<OrderCenterVo>> getPage(OrderConditionDto dto);

    /**
     * 根据客户id查询订单数量
     * @param customerId
     * @return
     */
    ResultVo<Map<String, Object>> getOrderCount(Long customerId);
}
