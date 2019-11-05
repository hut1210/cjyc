package com.cjyc.common.system.service;

import com.cjyc.common.model.entity.Customer;

/**
 * 客户公用接口
 * @author JPG
 */
public interface ICsCustomerService {

    /**
     * 根据userId查询客户
     * @author JPG
     * @since 2019/11/5 9:12
     * @param userId
     * @param isCache
     */
    Customer getByUserId(Long userId, boolean isCache);

    /**
     * 根据手机号查询客户
     * @author JPG
     * @since 2019/11/5 9:13
     * @param customerPhone
     * @param isCache
     */
    Customer getByPhone(String customerPhone, boolean isCache);

    /**
     * 保存客户
     * @author JPG
     * @since 2019/11/5 12:30
     * @param customer
     * @return Customer 返回userId
     */
    Customer save(Customer customer);
}
