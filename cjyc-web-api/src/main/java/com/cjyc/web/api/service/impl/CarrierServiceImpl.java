package com.cjyc.web.api.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cjyc.common.model.dao.ICarrierDao;
import com.cjyc.common.model.entity.Carrier;
import com.cjyc.web.api.service.ICarrierService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

@Service
@Slf4j
@Transactional(propagation = Propagation.REQUIRED, rollbackFor = RuntimeException.class)
public class CarrierServiceImpl extends ServiceImpl<ICarrierDao, Carrier> implements ICarrierService {

    @Resource
    private ICarrierDao iCarrierDao;
}