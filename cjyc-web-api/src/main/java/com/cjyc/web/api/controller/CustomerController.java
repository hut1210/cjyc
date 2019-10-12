package com.cjyc.web.api.controller;

import com.cjyc.common.model.dto.BasePageDto;
import com.cjyc.common.model.dto.web.CustomerDto;
import com.cjyc.common.model.dto.web.KeyCustomerDto;
import com.cjyc.common.model.dto.web.SelectCustomerDto;
import com.cjyc.common.model.dto.web.SelectKeyCustomerDto;
import com.cjyc.common.model.vo.PageVo;
import com.cjyc.common.model.vo.web.CustomerVo;
import com.cjyc.common.model.vo.web.ListKeyCustomerVo;
import com.cjyc.common.model.vo.web.ShowKeyCustomerVo;
import com.cjyc.common.model.entity.Customer;
import com.cjyc.common.model.enums.ResultEnum;
import com.cjyc.common.model.util.BaseResultUtil;
import com.cjyc.common.model.vo.ResultVo;
import com.cjyc.web.api.service.ICustomerService;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
    public ResultVo saveCustomer(@Validated({ CustomerDto.SaveCustomerVo.class }) @RequestBody CustomerDto customerDto){
        boolean result = iCustomerService.saveCustomer(customerDto);
        return result ? BaseResultUtil.getVo(ResultEnum.SUCCESS.getCode(),ResultEnum.SUCCESS.getMsg())
                : BaseResultUtil.getVo(ResultEnum.FAIL.getCode(),ResultEnum.FAIL.getMsg());
    }

    @ApiOperation(value = "分页查看移动端用户", notes = "分页查看移动端用户", httpMethod = "POST")
    @PostMapping(value = "/getAllCustomer")
    public ResultVo<PageVo<Customer>> getAllCustomer(@RequestBody BasePageDto basePageDto){
        PageInfo<Customer> pageInfo = iCustomerService.getAllCustomer(basePageDto);
        return BaseResultUtil.getPageVo(ResultEnum.SUCCESS.getCode(), ResultEnum.SUCCESS.getMsg(),pageInfo);
    }

    @ApiOperation(value = "根据用户id查看移动端用户", notes = "根据用户id查看移动端用户", httpMethod = "POST")
    @PostMapping(value = "/showCustomerById/{id}")
    public ResultVo<Customer> showCustomerById(@PathVariable Long id){
        Customer customer = iCustomerService.showCustomerById(id);
        return BaseResultUtil.getVo(ResultEnum.SUCCESS.getCode(), ResultEnum.SUCCESS.getMsg(),customer);
    }


    @ApiOperation(value = "更新移动端用户", notes = "更新移动端用户", httpMethod = "POST")
    @PostMapping(value = "/updateCustomer")
    public ResultVo updateCustomer(@Validated({ CustomerDto.UpdateCustomerVo.class }) @RequestBody CustomerDto customerDto){
        boolean result = iCustomerService.updateCustomer(customerDto);
        return result ? BaseResultUtil.getVo(ResultEnum.SUCCESS.getCode(),ResultEnum.SUCCESS.getMsg())
                : BaseResultUtil.getVo(ResultEnum.FAIL.getCode(),ResultEnum.FAIL.getMsg());
    }

    @ApiOperation(value = "根据id删除移动端用户", notes = "根据id删除移动端用户", httpMethod = "POST")
    @PostMapping(value = "/delCustomerByIds")
    public ResultVo delCustomerByIds(@RequestBody List<Long> ids){
        boolean result = iCustomerService.delCustomerByIds(ids);
        return result ? BaseResultUtil.getVo(ResultEnum.SUCCESS.getCode(),ResultEnum.SUCCESS.getMsg())
                : BaseResultUtil.getVo(ResultEnum.FAIL.getCode(),ResultEnum.FAIL.getMsg());
    }

    @ApiOperation(value = "根据条件查询移动端用户", notes = "根据条件查询移动端用户", httpMethod = "POST")
    @PostMapping(value = "/findCustomer")
    public ResultVo<PageVo<CustomerVo>> findCustomer(@RequestBody SelectCustomerDto selectCustomerDto){
        PageInfo<CustomerVo> pageInfo = iCustomerService.findCustomer(selectCustomerDto);
        return BaseResultUtil.getPageVo(ResultEnum.SUCCESS.getCode(), ResultEnum.SUCCESS.getMsg(),pageInfo);
    }

    @ApiOperation(value = "新增大客户&合同", notes = "新增大客户&合同", httpMethod = "POST")
    @PostMapping(value = "/saveKeyCustAndContract")
    public ResultVo saveKeyCustAndContract(@Validated({ KeyCustomerDto.SaveKeyCustomerVo.class }) @RequestBody KeyCustomerDto keyCustomerDto){
        boolean result =  iCustomerService.saveKeyCustAndContract(keyCustomerDto);
        return result ? BaseResultUtil.getVo(ResultEnum.SUCCESS.getCode(),ResultEnum.SUCCESS.getMsg())
                : BaseResultUtil.getVo(ResultEnum.FAIL.getCode(),ResultEnum.FAIL.getMsg());
    }

    @ApiOperation(value = "根据ids删除大用户", notes = "根据ids删除大用户", httpMethod = "POST")
    @PostMapping(value = "/delKeyCustomerByIds")
    public ResultVo delKeyCustomerByIds(@RequestBody List<Long> ids){
        boolean result = iCustomerService.delKeyCustomerByIds(ids);
        return result ? BaseResultUtil.getVo(ResultEnum.SUCCESS.getCode(),ResultEnum.SUCCESS.getMsg())
                : BaseResultUtil.getVo(ResultEnum.FAIL.getCode(),ResultEnum.FAIL.getMsg());
    }

    @ApiOperation(value = "分页查看大客户", notes = "分页查看大客户", httpMethod = "POST")
    @PostMapping(value = "/getAllKeyCustomer")
    public ResultVo<PageVo<ListKeyCustomerVo>> getAllKeyCustomer(@RequestBody BasePageDto pageVo){
        PageInfo<ListKeyCustomerVo> pageInfo = iCustomerService.getAllKeyCustomer(pageVo);
        return BaseResultUtil.getPageVo(ResultEnum.SUCCESS.getCode(), ResultEnum.SUCCESS.getMsg(),pageInfo);
    }

    @ApiOperation(value = "大客户id查看大客户&合同", notes = "大客户id查看大客户&合同", httpMethod = "POST")
    @PostMapping(value = "/showKeyCustomerById/{id}")
    public ResultVo<ShowKeyCustomerVo> showKeyCustomerById(@PathVariable Long id){
        ShowKeyCustomerVo dto = iCustomerService.showKeyCustomerById(id);
        return BaseResultUtil.getVo(ResultEnum.SUCCESS.getCode(), ResultEnum.SUCCESS.getMsg(),dto);
    }

    @ApiOperation(value = "更新大客户&合同", notes = "更新大客户&合同", httpMethod = "POST")
    @PostMapping(value = "/updateKeyCustomer")
    public ResultVo updateKeyCustomer(@Validated({ KeyCustomerDto.UpdateKeyCustomerVo.class }) @RequestBody KeyCustomerDto customerDto){
        boolean result = iCustomerService.updateKeyCustomer(customerDto);
        return result ? BaseResultUtil.getVo(ResultEnum.SUCCESS.getCode(),ResultEnum.SUCCESS.getMsg())
                : BaseResultUtil.getVo(ResultEnum.FAIL.getCode(),ResultEnum.FAIL.getMsg());
    }

    @ApiOperation(value = "根据条件查询大客户", notes = "根据条件查询大客户", httpMethod = "POST")
    @PostMapping(value = "/findKeyCustomer")
    public ResultVo<PageVo<ListKeyCustomerVo>> findKeyCustomer(@RequestBody SelectKeyCustomerDto selectKeyCustomerDto){
        PageInfo<ListKeyCustomerVo> pageInfo = iCustomerService.findKeyCustomer(selectKeyCustomerDto);
        return BaseResultUtil.getPageVo(ResultEnum.SUCCESS.getCode(), ResultEnum.SUCCESS.getMsg(),pageInfo);
    }
}