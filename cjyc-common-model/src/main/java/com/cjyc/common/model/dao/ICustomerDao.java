package com.cjyc.common.model.dao;

import com.cjyc.common.model.entity.Customer;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.springframework.stereotype.Repository;

/**
 * <p>
 * 客户表（登录用户端APP用户） Mapper 接口
 * </p>
 *
 * @author JPG
 * @since 2019-09-29
 */
@Repository
public interface ICustomerDao extends BaseMapper<Customer> {

}
