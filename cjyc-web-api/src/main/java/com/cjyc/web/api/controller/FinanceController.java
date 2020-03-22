package com.cjyc.web.api.controller;

import cn.afterturn.easypoi.excel.entity.ExportParams;
import com.cjyc.common.model.dto.web.finance.*;
import com.cjyc.common.model.util.BaseResultUtil;
import com.cjyc.common.model.util.ExcelUtil;
import com.cjyc.common.model.vo.PageVo;
import com.cjyc.common.model.vo.ResultVo;
import com.cjyc.common.model.vo.web.finance.*;
import com.cjyc.web.api.service.IFinanceService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 财务
 *
 * @author JPG
 */
@RestController
@Api(tags = "资金-财务")
@RequestMapping(value = "/finance",
        produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
@Slf4j
public class FinanceController {

    @Autowired
    private IFinanceService financeService;

    @ApiOperation(value = "财务总流水列表")
    @PostMapping(value = "/getFinanceList")
    public ResultVo<PageVo<FinanceVo>> getFinanceList(@RequestBody FinanceQueryDto financeQueryDto) {
        return financeService.getFinanceList(financeQueryDto);
    }

    @ApiOperation(value = "成本详情列表")
    @PostMapping(value = "/detail/{no}")
    public ResultVo<List<ExportFinanceDetailVo>> getFinanceDetailList(@PathVariable String no) {
        return financeService.getFinanceDetailList(no);
    }

    @ApiOperation(value = "导出Excel")
    @GetMapping(value = "/export")
    public ResultVo exportExcel(HttpServletResponse response, FinanceQueryDto financeQueryDto) {

        log.info("financeQueryDto ={}", financeQueryDto.toString());
        Map map = financeService.exportExcel(financeQueryDto);

        List<ExportFinanceVo> financeVoList = (List<ExportFinanceVo>) map.get("financeVoList");
        List<ExportFinanceDetailVo> detailList = (List<ExportFinanceDetailVo>) map.get("detailVoList");

        if (CollectionUtils.isEmpty(financeVoList)) {
            return BaseResultUtil.success("未查询到结果");
        }

        ExportParams totalFlow = new ExportParams();
        totalFlow.setSheetName("总流水");
        ExportParams detail = new ExportParams();
        detail.setSheetName("成本明细");

        Map<String, Object> totalFlowMap = new HashMap<>(4);
        totalFlowMap.put("title", totalFlow);
        totalFlowMap.put("entity", ExportFinanceVo.class);
        totalFlowMap.put("data", financeVoList);

        Map<String, Object> detailMap = new HashMap<>(4);
        detailMap.put("title", detail);
        detailMap.put("entity", ExportFinanceDetailVo.class);
        detailMap.put("data", detailList);

        List<Map<String, Object>> sheetsList = new ArrayList<>();
        sheetsList.add(totalFlowMap);
        sheetsList.add(detailMap);

        String fileName = "财务总流水.xls";
        try {
            ExcelUtil.exportMultipleSheets(sheetsList, fileName, response);
            return null;
        } catch (IOException e) {
            log.error("导出财务总流水异常:", e);
            return BaseResultUtil.fail("导出财务总流水异常" + e.getMessage());
        }

    }

    @ApiOperation(value = "财务应收账款列表")
    @PostMapping(value = "/getFinanceReceiptList")
    public ResultVo<PageVo<FinanceReceiptVo>> getFinanceReceiptList(@RequestBody FinanceQueryDto financeQueryDto) {
        return financeService.getFinanceReceiptList(financeQueryDto);
    }

    @ApiOperation(value = "申请开票")
    @PostMapping(value = "/applySettlement")
    public ResultVo applySettlement(@RequestBody ApplySettlementDto applySettlementDto) {
        financeService.applySettlement(applySettlementDto);
        return BaseResultUtil.success();
    }

    @ApiOperation(value = "等待开票列表")
    @PostMapping(value = "/getWaitInvoiceList")
    public ResultVo<PageVo<WaitInvoiceVo>> getWaitInvoiceList(@RequestBody WaitQueryDto waitInvoiceQueryDto) {
        return financeService.getWaitInvoiceList(waitInvoiceQueryDto);
    }

    @ApiOperation(value = "确认开票")
    @PostMapping(value = "/confirm")
    public ResultVo confirmSettlement(@RequestBody String serialNumber, @RequestBody String invoiceNo) {
        financeService.confirmSettlement(serialNumber, invoiceNo);
        return BaseResultUtil.success();
    }

    @ApiOperation(value = "撤回")
    @PostMapping(value = "/cancel")
    public ResultVo cancel(@RequestBody String serialNumber) {
        financeService.cancelSettlement(serialNumber);
        return BaseResultUtil.success();
    }

    @ApiOperation(value = "等待回款列表")
    @PostMapping(value = "/getWaitForBackList")
    public ResultVo<PageVo<WaitForBackVo>> getWaitForBackList(@RequestBody WaitQueryDto waitInvoiceQueryDto) {
        return financeService.getWaitForBackList(waitInvoiceQueryDto);
    }

    @ApiOperation(value = "核销")
    @PostMapping(value = "/writeOff")
    public ResultVo writeOff(@RequestBody String serialNumber, @RequestBody String invoiceNo) {
        financeService.writeOff(serialNumber, invoiceNo);
        return BaseResultUtil.success();
    }

    @ApiOperation(value = "结算明细")
    @PostMapping(value = "/detail")
    public ResultVo detail(@RequestBody Long Id) {
        SettlementDetailVo settlementDetailVo = financeService.detail(Id);
        return BaseResultUtil.success(settlementDetailVo);
    }

    @ApiOperation(value = "已收款(账期)列表")
    @PostMapping(value = "/getReceivableList")
    public ResultVo<PageVo<ReceivableVo>> getReceivableList(@RequestBody WaitQueryDto waitInvoiceQueryDto) {
        return financeService.getReceivableList(waitInvoiceQueryDto);
    }

    @ApiOperation(value = "代收款(时付)列表")
    @PostMapping(value = "/getCollectReceiveList")
    public ResultVo<PageVo<CollectReceiveVo>> getCollectReceiveList(@RequestBody CollectReceiveQueryDto collectReceiveQueryDto) {
        return financeService.getCollectReceiveList(collectReceiveQueryDto);
    }

    @ApiOperation(value = "代收款(确认回款)")
    @PostMapping(value = "/updateBackState")
    public ResultVo updateBackState(@RequestBody String wayBillNo) {
        return financeService.updateBackState(wayBillNo);
    }

    @ApiOperation(value = "代收款(结算明细)")
    @PostMapping(value = "/settleDetail")
    public ResultVo<CashSettlementDetailVo> settleDetail(@RequestBody String wayBillNo) {
        return financeService.settleDetail(wayBillNo);
    }

    @ApiOperation(value = "已收款(时付)列表")
    @PostMapping(value = "/getPaymentList")
    public ResultVo<PageVo<PaymentVo>> getPaymentList(@RequestBody FinanceQueryDto financeQueryDto) {
        return financeService.getPaymentList(financeQueryDto);
    }

    @ApiOperation(value = "导出应收账款-已收款（时付）Excel")
    @GetMapping(value = "/exportPayment")
    public ResultVo exportPayment(HttpServletResponse response, FinanceQueryDto financeQueryDto) {

        log.info("financeQueryDto ={}", financeQueryDto.toString());
        List<PaymentVo> financeVoList = financeService.exportPaymentExcel(financeQueryDto);
        if (CollectionUtils.isEmpty(financeVoList)) {
            return BaseResultUtil.success("未查询到结果");
        }
        String title = "应收账款";
        String sheetName = "已收款（时付）";
        String fileName = "应收账款.xls";
        log.info("financeVoList.size = " + financeVoList.size());
        try {
            ExcelUtil.exportExcel(financeVoList, title, sheetName, PaymentVo.class, fileName, response);
            return null;
        } catch (IOException e) {
            log.error("导出应收账款异常:", e);
            return BaseResultUtil.fail("导出应收账款异常" + e.getMessage());
        }
    }

    @ApiOperation(value = "已付款(时付)列表")
    @PostMapping(value = "/getPaidList")
    public ResultVo<PageVo<PaidNewVo>> getPaidList(@RequestBody PayMentQueryDto payMentQueryDto) {
        return financeService.getPaidListNew(payMentQueryDto);
    }

    @ApiOperation(value = "已收款(账期)列表")
    @PostMapping(value = "/listPaymentDaysInfo")
    public ResultVo<PageVo<ReceiveOrderCarDto>> listPaymentDaysInfo(@RequestBody FinanceQueryDto financeQueryDto) {
        return financeService.listPaymentDaysInfo(financeQueryDto);
    }

    @ApiOperation(value = "应收账款结算-待开票(账期)列表查询")
    @PostMapping(value = "/listReceiveSettlementNeedInvoice")
    public ResultVo<PageVo<ReceiveSettlementDto>> listReceiveSettlementNeedInvoice(@RequestBody ReceiveSettlementNeedInvoiceVo receiveSettlementNeedInvoiceVo) {
        return financeService.listReceiveSettlementNeedInvoice(receiveSettlementNeedInvoiceVo);
    }

    @ApiOperation(value = "应收账款结算-待回款(账期)列表查询")
    @PostMapping(value = "/listReceiveSettlementNeedPayed")
    public ResultVo<PageVo<ReceiveSettlementDto>> listReceiveSettlementNeedPayed(@RequestBody ReceiveSettlementNeedPayedVo receiveSettlementNeedPayedVo) {
        return financeService.listReceiveSettlementNeedPayed(receiveSettlementNeedPayedVo);
    }

    @ApiOperation(value = "应收账款结算-已收款(账期)列表查询")
    @PostMapping(value = "/listReceiveSettlementPayed")
    public ResultVo<PageVo<ReceiveSettlementDto>> listReceiveSettlementPayed(@RequestBody ReceiveSettlementPayedVo receiveSettlementPayedVo) {
        return financeService.listReceiveSettlementPayed(receiveSettlementPayedVo);
    }

    @ApiOperation(value = "应收账款(账期)结算申请")
    @PostMapping(value = "/applyReceiveSettlement")
    public ResultVo applyReceiveSettlement(@RequestBody ApplyReceiveSettlementVo applyReceiveSettlementVo) {
        return financeService.applyReceiveSettlement(applyReceiveSettlementVo);
    }

    @ApiOperation(value = "应收账款(账期)-待开票-撤回")
    @PostMapping(value = "/cancelReceiveSettlement/{serialNumber}")
    public ResultVo cancelReceiveSettlement(@PathVariable String serialNumber) {
        return financeService.cancelReceiveSettlement(serialNumber);
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
