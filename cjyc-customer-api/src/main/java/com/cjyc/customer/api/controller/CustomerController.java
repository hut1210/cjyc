package com.cjyc.customer.api.controller;

/**
 * @auther litan
 * @description: com.cjyc.customer.api.controller
 * @date:2019/10/9
 */

import com.cjyc.common.model.entity.Customer;
import com.cjyc.common.model.util.BaseResultUtil;
import com.cjyc.common.model.util.ResultEnum;
import com.cjyc.common.model.vo.ResultVo;
import com.cjyc.customer.api.dto.OrderDto;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

/**
 * @author leo
 * @date 2019/7/27.
 */
@RestController
@RequestMapping("/customer")
@Api(tags = "customer",description = "客户信息相关的接口")
public class CustomerController {

    /**
     * 客户端获取联系人接口
     * */
    @ApiOperation(value = "客户端获取联系人接口", notes = "客户端获取联系人接口", httpMethod = "POST")
    @RequestMapping(value = "/getContacts", method = RequestMethod.POST)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "customerId", value = "客户id",  required = true, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "page", value = "页数", defaultValue = "1", dataType = "Integer", paramType = "query"),
            @ApiImplicitParam(name = "pageSize", value = "每页条数", defaultValue = "20", dataType = "Integer", paramType = "query")
    })
    public ResultVo getContacts(String customerId, Integer page, Integer pageSize) {

        PageInfo<Customer> customerPageInfo = null;
        return BaseResultUtil.getVo(ResultEnum.SUCCESS.getCode(),ResultEnum.SUCCESS.getMsg(),customerPageInfo);
    }
}
