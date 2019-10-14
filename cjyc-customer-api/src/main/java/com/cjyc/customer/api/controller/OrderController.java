package com.cjyc.customer.api.controller;


import com.cjyc.common.model.dto.BasePageDto;
import com.cjyc.common.model.enums.ResultEnum;
import com.cjyc.common.model.util.BasePageUtil;
import com.cjyc.common.model.util.BaseResultUtil;
import com.cjyc.common.model.vo.PageVo;
import com.cjyc.common.model.vo.ResultVo;
import com.cjyc.common.model.vo.customer.OrderCenterVo;
import com.cjyc.customer.api.dto.OrderDto;
import com.cjyc.customer.api.service.IOrderService;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;


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

    @ApiOperation(value = "分页查看待确认订单", notes = "分页查看待确认订单", httpMethod = "POST")
    @PostMapping(value = "/getWaitConFirmOrders")
    public ResultVo<PageVo<OrderCenterVo>> getWaitConFirmOrders(@RequestBody BasePageDto basePageDto){
        BasePageUtil.initPage(basePageDto);
        PageInfo<OrderCenterVo> pageInfo = orderService.getWaitConFirmOrders(basePageDto);
        return BaseResultUtil.getPageVo(ResultEnum.SUCCESS.getCode(), ResultEnum.SUCCESS.getMsg(),pageInfo);
    }

    @ApiOperation(value = "分页查看运输中订单", notes = "分页查看运输中订单", httpMethod = "POST")
    @PostMapping(value = "/getTransOrders")
    public ResultVo<PageVo<OrderCenterVo>> getTransOrders(@RequestBody BasePageDto basePageDto){
        BasePageUtil.initPage(basePageDto);
        PageInfo<OrderCenterVo> pageInfo = orderService.getTransOrders(basePageDto);
        return BaseResultUtil.getPageVo(ResultEnum.SUCCESS.getCode(), ResultEnum.SUCCESS.getMsg(),pageInfo);
    }

    @ApiOperation(value = "分页查看已支付订单", notes = "分页查看已支付订单", httpMethod = "POST")
    @PostMapping(value = "/getPaidOrders")
    public ResultVo<PageVo<OrderCenterVo>> getPaidOrders(@RequestBody BasePageDto basePageDto){
        BasePageUtil.initPage(basePageDto);
        PageInfo<OrderCenterVo> pageInfo = orderService.getPaidOrders(basePageDto);
        return BaseResultUtil.getPageVo(ResultEnum.SUCCESS.getCode(), ResultEnum.SUCCESS.getMsg(),pageInfo);
    }

    @ApiOperation(value = "分页查看全部订单", notes = "分页查看全部订单", httpMethod = "POST")
    @PostMapping(value = "/getAllOrders")
    public ResultVo<PageVo<OrderCenterVo>> getAllOrders(@RequestBody BasePageDto basePageDto){
        BasePageUtil.initPage(basePageDto);
        PageInfo<OrderCenterVo> pageInfo = orderService.getAllOrders(basePageDto);
        return BaseResultUtil.getPageVo(ResultEnum.SUCCESS.getCode(), ResultEnum.SUCCESS.getMsg(),pageInfo);
    }


}
