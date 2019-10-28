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
    CustomerPartner getPartnerByUserId(@Param("userId") Long userId);

}
