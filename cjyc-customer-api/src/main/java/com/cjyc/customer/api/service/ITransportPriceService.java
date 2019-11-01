package com.cjyc.customer.api.service;


import com.cjyc.common.model.dto.customer.freightBill.TransportPriceDto;
import com.cjyc.common.model.vo.ResultVo;

/**
 *  @author: zj
 *  @Date: 2019/10/12 16:42
 *  @Description:价格相关查询
 */
public interface ITransportPriceService {

    /**
     * 根据起始城市编码和目的地城市编码获取班线价格
     * @param dto
     * @return
     */
    ResultVo getLinePriceByCode(TransportPriceDto dto);
}
