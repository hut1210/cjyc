package com.cjyc.web.api.service;

import com.alibaba.fastjson.JSONObject;
import com.cjyc.common.model.entity.Customer;
import com.cjyc.common.model.vo.CustomerVo;
import com.cjyc.common.model.vo.KeyCustomerDto;
import com.cjyc.common.model.vo.ResultVo;
import com.cjyc.web.api.dto.BasePageVo;
import com.cjyc.web.api.dto.CustomerDto;
import com.cjyc.web.api.dto.KeyCustomerVo;
import com.github.pagehelper.PageInfo;

/**
 *  @author: zj
 *  @Date: 2019/9/29 15:01
 *  @Description: 用户接口
 */
public interface ICustomerService  {

    /**
     *  新增移动端用户
     * @param customerDto
     * @return
     */
    boolean saveCustomer(CustomerDto customerDto);

    /**
     * 分页查询移动端用户
     * @param pageNo  当前页
     * @param pageSize 每页大小
     * @return
     */
    PageInfo<Customer> getAllCustomer(Integer pageNo, Integer pageSize);

    /**
     * 根据用户id查看移动端用户
     * @param id
     * @return
     */
    Customer showCustomerById(Long id);

    /**
     * 更新移动端用户
     * @param customerDto
     * @return
     */
    boolean updateCustomer(CustomerDto customerDto);

    /**
     * 根据id删除移动端用户
     * @param arrIds
     * @return
     */
    boolean deleteCustomer(Long[] arrIds);

    /**
     * 根据条件查询移动端用户
     * @param jsonObject
     * @return
     */
    PageInfo<CustomerVo> findCustomer(JSONObject jsonObject);

    /**
     * 新增大客户&合同
     * @param keyCustomerVo
     * @return
     */
    boolean saveKeyCustAndContract(KeyCustomerVo keyCustomerVo);

    /**
     * 根据ids删除大客户
     * @param arrIds
     * @return
     */
    boolean deleteKeyCustomer(Long[] arrIds);

    /**
     * 分页查看所有大客户
     * @param pageVo
     * @return
     */
    PageInfo<KeyCustomerDto> getAllKeyCustomer(BasePageVo pageVo);

}
