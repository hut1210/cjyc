package com.cjyc.common.system.service;


import com.cjyc.common.model.entity.OperateLog;

/**
 * Created by leo on 2019/7/26.
 */
public interface ISystemLogService {

    void recordLog(OperateLog log);

    void updLog(String logId, Long time);
}
