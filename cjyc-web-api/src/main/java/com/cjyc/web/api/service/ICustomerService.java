package com.cjyc.web.api.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.cjyc.common.model.dto.web.OperateDto;
import com.cjyc.common.model.dto.web.customer.*;
import com.cjyc.common.model.entity.CustomerContract;
import com.cjyc.common.model.vo.PageVo;
import com.cjyc.common.model.vo.ResultVo;
import com.cjyc.common.model.entity.Customer;
import com.cjyc.common.model.vo.web.coupon.CustomerCouponSendVo;
import com.cjyc.common.model.vo.web.customer.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 *  @author: zj
 *  @Date: 2019/9/29 15:01
 *  @Description: 用户接口
 */
public interface ICustomerService  extends IService<Customer> {

    /**
     * 根据手机号获取客户信息
     * @param dto
     * @return
     */
    ResultVo<CustomerInfoVo> findCustomerInfo(ExistCustomreDto dto);
    /**
     * 验证用户是否已存在
     * @param dto
     * @return
     */
    ResultVo existCustomer(ExistCustomreDto dto);

    /**
     *  新增移动端用户
     * @param dto
     * @return
     */
    ResultVo saveCustomer(CustomerDto dto);

    /**
     * 更新移动端用户
     * @param dto
     * @return
     */
    ResultVo modifyCustomer(CustomerDto dto);

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
    ResultVo saveOrModifyKey(KeyCustomerDto dto);

    /**
     *  审核/删除大客户
     * @param  dto
     * @return
     */
    ResultVo verifyCustomer(OperateDto dto);

    /**
     * 根据大客户id查看大客户&合同
     * @param customerId
     * @return
     */
    ResultVo<ShowKeyCustomerVo> showKeyCustomer(Long customerId);

    /**
     * 根据条件查询大客户信息
     * @param dto
     * @return
     */
    ResultVo<PageVo<ListKeyCustomerVo>> findKeyCustomer(SelectKeyCustomerDto dto);

    @Override
    boolean save(Customer customer);

    Customer selectById(Long customerId);

    /**
     * 新增合伙人
     * @param dto
     * @return
     */
    ResultVo saveOrModifyPartner(PartnerDto dto);

    /**
     * 根据条件分页查看合伙人
     * @param dto
     * @return
     */
    ResultVo<PageVo<CustomerPartnerVo>> findPartner(CustomerPartnerDto dto);

    /**
     * 根据客户id查询合伙人
     * @param customerId
     * @return
     */
    ResultVo<ShowPartnerVo> showPartner(Long customerId);

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
     * 通过大客户customerId获取有效期合同
     * @param customerId
     * @return
     */
    ResultVo getContractByCustomerId(Long customerId);

    /**
     * 查看客户优惠券
     * @param dto
     * @return
     */
    ResultVo customerCoupon(CustomerCouponDto dto);

    /**
     * 根据customerId查询该用户的优惠券
     * @param customerId
     * @return
     */
    ResultVo<List<CustomerCouponSendVo>> getCouponByCustomerId(Long customerId);

    /**
     * 导出C端客户信息
     * @param request
     * @param response
     */
    void exportCustomerExcel(HttpServletRequest request, HttpServletResponse response);

    /**
     * 导出大客户信息
     * @param request
     * @param response
     */
    void exportKeyExcel(HttpServletRequest request, HttpServletResponse response);

    /**
     * 导出合伙人信息
     * @param request
     * @param response
     */
    void exportPartnerExcel(HttpServletRequest request, HttpServletResponse response);

    /************************************韵车集成改版 st***********************************/

    /**
     *  新增移动端用户
     * @param dto
     * @return
     */
    ResultVo saveCustomerNew(CustomerDto dto);

    /**
     * 更新移动端用户
     * @param dto
     * @return
     */
    ResultVo modifyCustomerNew(CustomerDto dto);

    /**
     * 新增修改大客户&合同
     * @param dto
     * @return
     */
    ResultVo saveOrModifyKeyNew(KeyCustomerDto dto);

    /**
     * 新增修改合伙人
     * @param dto
     * @return
     */
    ResultVo saveOrModifyPartnerNew(PartnerDto dto);

    /**
     * 根据条件查询C端客户
     * @param dto
     * @return
     */
    ResultVo<PageVo<CustomerVo>> findCustomerNew(SelectCustomerDto dto);

    /**
     * 根据条件查询大客户
     * @param dto
     * @return
     */
    ResultVo<PageVo<ListKeyCustomerVo>> findKeyCustomerNew(SelectKeyCustomerDto dto);

    /**
     * 根据条件查询合伙人
     * @param dto
     * @return
     */
    ResultVo<PageVo<CustomerPartnerVo>> findPartnerNew(CustomerPartnerDto dto);

    /**
     * 审核/冻结/解冻客户
     * @param dto
     * @return
     */
    ResultVo verifyCustomerNew(OperateDto dto);

    /**
     * 根据输入手机号/用户名称模糊查询用户信息
     * @param keyword
     * @return
     */
    ResultVo findCustomerByKey(String keyword);

    /**
     * C端客户导入Excel文件
     * @param file
     * @return
     */
    boolean importCustomerExcel(MultipartFile file, Long loginId);

    /**
     * 大客户导入Excel文件
     * @param file
     * @return
     */
    boolean importKeyExcel(MultipartFile file, Long loginId);

    /**
     * 合伙人导入Excel文件
     * @param file
     * @return
     */
    boolean importPartnerExcel(MultipartFile file, Long loginId);

    /**
     * 银行信息导入Excel文件
     * @param file
     * @return
     */
    boolean importBankInfoExcel(MultipartFile file);

    /**
     * 根据合同id获取合同信息
     * @param contractId
     * @return
     */
    ResultVo<CustomerContract> findContract(Long contractId);
}
