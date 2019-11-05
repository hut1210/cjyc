package com.cjyc.web.api.controller;

import com.cjyc.common.model.dto.web.invoice.InvoiceDetailAndConfirmDto;
import com.cjyc.common.model.dto.web.invoice.InvoiceQueryDto;
import com.cjyc.common.model.entity.InvoiceApply;
import com.cjyc.common.model.vo.PageVo;
import com.cjyc.common.model.vo.ResultVo;
import com.cjyc.common.model.vo.web.invoice.InvoiceDetailVo;
import com.cjyc.web.api.service.IInvoiceApplyService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * <p>
 * 发票申请信息表 前端控制器
 * </p>
 *
 * @author JPG
 * @since 2019-11-02
 */
@Api(tags = "发票管理")
@RestController
@RequestMapping("/invoice")
public class InvoiceApplyController {
    @Autowired
    private IInvoiceApplyService invoiceApplyService;

    @ApiOperation(value = "分页查询发票申请信息列表")
    @PostMapping("/getInvoicePage")
    public ResultVo<PageVo<List<InvoiceApply>>> getInvoiceApplyPage(@RequestBody InvoiceQueryDto dto){
        return invoiceApplyService.getInvoiceApplyPage(dto);
    }

    @ApiOperation(value = "查看明细")
    @PostMapping("/getDetail")
    public ResultVo<InvoiceDetailVo> getDetail(@RequestBody @Validated({InvoiceDetailAndConfirmDto.GetDetail.class}) InvoiceDetailAndConfirmDto dto){
        return invoiceApplyService.getDetail(dto);
    }

    @ApiOperation(value = "确认开票")
    @PostMapping("/confirmInvoice")
    public ResultVo confirmInvoice(@RequestBody @Validated({InvoiceDetailAndConfirmDto.ConfirmInvoice.class}) InvoiceDetailAndConfirmDto dto){
        return invoiceApplyService.confirmInvoice(dto);
    }

}
