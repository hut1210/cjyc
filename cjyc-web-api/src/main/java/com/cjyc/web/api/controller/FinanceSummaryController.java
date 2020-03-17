package com.cjyc.web.api.controller;

import com.cjyc.common.model.dto.web.finance.CooperatorSearchDto;
import com.cjyc.common.model.dto.web.finance.FinanceQueryDto;
import com.cjyc.common.model.dto.web.finance.PayMentQueryDto;
import com.cjyc.common.model.util.BaseResultUtil;
import com.cjyc.common.model.vo.PageVo;
import com.cjyc.common.model.vo.ResultVo;
import com.cjyc.common.model.vo.web.finance.FinanceReceiptVo;
import com.cjyc.web.api.service.ITradeBillSummaryService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

/**
 * @Author: Hut
 * @Date: 2020/03/16 10:32
 **/
@RestController
@Api(tags = "资金-财务统计")
@RequestMapping(value = "/finance/summary",
        produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
@Slf4j
public class FinanceSummaryController {

    @Resource
    private ITradeBillSummaryService tradeBillService;

    @ApiOperation(value = "收入合计")
    @PostMapping(value = "/incomeSummary")
    public ResultVo incomeSummary(@RequestBody FinanceQueryDto financeQueryDto){
        Map<String, BigDecimal> map = new HashMap<>();
        BigDecimal incomeSummary = tradeBillService.incomeSummary(financeQueryDto);
        map.put("incomeSummary",incomeSummary.divide(new BigDecimal(100)));
        return BaseResultUtil.success(map);
    }

    @ApiOperation(value = "成本合计")
    @PostMapping(value = "/costSummary")
    public ResultVo costSummary(@RequestBody FinanceQueryDto financeQueryDto){
        Map<String, BigDecimal> map = new HashMap<>();
        BigDecimal costSummary = tradeBillService.costSummary(financeQueryDto);
        map.put("costSummary",costSummary.divide(new BigDecimal(100)));
        return BaseResultUtil.success(map);
    }

    @ApiOperation(value = "毛利")
    @PostMapping(value = "/grossProfit")
    public ResultVo grossProfit(@RequestBody FinanceQueryDto financeQueryDto){
        Map<String, BigDecimal> map = new HashMap<>();
        BigDecimal grossProfit = tradeBillService.grossProfit(financeQueryDto);
        map.put("grossProfit",grossProfit.divide(new BigDecimal(100)));
        return BaseResultUtil.success(map);
    }

    @ApiOperation(value = "应收账款汇总")
    @PostMapping(value = "/receiptSummary")
    public ResultVo receiptSummary(@RequestBody FinanceQueryDto financeQueryDto){
        Map<String, BigDecimal> map = new HashMap<>();
        BigDecimal receiptSummary = tradeBillService.receiptSummary(financeQueryDto);
        map.put("receiptSummary",receiptSummary.divide(new BigDecimal(100)));
        return BaseResultUtil.success(map);
    }

    @ApiOperation(value = "承运商应付款汇总")
    @PostMapping(value = "/payToCarrierSummary")
    public ResultVo payToCarrierSummary(@RequestBody PayMentQueryDto payMentQueryDto){
        Map<String, BigDecimal> map = new HashMap<>();
        BigDecimal carrierSummary = tradeBillService.payToCarrierSummary(payMentQueryDto);
        map.put("carrierSummary",carrierSummary.divide(new BigDecimal(100)));
        return BaseResultUtil.success(map);
    }

    @ApiOperation(value = "承运商已付款汇总")
    @PostMapping(value = "/paidToCarrierSummary")
    public ResultVo paidToCarrierSummary(@RequestBody PayMentQueryDto payMentQueryDto){
        Map<String, BigDecimal> map = new HashMap<>();
        BigDecimal paidCarrierSummary = tradeBillService.paidToCarrierSummary(payMentQueryDto);
        map.put("paidCarrierSummary",paidCarrierSummary.divide(new BigDecimal(100)));
        return BaseResultUtil.success(map);
    }

    @ApiOperation(value = "合伙人应付款汇总")
    @PostMapping(value = "/payToCooperatorSummary")
    public ResultVo payToCooperatorSummary(@RequestBody CooperatorSearchDto cooperatorSearchDto){
        Map<String, BigDecimal> map = new HashMap<>();
        BigDecimal payCooperatorSummary = tradeBillService.payToCooperatorSummary(cooperatorSearchDto);
        map.put("payCooperatorSummary",payCooperatorSummary.divide(new BigDecimal(100)));
        return BaseResultUtil.success(map);
    }

    @ApiOperation(value = "合伙人已付款汇总")
    @PostMapping(value = "/paidToCooperatorSummary")
    public ResultVo paidToCooperatorSummary(@RequestBody CooperatorSearchDto cooperatorSearchDto){
        Map<String, BigDecimal> map = new HashMap<>();
        BigDecimal paidCooperatorSummary = tradeBillService.paidToCooperatorSummary(cooperatorSearchDto);
        map.put("paidToCooperatorSummary",paidCooperatorSummary.divide(new BigDecimal(100)));
        return BaseResultUtil.success(map);
    }
}
