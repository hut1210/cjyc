package com.cjyc.web.api.controller;

import com.cjyc.common.model.dto.web.finance.*;
import com.cjyc.common.model.vo.PageVo;
import com.cjyc.common.model.vo.ResultVo;
import com.cjyc.common.model.vo.web.finance.*;
import com.cjyc.web.api.service.IFinanceService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpServletResponse;

/**
 * 财务
 *
 * @author JPG
 */
@RestController
@Api(tags = "资金-财务")
@RequestMapping(value = "/finance", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
@Slf4j
public class FinanceController {

    @Autowired
    private IFinanceService financeService;

    @ApiOperation(value = "财务总流水列表")
    @PostMapping(value = "/getFinanceList")
    public ResultVo<PageVo<FinanceVo>> getFinanceList(@RequestBody FinanceQueryDto financeQueryDto) {
        return financeService.getFinanceList(financeQueryDto);
    }

    @ApiOperation(value = "导出Excel")
    @GetMapping(value = "/export")
    public ResultVo exportExcel(HttpServletResponse response, FinanceQueryDto financeQueryDto) {
        return financeService.exportExcel(response, financeQueryDto);
    }

    @ApiOperation(value = "已收款(时付)列表")
    @PostMapping(value = "/getPaymentList")
    public ResultVo<PageVo<PaymentVo>> getPaymentList(@RequestBody FinanceQueryDto financeQueryDto) {
        return financeService.getPaymentList(financeQueryDto);
    }

    @ApiOperation(value = "导出应收账款-已收款（时付）Excel")
    @GetMapping(value = "/exportPayment")
    public ResultVo<Object> exportPayment(HttpServletResponse response, FinanceQueryDto financeQueryDto) {
        return financeService.exportPaymentExcel(response, financeQueryDto);
    }

    @ApiOperation(value = "已付订单未完结列表")
    @PostMapping(value = "/getAdvancePayment")
    public ResultVo<PageVo<AdvancePaymentVo>> getAdvancePayment(@RequestBody FinanceQueryDto financeQueryDto) {
        return financeService.getAdvancePayment(financeQueryDto);
    }

    @ApiOperation(value = "导出已付订单未完结Excel")
    @GetMapping(value = "/exportAdvancePayment")
    public ResultVo exportAdvancePayment(HttpServletResponse response, FinanceQueryDto financeQueryDto) {
        return financeService.exportAdvancePaymentExcel(response, financeQueryDto);
    }

    @ApiOperation(value = "应收账款(账期)列表")
    @PostMapping(value = "/listPaymentDaysInfo")
    public ResultVo<PageVo<ReceiveOrderCarDto>> listPaymentDaysInfo(@RequestBody FinanceQueryDto financeQueryDto) {
        return financeService.listPaymentDaysInfo(financeQueryDto);
    }

    @ApiOperation(value = "导出应收账款（账期）Excel")
    @GetMapping(value = "/exportPaymentDaysInfo")
    public ResultVo exportPaymentDaysInfo(HttpServletResponse response, FinanceQueryDto financeQueryDto) {
        return financeService.exportPaymentDaysInfo(response, financeQueryDto);
    }

    @ApiOperation(value = "应收账款结算-待开票(账期)列表查询")
    @PostMapping(value = "/listReceiveSettlementNeedInvoice")
    public ResultVo<PageVo<ReceiveSettlementDto>> listReceiveSettlementNeedInvoice(@RequestBody ReceiveSettlementNeedInvoiceVo receiveSettlementNeedInvoiceVo) {
        return financeService.listReceiveSettlementNeedInvoice(receiveSettlementNeedInvoiceVo);
    }

    @ApiOperation(value = "导出应收账款-待开票（账期）Excel")
    @GetMapping(value = "/exportReceiveSettlementNeedInvoice")
    public ResultVo exportReceiveSettlementNeedInvoice(HttpServletResponse response, ReceiveSettlementNeedInvoiceVo receiveSettlementNeedInvoiceVo) {
        return financeService.exportReceiveSettlementNeedInvoice(response, receiveSettlementNeedInvoiceVo);
    }

    @ApiOperation(value = "应收账款结算-待回款(账期)列表查询")
    @PostMapping(value = "/listReceiveSettlementNeedPayed")
    public ResultVo<PageVo<ReceiveSettlementDto>> listReceiveSettlementNeedPayed(@RequestBody ReceiveSettlementNeedPayedVo receiveSettlementNeedPayedVo) {
        return financeService.listReceiveSettlementNeedPayed(receiveSettlementNeedPayedVo);
    }

    @ApiOperation(value = "导出应收账款-待回款（账期）Excel")
    @GetMapping(value = "/exportReceiveSettlementNeedPayed")
    public ResultVo exportReceiveSettlementNeedPayed(HttpServletResponse response, ReceiveSettlementNeedPayedVo receiveSettlementNeedPayedVo) {
        return financeService.exportReceiveSettlementNeedPayed(response, receiveSettlementNeedPayedVo);
    }

    @ApiOperation(value = "应收账款结算-已收款(账期)列表查询")
    @PostMapping(value = "/listReceiveSettlementPayed")
    public ResultVo<PageVo<ReceiveSettlementDto>> listReceiveSettlementPayed(@RequestBody ReceiveSettlementNeedInvoiceVo receiveSettlementNeedInvoiceVo) {
        return financeService.listReceiveSettlementPayed(receiveSettlementNeedInvoiceVo);
    }

    @ApiOperation(value = "导出应收账款-已收款（账期）Excel")
    @GetMapping(value = "/exportReceiveSettlementPayed")
    public ResultVo exportReceiveSettlementPayed(HttpServletResponse response, ReceiveSettlementNeedInvoiceVo receiveSettlementNeedInvoiceVo) {
        return financeService.exportReceiveSettlementPayed(response, receiveSettlementNeedInvoiceVo);
    }

    @ApiOperation(value = "应收账款(账期)结算申请")
    @PostMapping(value = "/applyReceiveSettlement")
    public ResultVo applyReceiveSettlement(@RequestBody ApplyReceiveSettlementVo applyReceiveSettlementVo) {
        return financeService.applyReceiveSettlement(applyReceiveSettlementVo);
    }

    @ApiOperation(value = "应收账款(账期)-待开票-撤回")
    @PostMapping(value = "/cancelReceiveSettlement")
    public ResultVo cancelReceiveSettlement(@RequestBody CancelInvoiceVo cancelInvoiceVo) {
        return financeService.cancelReceiveSettlement(cancelInvoiceVo);
    }

    @ApiOperation(value = "应收账款(账期)-待开票-确认开票")
    @PostMapping(value = "/confirmInvoice")
    public ResultVo confirmInvoice(@RequestBody ConfirmInvoiceVo confirmInvoiceVo) {
        return financeService.confirmInvoice(confirmInvoiceVo);
    }

    @ApiOperation(value = "应收账款(账期)-待回款-核销")
    @PostMapping(value = "/verificationReceiveSettlement")
    public ResultVo verificationReceiveSettlement(@RequestBody VerificationReceiveSettlementVo verificationReceiveSettlementVo) {
        return financeService.verificationReceiveSettlement(verificationReceiveSettlementVo);
    }

    @ApiOperation(value = "应收账款结算(账期)-结算明细查询")
    @PostMapping(value = "/listReceiveSettlementDetail/{serialNumber}")
    public ResultVo<ReceiveSettlementInvoiceDetailDto> listReceiveSettlementDetail(@PathVariable String serialNumber) {
        return financeService.listReceiveSettlementDetail(serialNumber);
    }

}
