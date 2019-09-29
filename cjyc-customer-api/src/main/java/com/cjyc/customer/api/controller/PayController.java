package com.cjyc.customer.api.controller;

import io.swagger.annotations.Api;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by leo on 2019/7/25.
 */
@RequestMapping("/pay")
@Api(tags = "支付",description = "支付相关的接口")
@RestController
public class PayController {

//    @Autowired
//    private ICustomerService customerService;
//
//    @ApiOperation(value = "支付测试接口", notes = "支付测试", httpMethod = "POST")
//    @RequestMapping(value = "/createOrderPay", method = RequestMethod.POST)
//    @ApiImplicitParams({
//            @ApiImplicitParam(name = "amount", value = "支付金额,单位：分", required = true, dataType = "String", paramType = "query")
//    })
//    @CustomerApiVersionNav(group = CustomerApiVersionContant.CUSTOMER_APP_100)
//    public ResultVo testPageList(BigDecimal amount){
//        //todo
//        return ResultVo.response(ResultEnum.SUCCESS.getCode(), ResultEnum.SUCCESS.getMsg(),amount);
//    }
}
