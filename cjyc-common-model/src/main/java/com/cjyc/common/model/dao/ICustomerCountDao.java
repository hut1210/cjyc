package com.cjyc.common.model.dao;

import com.cjyc.common.model.entity.CustomerCount;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cjyc.common.model.vo.web.customer.CustomerCountVo;
import org.apache.ibatis.annotations.Param;

import java.util.Map;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author JPG
 * @since 2019-10-26
 */
public interface ICustomerCountDao extends BaseMapper<CustomerCount> {

    /**
     * 统计用户下单量，运车量，总金额
     * @param userId
     * @return
     */
    CustomerCountVo count(@Param("userId") Long userId);

}
