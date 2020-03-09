package com.cjyc.foreign.api.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cjyc.foreign.api.dao.ITestDao;
import com.cjyc.foreign.api.entity.Line;
import com.cjyc.foreign.api.service.ITestService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class TestServiceImpl extends ServiceImpl<ITestDao, Line> implements ITestService {
    @Resource
    private ITestDao testDao;
    @Override
    public Line getLineById(Long id) {
        return testDao.getLineById(id);
    }
}
