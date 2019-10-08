package com.cjyc.customer.api.service.impl;

import com.cjyc.customer.api.dto.OrderDto;
import com.cjyc.customer.api.service.IOrderService;
import org.springframework.stereotype.Service;

/**
 * @auther litan
 * @description: com.cjyc.customer.api.service.impl
 * @date:2019/10/8
 */
@Service
public class OrderServiceImpl implements IOrderService{
    @Override
    public boolean commitOrder(OrderDto orderDto) {
        return false;
    }
}
