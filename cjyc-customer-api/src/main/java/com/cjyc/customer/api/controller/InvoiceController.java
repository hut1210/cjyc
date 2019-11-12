package com.cjyc.customer.api.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.cjyc.common.model.dto.customer.invoice.CustomerInvoiceAddDto;
import com.cjyc.common.model.dto.customer.invoice.InvoiceApplyQueryDto;
import com.cjyc.common.model.entity.CustomerInvoice;
import com.cjyc.common.model.entity.InvoiceApply;
import com.cjyc.common.model.util.BaseResultUtil;
import com.cjyc.common.model.vo.PageVo;
import com.cjyc.common.model.vo.ResultVo;
import com.cjyc.common.model.vo.customer.invoice.InvoiceOrderVo;
import com.cjyc.customer.api.service.ICustomerInvoiceService;
import com.cjyc.customer.api.service.IInvoiceApplyService;
import com.cjyc.customer.api.service.IOrderService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 *  @author: zj
 *  @Date: 2019/10/31 19:31
 *  @Description:用户端发票
 */
@Slf4j
@Api(tags = "发票管理")
@RestController
@RequestMapping("/invoice")
public class InvoiceController {
    @Resource
    private IInvoiceApplyService invoiceApplyService;
    @Resource
    private ICustomerInvoiceService customerInvoiceService;
    @Resource
    private IOrderService orderService;

    @ApiOperation(value = "分页查询开票历史")
    @PostMapping("/getInvoiceApplyPage")
    public ResultVo<PageVo<List<InvoiceApply>>> getInvoiceApplyPage(@RequestBody @Validated({InvoiceApplyQueryDto.InvoiceOrderAndInvoiceApplyQuery.class}) InvoiceApplyQueryDto dto){
        return invoiceApplyService.getInvoiceApplyPage(dto);
    }

    @ApiOperation(value = "分页查询开票历史订单明细")
    @PostMapping("/getInvoiceApplyOrderPage")
    public ResultVo<PageVo<InvoiceOrderVo>> getInvoiceApplyOrderPage(@RequestBody @Validated({InvoiceApplyQueryDto.InvoiceApplyOrderQuery.class}) InvoiceApplyQueryDto dto){
        return orderService.getInvoiceApplyOrderPage(dto);
    }

    @ApiOperation(value = "分页查询用户未开发票的订单")
    @PostMapping("/getUnInvoicePage")
    public ResultVo<PageVo<InvoiceOrderVo>> getUnInvoicePage(@RequestBody @Validated({InvoiceApplyQueryDto.InvoiceOrderAndInvoiceApplyQuery.class}) InvoiceApplyQueryDto dto){
        return orderService.getUnInvoicePage(dto);
    }

    @ApiOperation(value = "查询开票历史开票信息")
    @PostMapping("/getInvoiceInfo/{userId}")
    public ResultVo<CustomerInvoice> getInvoiceInfo(@PathVariable Long userId){
        CustomerInvoice invoice = customerInvoiceService.getOne(new QueryWrapper<CustomerInvoice>().lambda().eq(CustomerInvoice::getCustomerId, userId));
        return BaseResultUtil.success(invoice);
    }

    @ApiOperation(value = "确认开票")
    @PostMapping("/applyInvoice")
    public ResultVo applyInvoice(@RequestBody @Validated CustomerInvoiceAddDto dto){
        ResultVo resultVo = null;
        try {
            resultVo = invoiceApplyService.applyInvoice(dto);
        } catch (Exception e) {
            log.error("确认开票异常:{}",e);
            resultVo = BaseResultUtil.fail("开票失败");
        }
        return resultVo;
    }

}