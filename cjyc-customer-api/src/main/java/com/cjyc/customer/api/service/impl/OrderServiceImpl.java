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

        int isSimple = orderDto.getIsSimple();
        int saveType = orderDto.getSaveType();


        Order order = new Order();
        BeanUtils.copyProperties(orderDto,order);
        order.setId(001121212335653L);
        order.setNo("555666");
        //简单
        if(isSimple == 1){

        //详单
        }else if(isSimple == 0){
            //草稿
            if(saveType==0){
                order.setState(0);//待提交

            //正式下单
            }else if(saveType==1){
                order.setState(1);//待分配
            }
        }
        int id = orderDao.add(order);

        return id > 0 ? true : false;
    }
}
