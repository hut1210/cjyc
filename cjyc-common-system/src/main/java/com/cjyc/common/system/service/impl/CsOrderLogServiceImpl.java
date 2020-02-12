package com.cjyc.common.system.service.impl;

import com.cjkj.log.monitor.LogUtil;
import com.cjyc.common.model.dao.IOrderLogDao;
import com.cjyc.common.model.entity.Order;
import com.cjyc.common.model.entity.OrderLog;
import com.cjyc.common.model.entity.defined.UserInfo;
import com.cjyc.common.model.enums.log.OrderLogEnum;
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
    public void asyncSave(Order order, OrderLogEnum type, String[] logs, UserInfo userInfo) {
        try {
            OrderLog orderLog = new OrderLog();
            orderLog.setOrderId(order.getId());
            orderLog.setOrderNo(order.getNo());
            orderLog.setType(type.getCode());
            orderLog.setOuterLog(logs[0]);
            orderLog.setInnerLog(logs[1]);
            orderLog.setCreateTime(System.currentTimeMillis());
            orderLog.setCreateUser(userInfo.getName());
            orderLog.setCreateUserId(userInfo.getId());
            orderLog.setCreateUserPhone(userInfo.getPhone());
            orderLogDao.insert(orderLog);
        } catch (Exception e) {
            LogUtil.error("订单{}下单保存订单日志失败", order.getNo());
            LogUtil.error(e.getMessage(), e);
        }
    }
}
