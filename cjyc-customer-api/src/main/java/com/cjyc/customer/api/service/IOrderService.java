package com.cjyc.customer.api.service;

import com.cjyc.customer.api.dto.OrderDto;

/**
 * @auther litan
 * @description: com.cjyc.customer.api.service
 * @date:2019/10/8
 */
public interface IOrderService {
    boolean commitOrder(OrderDto orderDto);
}
