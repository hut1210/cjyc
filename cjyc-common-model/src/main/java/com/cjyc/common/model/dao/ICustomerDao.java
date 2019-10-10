package com.cjyc.common.model.dao;

import com.cjyc.common.model.dto.web.CustomerDto;
import com.cjyc.common.model.dto.web.ListKeyCustomerDto;
import com.cjyc.common.model.entity.Customer;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cjyc.common.model.vo.web.SelectCustomerVo;
import com.cjyc.common.model.vo.web.SelectKeyCustomerVo;
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
    List<CustomerDto> findCustomer(SelectCustomerVo customerVo);

    /**
     * 查询所有大客户用户
     * @return
     */
    List<ListKeyCustomerDto> getAllKeyCustomter();

    /**
     * 根据条件查询大客户用户
     * @param keyCustomerVo
     * @return
     */
    List<ListKeyCustomerDto> findKeyCustomter(SelectKeyCustomerVo keyCustomerVo);


}
