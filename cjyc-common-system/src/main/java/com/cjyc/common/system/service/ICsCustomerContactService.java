package com.cjyc.common.system.service;

import com.cjyc.common.model.entity.Order;

public interface ICsCustomerContactService {

    /**
     * 保存订单收车人和发车人
     * @author JPG
     * @since 2019/11/13 13:57
     * @param order
     */
    void saveByOrder(Order order);
}
