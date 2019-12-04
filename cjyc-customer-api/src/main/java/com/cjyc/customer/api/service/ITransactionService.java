package com.cjyc.customer.api.service;

import com.pingplusplus.model.Event;
import com.pingplusplus.model.Order;

/**
 * @Author:Hut
 * @Date:2019/11/20 16:40
 */
public interface ITransactionService {
    void saveTransactions(Object obj, String state);

    void cancelOrderRefund(String orderCode);

    void updateTransactions(Order object, Event event, String s);

    String getTradeBillByOrderNo(String orderCode);
}
