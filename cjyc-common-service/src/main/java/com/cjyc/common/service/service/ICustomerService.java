package com.cjyc.common.service.service;

import com.cjyc.common.model.entity.Customer;
import com.cjyc.common.model.entity.CustomerContact;
import com.cjyc.common.model.entity.CustomerLine;
import com.github.pagehelper.PageInfo;

/**
 * 客户信息service
 * @auther litan
 * @description: com.cjyc.common.service
 * @date:2019/9/28
 */
public interface ICustomerService {

    void addUser();

    PageInfo<CustomerLine> getLineHistoryPage(Long customerId, Integer pageNum, Integer pageSize);
}
