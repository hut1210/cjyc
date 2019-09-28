package com.cjyc.common.service;

import com.cjyc.common.entity.OperationLog;

/**
 * Created by leo on 2019/7/26.
 */
public interface ILogService {

    void recordLog(OperationLog log);

    void updLog(String logId, Long time);
}
