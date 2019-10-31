package com.cjyc.web.api.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.cjkj.usercenter.dto.yc.SelectUsersByRoleResp;
import com.cjyc.common.model.dto.web.role.AddRoleDto;
import com.cjyc.common.model.entity.Role;
import com.cjyc.common.model.vo.ResultVo;

import java.util.List;

/**
 * 角色信息service
 */

public interface IRoleService extends IService<Role> {

    /**
     * 添加角色信息
     * @param dto
     * @return
     */
    ResultVo addRole(AddRoleDto dto);

    /**
     * 删除角色信息
     * @param id
     * @return
     */
    ResultVo deleteRole(Long id);

    /**
     * 根据角色查询关联用户信息
     * @param roleId
     * @return
     */
    ResultVo<List<SelectUsersByRoleResp>> getUsersByRoleId(Long roleId);
}
