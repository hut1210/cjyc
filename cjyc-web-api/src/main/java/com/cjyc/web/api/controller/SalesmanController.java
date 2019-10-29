package com.cjyc.web.api.controller;

import com.cjyc.common.model.dto.web.salesman.AddDto;
import com.cjyc.common.model.dto.web.salesman.AssignRoleDto;
import com.cjyc.common.model.dto.web.salesman.ResetStateDto;
import com.cjyc.common.model.util.BaseResultUtil;
import com.cjyc.common.model.vo.ResultVo;
import com.cjyc.web.api.service.ISalesmanService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Api(tags = "系统用户")
@RestController
@RequestMapping("/salesman")
public class SalesmanController {
    @Autowired
    private ISalesmanService salesmanService;

    @PostMapping("/saveOrUpdate")
    @ApiOperation(value = "保存或修改业务员信息")
    public ResultVo saveOrUpdate(@Valid @RequestBody AddDto dto) {
        return salesmanService.saveAdmin(dto);
    }

    @PostMapping("/assignRoles")
    @ApiOperation(value = "分配角色(即分配业务范围)")
    public ResultVo assignRoles(@Valid @RequestBody AssignRoleDto dto){
        return salesmanService.assignRoles(dto);
    }

    @PostMapping("/resetState")
    @ApiOperation(value = "设置用户状态（即启用停用)")
    public ResultVo resetState(@Valid @RequestBody ResetStateDto dto) {
        return salesmanService.resetState(dto);
    }

    @PostMapping("/resetPwd/{id}")
    @ApiOperation(value = "重置用户密码")
    public ResultVo resetPwd(@ApiParam(name = "id", value = "用户标识", required = true)
                             @PathVariable("id")Long id) {
        return salesmanService.resetPwd(id);
    }
}
