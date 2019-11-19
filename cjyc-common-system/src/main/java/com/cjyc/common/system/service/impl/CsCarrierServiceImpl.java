package com.cjyc.common.system.service.impl;

import com.cjyc.common.model.dao.ICarrierDao;
import com.cjyc.common.model.entity.Carrier;
import com.cjyc.common.system.service.ICsCarrierService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class CsCarrierServiceImpl implements ICsCarrierService {
    @Resource
    private ICarrierDao carrierDao;

}
