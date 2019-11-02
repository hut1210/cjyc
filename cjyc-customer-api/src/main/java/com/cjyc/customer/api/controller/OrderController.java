package com.cjyc.customer.api.controller;

import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.cjyc.common.model.dto.customer.order.OrderQueryDto;
import com.cjyc.common.model.dto.customer.order.OrderUpdateDto;
import com.cjyc.common.model.entity.Order;
import com.cjyc.common.model.enums.order.OrderStateEnum;
import com.cjyc.common.model.util.BaseResultUtil;
import com.cjyc.common.model.vo.PageVo;
import com.cjyc.common.model.vo.ResultVo;
import com.cjyc.common.model.vo.customer.order.OrderCenterDetailVo;
import com.cjyc.common.model.vo.customer.order.OrderCenterVo;
import com.cjyc.customer.api.dto.OrderDto;
import com.cjyc.customer.api.service.IOrderService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

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

    /**
     * 客户端下单
     * */
    @ApiOperation(value = "客户端下单接口", notes = "客户端下单", httpMethod = "POST")
    @RequestMapping(value = "/commit", method = RequestMethod.POST,  consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResultVo commit(@RequestBody OrderDto orderDto) {
        boolean result = orderService.commitOrder(orderDto);
        return result ? BaseResultUtil.success(orderDto) : BaseResultUtil.fail();
    }

    /**
     * 客户修改订单接口
     * */
    @ApiOperation(value = "客户修改订单接口", notes = "客户修改订单接口", httpMethod = "POST")
    @RequestMapping(value = "/modify", method = RequestMethod.POST,  consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResultVo modify(@RequestBody OrderDto orderDto) {
        boolean result = orderService.modify(orderDto);
        return result ? BaseResultUtil.success(orderDto) : BaseResultUtil.fail();
    }

    @ApiOperation(value = "分页查询", notes = "根据条件分页查询订单", httpMethod = "POST")
    @PostMapping(value = "/getPage")
    public ResultVo<PageVo<OrderCenterVo>> getPage(@RequestBody @Validated OrderQueryDto dto){
        return orderService.getPage(dto);
    }

    @ApiOperation(value = "查询订单数量", notes = "查询各种订单状态下的订单数量", httpMethod = "POST")
    @PostMapping(value = "/getOrderCount/{userId}")
    public ResultVo<Map<String,Object>> getOrderCount(@PathVariable Long userId){
        return orderService.getOrderCount(userId);
    }

    @ApiOperation(value = "取消订单", notes = "：参数orderNo(订单号),userId(客户ID)",httpMethod = "POST")
    @PostMapping(value = "/cancelOrder")
    public ResultVo cancelOrder(@RequestBody @Validated({OrderUpdateDto.CancelAndPlaceOrder.class}) OrderUpdateDto dto){
        boolean result = orderService.update(new UpdateWrapper<Order>().lambda().set(Order::getState, OrderStateEnum.F_CANCEL.code)
                .eq(Order::getNo,dto.getOrderNo()).eq(Order::getCustomerId,dto.getUserId()));
        return result ? BaseResultUtil.success() : BaseResultUtil.fail();
    }

    @ApiOperation(value = "确认下单", notes = "：参数orderNo(订单号),userId(客户ID)", httpMethod = "POST")
    @PostMapping(value = "/placeOrder")
    public ResultVo placeOrder(@RequestBody @Validated({OrderUpdateDto.CancelAndPlaceOrder.class}) OrderUpdateDto dto){
        boolean result = orderService.update(new UpdateWrapper<Order>().lambda().set(Order::getState,OrderStateEnum.SUBMITTED.code)
                .eq(Order::getNo,dto.getOrderNo()).eq(Order::getCustomerId,dto.getUserId()));
        return result ? BaseResultUtil.success() : BaseResultUtil.fail();
    }

    @ApiOperation(value = "查询订单明细", notes = "根据条件查询订单明细：参数orderNo(订单号),userId(客户ID)", httpMethod = "POST")
    @PostMapping(value = "/getDetail")
    public ResultVo<OrderCenterDetailVo> getDetail(@RequestBody @Validated({OrderUpdateDto.GetDetail.class}) OrderUpdateDto dto){
        return orderService.getDetail(dto);
    }

    @ApiOperation(value = "确认收车", notes = "：参数orderNo(订单号),userId(客户ID),carIdList:车辆id列表", httpMethod = "POST")
    @PostMapping(value = "/confirmPickCar")
    public ResultVo confirmPickCar(@RequestBody @Validated({OrderUpdateDto.ConfirmPickCar.class}) OrderUpdateDto dto){
        return orderService.confirmPickCar(dto);
    }


}
