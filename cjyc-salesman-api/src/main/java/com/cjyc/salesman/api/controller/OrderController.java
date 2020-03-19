package com.cjyc.salesman.api.controller;

import com.cjyc.common.model.dto.salesman.order.SalesOrderDetailDto;
import com.cjyc.common.model.dto.salesman.order.SalesOrderQueryDto;
import com.cjyc.common.model.dto.salesman.order.SalesmanQueryDto;
import com.cjyc.common.model.dto.web.order.*;
import com.cjyc.common.model.entity.Admin;
import com.cjyc.common.model.enums.PayModeEnum;
import com.cjyc.common.model.enums.UserTypeEnum;
import com.cjyc.common.model.enums.order.OrderPickTypeEnum;
import com.cjyc.common.model.enums.order.OrderStateEnum;
import com.cjyc.common.model.util.BaseResultUtil;
import com.cjyc.common.model.vo.PageVo;
import com.cjyc.common.model.vo.ResultVo;
import com.cjyc.common.model.vo.salesman.order.SalesOrderDetailVo;
import com.cjyc.common.model.vo.salesman.order.SalesOrderVo;
import com.cjyc.common.model.vo.web.OrderCarVo;
import com.cjyc.common.model.vo.web.admin.AdminPageVo;
import com.cjyc.common.system.service.ICsAdminService;
import com.cjyc.common.system.service.ICsOrderService;
import com.cjyc.salesman.api.service.IAdminService;
import com.cjyc.salesman.api.service.IOrderService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

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
        reqDto.setLoginPhone(admin.getPhone());
        reqDto.setLoginType(UserTypeEnum.ADMIN.code);
        reqDto.setState(OrderStateEnum.WAIT_SUBMIT.code);
        //业务员上门变为代驾上门
        if(OrderPickTypeEnum.DISPATCH_SELF.code == reqDto.getPickType()){
            reqDto.setPickType(OrderPickTypeEnum.PILOT.code);
        }

        return csOrderService.save(reqDto);
    }

    /**
     * 提交并审核
     * @author JPG
     */
    @ApiOperation(value = "提交并审核")
    @PostMapping(value = "/commit")
    public ResultVo commitAndCheck(@Validated @RequestBody CommitOrderDto reqDto) {

        //验证用户存不存在
        Admin admin = csAdminService.validate(reqDto.getLoginId());
        reqDto.setLoginName(admin.getName());
        reqDto.setLoginPhone(admin.getPhone());
        reqDto.setLoginType(UserTypeEnum.ADMIN.code);
        //业务员上门变为代驾上门
        if(OrderPickTypeEnum.DISPATCH_SELF.code == reqDto.getPickType()){
            if(reqDto.getPayType() == PayModeEnum.PREPAY.code){
                return BaseResultUtil.fail("预付订单不能选择业务员上门，付款成功后再进行调度");
            }
            reqDto.setPickType(OrderPickTypeEnum.PILOT.code);
            reqDto.setDispatch(true);
        }


        //发送短信
        return csOrderService.commitAndCheck(reqDto);
    }

    /**
     * 直接提交并审核
     * @author JPG
     */
    @ApiOperation(value = "直接提交并审核")
    @PostMapping(value = "/simple/commit")
    public ResultVo simpleCommitAndCheck(@Validated @RequestBody CheckOrderDto reqDto) {

        //验证用户存不存在
        Admin admin = csAdminService.validate(reqDto.getLoginId());
        reqDto.setLoginName(admin.getName());
        reqDto.setLoginPhone(admin.getPhone());

        //发送短信
        return csOrderService.simpleCommitAndCheck(reqDto);
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
        reqDto.setLoginPhone(admin.getPhone());
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
        Admin toAdmin = csAdminService.validate(reqDto.getToAdminId());
        reqDto.setToAdminName(toAdmin.getName());
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
        reqDto.setLoginPhone(admin.getPhone());
        reqDto.setLoginType(UserTypeEnum.ADMIN);
        return csOrderService.cancel(reqDto);
    }


    @ApiOperation(value = "选择业务员")
    @PostMapping("/admin/list")
    public ResultVo<PageVo<AdminPageVo>> listAdmins(@Valid @RequestBody SalesmanQueryDto dto) {
        return BaseResultUtil.success(adminService.listPageNew(dto));
    }

    @ApiOperation(value = "下单/接单/全部列表")
    @PostMapping("/findOrder")
    public ResultVo<PageVo<SalesOrderVo>> findOrder(@Valid @RequestBody SalesOrderQueryDto dto) {
        return orderService.findOrder(dto);
    }

    @ApiOperation(value = "订单详情")
    @PostMapping("/findOrderDetail")
    public ResultVo<SalesOrderDetailVo> findOrderDetail(@Valid @RequestBody SalesOrderDetailDto dto) {
        return orderService.findOrderDetail(dto);
    }


    /**
     * 查询订单-根据车辆ID
     * @author JPG
     */
    @ApiOperation(value = "查询车辆和订单详细信息-根据车辆ID")
    @PostMapping(value = "/car/vo/get")
    public ResultVo<OrderCarVo> getCarVo(@Valid @RequestBody OrderCarIdDto reqDto) {
        OrderCarVo vo = orderService.getCarVoById(reqDto.getOrderCarId());
        return BaseResultUtil.success(vo);
    }



}
