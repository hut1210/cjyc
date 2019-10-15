package com.cjyc.web.api.service.impl;

import com.cjyc.common.model.constant.NoConstant;
import com.cjyc.common.model.dao.IIncrementerDao;
import com.cjyc.common.model.dao.IOrderCarDao;
import com.cjyc.common.model.dao.IOrderDao;
import com.cjyc.common.model.entity.Order;
import com.cjyc.common.model.entity.OrderCar;
import com.cjyc.web.api.dto.OrderCarDto;
import com.cjyc.web.api.dto.OrderDto;
import com.cjyc.web.api.service.IOrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @auther litan
 * @description: com.cjyc.web.api.service.impl
 * @date:2019/10/15
 */
@Service
@Slf4j
public class OrderSerivceImpl implements IOrderService{
    @Autowired
    IOrderDao orderDao;

    @Autowired
    IIncrementerDao incrementerDao;

    @Resource
    IOrderCarDao iOrderCarDao;

    @Override
    public boolean commitOrder(OrderDto orderDto) {
        int saveType = orderDto.getSaveType();

        Order order = new Order();
        BeanUtils.copyProperties(orderDto,order);

        //获取订单业务编号
        String orderNo = incrementerDao.getIncrementer(NoConstant.ORDER_PREFIX);
        order.setNo(orderNo);

        //草稿
        if(saveType==0){
            order.setState(0);//待提交
            //正式下单
        }else if(saveType==1){
            order.setState(1);//待分配
        }

        order.setSource(1);
        order.setCarNum(orderDto.getOrderCarDtoList().size());
        order.setCreateTime(System.currentTimeMillis());
        order.setCreateUserName(orderDto.getCustomerName());
        order.setCreateUserType(0);
        int count = orderDao.addOrder(order);

        //保存车辆信息
        List<OrderCarDto> carDtoList =  orderDto.getOrderCarDtoList();
        if(count > 0){

            for(OrderCarDto orderCarDto : carDtoList){

                OrderCar orderCar = new OrderCar();
                BeanUtils.copyProperties(orderCarDto,orderCar);
                String carNo = incrementerDao.getIncrementer(NoConstant.CAR_PREFIX);
                orderCar.setOrderNo(orderNo);
                orderCar.setOrderId(order.getId());
                orderCar.setNo(carNo);
                iOrderCarDao.insert(orderCar);
            }
        }

        return count > 0 ? true : false;
    }
}
