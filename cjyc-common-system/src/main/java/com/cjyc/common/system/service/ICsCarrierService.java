package com.cjyc.common.system.service;

import com.cjyc.common.model.entity.Carrier;

import java.util.List;

public interface ICsCarrierService {
    List<Carrier> getBelongListByDriver(Long driverId);

    List<Long> getBelongIdsListByDriver(Long driverId);
}
