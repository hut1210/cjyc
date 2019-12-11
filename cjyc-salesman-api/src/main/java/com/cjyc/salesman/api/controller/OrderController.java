package com.cjyc.salesman.api.controller;

import com.cjyc.common.model.dto.salesman.PageSalesDto;
import com.cjyc.common.model.dto.salesman.order.AppOrderQueryDto;
import com.cjyc.common.model.dto.salesman.order.SalesmanQueryDto;
import com.cjyc.common.model.dto.web.order.ComputeCarEndpointDto;
import com.cjyc.common.model.util.BaseResultUtil;
import com.cjyc.common.model.vo.PageVo;
import com.cjyc.common.model.vo.ResultVo;
import com.cjyc.common.model.vo.salesman.order.AppOrderVo;
import com.cjyc.common.model.vo.web.admin.AdminPageVo;
import com.cjyc.common.model.vo.web.order.DispatchAddCarVo;
import com.cjyc.common.system.service.ICsOrderService;
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
    @Resource
    private ICsOrderService csOrderService;

    @ApiOperation(value = "选择业务员")
    @PostMapping("/admin/list")
    public ResultVo<PageVo<AdminPageVo>> listAdmins(@Valid @RequestBody SalesmanQueryDto dto) {
        return BaseResultUtil.success(adminService.listPage(dto));
    }

    @ApiOperation(value = "草稿列表")
    @PostMapping("/findDraftOrder")
    public ResultVo<PageVo<AppOrderVo>> findDraftOrder(@Valid @RequestBody PageSalesDto dto) {
        return orderService.findDraftOrder(dto);
    }

    @ApiOperation(value = "接单/全部列表")
    @PostMapping("/findOrder")
    public ResultVo<PageVo<AppOrderVo>> findOrder(@Valid @RequestBody AppOrderQueryDto dto) {
        return orderService.findOrder(dto);
    }


    /**
     * 根据订单车辆ID查询可调度起始地和目的地
     */
    @ApiOperation(value = "根据订单车辆ID查询可调度起始地和目的地")
    @PostMapping(value = "/computer/car/endpoint")
    public ResultVo<DispatchAddCarVo> computerCarEndpoint(@RequestBody ComputeCarEndpointDto reqDto) {
        return csOrderService.computerCarEndpoint(reqDto);
    }


}
