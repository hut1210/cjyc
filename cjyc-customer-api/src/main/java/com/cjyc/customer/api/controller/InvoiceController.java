package com.cjyc.customer.api.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.cjyc.common.model.dto.customer.invoice.CustomerInvoiceAddDto;
import com.cjyc.common.model.dto.customer.invoice.InvoiceOrderQueryDto;
import com.cjyc.common.model.entity.CustomerInvoice;
import com.cjyc.common.model.util.BaseResultUtil;
import com.cjyc.common.model.vo.ResultVo;
import com.cjyc.common.model.vo.customer.invoice.InvoiceOrderVo;
import com.cjyc.customer.api.service.ICustomerInvoiceService;
import com.cjyc.customer.api.service.IInvoiceOrderService;
import com.cjyc.customer.api.service.IOrderService;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import netscape.javascript.JSObject;
import org.springframework.data.repository.query.Param;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 *  @author: zj
 *  @Date: 2019/10/31 19:31
 *  @Description:用户端发票
 */
@Api(tags = "发票管理")
@RestController
@RequestMapping("/invoice")
public class InvoiceController {
    @Resource
    private IInvoiceOrderService invoiceOrderService;
    @Resource
    private ICustomerInvoiceService customerInvoiceService;
    @Resource
    private IOrderService orderService;

    @ApiOperation(value = "分页查询用户未开发票的订单")
    @PostMapping("/getUnInvoiceList")
    public ResultVo<PageInfo<InvoiceOrderVo>> getUnInvoiceOrderList(@RequestBody @Validated InvoiceOrderQueryDto dto){
        return orderService.getUnInvoiceOrderList(dto);
    }

    @ApiOperation(value = "查询开票信息")
    @PostMapping("/getInvoiceInfo/{userId}")
    public ResultVo<CustomerInvoice> getInvoiceInfo(@PathVariable Long userId){
        CustomerInvoice invoice = customerInvoiceService.getOne(new QueryWrapper<CustomerInvoice>().lambda().eq(CustomerInvoice::getCustomerId, userId));
        return BaseResultUtil.success(invoice);
    }

    @ApiOperation(value = "新增发票信息")
    @PostMapping("/add")
    public ResultVo add(@RequestBody @Validated CustomerInvoiceAddDto dto){
        return customerInvoiceService.add(dto);
    }

}