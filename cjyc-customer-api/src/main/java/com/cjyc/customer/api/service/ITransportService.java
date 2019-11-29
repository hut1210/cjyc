package com.cjyc.customer.api.service;

import com.cjyc.common.model.dto.customer.freightBill.TransportDto;
import com.cjyc.common.model.vo.ResultVo;
import com.cjyc.common.model.vo.customer.customerLine.StoreListVo;

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

    /**
     * 获取业务中心
     * @param
     * @return
     */
    ResultVo<StoreListVo> findStore();
}
