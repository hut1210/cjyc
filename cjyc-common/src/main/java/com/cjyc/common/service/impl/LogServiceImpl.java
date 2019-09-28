package com.cjyc.common.service.impl;

import com.cjyc.common.dao.OperationLogDao;
import com.cjyc.common.entity.OperationLog;
import com.cjyc.common.service.ILogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by leo on 2019/7/26.
 */
@Service("logServiceImpl")
public class LogServiceImpl implements ILogService {

    @Autowired
    private OperationLogDao operationLogDao;

    @Override
    public void recordLog(OperationLog log) {
        operationLogDao.insert(log);
    }

    @Override
    public void updLog(String logId, Long time) {
        OperationLog log = operationLogDao.selectById(logId);
        log.setCostTime(time);
        operationLogDao.updateById(log);
    }
}
