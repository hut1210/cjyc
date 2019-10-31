package com.cjyc.web.api.controller;

import com.cjkj.usercenter.dto.yc.SelectUsersByRoleResp;
import com.cjyc.common.model.dto.web.role.AddRoleDto;
import com.cjyc.common.model.vo.ResultVo;
import com.cjyc.common.model.vo.web.role.AdminListVo;
import com.cjyc.web.api.service.IRoleService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@Api(value = "角色管理")
@CrossOrigin(allowCredentials = "true", allowedHeaders = "*")
@RestController
@RequestMapping("/role")
public class RoleController {
    @Autowired
    private IRoleService roleService;

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
}
