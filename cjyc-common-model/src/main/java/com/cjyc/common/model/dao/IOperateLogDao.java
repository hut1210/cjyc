package com.cjyc.common.model.dao;

import com.cjyc.common.model.entity.OperateLog;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.springframework.stereotype.Repository;

/**
 * <p>
 * 操作日志 Mapper 接口
 * </p>
 *
 * @author JPG
 * @since 2019-09-29
 */
@Repository
public interface IOperateLogDao extends BaseMapper<OperateLog> {

}
