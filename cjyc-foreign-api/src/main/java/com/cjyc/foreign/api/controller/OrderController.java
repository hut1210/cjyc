package com.cjyc.foreign.api.controller;

import com.cjkj.common.model.ResultData;
import com.cjyc.foreign.api.dto.req.CancelOrderDto;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
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

    @ApiOperation(value = "取消订单")
    @PostMapping("/cancelOrder")
    public ResultData<String> cancelOrder(@Valid @RequestBody CancelOrderDto reqDto) {
        return ResultData.ok("成功");
    }
}
