package com.cjyc.common.model.dao;

import com.cjyc.common.model.entity.CustomerContract;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

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
     */
    int deleteContractByCustomerId(@Param("customerId") Long customerId);

}
