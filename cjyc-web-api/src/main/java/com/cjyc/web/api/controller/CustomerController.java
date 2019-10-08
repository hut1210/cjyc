package com.cjyc.web.api.controller;

import com.alibaba.fastjson.JSONObject;
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

    @ApiOperation(value = "分页查看移动端用户", notes = "分页查看移动端用户", httpMethod = "POST")
    @PostMapping(value = "/getAllCustomer/{pageNo}/{pageSize}")
    public ResultVo getAllCustomer(@PathVariable Integer pageNo,@PathVariable Integer pageSize){
        return iCustomerService.getAllCustomer(pageNo,pageSize);
    }

    @ApiOperation(value = "根据用户id查看移动端用户", notes = "根据用户id查看移动端用户", httpMethod = "GET")
    @GetMapping(value = "/showCustomerById/{id}")
    public ResultVo showCustomerById(@PathVariable Long id){
        return iCustomerService.showCustomerById(id);
    }


    @ApiOperation(value = "更新移动端用户", notes = "更新移动端用户", httpMethod = "PUT")
    @PutMapping(value = "/updateCustomer")
    public ResultVo updateCustomer(@Validated({ CustomerDto.UpdateCustomerVo.class }) @RequestBody CustomerDto customerDto){
        return iCustomerService.updateCustomer(customerDto);
    }

    @ApiOperation(value = "根据id删除移动端用户", notes = "根据id删除移动端用户", httpMethod = "DELETE")
    @DeleteMapping(value = "/deleteCustomer/{arrIds}")
    public ResultVo deleteCustomer(@PathVariable Long[] arrIds){
        return iCustomerService.deleteCustomer(arrIds);
    }

    @ApiOperation(value = "根据id删除移动端用户", notes = "根据id删除移动端用户", httpMethod = "POST")
    @PostMapping(value = "/findCustomer")
    public ResultVo findCustomer(@RequestBody JSONObject jsonObject){
        return iCustomerService.findCustomer(jsonObject);
    }
}