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

    @ApiOperation(value = "导出Excel")
    @GetMapping(value = "/export")
    public ResultVo exportExcel(HttpServletResponse response, FinanceQueryDto financeQueryDto) {
        Map map = financeService.exportExcel(financeQueryDto);
        List<ExportFinanceVo> financeVoList = (List<ExportFinanceVo>) map.get("financeVoList");
        List<ExportFinanceDetailVo> detailList = (List<ExportFinanceDetailVo>) map.get("detailVoList");
        if (CollectionUtils.isEmpty(financeVoList)) {
            return BaseResultUtil.success("未查询到结果");
        }
        /**
         * 总流水数据
         */
        ExportParams totalFlow = new ExportParams();
        totalFlow.setSheetName("总流水");
        Map<String, Object> totalFlowMap = new HashMap<>(4);
        totalFlowMap.put("title", totalFlow);
        totalFlowMap.put("entity", ExportFinanceVo.class);
        totalFlowMap.put("data", financeVoList);
        /**
         * 成本明细数据
         */
        ExportParams detail = new ExportParams();
        detail.setSheetName("成本明细");
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
        List<AdvancePaymentVo> advancePaymentVoList = financeService.exportAdvancePaymentExcel(financeQueryDto);
        if (CollectionUtils.isEmpty(advancePaymentVoList)) {
            return BaseResultUtil.success("未查询到结果");
        }
        String title = "已付订单未完结";
        String sheetName = "已付订单未完结";
        String fileName = "已付订单未完结.xls";
        try {
            ExcelUtil.exportExcel(advancePaymentVoList, title, sheetName, AdvancePaymentVo.class, fileName, response);
            return null;
        } catch (IOException e) {
            log.error("导出已付订单未完结异常:", e);
            return BaseResultUtil.fail("导出已付订单未完结异常" + e.getMessage());
        }
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
