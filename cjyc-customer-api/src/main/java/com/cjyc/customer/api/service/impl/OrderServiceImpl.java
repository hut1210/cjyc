package com.cjyc.customer.api.service.impl;

import com.cjyc.common.model.dao.IOrderDao;
import com.cjyc.common.model.entity.Order;
import com.cjyc.customer.api.dto.OrderDto;
import com.cjyc.customer.api.service.IOrderService;
import org.springframework.beans.BeanUtils;
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
        BeanUtils.copyProperties(orderDto,order);
        order.setId(001121212335653L);
        order.setNo("555666");
        int id = orderDao.add(order);
        System.out.print("adsf----->"+id);
        return false;
    }
}
