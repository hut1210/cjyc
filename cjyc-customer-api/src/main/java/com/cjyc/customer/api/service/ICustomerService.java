package com.cjyc.customer.api.service;


import com.cjyc.customer.api.entity.Customer;
import com.github.pagehelper.PageInfo;

/**
 * Created by leo on 2019/7/23.
 */
public interface ICustomerService {

  void addUser();

  PageInfo<Customer> pageList(Integer pageNum, Integer pageSize);
}
