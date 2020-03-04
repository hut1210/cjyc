package com.cjyc.common.system.service;

import com.cjyc.common.model.entity.Order;
import com.cjyc.common.model.entity.OrderChangeLog;
import com.cjyc.common.model.entity.defined.FullOrder;
import com.cjyc.common.model.entity.defined.UserInfo;
import com.cjyc.common.model.enums.order.OrderChangeTypeEnum;

public interface ICsOrderChangeLogService {

    /**
     * 保存操作记录
     * @author JPG
     * @since 2019/11/5 16:12
     * @param orderChangeLog
     */
    //void asyncSaveBatch(OrderChangeLog orderChangeLog);

    /**
     * 保存操作记录
     * @author JPG
     * @since 2019/11/5 16:28
     * @param order
     * @param changeType
     * @param content {旧内容，新内容，原因}
     * @param userInfo {创建人ID，创建人名称}
     */
    void asyncSave(Order order, OrderChangeTypeEnum changeType, String[] content, UserInfo userInfo);

    void asyncSaveForChangePrice(FullOrder oldOrder, FullOrder newOrder, String reason, UserInfo userInfo);
}
