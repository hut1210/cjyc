package com.cjyc.common.model.dao;

import com.cjyc.common.model.entity.Admin;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Set;

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

    List<Long> findStoreBizScope(@Param("adminId") Long adminId);

    /**
     * 根据承运商id获取Admin
     * @param carrierId
     * @return
     */
    Admin getByCarrierId(@Param("carrierId") Long carrierId);

    List<Admin> findListByUserIds(Set<Long> userIds);
}
