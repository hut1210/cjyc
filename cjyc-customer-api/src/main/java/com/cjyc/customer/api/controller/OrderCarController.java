package com.cjyc.customer.api.controller;

import com.cjyc.customer.api.service.IOrderCarService;
import io.swagger.annotations.Api;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 *  @author: zj
 *  @Date: 2019/10/14 9:36
 *  @Description:订单明细（车辆信息）
 */
@Api(tags = "订单明细（车辆信息）")
@CrossOrigin
@RestController
@RequestMapping("/orderCar")
public class OrderCarController {

    @Resource
    private IOrderCarService iOrderCarService;

}