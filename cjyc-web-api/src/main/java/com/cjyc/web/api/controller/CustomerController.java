package com.cjyc.web.api.controller;

import com.cjyc.common.model.vo.PageVo;
import com.cjyc.common.model.vo.web.CustomerVo;
import com.cjyc.common.model.vo.web.ListKeyCustomerVo;
import com.cjyc.common.model.vo.web.ShowKeyCustomerVo;
import com.cjyc.common.model.entity.Customer;
import com.cjyc.common.model.enums.ResultEnum;
import com.cjyc.common.model.util.BaseResultUtil;
import com.cjyc.common.model.vo.ResultVo;
import com.cjyc.common.model.vo.BasePageVo;
import com.cjyc.common.model.vo.web.CustomerVo;
import com.cjyc.common.model.vo.web.KeyCustomerVo;
import com.cjyc.common.model.vo.web.SelectCustomerVo;
import com.cjyc.common.model.vo.web.SelectKeyCustomerVo;
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
 *  @Description: 韵车大客户/移动端用户
 */
@Api(tags = "韵车大客户/移动端用户")
@CrossOrigin
@RestController
@RequestMapping("/customerController")
public class CustomerController {

    @Autowired
    private ICustomerService iCustomerService;

    @ApiOperation(value = "新增移动端用户", notes = "新增移动端用户", httpMethod = "POST")
    @RequestMapping(value = "/saveCustomer", method = RequestMethod.POST)
    public ResultVo saveCustomer(@Validated({ CustomerVo.SaveCustomerVo.class }) @RequestBody CustomerVo customerVo){
        boolean result = iCustomerService.saveCustomer(customerVo);
        return result ? BaseResultUtil.getVo(ResultEnum.SUCCESS.getCode(),ResultEnum.SUCCESS.getMsg())
                : BaseResultUtil.getVo(ResultEnum.FAIL.getCode(),ResultEnum.FAIL.getMsg());
    }

    @ApiOperation(value = "分页查看移动端用户", notes = "分页查看移动端用户", httpMethod = "POST")
    @PostMapping(value = "/getAllCustomer")
    public ResultVo getAllCustomer(@RequestBody BasePageVo basePageVo){
        PageInfo<Customer> pageInfo = iCustomerService.getAllCustomer(basePageVo);
        return BaseResultUtil.getPageVo(ResultEnum.SUCCESS.getCode(), ResultEnum.SUCCESS.getMsg(),pageInfo);
    }

    @ApiOperation(value = "根据用户id查看移动端用户", notes = "根据用户id查看移动端用户", httpMethod = "GET")
    @GetMapping(value = "/showCustomerById/{id}")
    public ResultVo showCustomerById(@PathVariable Long id){
        Customer customer = iCustomerService.showCustomerById(id);
        return BaseResultUtil.getVo(ResultEnum.SUCCESS.getCode(), ResultEnum.SUCCESS.getMsg(),customer);
    }


    @ApiOperation(value = "更新移动端用户", notes = "更新移动端用户", httpMethod = "PUT")
    @PutMapping(value = "/updateCustomer")
    public ResultVo updateCustomer(@Validated({ CustomerVo.UpdateCustomerVo.class }) @RequestBody CustomerVo customerDto){
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

    @ApiOperation(value = "根据条件查询移动端用户", notes = "根据条件查询移动端用户", httpMethod = "POST")
    @PostMapping(value = "/findCustomer")
    public ResultVo findCustomer(@RequestBody SelectCustomerVo selectCustomerVo){
        PageInfo<CustomerDto> pageInfo = iCustomerService.findCustomer(selectCustomerVo);
        return BaseResultUtil.getPageVo(ResultEnum.SUCCESS.getCode(), ResultEnum.SUCCESS.getMsg(),pageInfo);
    }

    @ApiOperation(value = "新增大客户&合同", notes = "新增大客户&合同", httpMethod = "POST")
    @RequestMapping(value = "/saveKeyCustAndContract", method = RequestMethod.POST)
    public ResultVo saveKeyCustAndContract(@Validated({ KeyCustomerVo.SaveKeyCustomerVo.class }) @RequestBody KeyCustomerVo keyCustomerVo){
        boolean result =  iCustomerService.saveKeyCustAndContract(keyCustomerVo);
        return result ? BaseResultUtil.getVo(ResultEnum.SUCCESS.getCode(),ResultEnum.SUCCESS.getMsg())
                : BaseResultUtil.getVo(ResultEnum.FAIL.getCode(),ResultEnum.FAIL.getMsg());
    }

    @ApiOperation(value = "根据ids删除大用户", notes = "根据ids删除大用户", httpMethod = "DELETE")
    @DeleteMapping(value = "/deleteKeyCustomer/{arrIds}")
    public ResultVo deleteKeyCustomer(@PathVariable Long[] arrIds){
        boolean result = iCustomerService.deleteKeyCustomer(arrIds);
        return result ? BaseResultUtil.getVo(ResultEnum.SUCCESS.getCode(),ResultEnum.SUCCESS.getMsg())
                : BaseResultUtil.getVo(ResultEnum.FAIL.getCode(),ResultEnum.FAIL.getMsg());
    }

    @ApiOperation(value = "分页查看大客户", notes = "分页查看大客户", httpMethod = "POST")
    @PostMapping(value = "/getAllKeyCustomer")
    public ResultVo getAllKeyCustomer(@RequestBody BasePageVo pageVo){
        PageInfo<ListKeyCustomerDto> pageInfo = iCustomerService.getAllKeyCustomer(pageVo);
        return BaseResultUtil.getPageVo(ResultEnum.SUCCESS.getCode(), ResultEnum.SUCCESS.getMsg(),pageInfo);
    }

    @ApiOperation(value = "大客户id查看大客户&合同", notes = "大客户id查看大客户&合同", httpMethod = "GET")
    @GetMapping(value = "/showKeyCustomerById/{id}")
    public ResultVo showKeyCustomerById(@PathVariable Long id){
        ShowKeyCustomerDto dto = iCustomerService.showKeyCustomerById(id);
        return BaseResultUtil.getVo(ResultEnum.SUCCESS.getCode(), ResultEnum.SUCCESS.getMsg(),dto);
    }

    @ApiOperation(value = "更新大客户&合同", notes = "更新大客户&合同", httpMethod = "PUT")
    @PutMapping(value = "/updateKeyCustomer")
    public ResultVo updateKeyCustomer(@Validated({ KeyCustomerVo.UpdateKeyCustomerVo.class }) @RequestBody KeyCustomerVo customerVo){
        boolean result = iCustomerService.updateKeyCustomer(customerVo);
        return result ? BaseResultUtil.getVo(ResultEnum.SUCCESS.getCode(),ResultEnum.SUCCESS.getMsg())
                : BaseResultUtil.getVo(ResultEnum.FAIL.getCode(),ResultEnum.FAIL.getMsg());
    }

    @ApiOperation(value = "根据条件查询大客户", notes = "根据条件查询大客户", httpMethod = "POST")
    @PostMapping(value = "/findKeyCustomer")
    public ResultVo findKeyCustomer(@RequestBody SelectKeyCustomerVo selectKeyCustomerVo){
        PageInfo<ListKeyCustomerDto> pageInfo = iCustomerService.findKeyCustomer(selectKeyCustomerVo);
        return BaseResultUtil.getPageVo(ResultEnum.SUCCESS.getCode(), ResultEnum.SUCCESS.getMsg(),pageInfo);
    }
}