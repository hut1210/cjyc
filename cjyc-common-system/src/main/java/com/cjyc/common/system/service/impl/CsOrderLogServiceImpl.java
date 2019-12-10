package com.cjyc.common.system.service.impl;

import com.cjyc.common.model.dao.IOrderLogDao;
import com.cjyc.common.model.entity.Order;
import com.cjyc.common.model.entity.OrderLog;
import com.cjyc.common.model.entity.defined.UserInfo;
import com.cjyc.common.model.enums.log.OrderLogTypeEnum;
import com.cjyc.common.system.service.ICsOrderLogService;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class CsOrderLogServiceImpl implements ICsOrderLogService {
    @Resource
    private IOrderLogDao orderLogDao;

    @Async
    @Override
    public void asyncSave(OrderLog order) {


    }

    @Async
    @Override
    public void asyncSave(Order order, OrderLogTypeEnum type, Object[] objects, UserInfo userInfo) {
        OrderLog orderLog = new OrderLog();
        orderLog.setOrderId(order.getId());
        orderLog.setOrderNo(order.getNo());
        orderLog.setType(type.getCode());
        orderLog.setInnerLog(String.valueOf(objects[0]));
        orderLog.setOuterLog(String.valueOf(objects[1]));
        orderLog.setCreateTime(System.currentTimeMillis());
        if(userInfo != null){
            orderLog.setCreateUser(userInfo.getName());
            orderLog.setCreateUserId(userInfo.getId());
            orderLog.setCreateUserPhone(userInfo.getPhone());
        }
        orderLogDao.insert(orderLog);
    }
}
