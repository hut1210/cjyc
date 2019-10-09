package com.cjyc.web.api.controller;

import com.alibaba.fastjson.JSONObject;
import com.cjyc.common.model.entity.Customer;
import com.cjyc.common.model.enums.ResultEnum;
import com.cjyc.common.model.util.BaseResultUtil;
import com.cjyc.common.model.vo.CustomerVo;
import com.cjyc.common.model.vo.KeyCustomerDto;
import com.cjyc.common.model.vo.ResultVo;
import com.cjyc.web.api.dto.BasePageVo;
import com.cjyc.web.api.dto.CustomerDto;
import com.cjyc.web.api.dto.KeyCustomerVo;
import com.cjyc.web.api.service.ICustomerService;
import com.github.pagehelper.PageInfo;
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
        boolean result = iCustomerService.saveCustomer(customerDto);
        return result ? BaseResultUtil.getVo(ResultEnum.SUCCESS.getCode(),ResultEnum.SUCCESS.getMsg())
                : BaseResultUtil.getVo(ResultEnum.FAIL.getCode(),ResultEnum.FAIL.getMsg());
    }

    @ApiOperation(value = "分页查看移动端用户", notes = "分页查看移动端用户", httpMethod = "POST")
    @PostMapping(value = "/getAllCustomer/{pageNo}/{pageSize}")
    public ResultVo getAllCustomer(@PathVariable Integer pageNo,@PathVariable Integer pageSize){
        PageInfo<Customer> pageInfo = iCustomerService.getAllCustomer(pageNo,pageSize);
        return BaseResultUtil.getVo(ResultEnum.SUCCESS.getCode(), ResultEnum.SUCCESS.getMsg(),pageInfo);
    }

    @ApiOperation(value = "根据用户id查看移动端用户", notes = "根据用户id查看移动端用户", httpMethod = "GET")
    @GetMapping(value = "/showCustomerById/{id}")
    public ResultVo showCustomerById(@PathVariable Long id){
        Customer customer = iCustomerService.showCustomerById(id);
        return BaseResultUtil.getVo(ResultEnum.SUCCESS.getCode(), ResultEnum.SUCCESS.getMsg(),customer);
    }


    @ApiOperation(value = "更新移动端用户", notes = "更新移动端用户", httpMethod = "PUT")
    @PutMapping(value = "/updateCustomer")
    public ResultVo updateCustomer(@Validated({ CustomerDto.UpdateCustomerVo.class }) @RequestBody CustomerDto customerDto){
        boolean result = iCustomerService.updateCustomer(customerDto);
        return result ? BaseResultUtil.getVo(ResultEnum.SUCCESS.getCode(),ResultEnum.SUCCESS.getMsg())
                : BaseResultUtil.getVo(ResultEnum.FAIL.getCode(),ResultEnum.FAIL.getMsg());
    }

    @ApiOperation(value = "根据id删除移动端用户", notes = "根据id删除移动端用户", httpMethod = "DELETE")
    @DeleteMapping(value = "/deleteCustomer/{arrIds}")
    public ResultVo deleteCustomer(@PathVariable Long[] arrIds){
        boolean result = iCustomerService.deleteCustomer(arrIds);
        return result ? BaseResultUtil.getVo(ResultEnum.SUCCESS.getCode(),ResultEnum.SUCCESS.getMsg())
                : BaseResultUtil.getVo(ResultEnum.FAIL.getCode(),ResultEnum.FAIL.getMsg());
    }

    @ApiOperation(value = "根据id删除移动端用户", notes = "根据id删除移动端用户", httpMethod = "POST")
    @PostMapping(value = "/findCustomer")
    public ResultVo findCustomer(@RequestBody JSONObject jsonObject){
        PageInfo<CustomerVo> pageInfo = iCustomerService.findCustomer(jsonObject);
        return BaseResultUtil.getVo(ResultEnum.SUCCESS.getCode(), ResultEnum.SUCCESS.getMsg(),pageInfo);
    }

    @ApiOperation(value = "新增大客户&合同", notes = "新增大客户&合同", httpMethod = "POST")
    @RequestMapping(value = "/saveKeyCustAndContract", method = RequestMethod.POST)
    public ResultVo saveKeyCustAndContract(@Validated({ KeyCustomerVo.SaveKeyCustomerVo.class }) @RequestBody KeyCustomerVo keyCustomerVo){
        return iCustomerService.saveKeyCustAndContract(keyCustomerVo);
    }

    @ApiOperation(value = "根据ids删除大用户", notes = "根据ids删除大用户", httpMethod = "DELETE")
    @DeleteMapping(value = "/deleteKeyCustomer/{arrIds}")
    public ResultVo deleteKeyCustomer(@PathVariable Long[] arrIds){
        return iCustomerService.deleteKeyCustomer(arrIds);
    }

    @ApiOperation(value = "分页查看大客户", notes = "分页查看大客户", httpMethod = "POST")
    @PostMapping(value = "/getAllKeyCustomer")
    public ResultVo getAllKeyCustomer(@RequestBody BasePageVo pageVo){
        PageInfo<KeyCustomerDto> pageInfo = iCustomerService.getAllKeyCustomer(pageVo);
        return BaseResultUtil.getVo(ResultEnum.SUCCESS.getCode(), ResultEnum.SUCCESS.getMsg(),pageInfo);
    }

}