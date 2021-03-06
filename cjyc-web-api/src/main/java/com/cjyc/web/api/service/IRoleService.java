package com.cjyc.web.api.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.cjkj.usercenter.dto.yc.SelectUsersByRoleResp;
import com.cjyc.common.model.dto.web.role.AddRoleDto;
import com.cjyc.common.model.dto.web.role.ModifyRoleMenusDto;
import com.cjyc.common.model.dto.web.role.SetRoleForAppDto;
import com.cjyc.common.model.entity.Role;
import com.cjyc.common.model.vo.ResultVo;
import com.cjyc.common.model.vo.web.role.SelectUserByRoleVo;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
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
    ResultVo<List<SelectUserByRoleVo>> getUsersByRoleId(Long roleId);

    ResultVo<List<SelectUsersByRoleResp>> getDeptRoleListByUserRoleId(Long roleId);

    /**
     * 查询所有角色信息列表
     * @return
     */
    ResultVo<List<Role>> getAllList(String roleName);

    /**
     * 根据角色id获取用户类型
     * @param roleId
     * @return
     */
    ResultVo<Integer> getUserTypeByRole(Long roleId);

    /**
     * 变更角色-资源列表信息
     * @param dto
     * @return
     */
    ResultVo modifyRoleMenus(ModifyRoleMenusDto dto);

    /**
     * 根据角色id获取角色所拥有的末级id列表
     * @param roleId
     * @return
     */
    ResultVo<List<String>> getBtmMenuIdsByRoleId(Long roleId);

    /*********************************韵车集成改版 st*****************************/
    /**
     * 添加角色信息
     * @param dto
     * @return
     */
    ResultVo addRoleNew(AddRoleDto dto);

    /**
     * 根据角色查询关联用户信息 改版
     * @param roleId
     * @return
     */
    ResultVo<List<SelectUserByRoleVo>> getUsersByRoleIdNew(Long roleId);

    /**
     * 查询所有角色信息列表
     * @return
     */
    ResultVo<List<Role>> getAllListNew(String roleName);

    /**
     * 根据角色id获取角色所拥有的末级id列表
     * @param roleId
     * @return
     */
    ResultVo<List<String>> getBtmMenuIdsByRoleIdNew(Long roleId);

    /**
     * 变更角色-资源列表信息
     * @param dto
     * @return
     */
    ResultVo modifyRoleMenusNew(ModifyRoleMenusDto dto);

    /**
     * 更新业务员APP相关角色信息
     * @param dto
     * @return
     */
    ResultVo setRoleForApp(SetRoleForAppDto dto);

    /**
     * 角色管理导出到excel
     * @param request
     * @param response
     */
    void exportRoleListExcel(HttpServletRequest request, HttpServletResponse response);
    /*********************************韵车集成改版 ed*****************************/
}
