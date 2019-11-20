package com.cjyc.common.system.service.impl;

import com.cjyc.common.model.dao.ICarrierDao;
import com.cjyc.common.model.entity.Carrier;
import com.cjyc.common.system.service.ICsCarrierService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class CsCarrierServiceImpl implements ICsCarrierService {
    @Resource
    private ICarrierDao carrierDao;

    @Override
    public List<Carrier> getBelongListByDriver(Long driverId) {
        return carrierDao.findBelongListByDriverId(driverId);
    }

    @Override
    public List<Long> getBelongIdsListByDriver(Long driverId) {
        return carrierDao.getBelongIdsListByDriver(driverId);
    }
}
