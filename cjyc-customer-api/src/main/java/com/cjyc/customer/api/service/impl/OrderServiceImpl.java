package com.cjyc.customer.api.service.impl;

import com.cjyc.common.model.constant.NoConstant;
import com.cjyc.common.model.dao.IIncrementerDao;
import com.cjyc.common.model.dao.IOrderDao;
import com.cjyc.common.model.entity.Order;
import com.cjyc.common.model.vo.customer.OrderCenterVo;
import com.cjyc.customer.api.dto.OrderDto;
import com.cjyc.customer.api.service.IOrderService;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @auther litan
 * @description: com.cjyc.customer.api.service.impl
 * @date:2019/10/8
 */
@Service
@Slf4j
public class OrderServiceImpl implements IOrderService{

    @Autowired
    IOrderDao orderDao;
    @Autowired
    IIncrementerDao incrementerDao;

    @Override
    public boolean commitOrder(OrderDto orderDto) {

        int isSimple = orderDto.getIsSimple();
        int saveType = orderDto.getSaveType();

        Order order = new Order();
        BeanUtils.copyProperties(orderDto,order);

        //获取订单业务编号
        String orderNo = incrementerDao.getIncrementer(NoConstant.ORDER_PREFIX);
        order.setNo(orderNo);
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

    @Override
    public PageInfo<OrderCenterVo> getWaitConFirmOrders() {
        try{


        }catch (Exception e){
            log.info("获取待确认订单出现异常");
        }
        return null;
    }
}
