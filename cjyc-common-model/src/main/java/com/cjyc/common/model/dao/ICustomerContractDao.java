package com.cjyc.common.model.dao;

import com.cjyc.common.model.vo.web.customer.CustomerContractVo;
import com.cjyc.common.model.entity.CustomerContract;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 客户（企业）合同表 Mapper 接口
 * </p>
 *
 * @author JPG
 * @since 2019-09-29
 */
public interface ICustomerContractDao extends BaseMapper<CustomerContract> {

    /**
     * 根据大客户customerId删除合同
     * @param customerId
     * @return
     */
    int removeKeyContract(@Param("customerId") Long customerId);

    /**
     * 根据大客户customerUserId查询合同
     * @param customerId
     * @return
     */
    List<CustomerContractVo> getCustContractByCustId(@Param("customerId") Long customerId);

}
