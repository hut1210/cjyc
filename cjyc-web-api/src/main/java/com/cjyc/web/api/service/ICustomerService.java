package com.cjyc.web.api.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.cjyc.common.model.dto.web.OperateDto;
import com.cjyc.common.model.dto.web.customer.*;
import com.cjyc.common.model.vo.ResultVo;
import com.cjyc.common.model.entity.Customer;

/**
 *  @author: zj
 *  @Date: 2019/9/29 15:01
 *  @Description: 用户接口
 */
public interface ICustomerService  extends IService<Customer> {

    /**
     *  新增移动端用户
     * @param dto
     * @return
     */
    boolean saveCustomer(CustomerDto dto);

    /**
     * 更新移动端用户
     * @param dto
     * @return
     */
    boolean modifyCustomer(CustomerDto dto);

    /**
     * 根据条件查询移动端用户
     * @param dto
     * @return
     */
    ResultVo findCustomer(SelectCustomerDto dto);

    /**
     * 新增大客户&合同
     * @param dto
     * @return
     */
    boolean saveKeyCustomer(KeyCustomerDto dto);

    /**
     *  审核/删除大客户
     * @param  dto
     * @return
     */
    boolean verifyCustomer(OperateDto dto);

    /**
     * 根据大客户id查看大客户&合同
     * @param id
     * @return
     */
    ResultVo showKeyCustomer(Long id);

    /**
     * 更新大客户&合同
     * @param keyCustomerDto
     * @return
     */
    boolean modifyKeyCustomer(KeyCustomerDto keyCustomerDto);

    /**
     * 根据条件查询大客户信息
     * @param dto
     * @return
     */
    ResultVo findKeyCustomer(SelectKeyCustomerDto dto);

    @Override
    boolean save(Customer customer);

    Customer selectById(Long customerId);

    /**
     * 新增合伙人
     * @param dto
     * @return
     */
    ResultVo savePartner(PartnerDto dto);

    /**
     * 更新合伙人
     * @param dto
     * @return
     */
    ResultVo modifyPartner(PartnerDto dto);
    /**
     * 根据条件分页查看合伙人
     * @param dto
     * @return
     */
    ResultVo findPartner(CustomerPartnerDto dto);

    Customer selectByPhone(String customerPhone);

    @Override
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
     * 根据userId查询该用户的优惠券
     * @param userId
     * @return
     */
    ResultVo getCouponByUserId(Long userId);
}
