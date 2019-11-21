package com.cjyc.salesman.api.controller;

import com.cjyc.common.model.dto.BasePageDto;
import com.cjyc.common.model.entity.Admin;
import com.cjyc.common.model.util.BaseResultUtil;
import com.cjyc.common.model.vo.ResultVo;
import com.cjyc.salesman.api.service.IAdminService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@Api(tags = "管理员")
@Slf4j
@RestController
@RequestMapping(value = "/admin",
        produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
public class AdminController {

    @Autowired
    private IAdminService adminService;

    @ApiOperation(value = "根据ID获取管理员本地信息", notes = "")
    @PostMapping("/get/{id}")
    public ResultVo<Admin> getById(@PathVariable long id) {
        Admin admin = adminService.getById(id);
        return BaseResultUtil.success(admin);
    }


    @ApiOperation(value = "根据ID获取管理员本地信息", notes = "")
    @PostMapping("/list")
    public ResultVo<Admin> list(@PathVariable BasePageDto BasePageDto) {
        Admin admin = adminService.getById(BasePageDto);
        return BaseResultUtil.success(admin);
    }





}
