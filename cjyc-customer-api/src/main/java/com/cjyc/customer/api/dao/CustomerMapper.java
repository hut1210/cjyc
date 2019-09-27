package com.cjyc.customer.api.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cjyc.customer.api.entity.Customer;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;


/**
 * @author leo
 * @date 2019/7/23.
 */
@Repository
@Mapper
public interface CustomerMapper extends BaseMapper<Customer>{

    Customer selectByPhone(String phone);

}
