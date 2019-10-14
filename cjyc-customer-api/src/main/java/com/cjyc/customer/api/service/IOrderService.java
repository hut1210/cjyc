package com.cjyc.customer.api.service;

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
    PageInfo<OrderCenterVo> getWaitConFirmOrders();
}
