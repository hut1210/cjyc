package com.cjyc.salesman.api.controller;

import com.cjyc.common.model.util.BaseResultUtil;
import com.cjyc.common.model.vo.ResultVo;
import com.cjyc.salesman.api.dto.OrderDto;
import com.cjyc.salesman.api.service.IOrderService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.jaxb.SpringDataJaxb;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * @auther litan
 * @description: com.cjyc.salesman.api.controller
 * @date:2019/10/14
 */
@Api(tags = "订单")
@Slf4j
@RestController
@RequestMapping(value = "/order",
        consumes = MediaType.APPLICATION_JSON_UTF8_VALUE,
        produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
public class OrderController {

    @Autowired
    IOrderService orderService;

    /**
     * 确认订单
     * */
    @ApiOperation(value = "客户端下单接口", notes = "客户端下单", httpMethod = "POST")
    @RequestMapping(value = "/confirm", method = RequestMethod.POST,  consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResultVo confirm(@RequestBody OrderDto orderDto) {
        boolean result = orderService.confirm(orderDto);
        return result ? BaseResultUtil.success(orderDto) : BaseResultUtil.fail();
    }

}
