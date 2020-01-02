package com.cjyc.common.system.service;

import com.cjyc.common.model.dto.customer.freightBill.TransportDto;
import com.cjyc.common.model.entity.Line;
import com.cjyc.common.model.vo.ResultVo;

import java.util.Map;

public interface ICsLineService {
    Line getLineByCity(String startCityCode, String endCityCode, boolean isSearchCache);

    /**
     * 根据起始城市编码和目的地城市编码获取班线价格
     * @param dto
     * @return
     */
    ResultVo<Map<String,Object>> linePriceByCode(TransportDto dto);
}
