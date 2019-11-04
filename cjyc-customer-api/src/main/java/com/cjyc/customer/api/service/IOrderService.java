package com.cjyc.customer.api.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.cjyc.common.model.dto.customer.invoice.InvoiceApplyQueryDto;
import com.cjyc.common.model.dto.customer.order.OrderQueryDto;
import com.cjyc.common.model.dto.customer.order.OrderUpdateDto;
import com.cjyc.common.model.entity.Order;
import com.cjyc.common.model.vo.PageVo;
import com.cjyc.common.model.vo.ResultVo;
import com.cjyc.common.model.vo.customer.order.OrderCenterDetailVo;
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
    ResultVo<PageVo<OrderCenterVo>> getPage(OrderQueryDto dto);

    /**
     * 根据客户id查询订单数量
     * @param userId
     * @return
     */
    ResultVo<Map<String, Object>> getOrderCount(Long userId);

    /**
     * 根据条件查询各种状态下的订单明细
     * @param dto
     * @return
     */
    ResultVo<OrderCenterDetailVo> getDetail(OrderUpdateDto dto);

    /**
     * 确认收车
     * @param dto
     * @return
     */
    ResultVo confirmPickCar(OrderUpdateDto dto);

    /**
     * 查询为开发票订单列表
     * @param dto
     * @return
     */
    ResultVo getUnInvoiceOrderList(InvoiceApplyQueryDto dto);
}
