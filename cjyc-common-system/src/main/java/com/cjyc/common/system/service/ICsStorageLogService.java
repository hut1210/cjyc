package com.cjyc.common.system.service;

import com.cjyc.common.model.entity.CarStorageLog;

import java.util.Set;

public interface ICsStorageLogService {
    void asyncSave(CarStorageLog carStorageLog);

    void asyncSaveBatch(Set<CarStorageLog> storageLogSet);
}
