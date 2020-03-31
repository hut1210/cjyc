package com.cjyc.common.system.feign;

import com.cjkj.common.constant.ServiceNameConstants;
import com.cjkj.common.feign.fallback.UserServiceFallbackFactory;
import com.cjkj.common.model.ResultData;
import com.cjkj.usercenter.dto.asc.MenuResp;
import com.cjkj.usercenter.dto.common.*;
import com.cjkj.usercenter.dto.yc.SelectUsersByRoleReq;
import com.cjkj.usercenter.dto.yc.SelectUsersByRoleResp;
import com.cjkj.usercenter.dto.yc.UpdateBatchRoleMenusReq;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

/**
 * sys角色接口
 * @author JPG
 */
@FeignClient(value = ServiceNameConstants.USER_SERVICE, fallbackFactory = UserServiceFallbackFactory.class, decode404 = true)
public interface ISysRoleService {

    /**
     * 根据角色id查询角色信息
     * @author JPG
     * @since 2019/11/6 11:47
     * @param id
     */
    @GetMapping("/feign/uc/getRole/{id}")
    ResultData<SelectRoleResp> getById(@PathVariable(value="id") Long id);

    /**
     * 保存角色
     * @author JPG
     * @since 2019/10/21 9:43
     * @param addRoleReq 参数
     * @return ResultData<AddRoleResp>
     */
    @PostMapping("/feign/uc/addRole")
    ResultData<AddRoleResp> save(@RequestBody AddRoleReq addRoleReq);

    /**
     * 批量保存角色信息
     * @param batchRoleReq
     * @return
     */
    @PostMapping("/feign/uc/addBatchRole")
    ResultData saveBatch(@RequestBody AddBatchRoleReq batchRoleReq);

    /**
     * 保存角色
     * @author JPG
     * @since 2019/10/21 9:43
     * @param updateRoleReq 参数
     * @return ResultData
     */
    @PostMapping("/feign/uc/updateRole")
    ResultData update(@RequestBody UpdateRoleReq updateRoleReq);


    /**
     * 查询角色信息：根据部门id查询多级组织下的所有角色
     * @author JPG
     * @since 2019/10/21 9:46
     * @param deptId 组机机构ID
     * @return ResultData<List<SelectRoleResp>>
     */
    @GetMapping("feign/uc/getMultiLevelRoles/{deptId}")
    ResultData<List<SelectRoleResp>> getMultiLevelList(@PathVariable(value="deptId") Long deptId);

    /**
     * 查询角色信息：根据部门id查询一级组织下的所有角色
     * @author JPG
     * @since 2019/10/21 9:46
     * @param deptId 部门ID
     * @return ResultData<List<SelectRoleResp>>
     */
    @GetMapping("/feign/uc/getSingleLevelRoles/{deptId}")
    ResultData<List<SelectRoleResp>> getSingleLevelList(@PathVariable(value="deptId") Long deptId);


    /**
     * 查询角色信息：根据用户id
     * @author JPG
     * @since 2019/10/21 9:46
     * @param userId 用户ID
     * @return ResultData<List<SelectRoleResp>>
     */
    @GetMapping("/feign/uc/getRoles/{userId}")
    ResultData<List<SelectRoleResp>> getListByUserId(@PathVariable(value="userId") Long userId);

    /**
     * 查询角色信息：根据用户id
     * @author JPG
     * @since 2019/10/21 9:46
     * @param roleId 角色ID
     * @return  ResultData<SelectRoleResp>
     */
    @PostMapping("/feign/uc/deleteRole/{roleId}")
    ResultData<SelectRoleResp> delete(@PathVariable(value="roleId") Long roleId);

    /**
     * 批量删除角色信息
     * @param req
     * @return
     */
    @PostMapping("/feign/uc/deleteBatchRole")
    ResultData deleteBatch(@RequestBody DeleteBatchRoleReq req);

    /**
     * 获取角色关联用户列表信息
     * @param req
     * @return
     */
    @GetMapping("/feign/yc/getUsersByRoleId")
    ResultData<List<SelectUsersByRoleResp>> getUsersByRoleId(@RequestBody SelectUsersByRoleReq req);

    /**
     * 获取韵车系统资源列表
     * @return
     */
    @GetMapping("/feign/yc/getMenuList")
    ResultData<List<MenuResp>> getMenuList();

    /**
     * 批量更新角色-菜单列表信息
     * @param req
     * @return
     */
    @PostMapping("/feign/yc/batchUpdateRoleMenus")
    ResultData batchUpdateRoleMenus(@RequestBody UpdateBatchRoleMenusReq req);

    /**
     * 根据角色id获取角色所拥有末级菜单列表
     * @param roleId
     * @return
     */
    @GetMapping("/feign/yc/getBottomMenuIdsByRoleId/{roleId}")
    ResultData<List<Long>> getBottomMenuIdsByRoleId(@PathVariable(value="roleId") Long roleId);

    /**
     * 撤销指定用户的指定角色
     * @param userId
     * @param roleId
     * @return
     */
    @PostMapping("/feign/uc/revokeRole/{userId}/{roleId}")
    ResultData revokeRole(@PathVariable(value="userId") Long userId,@PathVariable(value="roleId") Long roleId);
}
