package com.cjyc.common.system.service.impl;

import com.alibaba.fastjson.JSON;
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
    public void asyncSave(OrderChangeLog orderChangeLog) {
        orderChangeLogDao.insert(orderChangeLog);
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
    public void asyncSave(Order order, OrderChangeTypeEnum changeType, Object[] content, Object[] creator) {
        OrderChangeLog orderChangeLog = new OrderChangeLog();
        orderChangeLog.setOrderNo(order.getNo());
        orderChangeLog.setOrderId(order.getId());
        orderChangeLog.setName(changeType.name);
        orderChangeLog.setType(changeType.code);
        orderChangeLog.setOldContent(JSON.toJSONString(content[0]));
        orderChangeLog.setNewContent(JSON.toJSONString(content[1]));
        orderChangeLog.setReason(String.valueOf(content[2]));
        orderChangeLog.setState(CommonStateEnum.CHECKED.code);
        orderChangeLog.setCreateTime(System.currentTimeMillis());
        orderChangeLog.setCreateUserId(Long.valueOf(String.valueOf(creator[0])));
        orderChangeLog.setCreateUser(String.valueOf(creator[1]));
        orderChangeLogDao.insert(orderChangeLog);
    }

}
