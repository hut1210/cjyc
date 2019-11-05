package com.cjyc.web.api.controller;

import com.cjyc.common.model.dto.web.customer.*;
import com.cjyc.common.model.util.BasePageUtil;
import com.cjyc.common.model.vo.PageVo;
import com.cjyc.common.model.vo.web.customer.CustomerCountVo;
import com.cjyc.common.model.vo.web.customer.CustomerVo;
import com.cjyc.common.model.vo.web.customer.ListKeyCustomerVo;
import com.cjyc.common.model.vo.web.customer.ShowKeyCustomerVo;
import com.cjyc.common.model.enums.ResultEnum;
import com.cjyc.common.model.util.BaseResultUtil;
import com.cjyc.common.model.vo.ResultVo;
import com.cjyc.web.api.service.ICustomerService;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
        boolean result = customerService.saveCustomer(customerDto);
        return result ? BaseResultUtil.success():BaseResultUtil.fail(ResultEnum.FAIL.getMsg());
    }

    @ApiOperation(value = "更新移动端用户")
    @PostMapping(value = "/modifyCustomer")
    public ResultVo modifyCustomer(@Validated({ CustomerDto.UpdateCustomerVo.class }) @RequestBody CustomerDto customerDto){
        boolean result = customerService.modifyCustomer(customerDto);
        return result ? BaseResultUtil.success():BaseResultUtil.fail(ResultEnum.FAIL.getMsg());
    }

    @ApiOperation(value = "根据id删除移动端用户")
    @PostMapping(value = "/removeCustomer/{id}")
    public ResultVo removeCustomer(@PathVariable Long id){
        boolean result = customerService.removeById(id);
        return result ? BaseResultUtil.success():BaseResultUtil.fail(ResultEnum.FAIL.getMsg());
    }

    @ApiOperation(value = "根据条件查询移动端用户")
    @PostMapping(value = "/findCustomer")
    public ResultVo<PageVo<CustomerVo>> findCustomer(@RequestBody SelectCustomerDto dto){
        BasePageUtil.initPage(dto);
        return customerService.findCustomer(dto);
    }

    @ApiOperation(value = "新增大客户&合同")
    @PostMapping(value = "/saveKeyCustomer")
    public ResultVo saveKeyCustomer(@Validated({ KeyCustomerDto.SaveKeyCustomerVo.class }) @RequestBody KeyCustomerDto dto){
        boolean result =  customerService.saveKeyCustomer(dto);
        return result ? BaseResultUtil.success():BaseResultUtil.fail(ResultEnum.FAIL.getMsg());
    }

    @ApiOperation(value = "根据id删除/审核大用户")
    @PostMapping(value = "/removeKeyCustomer/{id}/{flag}")
    public ResultVo verifyKeyCustomer(@PathVariable @ApiParam(value = "大客户id",required = true) Long id,
                                      @PathVariable @ApiParam(value = "3：审核通过 4：审核拒绝 7:删除",required = true) Integer flag){
        boolean result = customerService.verifyKeyCustomer(id,flag);
        return result ? BaseResultUtil.success():BaseResultUtil.fail(ResultEnum.FAIL.getMsg());
    }

    @ApiOperation(value = "大客户id查看大客户&合同")
    @PostMapping(value = "/showKeyCustomer/{id}")
    public ResultVo showKeyCustomer(@PathVariable Long id){
        return customerService.showKeyCustomer(id);
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
        BasePageUtil.initPage(dto);
        return customerService.findKeyCustomer(dto);
    }

    @ApiOperation(value = "新增合伙人")
    @PostMapping(value = "/savePartner")
    public ResultVo savePartner(@Validated({ PartnerDto.SavePartnerDto.class }) @RequestBody PartnerDto dto){
        return customerService.savePartner(dto);
    }

    @ApiOperation(value = "审核/删除合伙人")
    @PostMapping(value = "/verifyPartner/{id}/{flag}")
    public ResultVo verifyPartner(@PathVariable @ApiParam(value = "合伙人id",required = true) Long id,
                                          @PathVariable @ApiParam(value = "标志 3：审核通过 4：审核拒绝 7：删除",required = true) Integer flag){
        boolean result =  customerService.verifyPartner(id,flag);
        return result ? BaseResultUtil.success():BaseResultUtil.fail(ResultEnum.FAIL.getMsg());
    }

    @ApiOperation(value = "根据条件分页查看合伙人")
    @PostMapping(value = "/findPartner")
    public ResultVo findPartner(CustomerPartnerDto dto){
        BasePageUtil.initPage(dto);
        return customerService.findPartner(dto);
    }


    @ApiOperation(value = "根据输入手机号/用户名称模糊查询用户信息")
    @PostMapping(value = "/getAllCustomerByKey/{keyword}")
    public ResultVo getAllCustomerByKey(@PathVariable @ApiParam(value = "手机号/用户名",required = true) String keyword){
        return customerService.getAllCustomerByKey(keyword);
    }

    @ApiOperation(value = "根据输入大客户userId获取大客户有效期合同")
    @PostMapping(value = "/getCustContractByUserId/{userId}")
    public ResultVo getCustContractByUserId(@PathVariable @ApiParam(value = "大客户userId",required = true) Long userId){
        return customerService.getCustContractByUserId(userId);
    }

    @ApiOperation(value = "查看客户优惠券")
    @PostMapping(value = "/getCouponByTerm")
    public ResultVo getCustomerCouponByTerm(CustomerCouponDto dto){
        return customerService.getCustomerCouponByTerm(dto);
    }

    @ApiOperation(value = "根据userId查看客户优惠券")
    @PostMapping(value = "/getCouponByUserId/{userId}")
    public ResultVo getCouponByUserId(@PathVariable @ApiParam(value = "客户userId",required = true) Long userId){
        return customerService.getCouponByUserId(userId);
    }
}