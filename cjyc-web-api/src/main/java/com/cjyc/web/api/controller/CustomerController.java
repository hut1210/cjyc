package com.cjyc.web.api.controller;

import com.cjyc.common.model.dto.web.OperateDto;
import com.cjyc.common.model.dto.web.customer.*;
import com.cjyc.common.model.vo.PageVo;
import com.cjyc.common.model.vo.web.customer.CustomerVo;
import com.cjyc.common.model.enums.ResultEnum;
import com.cjyc.common.model.util.BaseResultUtil;
import com.cjyc.common.model.vo.ResultVo;
import com.cjyc.web.api.service.ICustomerService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 *  @author: zj
 *  @Date: 2019/9/29 14:37
 *  @Description: 韵车C端客户/大客户/合伙人
 */
@Api(tags = "韵车C端客户/大客户/合伙人")
@CrossOrigin
@RestController
@RequestMapping("/customer")
public class CustomerController {

    @Autowired
    private ICustomerService customerService;

    @ApiOperation(value = "新增移动端用户")
    @PostMapping(value = "/saveCustomer")
    public ResultVo saveCustomer(@Validated({ CustomerDto.SaveCustomerVo.class }) @RequestBody CustomerDto customerDto){
        return customerService.saveCustomer(customerDto);
    }

    @ApiOperation(value = "更新移动端用户")
    @PostMapping(value = "/modifyCustomer")
    public ResultVo modifyCustomer(@Validated({ CustomerDto.UpdateCustomerVo.class }) @RequestBody CustomerDto customerDto){
        boolean result = customerService.modifyCustomer(customerDto);
        return result ? BaseResultUtil.success():BaseResultUtil.fail(ResultEnum.FAIL.getMsg());
    }

    @ApiOperation(value = "根据条件查询移动端用户")
    @PostMapping(value = "/findCustomer")
    public ResultVo<PageVo<CustomerVo>> findCustomer(@RequestBody SelectCustomerDto dto){
        return customerService.findCustomer(dto);
    }

    @ApiOperation(value = "新增大客户&合同")
    @PostMapping(value = "/saveKeyCustomer")
    public ResultVo saveKeyCustomer(@Validated({ KeyCustomerDto.SaveKeyCustomerVo.class }) @RequestBody KeyCustomerDto dto){
        boolean result =  customerService.saveKeyCustomer(dto);
        return result ? BaseResultUtil.success():BaseResultUtil.fail(ResultEnum.FAIL.getMsg());
    }

    @ApiOperation(value = "根据主键id/当前登陆用户loginId删除/审核用户")
    @PostMapping(value = "/verifyCustomer")
    public ResultVo verifyCustomer(@RequestBody OperateDto dto){
        boolean result = customerService.verifyCustomer(dto);
        return result ? BaseResultUtil.success():BaseResultUtil.fail(ResultEnum.FAIL.getMsg());
    }

    @ApiOperation(value = "大客户customerId查看大客户&合同")
    @PostMapping(value = "/showKeyCustomer/{customerId}")
    public ResultVo showKeyCustomer(@PathVariable Long customerId){
        return customerService.showKeyCustomer(customerId);
    }

    @ApiOperation(value = "更新大客户&合同")
    @PostMapping(value = "/modifyKeyCustomer")
    public ResultVo modifyKeyCustomer(@Validated({ KeyCustomerDto.UpdateKeyCustomerVo.class }) @RequestBody KeyCustomerDto customerDto){
        boolean result = customerService.modifyKeyCustomer(customerDto);
        return result ? BaseResultUtil.success():BaseResultUtil.fail(ResultEnum.FAIL.getMsg());
    }

    @ApiOperation(value = "根据条件查询大客户")
    @PostMapping(value = "/findKeyCustomer")
    public ResultVo findKeyCustomer(@RequestBody SelectKeyCustomerDto dto){
        return customerService.findKeyCustomer(dto);
    }

    @ApiOperation(value = "新增合伙人")
    @PostMapping(value = "/savePartner")
    public ResultVo savePartner(@Validated({ PartnerDto.SavePartnerDto.class }) @RequestBody PartnerDto dto){
        return customerService.savePartner(dto);
    }

    @ApiOperation(value = "更新合伙人")
    @PostMapping(value = "/modifyPartner")
    public ResultVo modifyPartner(@Validated({ PartnerDto.updatePartnerDto.class }) @RequestBody PartnerDto dto){
        return customerService.modifyPartner(dto);
    }

    @ApiOperation(value = "根据条件分页查看合伙人")
    @PostMapping(value = "/findPartner")
    public ResultVo findPartner(@RequestBody CustomerPartnerDto dto){
        return customerService.findPartner(dto);
    }

    @ApiOperation(value = "根据输入手机号/用户名称模糊查询用户信息")
    @PostMapping(value = "/getAllCustomerByKey/{keyword}")
    public ResultVo getAllCustomerByKey(@PathVariable @ApiParam(value = "手机号/用户名",required = true) String keyword){
        return customerService.getAllCustomerByKey(keyword);
    }

    @ApiOperation(value = "根据输入大客户id(customerId)获取大客户有效期合同")
    @PostMapping(value = "/getContractByCustomerId/{customerId}")
    public ResultVo getContractByCustomerId(@PathVariable @ApiParam(value = "大客户id",required = true) Long customerId){
        return customerService.getContractByCustomerId(customerId);
    }

    @ApiOperation(value = "查看客户优惠券")
    @PostMapping(value = "/customerCoupon")
    public ResultVo customerCoupon(@RequestBody CustomerCouponDto dto){
        return customerService.customerCoupon(dto);
    }

    @ApiOperation(value = "根据customerId查看客户优惠券")
    @PostMapping(value = "/getCouponByCustomerId/{customerId}")
    public ResultVo getCouponByCustomerId(@PathVariable @ApiParam(value = "客户customerId",required = true) Long customerId){
        return customerService.getCouponByCustomerId(customerId);
    }
}