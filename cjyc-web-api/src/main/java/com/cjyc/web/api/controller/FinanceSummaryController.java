package com.cjyc.web.api.controller;

import com.cjyc.common.model.dto.web.finance.FinanceQueryDto;
import com.cjyc.common.model.util.BaseResultUtil;
import com.cjyc.common.model.vo.ResultVo;
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
        map.put("incomeSummary",incomeSummary);
        return BaseResultUtil.success(map);
    }

    @ApiOperation(value = "成本合计")
    @PostMapping(value = "/costSummary")
    public ResultVo costSummary(@RequestBody FinanceQueryDto financeQueryDto){
        Map<String, BigDecimal> map = new HashMap<>();
        BigDecimal costSummary = tradeBillService.costSummary(financeQueryDto);
        map.put("costSummary",costSummary);
        return BaseResultUtil.success(map);
    }

    @ApiOperation(value = "毛利")
    @PostMapping(value = "/grossProfit")
    public ResultVo grossProfit(@RequestBody FinanceQueryDto financeQueryDto){
        Map<String, BigDecimal> map = new HashMap<>();
        BigDecimal grossProfit = tradeBillService.grossProfit(financeQueryDto);
        map.put("grossProfit",grossProfit);
        return BaseResultUtil.success(map);
    }
}
