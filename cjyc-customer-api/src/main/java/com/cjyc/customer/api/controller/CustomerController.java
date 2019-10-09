package com.cjyc.customer.api.controller;

/**
 * @auther litan
 * @description: com.cjyc.customer.api.controller
 * @date:2019/10/9
 */

import com.cjyc.common.model.entity.Customer;
import com.cjyc.common.model.entity.CustomerContact;
import com.cjyc.common.model.util.BaseResultUtil;
import com.cjyc.common.model.vo.ResultVo;
import com.cjyc.common.service.service.ICustomerService;
import com.cjyc.customer.api.dto.OrderDto;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author leo
 * @date 2019/7/27.
 */
@RestController
@RequestMapping("/customer")
@Api(tags = "customer",description = "客户信息相关的接口")
public class CustomerController {

    @Autowired
    private ICustomerService customerService;


}
