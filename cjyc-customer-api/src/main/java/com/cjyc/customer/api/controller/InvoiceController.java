package com.cjyc.customer.api.controller;

import com.cjyc.common.model.dto.customer.invoice.InvoiceOrderQueryDto;
import com.cjyc.common.model.vo.ResultVo;
import com.cjyc.common.model.vo.customer.invoice.InvoiceOrderVo;
import com.cjyc.customer.api.service.IInvoiceOrderService;
import com.cjyc.customer.api.service.IOrderService;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 *  @author: zj
 *  @Date: 2019/10/31 19:31
 *  @Description:用户端发票
 */
@Api(tags = "发票管理")
@CrossOrigin
@RestController
@RequestMapping("/invoice")
public class InvoiceController {
    @Resource
    private IInvoiceOrderService invoiceOrderService;
    @Resource
    private IOrderService orderService;

    @ApiOperation(value = "分页查询用户未开发票的订单")
    @PostMapping(value = "/getUnInvoiceList")
    public ResultVo<PageInfo<InvoiceOrderVo>> getUnInvoiceOrderList(@RequestBody @Validated InvoiceOrderQueryDto dto){
        return orderService.getUnInvoiceOrderList(dto);
    }

}