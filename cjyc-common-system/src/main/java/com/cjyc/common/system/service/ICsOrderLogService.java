package com.cjyc.common.system.service;

import com.cjyc.common.model.entity.OrderLog;

public interface ICsOrderLogService {
    void asyncSave(OrderLog order);
}
