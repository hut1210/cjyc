package com.cjyc.customer.api.service;


import java.math.BigDecimal;

/**
 *  @author: zj
 *  @Date: 2019/10/12 16:42
 *  @Description:价格相关查询
 */
public interface IPriceQueryService {

    /**
     * 根据起始城市编码和目的地城市编码获取班线价格
     * @param fromCode
     * @param toCode
     * @return
     */
    String getLinePriceByCode(String fromCode,String toCode);
}
