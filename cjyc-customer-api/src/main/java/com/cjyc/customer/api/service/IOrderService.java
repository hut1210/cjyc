package com.cjyc.customer.api.service;

import com.cjyc.common.model.dto.BasePageDto;
import com.cjyc.common.model.vo.customer.OrderCenterVo;
import com.cjyc.customer.api.dto.OrderDto;
import com.github.pagehelper.PageInfo;

/**
 * @auther litan
 * @description: com.cjyc.customer.api.service
 * @date:2019/10/8
 */
public interface IOrderService {
    boolean commitOrder(OrderDto orderDto);

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

}
