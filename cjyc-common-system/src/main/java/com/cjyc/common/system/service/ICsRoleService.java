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
}
