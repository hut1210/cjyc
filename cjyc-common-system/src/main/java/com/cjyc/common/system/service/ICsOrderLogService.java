package com.cjyc.common.system.service;

import com.cjyc.common.model.entity.Order;
import com.cjyc.common.model.entity.defined.UserInfo;
import com.cjyc.common.model.enums.log.OrderLogEnum;

public interface ICsOrderLogService {
    void asyncSave(Order order, OrderLogEnum orderLogEnum, String[] logs, UserInfo userInfo);
}
