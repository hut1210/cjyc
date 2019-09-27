package com.cjyc.customer.api.controller;


import com.cjkj.common.utils.JsonUtil;
import com.cjyc.customer.api.annotations.ApiVersion;
import com.cjyc.customer.api.config.RetCodeEnum;
import com.cjyc.customer.api.config.RetResult;
import com.cjyc.customer.api.entity.Customer;
import com.cjyc.customer.api.service.ICustomerService;
import com.cjyc.customer.api.until.ApiVersionContant;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by leo on 2019/7/25.
 */
@RequestMapping("/order")
@Api(tags = "订单",description = "订单录相关的接口")
@RestController
public class OrderController {

    @Autowired
    private ICustomerService customerService;

    /**
     * 测试分页
     * */
    @ApiOperation(value = "分页测试接口", notes = "分页测试", httpMethod = "POST")
    @RequestMapping(value = "/testPageList", method = RequestMethod.POST)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "pageNum", value = "当前页", required = true, dataType = "Integer", paramType = "query"),
            @ApiImplicitParam(name = "pageSize", value = "条数", required = true, dataType = "Integer", paramType = "query")
    })
    @ApiVersion(group = ApiVersionContant.CUSTOMER_APP_100)
    public String testPageList(Integer pageNum, Integer pageSize){
        PageInfo<Customer> customerPageInfo = customerService.pageList(pageNum,pageSize);
        return JsonUtil.toJson(RetResult.buildResponse(RetCodeEnum.SUCCESS.getCode(),RetCodeEnum.SUCCESS.getMsg(),customerPageInfo));
    }
}
