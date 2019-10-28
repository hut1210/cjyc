package com.cjyc.web.api.controller;

import com.cjyc.common.model.dto.web.customer.*;
import com.cjyc.common.model.util.BasePageUtil;
import com.cjyc.common.model.vo.PageVo;
import com.cjyc.common.model.vo.web.CustomerVo;
import com.cjyc.common.model.vo.web.ListKeyCustomerVo;
import com.cjyc.common.model.vo.web.ShowKeyCustomerVo;
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
 *  @Description: 韵车大客户/移动端用户
 */
@Api(tags = "韵车大客户/移动端用户")
@CrossOrigin
@RestController
@RequestMapping("/customer")
public class CustomerController {

    @Autowired
    private ICustomerService iCustomerService;

    @ApiOperation(value = "新增移动端用户")
    @PostMapping(value = "/saveCustomer")
    public ResultVo saveCustomer(@Validated({ CustomerDto.SaveCustomerVo.class }) @RequestBody CustomerDto customerDto){
        boolean result = iCustomerService.saveCustomer(customerDto);
        return result ? BaseResultUtil.getVo(ResultEnum.SUCCESS.getCode(),ResultEnum.SUCCESS.getMsg())
                : BaseResultUtil.getVo(ResultEnum.FAIL.getCode(),ResultEnum.FAIL.getMsg());
    }

    @ApiOperation(value = "更新移动端用户")
    @PostMapping(value = "/updateCustomer")
    public ResultVo updateCustomer(@Validated({ CustomerDto.UpdateCustomerVo.class }) @RequestBody CustomerDto customerDto){
        boolean result = iCustomerService.updateCustomer(customerDto);
        return result ? BaseResultUtil.getVo(ResultEnum.SUCCESS.getCode(),ResultEnum.SUCCESS.getMsg())
                : BaseResultUtil.getVo(ResultEnum.FAIL.getCode(),ResultEnum.FAIL.getMsg());
    }

    @ApiOperation(value = "根据id删除移动端用户")
    @PostMapping(value = "/delCustomerByIds")
    public ResultVo delCustomerByIds(@RequestBody List<Long> ids){
        boolean result = iCustomerService.delCustomerByIds(ids);
        return result ? BaseResultUtil.getVo(ResultEnum.SUCCESS.getCode(),ResultEnum.SUCCESS.getMsg())
                : BaseResultUtil.getVo(ResultEnum.FAIL.getCode(),ResultEnum.FAIL.getMsg());
    }

    @ApiOperation(value = "根据条件查询移动端用户")
    @PostMapping(value = "/findCustomerByTerm")
    public ResultVo<PageVo<CustomerVo>> findCustomerByTerm(@RequestBody SelectCustomerDto dto){
        BasePageUtil.initPage(dto.getCurrentPage(),dto.getPageSize());
        PageInfo<CustomerVo> pageInfo = iCustomerService.findCustomerByTerm(dto);
        return BaseResultUtil.getPageVo(ResultEnum.SUCCESS.getCode(), ResultEnum.SUCCESS.getMsg(),pageInfo);
    }

    @ApiOperation(value = "新增大客户&合同")
    @PostMapping(value = "/saveKeyCustAndContract")
    public ResultVo saveKeyCustAndContract(@Validated({ KeyCustomerDto.SaveKeyCustomerVo.class }) @RequestBody KeyCustomerDto keyCustomerDto){
        boolean result =  iCustomerService.saveKeyCustAndContract(keyCustomerDto);
        return result ? BaseResultUtil.getVo(ResultEnum.SUCCESS.getCode(),ResultEnum.SUCCESS.getMsg())
                : BaseResultUtil.getVo(ResultEnum.FAIL.getCode(),ResultEnum.FAIL.getMsg());
    }

    @ApiOperation(value = "根据ids删除大用户")
    @PostMapping(value = "/delKeyCustomerByIds")
    public ResultVo delKeyCustomerByIds(@RequestBody List<Long> ids){
        boolean result = iCustomerService.delKeyCustomerByIds(ids);
        return result ? BaseResultUtil.getVo(ResultEnum.SUCCESS.getCode(),ResultEnum.SUCCESS.getMsg())
                : BaseResultUtil.getVo(ResultEnum.FAIL.getCode(),ResultEnum.FAIL.getMsg());
    }

    @ApiOperation(value = "大客户id查看大客户&合同", notes = "大客户id查看大客户&合同", httpMethod = "POST")
    @PostMapping(value = "/showKeyCustomerById/{id}")
    public ResultVo<ShowKeyCustomerVo> showKeyCustomerById(@PathVariable Long id){
        ShowKeyCustomerVo dto = iCustomerService.showKeyCustomerById(id);
        return BaseResultUtil.getVo(ResultEnum.SUCCESS.getCode(), ResultEnum.SUCCESS.getMsg(),dto);
    }

    @ApiOperation(value = "更新大客户&合同")
    @PostMapping(value = "/updateKeyCustomer")
    public ResultVo updateKeyCustomer(@Validated({ KeyCustomerDto.UpdateKeyCustomerVo.class }) @RequestBody KeyCustomerDto customerDto){
        boolean result = iCustomerService.updateKeyCustomer(customerDto);
        return result ? BaseResultUtil.getVo(ResultEnum.SUCCESS.getCode(),ResultEnum.SUCCESS.getMsg())
                : BaseResultUtil.getVo(ResultEnum.FAIL.getCode(),ResultEnum.FAIL.getMsg());
    }

    @ApiOperation(value = "根据条件查询大客户")
    @PostMapping(value = "/findKeyCustomer")
    public ResultVo<PageVo<ListKeyCustomerVo>> findKeyCustomer(@RequestBody SelectKeyCustomerDto dto){
        BasePageUtil.initPage(dto.getCurrentPage(),dto.getPageSize());
        PageInfo<ListKeyCustomerVo> pageInfo = iCustomerService.findKeyCustomer(dto);
        return BaseResultUtil.getPageVo(ResultEnum.SUCCESS.getCode(), ResultEnum.SUCCESS.getMsg(),pageInfo);
    }

    @ApiOperation(value = "新增/更新合伙人")
    @PostMapping(value = "/addOrUpdatePartner")
    public ResultVo addOrUpdatePartner(@Validated({ PartnerDto.SaveOrUpdatePartnerDto.class }) @RequestBody PartnerDto dto){
        return iCustomerService.addOrUpdatePartner(dto);
    }

    @ApiOperation(value = "审核/删除合伙人")
    @PostMapping(value = "/verifyOrDeletePartner/{id}/{flag}")
    public ResultVo verifyOrDeletePartner(@PathVariable @ApiParam(value = "合伙人id",required = true) Long id,
                                          @PathVariable @ApiParam(value = "标志 3：审核通过 7：删除",required = true) Integer flag){
        return iCustomerService.verifyOrDeletePartner(id,flag);
    }

}