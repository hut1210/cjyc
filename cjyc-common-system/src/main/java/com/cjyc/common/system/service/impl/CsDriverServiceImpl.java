package com.cjyc.common.system.service.impl;

import com.cjyc.common.model.dao.IDriverDao;
import com.cjyc.common.model.entity.Driver;
import com.cjyc.common.system.service.ICsDriverService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class CsDriverServiceImpl implements ICsDriverService {
    @Resource
    private IDriverDao driverDao;
    @Override
    public Driver getById(Long userId) {
        return driverDao.selectById(userId);
    }

    @Override
    public Driver getByUserId(Long userId) {
        return driverDao.findByUserId(userId);
    }
}
