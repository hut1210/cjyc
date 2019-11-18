package com.cjyc.web.api.controller;

import com.cjkj.common.model.ResultData;
import com.cjkj.common.model.ReturnMsg;
import com.cjkj.usercenter.dto.asc.MenuResp;
import com.cjkj.usercenter.dto.yc.SelectUsersByRoleResp;
import com.cjyc.common.model.dto.web.role.AddRoleDto;
import com.cjyc.common.model.util.BaseResultUtil;
import com.cjyc.common.model.vo.ResultVo;
import com.cjyc.common.model.vo.web.role.AdminListVo;
import com.cjyc.common.system.feign.ISysRoleService;
import com.cjyc.web.api.service.IRoleService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@Api(tags = "基础数据-角色")
@CrossOrigin(allowCredentials = "true", allowedHeaders = "*")
@RestController
@RequestMapping("/role")
public class RoleController {
    @Autowired
    private IRoleService roleService;
    @Autowired
    private ISysRoleService sysRoleService;

    @PostMapping("/add")
    @ApiOperation(value = "添加角色信息")
    public ResultVo add(@Valid @RequestBody AddRoleDto dto) {
        return roleService.addRole(dto);
    }

    @PostMapping("/delete/{id}")
    @ApiOperation(value = "删除角色信息")
    public ResultVo delete(@ApiParam(name = "id", value = "角色标识", required = true)
                           @PathVariable Long id) {
        return roleService.deleteRole(id);
    }

    @GetMapping("/getUsersByRoleId/{roleId}")
    @ApiOperation(value = "根据角色id获取关联用户列表信息")
    public ResultVo<List<SelectUsersByRoleResp>> getUsersByRoleId(
            @ApiParam(name = "roleId", value = "角色标识", required = true)
            @PathVariable Long roleId) {
        return roleService.getUsersByRoleId(roleId);
    }



    @GetMapping("/getMenuList")
    @ApiOperation(value = "获取韵车系统资源列表")
    public ResultVo<List<MenuResp>> getMenuList() {
        ResultData<List<MenuResp>> rd = sysRoleService.getMenuList();
        if (!ReturnMsg.SUCCESS.getCode().equals(rd.getCode())) {
            return BaseResultUtil.fail(rd.getMsg());
        }
        return BaseResultUtil.success(rd.getData());
    }


    /**
     * 查询角色对应机构下的角色列表
     * @author JPG
     * @since 2019/11/18 13:17
     * @param roleId
     */
    @GetMapping("/dept/role/list/{roleId}")
    @ApiOperation(value = "根据角色id获取关联用户列表信息")
    public ResultVo<List<SelectUsersByRoleResp>> getDeptRoleListByUserRoleId(
            @ApiParam(name = "roleId", value = "角色标识", required = true)
            @PathVariable Long roleId) {
        return roleService.getDeptRoleListByUserRoleId(roleId);
    }
}
