package com.cjyc.web.api.controller;

import com.cjyc.common.model.dto.web.finance.*;
import com.cjyc.common.model.vo.PageVo;
import com.cjyc.common.model.vo.ResultVo;
import com.cjyc.common.model.vo.web.finance.FinancePayableVo;
import com.cjyc.common.model.vo.web.finance.PaidNewVo;
import com.cjyc.common.model.vo.web.finance.SettlementVo;
import com.cjyc.web.api.service.IFinanceService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

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

    @ApiOperation(value = "财务应付账款列表")
    @PostMapping(value = "/list")
    public ResultVo<PageVo<FinancePayableVo>> getFinancePayableList(@RequestBody PayableQueryDto payableQueryDto){
        return financeService.getFinancePayableList(payableQueryDto);
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

    @ApiOperation(value = "已付款列表")
    @PostMapping(value = "/paid")
    public ResultVo paid(@RequestBody PayablePaidQueryDto payablePaidQueryDto){
        return financeService.paid(payablePaidQueryDto);
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

    @ApiOperation(value = "对外支付")
    @PostMapping(value = "/externalPay")
    public ResultVo externalPayment(@RequestBody ExternalPaymentDto externalPaymentDto){
        return financeService.externalPayment(externalPaymentDto);
    }
}
