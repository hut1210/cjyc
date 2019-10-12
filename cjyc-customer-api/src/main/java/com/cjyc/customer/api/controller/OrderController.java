package com.cjyc.customer.api.controller;

import com.cjyc.common.model.enums.ResultEnum;
import com.cjyc.common.model.util.BaseResultUtil;
import com.cjyc.common.model.vo.ResultVo;
import com.cjyc.customer.api.dto.OrderDto;
import com.cjyc.customer.api.service.IOrderService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;


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
     * 客户端下单--dto接收
     * */
    @ApiOperation(value = "客户端下单接口", notes = "客户端下单", httpMethod = "POST")
    @RequestMapping(value = "/commitOrder", method = RequestMethod.POST,  consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResultVo commitOrder(@RequestBody OrderDto orderDto) {
        boolean result = orderService.commitOrder(orderDto);

        return result ? BaseResultUtil.getVo(ResultEnum.SUCCESS.getCode(), ResultEnum.SUCCESS.getMsg(),orderDto)
                : BaseResultUtil.getVo(ResultEnum.FAIL.getCode(),"提交失败",orderDto);
    }
}
