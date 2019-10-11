package com.cjyc.web.api.service;

import com.cjyc.common.model.vo.web.CustomerVo;
import com.cjyc.common.model.vo.web.ListKeyCustomerVo;
import com.cjyc.common.model.vo.web.ShowKeyCustomerVo;
import com.cjyc.common.model.entity.Customer;
import com.cjyc.common.model.dto.BasePageDto;
import com.cjyc.common.model.dto.web.CustomerDto;
import com.cjyc.common.model.dto.web.KeyCustomerDto;
import com.cjyc.common.model.dto.web.SelectCustomerDto;
import com.cjyc.common.model.dto.web.SelectKeyCustomerDto;
import com.github.pagehelper.PageInfo;

import java.util.List;

/**
 *  @author: zj
 *  @Date: 2019/9/29 15:01
 *  @Description: 用户接口
 */
public interface ICustomerService  {

    /**
     *  新增移动端用户
     * @param customerVo
     * @return
     */
    boolean saveCustomer(CustomerDto customerVo);

    /**
     *  分页查询所有移动端用户
     * @param basePageDto
     * @return
     */
    PageInfo<Customer> getAllCustomer(BasePageDto basePageDto);

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
    boolean deleteCustomer(List<Long> arrIds);

    /**
     * 根据条件查询移动端用户
     * @param selectCustomerDto
     * @return
     */
    PageInfo<CustomerVo> findCustomer(SelectCustomerDto selectCustomerDto);

    /**
     * 新增大客户&合同
     * @param keyCustomerDto
     * @return
     */
    boolean saveKeyCustAndContract(KeyCustomerDto keyCustomerDto);

    /**
     * 根据ids删除大客户
     * @param arrIds
     * @return
     */
    boolean deleteKeyCustomer(List<Long> arrIds);

    /**
     * 分页查看所有大客户
     * @param pageVo
     * @return
     */
    PageInfo<ListKeyCustomerVo> getAllKeyCustomer(BasePageDto pageVo);

    /**
     * 根据大客户id查看大客户&合同
     * @param id
     * @return
     */
    ShowKeyCustomerVo showKeyCustomerById(Long id);

    /**
     * 更新大客户&合同
     * @param keyCustomerDto
     * @return
     */
    boolean updateKeyCustomer(KeyCustomerDto keyCustomerDto);

    /**
     * 根据条件查询大客户信息
     * @param selectKeyCustomerDto
     * @return
     */
    PageInfo<ListKeyCustomerVo> findKeyCustomer(SelectKeyCustomerDto selectKeyCustomerDto);

}
