package com.cjyc.common.system.service.impl;

import com.cjyc.common.model.dao.ICarStorageLogDao;
import com.cjyc.common.model.entity.CarStorageLog;
import com.cjyc.common.system.service.ICsStorageLogService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Collection;
import java.util.Set;

@Slf4j
@Service
public class CsStorageLogServiceImpl implements ICsStorageLogService {

    @Resource
    private ICarStorageLogDao carStorageLogDao;

    @Async
    @Override
    public void asyncSave(CarStorageLog carStorageLog) {
        try {
            carStorageLogDao.insert(carStorageLog);
        } catch (Exception e) {
            log.error("保存出入库日志异常！",e);
        }
    }

    @Async
    @Override
    public void asyncSaveBatch(Collection<CarStorageLog> storageLogSet) {
        try {
            storageLogSet.forEach(s -> carStorageLogDao.insert(s));
        } catch (Exception e) {
            log.error("批量保存出入库日志异常！",e);
        }
    }
}
