package com.cjyc.customer.api.service;

import com.cjyc.common.model.entity.TradeBill;
import com.pingplusplus.model.Event;
import com.pingplusplus.model.Order;

import java.math.BigDecimal;

/**
 * @Author:Hut
 * @Date:2019/11/20 16:40
 */
public interface ITransactionService {
    void saveTransactions(Object obj, String state);

    void cancelOrderRefund(String orderCode);

    void updateTransactions(Order object, Event event, String s);

    TradeBill getTradeBillByOrderNo(String orderNo);

    BigDecimal getAmountByOrderNo(String orderNo);
}
