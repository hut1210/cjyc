package com.cjyc.common.model.dao;

import com.cjyc.common.model.entity.CustomerPartner;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author JPG
 * @since 2019-10-26
 */
public interface ICustomerPartnerDao extends BaseMapper<CustomerPartner> {

    /**
     * 根据合伙人userId获取合伙人附带信息
     * @param userId
     * @return
     */
    CustomerPartner getPartnerByUserId(@Param("loginId") Long userId);

    /**
     * 根据合伙人id删除合伙人附加信息
     * @param customerId
     * @return
     */
    int removeByCustomerId(@Param("customerId") Long customerId);

}
