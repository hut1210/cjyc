package com.cjyc.customer.api.service.impl;

import com.cjyc.common.model.dao.IOrderDao;
import com.cjyc.common.model.entity.Order;
import com.cjyc.customer.api.dto.OrderDto;
import com.cjyc.customer.api.service.IOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @auther litan
 * @description: com.cjyc.customer.api.service.impl
 * @date:2019/10/8
 */
@Service
public class OrderServiceImpl implements IOrderService{

    @Autowired
    IOrderDao orderDao;
    @Override
    public boolean commitOrder(OrderDto orderDto) {
        Order order = new Order();
        order.setNo("555666");
        orderDao.insert(order);
        return false;
    }
}
