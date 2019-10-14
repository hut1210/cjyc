package com.cjyc.salesman.api.controller;

import com.cjyc.common.model.entity.Organization;
import com.cjyc.common.model.entity.sys.SysRoleEntity;
import com.cjyc.common.model.util.BaseResultUtil;
import com.cjyc.common.model.vo.ResultVo;
import com.cjyc.salesman.api.service.IOrganizationService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Api(tags = "组织机构")
@Slf4j
@RestController
@RequestMapping(value = "/organization",
        consumes = MediaType.APPLICATION_JSON_UTF8_VALUE,
        produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
public class OrganizationController {

    @Autowired
    private IOrganizationService organizationService;


    @ApiOperation(value = "根据ID获取组织机构本地信息", notes = "")
    @GetMapping("/get/{id}")
    public ResultVo<Organization> getById(@PathVariable long id){
        Organization organization = organizationService.getById(id);
        return BaseResultUtil.success(organization);
    }

    @ApiOperation(value = "根据ID获取组织机构本地信息", notes = "")
    @GetMapping("/sys/role/list/{id}")
    public ResultVo<List<SysRoleEntity>> listRole(@PathVariable long id){
        List<SysRoleEntity> list= organizationService.getSysRoleList(id);
        return BaseResultUtil.success(list);
    }

}
