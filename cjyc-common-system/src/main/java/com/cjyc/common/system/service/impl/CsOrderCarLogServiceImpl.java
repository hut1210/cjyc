package com.cjyc.common.system.service.impl;

import com.cjyc.common.model.dao.IOrderCarLogDao;
import com.cjyc.common.model.entity.OrderCar;
import com.cjyc.common.model.entity.OrderCarLog;
import com.cjyc.common.model.entity.defined.UserInfo;
import com.cjyc.common.model.enums.log.OrderCarLogEnum;
import com.cjyc.common.model.enums.log.OrderLogTypeEnum;
import com.cjyc.common.system.service.ICsOrderCarLogService;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.List;

@Service
public class CsOrderCarLogServiceImpl implements ICsOrderCarLogService {
    @Resource
    private IOrderCarLogDao orderCarLogDao;

    @Async
    @Override
    public void asyncSave(List<OrderCar> orderCarList, OrderCarLogEnum type, Object[] objects, UserInfo userInfo) {
        if(CollectionUtils.isEmpty(orderCarList)){
            return;
        }
        orderCarList.forEach(orderCar -> {
            OrderCarLog orderCarLog = new OrderCarLog();
            orderCarLog.setOrderCarId(orderCar.getId());
            orderCarLog.setOrderCarNo(orderCar.getNo());
            orderCarLog.setType(type.getCode());
            orderCarLog.setInnerLog(String.valueOf(objects[0]));
            orderCarLog.setOuterLog(String.valueOf(objects[1]));
            orderCarLog.setCreateTime(System.currentTimeMillis());
            orderCarLog.setCreateUser(userInfo.getName());
            orderCarLog.setCreateUserType(userInfo.getUserType().code);
            orderCarLog.setCreateUserId(userInfo.getId());
            orderCarLog.setCreateUserPhone(userInfo.getPhone());
            orderCarLogDao.insert(orderCarLog);
        });
    }
}
