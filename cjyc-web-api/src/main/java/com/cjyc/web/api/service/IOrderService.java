package com.cjyc.web.api.service;

import com.cjyc.web.api.dto.OrderDto;

/**
 * @auther litan
 * @description: com.cjyc.web.api.service
 * @date:2019/10/15
 */
public interface IOrderService {

    boolean commitOrder(OrderDto orderDto);
}
