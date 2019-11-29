package com.cjyc.common.system.service.impl;

import com.cjyc.common.model.entity.OrderLog;
import com.cjyc.common.system.service.ICsOrderLogService;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class CsOrderLogServiceImpl implements ICsOrderLogService {
    @Async
    @Override
    public void asyncSave(OrderLog order) {


    }
}
