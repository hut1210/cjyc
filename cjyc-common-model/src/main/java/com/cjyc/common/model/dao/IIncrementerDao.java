package com.cjyc.common.model.dao;

import com.cjyc.common.model.entity.Incrementer;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

/**
 * <p>
 * 订单、任务编号自增表 Mapper 接口
 * </p>
 *
 * @author JPG
 * @since 2019-09-29
 */
@Repository
public interface IIncrementerDao extends BaseMapper<Incrementer> {

    String getIncrementer(@Param("preName") String preName);
}
