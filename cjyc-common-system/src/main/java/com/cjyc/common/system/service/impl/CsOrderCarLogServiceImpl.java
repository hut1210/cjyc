package com.cjyc.common.system.service.impl;

import com.cjyc.common.model.dao.IOrderCarLogDao;
import com.cjyc.common.model.entity.OrderCar;
import com.cjyc.common.model.entity.OrderCarLog;
import com.cjyc.common.model.entity.defined.UserInfo;
import com.cjyc.common.model.enums.log.OrderCarLogEnum;
import com.cjyc.common.system.service.ICsOrderCarLogService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.List;


@Service
@Slf4j
public class CsOrderCarLogServiceImpl implements ICsOrderCarLogService {
    @Resource
    private IOrderCarLogDao orderCarLogDao;

    /**
     * 车辆日志
     *
     * @param orderCar    车辆
     * @param logTypeEnum 日志类型
     * @param log         日志内容
     * @param userInfo    用户信息
     * @author JPG
     * @since 2019/12/9 19:59
     */
    @Async
    @Override
    public void asyncSave(OrderCar orderCar, OrderCarLogEnum logTypeEnum, String[] log, UserInfo userInfo) {
        if(orderCar == null){
            return;
        }
        try {
            OrderCarLog orderCarLog = new OrderCarLog();
            orderCarLog.setOrderCarId(orderCar.getId());
            orderCarLog.setOrderCarNo(orderCar.getNo());
            orderCarLog.setType(logTypeEnum.getCode());
            orderCarLog.setInnerLog(String.valueOf(log[0]));
            orderCarLog.setOuterLog(String.valueOf(log[1]));
            orderCarLog.setCreateTime(System.currentTimeMillis());
            orderCarLog.setCreateUser(userInfo.getName());
            orderCarLog.setCreateUserType(userInfo.getUserType().code);
            orderCarLog.setCreateUserId(userInfo.getId());
            orderCarLog.setCreateUserPhone(userInfo.getPhone());
            orderCarLogDao.insert(orderCarLog);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Async
    @Override
    public void asyncSave(List<OrderCar> orderCarList, OrderCarLogEnum logTypeEnum, String[] log, Object[] args, UserInfo userInfo) {
        if(CollectionUtils.isEmpty(orderCarList)){
            return;
        }
        try {
            orderCarList.forEach(orderCar -> {
                asyncSave(orderCar, logTypeEnum, log, userInfo);
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
