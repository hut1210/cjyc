package com.cjyc.web.api.controller;

import com.cjyc.common.model.dto.web.order.*;
import com.cjyc.common.model.entity.Admin;
import com.cjyc.common.model.enums.AdminStateEnum;
import com.cjyc.common.model.util.BaseResultUtil;
import com.cjyc.common.model.vo.PageVo;
import com.cjyc.common.model.vo.ResultVo;
import com.cjyc.common.model.vo.web.order.*;
import com.cjyc.web.api.service.IAdminService;
import com.cjyc.web.api.service.IBizScopeService;
import com.cjyc.web.api.service.IOrderService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * 订单
 * @author JPG
 */
@RestController
@Api(tags = "订单")
@RequestMapping(value = "/order",
        produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
public class OrderController {

    @Resource
    private IOrderService orderService;
    @Resource
    private IBizScopeService bizScopeService;
    @Resource
    private IAdminService adminService;


    /**
     * 保存,只保存无验证
     * @author JPG
     */
    @ApiOperation(value = "订单保存")
    @PostMapping(value = "/save")
    public ResultVo save(@RequestBody SaveOrderDto reqDto) {

        //验证用户存不存在
        Long userId = reqDto.getUserId();
        Admin admin = adminService.getByUserId(userId);
        if(admin == null){
            return BaseResultUtil.fail("用户不存在");
        }
        reqDto.setCreateUserId(admin.getUserId());
        reqDto.setCreateUserName(admin.getName());

        ResultVo resultVo = orderService.save(reqDto);
        //发送推送信息
        return resultVo;
    }


    /**
     * 提交
     * @author JPG
     */
    @ApiOperation(value = "订单提交")
    @PostMapping(value = "/commit")
    public ResultVo commit(@Validated @RequestBody CommitOrderDto reqDto) {

        //验证用户存不存在
        Long userId = reqDto.getUserId();
        Admin admin = adminService.getByUserId(userId);
        if(admin == null){
            return BaseResultUtil.fail("用户不存在");
        }
        reqDto.setCreateUserId(admin.getUserId());
        reqDto.setCreateUserName(admin.getName());

        ResultVo resultVo = orderService.commit(reqDto);

        //发送推送信息
        return resultVo;
    }


    /**
     * 完善订单信息
     * @author JPG
     */
    @ApiOperation(value = "完善订单信息")
    @PostMapping(value = "/replenish/info/update")
    public ResultVo replenishInfo(@RequestBody ReplenishOrderDto reqDto) {
        return orderService.replenishInfo(reqDto);
    }

    /**
     * 审核订单
     * @author JPG
     */
    @ApiOperation(value = "审核订单")
    @PostMapping(value = "/check")
    public ResultVo check(@RequestBody CheckOrderDto reqDto) {
        //验证用户存不存在
        Long userId = reqDto.getUserId();
        Admin admin = adminService.getByUserId(userId);
        if(admin == null){
            return BaseResultUtil.fail("用户不存在");
        }
        return orderService.check(reqDto);
    }

    /**
     * 驳回订单
     * @author JPG
     */
    @ApiOperation(value = "驳回订单")
    @PostMapping(value = "/reject")
    public ResultVo reject(@RequestBody RejectOrderDto reqDto) {
        //验证用户存不存在
        Long userId = reqDto.getUserId();
        Admin admin = adminService.getByUserId(userId);
        if(admin == null){
            return BaseResultUtil.fail("用户不存在");
        }
        return orderService.reject(reqDto);
    }




    /**
     * 查询订单-根据ID
     * @author JPG
     */
    @ApiOperation(value = "查询订单-根据ID")
    @PostMapping(value = "/get/{orderId}")
    public ResultVo<OrderVo> get(@PathVariable Long orderId) {
        OrderVo orderVo = orderService.getVoById(orderId);
        return BaseResultUtil.success(orderVo);
    }


    /**
     * 查询订单取消记录-根据ID
     * @author JPG
     */
    @ApiOperation(value = "查询订单取消记录-根据ID")
    @PostMapping(value = "/change/log/list")
    public ResultVo<List<ListOrderChangeLogVo>> get(@RequestBody ListOrderChangeLogDto reqDto) {
        List<ListOrderChangeLogVo> list = orderService.getChangeLogVoById(reqDto);
        return BaseResultUtil.success(list);
    }

    /**
     * 查询订单列表
     * @author JPG
     */
    @ApiOperation(value = "查询订单列表")
    @PostMapping(value = "/list")
    public ResultVo<PageVo<ListOrderVo>> list(@RequestBody ListOrderDto reqDto) {
        return orderService.list(reqDto);
    }

    /**
     * 查询订单车辆列表
     * @author JPG
     */
    @ApiOperation(value = "订单车辆查询")
    @PostMapping(value = "/car/list")
    public ResultVo<PageVo<ListOrderCarVo>> carlist(@RequestBody ListOrderCarDto reqDto) {
        return orderService.carlist(reqDto);
    }
    /**
     * 查询订单车辆运输状态-根据ID
     * @author JPG
     */
    @ApiOperation(value = "查询订单车辆运输状态-根据ID")
    @PostMapping(value = "/detail/info/{orderId}")
    public ResultVo<List<TransportInfoOrderCarVo>> detailInfo(@PathVariable Long orderId) {
        List<TransportInfoOrderCarVo> list = orderService.getTransportInfoVoById(orderId);
        return BaseResultUtil.success(list);
    }



    /**
     * 分配订单
     * @author JPG
     */
    @ApiOperation(value = "分配订单")
    @PostMapping(value = "/allot")
    public ResultVo allot(@Validated @RequestBody AllotOrderDto reqDto) {
        //验证操作人
        Admin admin = adminService.getByUserId(reqDto.getUserId());
        if(admin == null || admin.getState() != AdminStateEnum.CHECKED.code){
            return BaseResultUtil.fail("操作用户不存在或者已离职");
        }
        reqDto.setUserName(admin.getName());
        //验证被分配人
        Admin toAdmin = adminService.getByUserId(reqDto.getToUserId());
        if(toAdmin == null){
            return BaseResultUtil.fail("目标业务员不存在");
        }
        reqDto.setToUserName(toAdmin.getName());
        return orderService.allot(reqDto);
    }




    /**
     * TODO 订单运输信息
     */


    /**
     * 订单改价
     * @author JPG
     */
    @ApiOperation(value = "订单改价")
    @PostMapping(value = "/change/price")
    public ResultVo changePrice(@RequestBody ChangePriceOrderDto reqDto) {
        //验证操作人
        Admin admin = adminService.getByUserId(reqDto.getUserId());
        if(admin == null || admin.getState() != AdminStateEnum.CHECKED.code){
            return BaseResultUtil.fail("操作用户不存在或者已离职");
        }
        reqDto.setUserName(admin.getName());
        return orderService.changePrice(reqDto);
    }


    /**
     * 取消订单
     * @author JPG
     */
    @ApiOperation(value = "取消订单")
    @PostMapping(value = "/cancel")
    public ResultVo cancel(@RequestBody CancelOrderDto reqDto) {
        //验证操作人
        Admin admin = adminService.getByUserId(reqDto.getUserId());
        if(admin == null || admin.getState() != AdminStateEnum.CHECKED.code){
            return BaseResultUtil.fail("操作用户不存在或者已离职");
        }
        reqDto.setUserName(admin.getName());

        return orderService.cancel(reqDto);
    }


    /**
     * 作废订单
     * @author JPG
     */
    @ApiOperation(value = "作废订单")
    @PostMapping(value = "/obsolete")
    public ResultVo obsolete(@RequestBody CancelOrderDto reqDto) {
        //验证操作人
        Admin admin = adminService.getByUserId(reqDto.getUserId());
        if(admin == null || admin.getState() != AdminStateEnum.CHECKED.code){
            return BaseResultUtil.fail("操作用户不存在或者已离职");
        }
        reqDto.setUserName(admin.getName());

        return orderService.obsolete(reqDto);
    }



}
