package com.cjyc.web.api.controller;

import com.cjyc.common.base.ResultEnum;
import com.cjyc.common.base.ResultVo;
import com.cjyc.common.entity.Customer;
import com.cjyc.common.service.ICustomerService;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;


/**
 * @auther litan
 * @description: com.cjyc.web.api.controller
 * @date:2019/9/28
 */
@RestController
@RequestMapping("/order")
@Api(tags = "order",description = "订单接口,包含下单、查询等")
public class OrderController {

    @Resource(name="customerService")
    private ICustomerService customerServiceimpl;
    /**
     * 测试分页
     * */
    @ApiOperation(value = "分页测试接口", notes = "分页测试", httpMethod = "POST")
    @RequestMapping(value = "/testPageList", method = RequestMethod.POST)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "pageNum", value = "当前页", required = true, dataType = "Integer", paramType = "query"),
            @ApiImplicitParam(name = "pageSize", value = "条数", required = true, dataType = "Integer", paramType = "query")
    })
    public ResultVo testPageList(Integer pageNum, Integer pageSize){

        PageInfo<Customer> customerPageInfo = customerServiceimpl.pageList(pageNum,pageSize);
        return ResultVo.response(ResultEnum.SUCCESS.getCode(), ResultEnum.SUCCESS.getMsg(),customerPageInfo);
    }
}
