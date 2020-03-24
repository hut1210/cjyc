package com.cjyc.common.model.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cjyc.common.model.dto.salesman.customer.SalesCustomerDto;
import com.cjyc.common.model.dto.web.customer.CustomerPartnerDto;
import com.cjyc.common.model.dto.web.customer.CustomerfuzzyListDto;
import com.cjyc.common.model.dto.web.customer.SelectCustomerDto;
import com.cjyc.common.model.dto.web.customer.SelectKeyCustomerDto;
import com.cjyc.common.model.entity.Customer;
import com.cjyc.common.model.vo.salesman.customer.SalesCustomerVo;
import com.cjyc.common.model.vo.salesman.customer.SalesKeyCustomerVo;
import com.cjyc.common.model.vo.web.customer.*;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 客户表（登录用户端APP用户） Mapper 接口
 * </p>
 *
 * @author JPG
 * @since 2019-10-26
 */
public interface ICustomerDao extends BaseMapper<Customer> {

    /**
     * 根据用户userId查询用户
     * @param userId
     * @return
     */
    Customer getCustomerByUserId(@Param("loginId") Long userId);
    /**
     * 根据手机号，姓名，身份证号查询移动端用户
     * @param customerVo
     * @return
     */
    List<CustomerVo> findCustomer(SelectCustomerDto customerVo);

    /**
     * 查询所有大客户用户
     * @return
     */
    List<ListKeyCustomerVo> getAllKeyCustomter();

    /**
     * 根据条件查询大客户用户
     * @param keyCustomerVo
     * @return
     */
    List<ListKeyCustomerVo> findKeyCustomter(SelectKeyCustomerDto keyCustomerVo);

    /**
     * 根据条件查询合伙人
     * @param dto
     * @return
     */
    List<CustomerPartnerVo> getPartnerByTerm(CustomerPartnerDto dto);

    ShowPartnerVo showPartner(@Param("customerId") Long customerId);

    Customer findByPhone(@Param("phone") String phone);

    List<CustomerFuzzyListVo> findFuzzyList(@Param("paramsDto") CustomerfuzzyListDto paramsDto);

    /**
     * 根据用户名/手机号模糊匹配用户信息
     * @param keyword
     * @return
     */
    List<Map<String,Object>> getAllCustomerByKey(@Param("keyword") String keyword);

    /**
     * 根据大客户customerId查询有效期合同
     * @param customerId
     * @param now
     * @return
     */
    List<Map<String,Object>> getContractByCustomerId(@Param("customerId") Long customerId,@Param("now") Long now);

    Customer findByUserId(@Param("userId") Long userId);

    List<String> findAllNo();

    List<SalesCustomerVo> findCustomerPhoneName(SalesCustomerDto dto);

    List<SalesKeyCustomerVo> findSalesKeyCustomter(SalesCustomerDto dto);


    /************************************韵车集成改版 st***********************************/

    /**
     * 根据用户名/手机号模糊匹配用户信息
     * @param keyword
     * @return
     */
    List<Map<String,Object>> findCustomerByKey(@Param("keyword") String keyword);

    /**
     * 根据手机号，姓名，身份证号查询移动端用户
     * @param customerVo
     * @return
     */
    List<CustomerVo> findClientCustomer(SelectCustomerDto customerVo);

    /**
     * 根据条件查询大客户用户
     * @param keyCustomerVo
     * @return
     */
    List<ListKeyCustomerVo> findKeyAccountCustomter(SelectKeyCustomerDto keyCustomerVo);

    /**
     * 根据条件查询合伙人
     * @param dto
     * @return
     */
    List<CustomerPartnerVo> findCoPartner(CustomerPartnerDto dto);

    Customer findActive(Long id);

    /**
     * 根据条件查询合伙人
     * @param dto
     * @return
     */
    List<CustomerPartnerVo> findPartner(CustomerPartnerDto dto);

    /**
     * 查看升级合伙人信息
     * @param customerId
     * @return
     */
    ShowPartnerVo showUpPartner(@Param("customerId") Long customerId);

}
