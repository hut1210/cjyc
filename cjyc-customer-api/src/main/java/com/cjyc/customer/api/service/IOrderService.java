package com.cjyc.customer.api.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.cjyc.common.model.dto.customer.OrderConditionDto;
import com.cjyc.common.model.entity.Order;
import com.cjyc.common.model.vo.PageVo;
import com.cjyc.common.model.vo.ResultVo;
import com.cjyc.common.model.vo.customer.order.OrderCenterVo;
import com.cjyc.customer.api.dto.OrderDto;

import java.util.Map;

/**
 * @auther litan
 * @description: com.cjyc.customer.api.system
 * @date:2019/10/8
 */
public interface IOrderService extends IService<Order> {

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

    /**
     * 根据条件查询各种状态下的订单明细
     * @param dto
     * @return
     */
    ResultVo getDetail(OrderConditionDto dto);
}
