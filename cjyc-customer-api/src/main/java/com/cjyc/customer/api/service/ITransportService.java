package com.cjyc.customer.api.service;


import com.cjyc.common.model.dto.customer.freightBill.LineDto;
import com.cjyc.common.model.dto.customer.freightBill.TransportDto;
import com.cjyc.common.model.vo.ResultVo;

/**
 *  @author: zj
 *  @Date: 2019/10/12 16:42
 *  @Description:班线运输相关查询
 */
public interface ITransportService {

    /**
     * 根据起始城市编码和目的地城市编码获取班线价格
     * @param dto
     * @return
     */
    ResultVo linePriceByCode(TransportDto dto);
}
