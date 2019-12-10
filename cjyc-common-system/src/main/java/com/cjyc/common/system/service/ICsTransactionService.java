package com.cjyc.common.system.service;

import com.Pingxx.model.Order;
import com.cjyc.common.model.entity.TradeBill;
import com.pingplusplus.model.Event;

import java.math.BigDecimal;

/**
 * @Author:Hut
 * @Date:2019/11/20 16:40
 */
public interface ICsTransactionService {
    void saveTransactions(Object obj, String state);

    void cancelOrderRefund(String orderCode);

    void updateTransactions(Order object, Event event, String s);

    TradeBill getTradeBillByOrderNo(String orderNo);

    BigDecimal getAmountByOrderNo(String orderNo);

    BigDecimal getAmountByOrderCarIds(String orderCarIds);

    int save(Object obj);
}
