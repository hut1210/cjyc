package com.cjyc.common.system.service;

import com.cjkj.common.model.ResultData;
import com.cjkj.usercenter.dto.common.SelectRoleResp;
import com.cjyc.common.model.dto.salesman.customer.SalesCustomerDto;
import com.cjyc.common.model.dto.salesman.mine.AppCustomerIdDto;
import com.cjyc.common.model.entity.Customer;
import com.cjyc.common.model.entity.Driver;
import com.cjyc.common.model.entity.Role;
import com.cjyc.common.model.vo.ResultVo;
import com.cjyc.common.model.vo.salesman.customer.SalesCustomerListVo;
import com.cjyc.common.model.vo.salesman.customer.SalesKeyCustomerListVo;
import com.cjyc.common.model.vo.salesman.mine.AppContractVo;

import java.util.List;

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

    ResultVo<SalesCustomerListVo> findCustomer(SalesCustomerDto dto);

    ResultVo<Long> findRoleId(List<SelectRoleResp> roleResps);

    ResultVo<SalesKeyCustomerListVo> findKeyCustomer(SalesCustomerDto dto);

    ResultVo<AppContractVo> findCustomerContract(AppCustomerIdDto dto);
    /************************************韵车集成改版 st***********************************/
    /**
     * 将用户添加到架构组中
     * @param phone
     * @param name
     * @param role
     * @return
     */
    ResultData<Long> addUserToPlatform(String phone, String name, Role role);

    /**
     * 更新用户信息到架构组
     * @param customer
     * @param driver
     * @param newPhone
     * @return
     */
    ResultData<Boolean> updateUserToPlatform(Customer customer, Driver driver, String newPhone,String newName);

    /**
     * 保存用户
     * @param customerPhone
     * @param customerName
     * @param loginId
     * @return
     */
    ResultVo<Customer> saveCustomer(String customerPhone,String customerName,Long loginId);

    boolean validateActive(Long id);

    ResultVo<Customer> validateAndGetActive(Long id);
}
