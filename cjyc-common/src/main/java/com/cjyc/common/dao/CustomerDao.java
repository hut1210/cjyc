package com.cjyc.common.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cjyc.common.entity.Customer;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 * @auther litan
 * @description: com.cjyc.common.dao
 * @date:2019/9/28
 */
@Repository
@Mapper
public interface CustomerDao extends BaseMapper<Customer> {
    Customer selectByPhone(String phone);
}
