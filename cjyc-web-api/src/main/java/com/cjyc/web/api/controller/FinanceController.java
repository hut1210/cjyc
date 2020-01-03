package com.cjyc.web.api.controller;

import com.cjyc.common.model.dto.web.finance.*;
import com.cjyc.common.model.util.BaseResultUtil;
import com.cjyc.common.model.vo.PageVo;
import com.cjyc.common.model.vo.ResultVo;
import com.cjyc.common.model.vo.web.finance.*;
import com.cjyc.web.api.service.IFinanceService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 财务
 * @author JPG
 */
@RestController
@Api(tags = "资金-财务")
@RequestMapping(value = "/finance",
        produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
public class FinanceController {

    @Autowired
    private IFinanceService financeService;

    @ApiOperation(value = "财务总流水列表")
    @PostMapping(value = "/getFinanceList")
    public ResultVo<PageVo<FinanceVo>> getFinanceList(@RequestBody FinanceQueryDto financeQueryDto){
        return financeService.getFinanceList(financeQueryDto);
    }

    @ApiOperation(value = "导出Excel")
    @GetMapping("/exportExcel")
    public void exportExcel(HttpServletRequest request, HttpServletResponse response){
        financeService.exportExcel(request,response);
    }

    @ApiOperation(value = "财务应收账款列表")
    @PostMapping(value = "/getFinanceReceiptList")
    public ResultVo<PageVo<FinanceReceiptVo>> getFinanceReceiptList(@RequestBody FinanceQueryDto financeQueryDto){
        return financeService.getFinanceReceiptList(financeQueryDto);
    }

    @ApiOperation(value = "申请开票")
    @PostMapping(value = "/applySettlement")
    public ResultVo applySettlement(@RequestBody ApplySettlementDto applySettlementDto){
        financeService.applySettlement(applySettlementDto);
        return BaseResultUtil.success();
    }

    @ApiOperation(value = "等待开票列表")
    @PostMapping(value = "/getWaitInvoiceList")
    public ResultVo<PageVo<WaitInvoiceVo>> getWaitInvoiceList(@RequestBody WaitQueryDto waitInvoiceQueryDto){
        return financeService.getWaitInvoiceList(waitInvoiceQueryDto);
    }

    @ApiOperation(value = "确认开票")
    @PostMapping(value = "/confirm")
    public ResultVo confirmSettlement(@RequestBody String serialNumber,@RequestBody String invoiceNo){
        financeService.confirmSettlement(serialNumber,invoiceNo);
        return BaseResultUtil.success();
    }

    @ApiOperation(value = "撤回")
    @PostMapping(value = "/cancel")
    public ResultVo cancel(@RequestBody String serialNumber){
        financeService.cancelSettlement(serialNumber);
        return BaseResultUtil.success();
    }

    @ApiOperation(value = "等待回款列表")
    @PostMapping(value = "/getWaitForBackList")
    public ResultVo<PageVo<WaitForBackVo>> getWaitForBackList(@RequestBody WaitQueryDto waitInvoiceQueryDto){
        return financeService.getWaitForBackList(waitInvoiceQueryDto);
    }

    @ApiOperation(value = "核销")
    @PostMapping(value = "/writeOff")
    public ResultVo writeOff(@RequestBody String serialNumber,@RequestBody String invoiceNo){
        financeService.writeOff(serialNumber,invoiceNo);
        return BaseResultUtil.success();
    }

    @ApiOperation(value = "结算明细")
    @PostMapping(value = "/detail")
    public ResultVo detail(@RequestBody Long Id){
        SettlementDetailVo settlementDetailVo = financeService.detail(Id);
        return BaseResultUtil.success(settlementDetailVo);
    }

    @ApiOperation(value = "已收款(账期)列表")
    @PostMapping(value = "/getReceivableList")
    public ResultVo<PageVo<ReceivableVo>> getReceivableList(@RequestBody WaitQueryDto waitInvoiceQueryDto){
        return financeService.getReceivableList(waitInvoiceQueryDto);
    }

    @ApiOperation(value = "代收款(时付)列表")
    @PostMapping(value = "/getCollectReceiveList")
    public ResultVo<PageVo<CollectReceiveVo>> getCollectReceiveList(@RequestBody CollectReceiveQueryDto collectReceiveQueryDto){
        return financeService.getCollectReceiveList(collectReceiveQueryDto);
    }

    @ApiOperation(value = "代收款(确认回款)")
    @PostMapping(value = "/updateBackState")
    public ResultVo updateBackState(@RequestBody String wayBillNo){
        return financeService.updateBackState(wayBillNo);
    }

    @ApiOperation(value = "代收款(结算明细)")
    @PostMapping(value = "/settleDetail")
    public ResultVo<CashSettlementDetailVo> settleDetail(@RequestBody String wayBillNo){
        return financeService.settleDetail(wayBillNo);
    }

    @ApiOperation(value = "已收款(时付)列表")
    @PostMapping(value = "/getPaymentList")
    public ResultVo<PageVo<PaymentVo>> getPaymentList(@RequestBody FinanceQueryDto financeQueryDto){
        return financeService.getPaymentList(financeQueryDto);
    }

    @ApiOperation(value = "已付款(时付)列表")
    @PostMapping(value = "/getPaidList")
    public ResultVo<PageVo<PaidVo>> getPaidList(@RequestBody PayMentQueryDto payMentQueryDto){
        return financeService.getPaidList(payMentQueryDto);
    }

    @ApiOperation(value = "财务应付账款列表")
    @PostMapping(value = "/getFinancePayableList")
    public ResultVo<PageVo<FinancePayableVo>> getFinancePayableList(@RequestBody PayableQueryDto payableQueryDto){
        return financeService.getFinancePayableList(payableQueryDto);
    }

}
