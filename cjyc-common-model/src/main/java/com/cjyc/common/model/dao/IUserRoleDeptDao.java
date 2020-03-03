package com.cjyc.common.model.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cjyc.common.model.entity.UserRoleDept;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 用户-角色-机构信息表 Mapper 接口
 * </p>
 *
 * @author zcm
 * @since 2019-12-23
 */
public interface IUserRoleDeptDao extends BaseMapper<UserRoleDept> {
    /**
     * 获取非业务员角色id列表
     * @param userId 物流平台用户id
     * @return
     */
    List<Long> getNonSalesmanRoleIds(@Param("userId") Long userId);

    /**
     * 根据司机ID获取承运商id
     * @param userId
     * @return
     */
    List<Long> findDeptIds(@Param("userId") Long userId);
}
