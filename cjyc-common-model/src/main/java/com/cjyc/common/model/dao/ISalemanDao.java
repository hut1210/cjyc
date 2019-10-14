package com.cjyc.common.model.dao;

import com.cjyc.common.model.entity.Saleman;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * 业务员表 Mapper 接口
 * </p>
 *
 * @author JPG
 * @since 2019-09-29
 */
public interface ISalemanDao extends BaseMapper<Saleman> {

    Saleman findByPhone(@Param("phone") String phone);

}
