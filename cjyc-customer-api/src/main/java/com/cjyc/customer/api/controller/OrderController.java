package com.cjyc.customer.api.controller;

import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.cjyc.common.model.dto.customer.order.OrderQueryDto;
import com.cjyc.common.model.dto.customer.order.OrderUpdateDto;
import com.cjyc.common.model.dto.web.order.CommitOrderDto;
import com.cjyc.common.model.dto.web.order.SaveOrderDto;
import com.cjyc.common.model.entity.Customer;
import com.cjyc.common.model.entity.Order;
import com.cjyc.common.model.enums.order.OrderStateEnum;
import com.cjyc.common.model.util.BaseResultUtil;
import com.cjyc.common.model.vo.PageVo;
import com.cjyc.common.model.vo.ResultVo;
import com.cjyc.common.model.vo.customer.order.OrderCenterDetailVo;
import com.cjyc.common.model.vo.customer.order.OrderCenterVo;
import com.cjyc.common.system.service.ICsCustomerService;
import com.cjyc.customer.api.service.IOrderService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Map;


/**
 * Created by leo on 2019/7/25.
 */
@RequestMapping("/order")
@Api(tags = "订单管理")
@RestController
public class OrderController {
    @Autowired
    IOrderService orderService;
    @Resource
    private ICsCustomerService comCustomerService;
    /**
     * 保存,只保存无验证
     * @author JPG
     */
    @ApiOperation(value = "订单保存")
    @PostMapping(value = "/save")
    public ResultVo save(@RequestBody SaveOrderDto reqDto) {

        //验证用户存不存在
        Customer customer = comCustomerService.getByUserId(reqDto.getUserId(), true);
        if(customer == null){
            return BaseResultUtil.fail("用户不存在");
        }
        reqDto.setCreateUserId(customer.getUserId());
        reqDto.setCreateUserName(customer.getName());

        //发送推送信息
        return orderService.save(reqDto);
    }

    /**
     * 订单提交-客户
     * @author JPG
     */
    @ApiOperation(value = "订单提交-客户")
    @PostMapping(value = "/submit")
    public ResultVo submit(@Validated @RequestBody SaveOrderDto reqDto) {

        //验证用户存不存在
        Customer admin = comCustomerService.getById(reqDto.getUserId(), true);
        if(admin == null){
            return BaseResultUtil.fail("用户不存在");
        }
        reqDto.setCreateUserId(admin.getUserId());
        reqDto.setCreateUserName(admin.getName());

        //发送推送信息
        return orderService.submit(reqDto);
    }
    /**
     * 订单提交-业务员
     * @author JPG
     */
    @ApiOperation(value = "订单提交-业务员")
    @PostMapping(value = "/commit")
    public ResultVo commit(@Validated @RequestBody CommitOrderDto reqDto) {

        //验证用户存不存在
        Customer admin = comCustomerService.getByUserId(reqDto.getUserId(), true);
        if(admin == null){
            return BaseResultUtil.fail("用户不存在");
        }
        reqDto.setCreateUserId(admin.getUserId());
        reqDto.setCreateUserName(admin.getName());

        //发送推送信息
        return orderService.commit(reqDto);
    }


    @ApiOperation(value = "分页查询订单列表", notes = "根据条件分页查询订单", httpMethod = "POST")
    @PostMapping(value = "/getPage")
    public ResultVo<PageVo<OrderCenterVo>> getPage(@RequestBody @Validated OrderQueryDto dto){
        return orderService.getPage(dto);
    }

    @ApiOperation(value = "查询订单数量", notes = "查询各种订单状态下的订单数量", httpMethod = "POST")
    @PostMapping(value = "/getOrderCount/{loginId}")
    public ResultVo<Map<String,Object>> getOrderCount(@PathVariable Long loginId){
        return orderService.getOrderCount(loginId);
    }

    @ApiOperation(value = "取消订单", notes = "：参数orderNo(订单号),loginId(客户ID)",httpMethod = "POST")
    @PostMapping(value = "/cancelOrder")
    public ResultVo cancelOrder(@RequestBody @Validated({OrderUpdateDto.CancelAndPlaceOrder.class}) OrderUpdateDto dto){
        boolean result = orderService.update(new UpdateWrapper<Order>().lambda().set(Order::getState, OrderStateEnum.F_CANCEL.code)
                .eq(Order::getNo,dto.getOrderNo()).eq(Order::getCustomerId,dto.getLoginId()));
        return result ? BaseResultUtil.success() : BaseResultUtil.fail();
    }

    @ApiOperation(value = "确认下单", notes = "：参数orderNo(订单号),loginId(客户ID)", httpMethod = "POST")
    @PostMapping(value = "/placeOrder")
    public ResultVo placeOrder(@RequestBody @Validated({OrderUpdateDto.CancelAndPlaceOrder.class}) OrderUpdateDto dto){
        boolean result = orderService.update(new UpdateWrapper<Order>().lambda().set(Order::getState,OrderStateEnum.SUBMITTED.code)
                .eq(Order::getNo,dto.getOrderNo()).eq(Order::getCustomerId,dto.getLoginId()));
        return result ? BaseResultUtil.success() : BaseResultUtil.fail();
    }

    @ApiOperation(value = "查询订单明细", notes = "根据条件查询订单明细：参数orderNo(订单号),loginId(客户ID)", httpMethod = "POST")
    @PostMapping(value = "/getDetail")
    public ResultVo<OrderCenterDetailVo> getDetail(@RequestBody @Validated({OrderUpdateDto.GetDetail.class}) OrderUpdateDto dto){
        return orderService.getDetail(dto);
    }

    @ApiOperation(value = "确认收车", notes = "：参数orderNo(订单号),loginId(客户ID),carIdList:车辆id列表", httpMethod = "POST")
    @PostMapping(value = "/confirmPickCar")
    public ResultVo confirmPickCar(@RequestBody @Validated({OrderUpdateDto.ConfirmPickCar.class}) OrderUpdateDto dto){
        return orderService.confirmPickCar(dto);
    }


}
