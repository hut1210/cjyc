package com.cjyc.common.system.service;

import com.cjkj.common.model.ResultData;
import com.cjyc.common.model.dto.salesman.customer.SalesCustomerDto;
import com.cjyc.common.model.entity.Customer;
import com.cjyc.common.model.vo.ResultVo;
import com.cjyc.common.model.vo.salesman.customer.SalesCustomerVo;

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

    /**
     * 将用户保存到物流平台
     * @param customer
     * @return
     */
    ResultData<Long> addCustomerToPlatform(Customer customer);

    /**
     * 修改账号信息到物流平台：
     *  修改物流平台账号信息：如果修改账号则将要修改的账号不能存在否则修改失败
     * @param customer
     * @param newPhone
     * @return
     */
    ResultData<Boolean> updateCustomerToPlatform(Customer customer, String newPhone);

    Customer getById(Long userId, boolean isSearchCache);

    Customer validate(Long loginId);

    ResultVo<SalesCustomerVo> findCustomer(SalesCustomerDto dto);
}
