package com.cjyc.common.system.service;

import com.cjyc.common.model.entity.Role;

import java.util.List;

/**
 * 角色信息通用接口
 */
public interface ICsRoleService {

    /**
     * 获取韵车内部角色列表
     * @return
     */
    List<Role> getValidInnerRoleList();

    /**
     * 根据角色名称、类型查询角色信息
     * @param roleName
     * @param roleType
     * @return
     */
    public Role getByName(String roleName, Integer roleType);
}
