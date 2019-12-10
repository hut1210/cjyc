package com.cjyc.common.system.service;

import com.cjyc.common.model.entity.Order;
import com.cjyc.common.model.entity.OrderLog;
import com.cjyc.common.model.entity.defined.UserInfo;
import com.cjyc.common.model.enums.log.OrderLogTypeEnum;

public interface ICsOrderLogService {
    void asyncSave(OrderLog order);

    void asyncSave(Order order, OrderLogTypeEnum reject, Object[] objects, UserInfo userInfo);
}
