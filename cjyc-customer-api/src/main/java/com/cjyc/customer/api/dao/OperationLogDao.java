package com.cjyc.customer.api.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cjyc.customer.api.entity.OperationLog;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 * Created by leo on 2019/7/26.
 */
@Repository
@Mapper
public interface OperationLogDao extends BaseMapper<OperationLog> {
}
