package com.cjyc.common.service.service.impl;

import com.cjyc.common.model.dao.IOperateLogDao;
import com.cjyc.common.model.entity.OperateLog;
import com.cjyc.common.service.service.ILogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by leo on 2019/7/26.
 */
@Service("logServiceImpl")
public class LogServiceImpl implements ILogService {

    @Autowired
    private IOperateLogDao operateLogDao;

    @Override
    public void recordLog(OperateLog log) {
        operateLogDao.insert(log);
    }

    @Override
    public void updLog(String logId, Long time) {
        OperateLog log = operateLogDao.selectById(logId);
        log.setDuration(time);
        operateLogDao.updateById(log);
    }
}
