package com.cjyc.web.api.controller;

import com.cjyc.common.model.dto.web.OperateDto;
import com.cjyc.common.model.dto.web.customer.*;
import com.cjyc.common.model.vo.PageVo;
import com.cjyc.common.model.vo.web.coupon.CustomerCouponSendVo;
import com.cjyc.common.model.vo.web.customer.*;
import com.cjyc.common.model.vo.ResultVo;
import com.cjyc.web.api.service.ICustomerService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 *  @author: zj
 *  @Date: 2019/9/29 14:37
 *  @Description: 韵车C端客户/大客户/合伙人
 */
@Api(tags = "客户")
@CrossOrigin
@RestController
@RequestMapping("/customer")
public class CustomerController {

    @Autowired
    private ICustomerService customerService;

    @ApiOperation(value = "获取客户信息")
    @PostMapping(value = "/findCustomerInfo")
    public ResultVo<CustomerInfoVo> findCustomerInfo(@RequestBody ExistCustomreDto dto){
        return customerService.findCustomerInfo(dto);
    }

    @ApiOperation(value = "验证用户是否存在")
    @PostMapping(value = "/existCustomer")
    public ResultVo existCustomer(@RequestBody ExistCustomreDto dto){
        return customerService.existCustomer(dto);
    }

    @ApiOperation(value = "新增移动端用户")
    @PostMapping(value = "/saveCustomer")
    public ResultVo saveCustomer(@Validated @RequestBody CustomerDto customerDto){
        return customerService.saveCustomer(customerDto);
    }

    @ApiOperation(value = "更新移动端用户")
    @PostMapping(value = "/modifyCustomer")
    public ResultVo modifyCustomer(@Validated @RequestBody CustomerDto customerDto){
        return customerService.modifyCustomer(customerDto);
    }

    @ApiOperation(value = "根据条件查询移动端用户")
    @PostMapping(value = "/findCustomer")
    public ResultVo<PageVo<CustomerVo>> findCustomer(@RequestBody SelectCustomerDto dto){
        return customerService.findCustomer(dto);
    }

    @ApiOperation(value = "新增/修改大客户&合同")
    @PostMapping(value = "/saveOrModifyKey")
    public ResultVo saveOrModifyKey(@Validated @RequestBody KeyCustomerDto dto){
        return customerService.saveOrModifyKey(dto);
    }

    @ApiOperation(value = "根据主键id/当前登陆用户loginId删除/审核用户")
    @PostMapping(value = "/verifyCustomer")
    public ResultVo verifyCustomer(@RequestBody OperateDto dto){
        return customerService.verifyCustomer(dto);
    }

    @ApiOperation(value = "大客户customerId查看大客户&合同")
    @PostMapping(value = "/showKeyCustomer/{customerId}")
    public ResultVo<ShowKeyCustomerVo> showKeyCustomer(@PathVariable Long customerId){
        return customerService.showKeyCustomer(customerId);
    }

    @ApiOperation(value = "根据条件查询大客户")
    @PostMapping(value = "/findKeyCustomer")
    public ResultVo<PageVo<ListKeyCustomerVo>> findKeyCustomer(@RequestBody SelectKeyCustomerDto dto){
        return customerService.findKeyCustomer(dto);
    }

    @ApiOperation(value = "新增/修改合伙人")
    @PostMapping(value = "/saveOrModifyPartner")
    public ResultVo saveOrModifyPartner(@Validated  @RequestBody PartnerDto dto){
        return customerService.saveOrModifyPartner(dto);
    }

    @ApiOperation(value = "根据条件分页查看合伙人")
    @PostMapping(value = "/findPartner")
    public ResultVo<PageVo<CustomerPartnerVo>> findPartner(@RequestBody CustomerPartnerDto dto){
        return customerService.findPartner(dto);
    }

    @ApiOperation(value = "根据客户id查看合伙人")
    @PostMapping(value = "/showPartner/{customerId}")
    public ResultVo<ShowPartnerVo> showPartner(@PathVariable Long customerId){
        return customerService.showPartner(customerId);
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

    @ApiOperation(value = "查看客户优惠券(客户中心)")
    @PostMapping(value = "/customerCoupon")
    public ResultVo customerCoupon(@RequestBody CustomerCouponDto dto){
        return customerService.customerCoupon(dto);
    }

    @ApiOperation(value = "根据customerId查看客户优惠券")
    @PostMapping(value = "/getCouponByCustomerId/{customerId}")
    public ResultVo<List<CustomerCouponSendVo>> getCouponByCustomerId(@PathVariable @ApiParam(value = "客户customerId",required = true) Long customerId){
        return customerService.getCouponByCustomerId(customerId);
    }

    @ApiOperation(value = "C端客户导出Excel", notes = "\t 请求接口为/customer/exportCustomerExcel?contactPhone=手机号&contactMan=联系人&idCard=身份证")
    @GetMapping("/exportCustomerExcel")
    public void exportCustomerExcel(HttpServletRequest request, HttpServletResponse response){
        customerService.exportCustomerExcel(request,response);
    }

    @ApiOperation(value = "大客户导出Excel", notes = "\t 请求接口为/customer/exportKeyExcel?customerNo=编号&name=全称&state=状态&contactMan=联系人&contactPhone=手机号" +
            "&customerNature=客户类型&createUserName=创建人")
    @GetMapping("/exportKeyExcel")
    public void exportKeyExcel(HttpServletRequest request, HttpServletResponse response){
        customerService.exportKeyExcel(request,response);
    }

    @ApiOperation(value = "合伙人导出Excel", notes = "\t 请求接口为/customer/exportPartnerExcel?customerNo=编码&name=合伙人名称&settleType=结算方式" +
            "&contactMan=联系人&contactPhone=电话&isInvoice=是否开票&source=来源&socialCreditCode=社会码")
    @GetMapping("/exportPartnerExcel")
    public void exportPartnerExcel(HttpServletRequest request, HttpServletResponse response){
        customerService.exportPartnerExcel(request,response);
    }
}