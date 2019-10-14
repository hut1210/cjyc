package com.cjyc.common.model.dao;

import com.cjyc.common.model.entity.Admin;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * 韵车后台管理员表 Mapper 接口
 * </p>
 *
 * @author JPG
 * @since 2019-09-29
 */
public interface IAdminDao extends BaseMapper<Admin> {

    Admin findByUserId(@Param("userId") Long userId);
}
