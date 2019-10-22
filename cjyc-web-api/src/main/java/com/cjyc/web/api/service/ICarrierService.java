package com.cjyc.web.api.service;

import com.cjyc.common.model.dto.web.carrier.CarrierDto;

/**
 *  @author: zj
 *  @Date: 2019/10/18 15:22
 *  @Description:承运商管理接口
 */
public interface ICarrierService {

    /**
     * 添加承运商
     * @param dto
     * @return
     */
    boolean saveCarrier(CarrierDto dto);
}
