package com.cjyc.common.model.dao;

import com.cjyc.common.model.entity.Role;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cjyc.common.model.vo.web.role.SelectUserByRoleVo;

import java.util.List;

/**
 * <p>
 * 角色信息 Mapper 接口
 * </p>
 *
 * @author zcm
 * @since 2019-10-30
 */
public interface IRoleDao extends BaseMapper<Role> {

    Role findByName(String roleName);

    /**
     * 根据角色标识查询关联用户列表信息
     * @param roleId
     * @return
     */
    List<SelectUserByRoleVo> getUsersByRoleId(Long roleId);
}
