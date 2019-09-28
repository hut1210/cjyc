package com.cjyc.common.service;

import com.cjyc.common.entity.Customer;
import com.github.pagehelper.PageInfo;

/**
 * @auther litan
 * @description: com.cjyc.common.service
 * @date:2019/9/28
 */
public interface ICustomerService {

    void addUser();

    PageInfo<Customer> pageList(Integer pageNum, Integer pageSize);
}
