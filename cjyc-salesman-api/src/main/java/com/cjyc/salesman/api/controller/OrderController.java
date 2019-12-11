package com.cjyc.salesman.api.controller;

import com.cjyc.common.model.dto.salesman.order.SalesOrderDetailDto;
import com.cjyc.common.model.dto.salesman.order.SalesOrderQueryDto;
import com.cjyc.common.model.dto.salesman.order.SalesmanQueryDto;
import com.cjyc.common.model.util.BaseResultUtil;
import com.cjyc.common.model.vo.PageVo;
import com.cjyc.common.model.vo.ResultVo;
import com.cjyc.common.model.vo.salesman.order.SalesOrderDetailVo;
import com.cjyc.common.model.vo.salesman.order.SalesOrderVo;
import com.cjyc.common.model.vo.web.admin.AdminPageVo;
import com.cjyc.salesman.api.service.IAdminService;
import com.cjyc.salesman.api.service.IOrderService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
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
    @Resource
    private IOrderService orderService;

    @ApiOperation(value = "选择业务员")
    @PostMapping("/admin/list")
    public ResultVo<PageVo<AdminPageVo>> listAdmins(@Valid @RequestBody SalesmanQueryDto dto) {
        return BaseResultUtil.success(adminService.listPage(dto));
    }

    @ApiOperation(value = "草稿/接单/全部列表")
    @PostMapping("/findOrder")
    public ResultVo<PageVo<SalesOrderVo>> findOrder(@Valid @RequestBody SalesOrderQueryDto dto) {
        return orderService.findOrder(dto);
    }

    @ApiOperation(value = "订单详情")
    @PostMapping("/findOrderDetail")
    public ResultVo<SalesOrderDetailVo> findOrderDetail(@Valid @RequestBody SalesOrderDetailDto dto) {
        return orderService.findOrderDetail(dto);
    }

}
