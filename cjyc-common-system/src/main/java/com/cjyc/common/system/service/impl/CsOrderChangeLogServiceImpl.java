package com.cjyc.common.system.service.impl;

import com.cjkj.log.monitor.LogUtil;
import com.cjyc.common.model.dao.IOrderChangeLogDao;
import com.cjyc.common.model.entity.Order;
import com.cjyc.common.model.entity.OrderChangeLog;
import com.cjyc.common.model.entity.defined.UserInfo;
import com.cjyc.common.model.enums.CommonStateEnum;
import com.cjyc.common.model.enums.order.OrderChangeTypeEnum;
import com.cjyc.common.system.service.ICsOrderChangeLogService;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.text.MessageFormat;

@Service
public class CsOrderChangeLogServiceImpl implements ICsOrderChangeLogService {

    @Resource
    private IOrderChangeLogDao orderChangeLogDao;

    @Async
    @Override
    public void asyncSave(Order order, OrderChangeTypeEnum changeType, String[] content, UserInfo userInfo) {
        try {
            OrderChangeLog orderChangeLog = new OrderChangeLog();
            orderChangeLog.setOrderNo(order.getNo());
            orderChangeLog.setOrderId(order.getId());
            orderChangeLog.setName(changeType.name);
            orderChangeLog.setType(changeType.code);
            orderChangeLog.setOldContent(content[0]);
            orderChangeLog.setNewContent(content[1]);
            orderChangeLog.setReason(content[2]);
            orderChangeLog.setState(CommonStateEnum.CHECKED.code);
            orderChangeLog.setCreateTime(System.currentTimeMillis());
            orderChangeLog.setCreateUserId(userInfo.getId());
            orderChangeLog.setCreateUser(userInfo.getName());
            orderChangeLogDao.insert(orderChangeLog);
        } catch (NumberFormatException e) {
            LogUtil.error(MessageFormat.format("{0}{1},保存变更记录失败", changeType.name, order.getNo()));
        }
    }

}
