package com.cjyc.common.system.service;

import com.cjkj.common.model.ResultData;
import com.cjkj.usercenter.dto.yc.AddDeptAndUserResp;
import com.cjyc.common.model.dto.web.carrier.CarrierDto;
import com.cjyc.common.model.entity.Carrier;

import java.util.List;

public interface ICsCarrierService {

    /**
     * 将承运商相关信息保存到物流平台
     * @param carrier 承运商信息
     * @return
     */
    ResultData<AddDeptAndUserResp> saveCarrierToPlatform(Carrier carrier);

    List<Carrier> getBelongListByDriver(Long driverId);

    List<Long> getBelongIdsListByDriver(Long driverId);
    /**
     * 更新承运商信息
     * @param carrier
     * @param dto
     * @return
     */
    ResultData<Long> updateCarrierToPlatform(Carrier carrier, CarrierDto dto);
}
