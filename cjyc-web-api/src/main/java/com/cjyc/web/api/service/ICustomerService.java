package com.cjyc.web.api.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.cjyc.common.model.dto.web.customer.*;
import com.cjyc.common.model.vo.ResultVo;
import com.cjyc.common.model.vo.web.CustomerVo;
import com.cjyc.common.model.vo.web.ListKeyCustomerVo;
import com.cjyc.common.model.vo.web.ShowKeyCustomerVo;
import com.cjyc.common.model.entity.Customer;
import com.cjyc.common.model.dto.BasePageDto;
import com.github.pagehelper.PageInfo;

import java.util.List;

/**
 *  @author: zj
 *  @Date: 2019/9/29 15:01
 *  @Description: 用户接口
 */
public interface ICustomerService  extends IService<Customer> {

    /**
     *  新增移动端用户
     * @param customerDto
     * @return
     */
    boolean saveCustomer(CustomerDto customerDto);

    /**
     * 更新移动端用户
     * @param customerDto
     * @return
     */
    boolean modifyCustomer(CustomerDto customerDto);

    /**
     * 根据条件查询移动端用户
     * @param selectCustomerDto
     * @return
     */
    ResultVo findCustomerByTerm(SelectCustomerDto selectCustomerDto);

    /**
     * 新增大客户&合同
     * @param keyCustomerDto
     * @return
     */
    boolean saveKeyCustAndContract(KeyCustomerDto keyCustomerDto);

    /**
     * 根据ids删除大客户
     * @param ids
     * @return
     */
    boolean delKeyCustomerByIds(List<Long> ids);

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

    boolean save(Customer customer);

    Customer selectById(Long customerId);

    /**
     * 新增大客户
     * @param dto
     * @return
     */
    ResultVo addOrUpdatePartner(PartnerDto dto);

    /**
     * 审核/删除
     * @param id
     * @param flag
     * @return
     */
    ResultVo verifyOrDeletePartner(Long id,Integer flag);

    Customer selectByPhone(String customerPhone);

    boolean updateById(Customer customer);

    /**
     * 根据关键字(手机号/用户名称)模糊查询用户信息
     * @param keyword
     * @return
     */
    ResultVo getAllCustomerByKey(String keyword);

    /**
     * 通过大客户userId获取有效期合同
     * @param userId
     * @return
     */
    ResultVo getCustContractByUserId(Long userId);

    /**
     * 查看客户优惠券
     * @param dto
     * @return
     */
    ResultVo getCustomerCouponByTerm(CustomerCouponDto dto);

    /**
     * 根据手机号查询该用户的优惠券
     * @param userId
     * @return
     */
    ResultVo getCouponByUserId(Long userId);
}
