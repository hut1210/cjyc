package com.cjyc.web.api.controller;

import com.alibaba.nacos.client.naming.utils.CollectionUtils;
import com.cjyc.common.model.dto.web.finance.*;
import com.cjyc.common.model.util.BaseResultUtil;
import com.cjyc.common.model.vo.PageVo;
import com.cjyc.common.model.vo.ResultVo;
import com.cjyc.common.model.vo.web.finance.*;
import com.cjyc.common.system.service.ICsPingPayService;
import com.cjyc.common.system.service.ICsTransactionService;
import com.cjyc.web.api.service.IFinanceService;
import com.cjyc.web.api.util.ExcelUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * @Author: Hut
 * @Date: 2020/01/06 10:32
 **/
@RestController
@Api(tags = "资金-财务-应付账款")
@RequestMapping(value = "/finance/payable",
        produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
@Slf4j
public class FinancePayableController {

    @Autowired
    private IFinanceService financeService;

    @Autowired
    private ICsTransactionService csTransactionService;

    @Resource
    private ICsPingPayService csPingPayService;

    @ApiOperation(value = "财务应付账款列表")
    @PostMapping(value = "/list")
    public ResultVo<PageVo<FinancePayableVo>> getFinancePayableList(@RequestBody PayableQueryDto payableQueryDto){
        return financeService.getFinancePayableList(payableQueryDto);
    }

    @ApiOperation(value = "导出财务应付账款列表")
    @GetMapping(value = "/exportPayableAll")
    public ResultVo exportPayableAll(HttpServletResponse response, PayableQueryDto payableQueryDto){
        log.info("payableQueryDto ={}",payableQueryDto.toString());
        List<FinancePayableVo> financePayableVoList = financeService.exportPayableAll(payableQueryDto);
        if (CollectionUtils.isEmpty(financePayableVoList)) {
            return BaseResultUtil.success("未查询到结果");
        }
        String title = "应付账款";
        String sheetName = "应付账款";
        String fileName = "应付账款.xls";
        log.info("financePayableVoList.size = "+financePayableVoList.size());
        try {
            ExcelUtil.exportExcel(financePayableVoList, title, sheetName, FinancePayableVo.class, fileName, response);
            return null;
        } catch (Exception e) {
            log.error("导出应付账款异常:",e);
            return BaseResultUtil.fail("导出应付账款异常"+e.getMessage());
        }
    }

    @ApiOperation(value = "申请开票-获取申请开票运单信息")
    @PostMapping(value = "/applyTicket")
    public ResultVo get(@RequestBody List<String> taskNo){
        return financeService.getSettlementPayable(taskNo);
    }

    @ApiOperation(value = "申请开票-确认")
    @PostMapping(value = "/apply")
    public ResultVo apply(@RequestBody AppSettlementPayableDto appSettlementPayableDto){
        return financeService.apply(appSettlementPayableDto);
    }

    @ApiOperation(value = "等待收票列表")
    @PostMapping(value = "/collect")
    public ResultVo<PageVo<SettlementVo>> collect(@RequestBody WaitTicketCollectDto waitTicketCollectDto){
        return financeService.collect(waitTicketCollectDto);
    }

    @ApiOperation(value = "导出财务等待收票列表")
    @GetMapping(value = "/exportPayableCollect")
    public ResultVo exportPayableCollect(HttpServletResponse response, WaitTicketCollectDto waitTicketCollectDto){
        log.info("waitTicketCollectDto ={}",waitTicketCollectDto.toString());
        List<SettlementVo> settlementVoList = financeService.exportPayableCollect(waitTicketCollectDto);
        if (CollectionUtils.isEmpty(settlementVoList)) {
            return BaseResultUtil.success("未查询到结果");
        }
        String title = "等待收票";
        String sheetName = "等待收票";
        String fileName = "等待收票.xls";
        log.info("settlementVoList.size = "+settlementVoList.size());
        try {
            ExcelUtil.exportExcel(settlementVoList, title, sheetName, SettlementVo.class, fileName, response);
            return null;
        } catch (Exception e) {
            log.error("导出等待收票异常:",e);
            return BaseResultUtil.fail("导出等待收票异常"+e.getMessage());
        }
    }

    @ApiOperation(value = "确认收票-获取确认收票运单信息")
    @PostMapping(value = "/confirmTicket/{serialNumber}")
    public ResultVo confirmTicket(@PathVariable String serialNumber){
        return financeService.getConfirmTicket(serialNumber);
    }

    @ApiOperation(value = "确认收票-确认")
    @PostMapping(value = "/confirm")
    public ResultVo confirm(@RequestBody ConfirmTicketDto confirmTicketDto){
        return financeService.confirm(confirmTicketDto);
    }

    @ApiOperation(value = "撤回")
    @PostMapping(value = "/withdraw/{serialNumber}")
    public ResultVo withdraw(@PathVariable String serialNumber){
        return financeService.withdraw(serialNumber);
    }

    @ApiOperation(value = "等待付款列表")
    @PostMapping(value = "/payment")
    public ResultVo<PageVo<SettlementVo>> payment(@RequestBody WaitPaymentDto waitPaymentDto){
        return financeService.payment(waitPaymentDto);
    }

    @ApiOperation(value = "导出财务等待付款列表")
    @GetMapping(value = "/exportPayment")
    public ResultVo exportPayment(HttpServletResponse response, WaitPaymentDto waitPaymentDto){
        log.info("waitPaymentDto ={}",waitPaymentDto.toString());
        List<SettlementVo> settlementVoList = financeService.exportPayment(waitPaymentDto);
        if (CollectionUtils.isEmpty(settlementVoList)) {
            return BaseResultUtil.success("未查询到结果");
        }
        String title = "等待付款";
        String sheetName = "等待付款";
        String fileName = "等待付款.xls";
        log.info("settlementVoList.size = "+settlementVoList.size());
        try {
            ExcelUtil.exportExcel(settlementVoList, title, sheetName, SettlementVo.class, fileName, response);
            return null;
        } catch (Exception e) {
            log.error("导出等待付款异常:",e);
            return BaseResultUtil.fail("导出等待付款异常"+e.getMessage());
        }
    }

    @ApiOperation(value = "核销-获取核销运单信息")
    @PostMapping(value = "/writeOffTicket/{serialNumber}")
    public ResultVo writeOffTicket(@PathVariable String serialNumber){
        return financeService.getWriteOffTicket(serialNumber);
    }

    @ApiOperation(value = "核销-确认")
    @PostMapping(value = "/writeOff")
    public ResultVo writeOff(@RequestBody WriteOffTicketDto writeOffTicketDto){
        return financeService.writeOffPayable(writeOffTicketDto);
    }

    @ApiOperation(value = "已付款（账期）列表")
    @PostMapping(value = "/paid")
    public ResultVo paid(@RequestBody PayablePaidQueryDto payablePaidQueryDto){
        return financeService.paid(payablePaidQueryDto);
    }

    @ApiOperation(value = "导出财务已付款（账期）列表")
    @GetMapping(value = "/exportPaid")
    public ResultVo exportPaid(HttpServletResponse response, PayablePaidQueryDto payablePaidQueryDto){
        log.info("payablePaidQueryDto ={}",payablePaidQueryDto.toString());
        List<PayablePaidVo> payablePaidVoList = financeService.exportPaid(payablePaidQueryDto);
        if (CollectionUtils.isEmpty(payablePaidVoList)) {
            return BaseResultUtil.success("未查询到结果");
        }
        String title = "应付账款-已付款（账期）";
        String sheetName = "已付款（账期）";
        String fileName = "应付账款-已付款（账期）.xls";
        log.info("payablePaidVoList.size = "+payablePaidVoList.size());
        try {
            ExcelUtil.exportExcel(payablePaidVoList, title, sheetName, PayablePaidVo.class, fileName, response);
            return null;
        } catch (Exception e) {
            log.error("导出等待付款异常:",e);
            return BaseResultUtil.fail("导出等待付款异常"+e.getMessage());
        }
    }

    @ApiOperation(value = "结算明细")
    @PostMapping(value = "/detail/{serialNumber}")
    public ResultVo detail(@PathVariable String serialNumber){
        return financeService.payableDetail(serialNumber);
    }

    @ApiOperation(value = "已付款(时付)列表")
    @PostMapping(value = "/getPaidList")
    public ResultVo<PageVo<PaidNewVo>> getPaidList(@RequestBody PayMentQueryDto payMentQueryDto){
        return financeService.getPaidListNew(payMentQueryDto);
    }

    @ApiOperation(value = "应付账款-司机时付-根据运单号查看上游付款状态列表")
    @PostMapping(value = "/listDriverUpstreamPaidInfo")
    public ResultVo<PageVo<DriverUpstreamPaidInfoVo>> listDriverUpstreamPaidStatus(@PathVariable String waybillNo){
        return financeService.listDriverUpstreamPaidInfo(waybillNo);
    }

    @ApiOperation(value = "导出财务已付款（时付）列表")
    @GetMapping(value = "/exportTimePaid")
    public ResultVo exportTimePaid(HttpServletResponse response, PayMentQueryDto payMentQueryDto){
        log.info("payMentQueryDto ={}",payMentQueryDto.toString());
        List<PaidNewVo> payablePaidVoList = financeService.exportTimePaid(payMentQueryDto);
        if (CollectionUtils.isEmpty(payablePaidVoList)) {
            return BaseResultUtil.success("未查询到结果");
        }
        String title = "应付账款-已付款（时付）";
        String sheetName = "已付款（时付）";
        String fileName = "应付账款-已付款（时付）.xls";
        log.info("payablePaidVoList.size = "+payablePaidVoList.size());
        try {
            ExcelUtil.exportExcel(payablePaidVoList, title, sheetName, PaidNewVo.class, fileName, response);
            return null;
        } catch (Exception e) {
            log.error("导出应付账款-已付款（时付）异常:",e);
            return BaseResultUtil.fail("导出应付账款-已付款（时付）异常"+e.getMessage());
        }
    }

    @ApiOperation(value = "对外支付")
    @PostMapping(value = "/externalPay")
    public ResultVo externalPayment(@RequestBody ExternalPaymentDto externalPaymentDto){
        return financeService.externalPayment(externalPaymentDto);
    }
    @ApiOperation(value = "更新合伙人付款状态")
    @PostMapping(value = "/update/{orderNo}")
    public ResultVo updateCooperatorState(@PathVariable String orderNo){
        return csTransactionService.updateFailOrder(orderNo);
    }

    @ApiOperation("人工支付合伙人服务费")
    @PostMapping("/pay/{orderId}")
    public ResultVo payToCooperator(@PathVariable Long orderId){

        try{
            return csPingPayService.allinpayToCooperator(orderId);
        }catch (Exception e){
            log.error(e.getMessage(),e);
            return BaseResultUtil.fail("人工支付合伙人费用异常");
        }
    }

    @ApiOperation(value = "合伙人付款列表")
    @PostMapping(value = "/getCooperatorPaidList")
    public ResultVo<PageVo<CooperatorPaidVo>> getCooperatorPaidList(@RequestBody CooperatorSearchDto cooperatorSearchDto){
        return financeService.getCooperatorPaidList(cooperatorSearchDto);
    }

}
