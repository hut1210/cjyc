package com.cjyc.web.api.controller;

import com.cjyc.common.model.dto.web.invoice.InvoiceQueryDto;
import com.cjyc.common.model.vo.ResultVo;
import com.cjyc.common.model.vo.web.invoice.InvoiceDetailVo;
import com.cjyc.web.api.service.IInvoiceApplyService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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
    public ResultVo getInvoiceApplyPage(@RequestBody InvoiceQueryDto dto){
        return invoiceApplyService.getInvoiceApplyPage(dto);
    }

    @ApiOperation(value = "分页查询发票申请信息列表")
    @PostMapping("/getDetail/{userId}/{invoiceApplyId}")
    public ResultVo<ResultVo<InvoiceDetailVo>> getDetail(@PathVariable Long userId, @PathVariable Long invoiceApplyId){
        return invoiceApplyService.getDetail(userId,invoiceApplyId);
    }

}
