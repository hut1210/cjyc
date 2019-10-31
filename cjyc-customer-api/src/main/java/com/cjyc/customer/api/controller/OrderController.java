package com.cjyc.customer.api.controller;


import com.cjyc.common.model.dto.customer.OrderConditionDto;
import com.cjyc.common.model.util.BaseResultUtil;
import com.cjyc.common.model.vo.PageVo;
import com.cjyc.common.model.vo.ResultVo;
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
@Api(tags = "订单",description = "订单相关接口")
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

    @ApiOperation(value = "根据条件分页查询订单", notes = "根据条件分页查询订单", httpMethod = "POST")
    @PostMapping(value = "/getPage")
    public ResultVo<PageVo<OrderCenterVo>> getPage(@RequestBody @Validated OrderConditionDto dto){
        return orderService.getPage(dto);
    }

    @ApiOperation(value = "查询各种订单状态下的订单数量", notes = "根据条件分页查询订单", httpMethod = "POST")
    @PostMapping(value = "/getOrderCount/{customerId}")
    public ResultVo<Map<String,Object>> getOrderCount(@PathVariable Long customerId){
        return orderService.getOrderCount(customerId);
    }


}
