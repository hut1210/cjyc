package com.cjyc.common.model.dao;

import com.cjyc.common.model.entity.CustomerContact;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.springframework.stereotype.Repository;

/**
 * <p>
 * 客户常用联系人表 Mapper 接口
 * </p>
 *
 * @author JPG
 * @since 2019-09-29
 */
@Repository
public interface ICustomerContactDao extends BaseMapper<CustomerContact> {

}
