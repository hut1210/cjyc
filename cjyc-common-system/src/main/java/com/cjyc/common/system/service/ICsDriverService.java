package com.cjyc.common.system.service;

import com.cjyc.common.model.entity.Driver;

public interface ICsDriverService {
    Driver getById(Long userId);

    Driver getByUserId(Long userId);
}
