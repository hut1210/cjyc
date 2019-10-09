package com.cjyc.common.model.dao;

import com.cjyc.common.model.entity.Customer;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cjyc.common.model.vo.CustomerVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 客户表（登录用户端APP用户） Mapper 接口
 * </p>
 *
 * @author JPG
 * @since 2019-09-29
 */
public interface ICustomerDao extends BaseMapper<Customer> {

    /**
     * 根据手机号，姓名，身份证号查询移动端用户
     * @param phone
     * @param name
     * @param idCard
     * @return
     */
    List<CustomerVo> findCustomer(@Param("phone") String phone, @Param("name") String name, @Param("idCard") String idCard);

}
