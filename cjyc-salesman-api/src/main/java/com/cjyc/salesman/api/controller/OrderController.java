package com.cjyc.salesman.api.controller;

import com.cjyc.common.model.dto.salesman.order.SalesmanQueryDto;
import com.cjyc.common.model.util.BaseResultUtil;
import com.cjyc.common.model.vo.PageVo;
import com.cjyc.common.model.vo.ResultVo;
import com.cjyc.common.model.vo.web.admin.AdminPageVo;
import com.cjyc.common.system.feign.ISysUserService;
import com.cjyc.salesman.api.service.IAdminService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.validation.Valid;

@Api(tags = "订单")
@RestController
@RequestMapping(value = "/order",
        produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
public class OrderController {

    @Autowired
    private IAdminService adminService;

    @ApiOperation(value = "选择业务员")
    @RequestMapping("/admin/list")
    public ResultVo<PageVo<AdminPageVo>> listAdmins(@Valid @RequestBody SalesmanQueryDto dto) {
        return BaseResultUtil.success(adminService.listPage(dto));
    }
}
