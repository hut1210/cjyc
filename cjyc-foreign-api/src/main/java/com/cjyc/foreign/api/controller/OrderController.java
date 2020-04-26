package com.cjyc.foreign.api.controller;

import com.cjyc.common.model.vo.ResultVo;
import com.cjyc.foreign.api.dto.req.CancelOrderReqDto;
import com.cjyc.foreign.api.dto.req.OrderDetailReqDto;
import com.cjyc.foreign.api.dto.req.OrderSubmitReqDto;
import com.cjyc.foreign.api.dto.req.ReleaseCarReqDto;
import com.cjyc.foreign.api.dto.res.OrderDetailResDto;
import com.cjyc.foreign.api.service.IOrderService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 *
 */
@RestController
@Api(tags = {"订单"})
@RequestMapping("/order")
public class OrderController {
    @Autowired
    private IOrderService orderService;

    /**
     * 功能描述: 下单
     * @author liuxingxiang
     * @date 2020/3/11
     * @param reqDto
     * @return com.cjyc.common.model.vo.ResultVo<java.lang.String>
     */
    @ApiOperation(value = "下单")
    @PostMapping("/submitOrder")
    public ResultVo<String> submitOrder(@RequestBody @Validated OrderSubmitReqDto reqDto) {
        return orderService.submitOrder(reqDto);
    }

    /**
     * 功能描述: 查询订单详情
     * @author liuxingxiang
     * @date 2020/3/20
     * @param dto
     * @return com.cjyc.common.model.vo.ResultVo<com.cjyc.foreign.api.dto.res.OrderDetailResDto>
     */
    @ApiOperation(value = "根据订单号查询订单详情")
    @PostMapping(value = "/getOrderDetailByOrderNo")
    public ResultVo<OrderDetailResDto> getDetail(@RequestBody @Validated OrderDetailReqDto dto){
        return orderService.getOrderDetailByOrderNo(dto);
    }

    /**
     * 功能描述: 取消订单
     * @author zhangcangman
     * @date 2020/3/20
     * @param reqDto
     * @return com.cjyc.common.model.vo.ResultVo<java.lang.String>
     */
    @ApiOperation(value = "取消订单")
    @PostMapping("/cancelOrder")
    public ResultVo<String> cancelOrder(@Valid @RequestBody CancelOrderReqDto reqDto) {
        return orderService.cancelOrder(reqDto);
    }


    /**
     * 允许放车
     * @author JPG
     * @since 2020/4/21 9:48
     * @param reqDto
     */
    @ApiOperation(value = "允许放车")
    @PostMapping("/allow/release/car")
    public ResultVo<String> allowReleaseCar(@Valid @RequestBody ReleaseCarReqDto reqDto) {
        return orderService.allowReleaseCar(reqDto);
    }

}
