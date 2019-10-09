package com.cjyc.common.service.service;

import com.cjyc.common.model.entity.CustomerContact;
import com.github.pagehelper.PageInfo;

/**
 * @auther litan
 * @description: com.cjyc.common.service.service
 * @date:2019/10/9
 */
public interface ICustomerContactService {
    PageInfo<CustomerContact> getContactPage(Long customerId, Integer pageNum, Integer pageSize);
}
