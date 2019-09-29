package com.cjyc.web.api.controller;

import com.cjyc.common.model.vo.ResultVo;
import com.cjyc.web.api.dto.CustomerDto;
import com.cjyc.web.api.service.ICustomerService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;


/**
 *  @author: zj
 *  @Date: 2019/9/29 14:37
 *  @Description: 韵车移动端用户
 */
@Api(description = "韵车移动端用户 | CustomerController")
@CrossOrigin
@RestController
@RequestMapping("/customerController")
public class CustomerController {

    @Autowired
    private ICustomerService iCustomerService;

    @ApiOperation(value = "新增移动端用户", notes = "新增移动端用户", httpMethod = "POST")
    @RequestMapping(value = "/saveCustomer", method = RequestMethod.POST)
    public ResultVo saveCustomer(@Validated({ CustomerDto.SaveCustomerVo.class }) @RequestBody CustomerDto customerDto){
        return iCustomerService.saveCustomer(customerDto);
    }
}