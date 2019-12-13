package com.cjyc.common.system.service.impl;

import com.cjyc.common.model.dao.IOrderChangeLogDao;
import com.cjyc.common.model.entity.Order;
import com.cjyc.common.model.entity.OrderChangeLog;
import com.cjyc.common.model.enums.CommonStateEnum;
import com.cjyc.common.model.enums.order.OrderChangeTypeEnum;
import com.cjyc.common.model.enums.order.OrderStateEnum;
import com.cjyc.common.system.service.ICsOrderChangeLogService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class CsOrderChangeLogServiceImpl implements ICsOrderChangeLogService {

    @Resource
    private IOrderChangeLogDao orderChangeLogDao;
    /**
     * 保存操作记录
     *
     * @param orderChangeLog
     * @author JPG
     * @since 2019/11/5 16:12
     */
    @Async
    @Override
    public int asyncSave(OrderChangeLog orderChangeLog) {
        return orderChangeLogDao.insert(orderChangeLog);
    }

    /**
     * 保存操作内容
     *
     * @param order
     * @param changeType
     * @param content
     * @param creator
     * @author JPG
     * @since 2019/11/5 16:18
     */
    @Async
    @Override
    public int asyncSave(Order order, OrderChangeTypeEnum changeType, Object[] content, Object[] creator) {
        OrderChangeLog orderChangeLog = new OrderChangeLog();
        orderChangeLog.setOrderNo(order.getNo());
        orderChangeLog.setOrderId(order.getId());
        orderChangeLog.setName(changeType.name);
        orderChangeLog.setType(changeType.code);
        orderChangeLog.setOldContent(content[0].toString());
        orderChangeLog.setNewContent(content[1].toString());
        orderChangeLog.setReason(content[2].toString());
        orderChangeLog.setState(CommonStateEnum.CHECKED.code);
        orderChangeLog.setCreateTime(System.currentTimeMillis());
        orderChangeLog.setCreateUserId(Long.valueOf(creator[0].toString()));
        orderChangeLog.setCreateUser(creator[1].toString());
        return orderChangeLogDao.insert(orderChangeLog);
    }

}
