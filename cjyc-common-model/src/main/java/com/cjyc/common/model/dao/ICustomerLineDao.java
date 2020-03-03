package com.cjyc.common.model.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cjyc.common.model.entity.CustomerLine;
import com.cjyc.common.model.vo.customer.customerLine.CustomerLineVo;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @auther litan
 * @description: com.cjyc.common.model.dao
 * @date:2019/10/12
 */
@Repository
public interface ICustomerLineDao extends BaseMapper<CustomerLine>{

    /**
     * 根据客户id获取历史线路
     * @param customerId
     * @return
     */
    List<CustomerLineVo> findCustomerLine(@Param("customerId") Long customerId);

    /**
     * 根据客户id和登录id获取历史线路
     * @param loginId
     * @param customerId
     * @return
     */
    List<CustomerLineVo> findAppCustomerLine(@Param("loginId") Long loginId,@Param("customerId") Long customerId);
}
