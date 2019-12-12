package com.cjyc.salesman.api.controller;

import com.cjyc.common.model.dto.salesman.order.SalesOrderDetailDto;
import com.cjyc.common.model.dto.salesman.order.SalesOrderQueryDto;
import com.cjyc.common.model.dto.salesman.order.SalesmanQueryDto;
import com.cjyc.common.model.dto.web.order.*;
import com.cjyc.common.model.entity.Admin;
import com.cjyc.common.model.enums.order.OrderStateEnum;
import com.cjyc.common.model.util.BaseResultUtil;
import com.cjyc.common.model.vo.PageVo;
import com.cjyc.common.model.vo.ResultVo;
import com.cjyc.common.model.vo.salesman.order.SalesOrderDetailVo;
import com.cjyc.common.model.vo.salesman.order.SalesOrderVo;
import com.cjyc.common.model.vo.web.admin.AdminPageVo;
import com.cjyc.common.model.vo.web.order.DispatchAddCarVo;
import com.cjyc.common.system.service.ICsAdminService;
import com.cjyc.common.system.service.ICsOrderService;
import com.cjyc.salesman.api.service.IAdminService;
import com.cjyc.salesman.api.service.IOrderService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
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
    private ICsAdminService csAdminService;
    @Resource
    private IOrderService orderService;
    @Resource
    private ICsOrderService csOrderService;


    /**
     * 保存,只保存无验证
     * @author JPG
     */
    @ApiOperation(value = "订单保存")
    @PostMapping(value = "/save")
    public ResultVo save(@RequestBody SaveOrderDto reqDto) {

        //验证用户存不存在
        Admin admin = csAdminService.validate(reqDto.getLoginId());
        reqDto.setLoginName(admin.getName());
        reqDto.setCreateUserId(admin.getId());
        reqDto.setCreateUserName(admin.getName());

        ResultVo resultVo = csOrderService.save(reqDto, OrderStateEnum.WAIT_SUBMIT);
        //发送推送信息
        return resultVo;
    }

    /**
     * 提交
     * @author JPG
     */
    @ApiOperation(value = "订单提交")
    @PostMapping(value = "/commit")
    public ResultVo commitAndCheck(@Validated @RequestBody CommitOrderDto reqDto) {

        //验证用户存不存在
        Admin admin = csAdminService.validate(reqDto.getLoginId());
        reqDto.setLoginName(admin.getName());
        reqDto.setLoginPhone(admin.getPhone());
        reqDto.setCreateUserId(admin.getId());
        reqDto.setCreateUserName(admin.getName());

        //发送短信
        return csOrderService.commitAndCheck(reqDto);
    }
    /**
     * 审核订单
     * @author JPG
     */
    @ApiOperation(value = "审核订单")
    @PostMapping(value = "/check")
    public ResultVo check(@RequestBody CheckOrderDto reqDto) {
        //验证用户存不存在
        Admin admin = csAdminService.validate(reqDto.getLoginId());
        reqDto.setLoginName(admin.getName());
        return csOrderService.check(reqDto);
    }


    /**
     * 驳回订单
     * @author JPG
     */
    @ApiOperation(value = "驳回订单")
    @PostMapping(value = "/reject")
    public ResultVo reject(@RequestBody RejectOrderDto reqDto) {
        //验证用户存不存在
        Admin admin = csAdminService.validate(reqDto.getLoginId());
        reqDto.setLoginName(admin.getName());
        return csOrderService.reject(reqDto);
    }

    /**
     * 分配订单
     * @author JPG
     */
    @ApiOperation(value = "分配订单")
    @PostMapping(value = "/allot")
    public ResultVo allot(@Validated @RequestBody AllotOrderDto reqDto) {
        //验证用户存不存在
        Admin admin = csAdminService.validate(reqDto.getLoginId());
        reqDto.setLoginName(admin.getName());
        return csOrderService.allot(reqDto);
    }


    /**
     * 订单改价
     * @author JPG
     */
    @ApiOperation(value = "订单改价")
    @PostMapping(value = "/change/price")
    public ResultVo changePrice(@RequestBody ChangePriceOrderDto reqDto) {
        Admin admin = csAdminService.validate(reqDto.getLoginId());
        reqDto.setLoginName(admin.getName());
        return csOrderService.changePrice(reqDto);
    }


    /**
     * 取消订单
     * @author JPG
     */
    @ApiOperation(value = "取消订单")
    @PostMapping(value = "/cancel")
    public ResultVo cancel(@RequestBody CancelOrderDto reqDto) {
        Admin admin = csAdminService.validate(reqDto.getLoginId());
        reqDto.setLoginName(admin.getName());
        return csOrderService.cancel(reqDto);
    }


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


    /**
     * 根据订单车辆ID查询可调度起始地和目的地
     */
    @ApiOperation(value = "根据订单车辆ID查询计算起始地和目的地")
    @PostMapping(value = "/computer/car/endpoint")
    public ResultVo<DispatchAddCarVo> computerCarEndpoint(@RequestBody ComputeCarEndpointDto reqDto) {
        return csOrderService.computerCarEndpoint(reqDto);
    }


    @ApiOperation(value = "订单详情")
    @PostMapping("/findOrderDetail")
    public ResultVo<SalesOrderDetailVo> findOrderDetail(@Valid @RequestBody SalesOrderDetailDto dto) {
        return orderService.findOrderDetail(dto);
    }

}
