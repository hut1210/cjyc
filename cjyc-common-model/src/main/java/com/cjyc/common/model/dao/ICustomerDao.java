package com.cjyc.common.model.dao;

import com.cjyc.common.model.vo.web.CustomerVo;
import com.cjyc.common.model.vo.web.ListKeyCustomerVo;
import com.cjyc.common.model.entity.Customer;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cjyc.common.model.dto.web.SelectCustomerDto;
import com.cjyc.common.model.dto.web.SelectKeyCustomerDto;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

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

    Customer findByPhone(@Param("phone") String phone);
}
