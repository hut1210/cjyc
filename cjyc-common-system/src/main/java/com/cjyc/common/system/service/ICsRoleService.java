package com.cjyc.common.system.service;

import com.cjyc.common.model.entity.Role;

import java.util.List;

/**
 * 角色信息通用接口
 */
public interface ICsRoleService {

    List<Role> getRoleList();
}
