package com.cjyc.web.api.controller;

import com.cjyc.common.model.dto.web.finance.AppSettlementPayableDto;
import com.cjyc.common.model.dto.web.finance.ApplySettlementDto;
import com.cjyc.common.model.dto.web.finance.PayableQueryDto;
import com.cjyc.common.model.dto.web.finance.WaitTicketCollectDto;
import com.cjyc.common.model.util.BaseResultUtil;
import com.cjyc.common.model.vo.PageVo;
import com.cjyc.common.model.vo.ResultVo;
import com.cjyc.common.model.vo.web.finance.FinancePayableVo;
import com.cjyc.common.model.vo.web.finance.SettlementPayableVo;
import com.cjyc.common.model.vo.web.finance.SettlementVo;
import com.cjyc.web.api.service.IFinanceService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @Author: Hut
 * @Date: 2020/01/06 10:32
 **/
@RestController
@Api(tags = "资金-财务-应付账款")
@RequestMapping(value = "/finance/payable",
        produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
public class FinancePayableController {

    @Autowired
    private IFinanceService financeService;

    @ApiOperation(value = "财务应付账款列表")
    @PostMapping(value = "/list")
    public ResultVo<PageVo<FinancePayableVo>> getFinancePayableList(@RequestBody PayableQueryDto payableQueryDto){
        return financeService.getFinancePayableList(payableQueryDto);
    }

    @ApiOperation(value = "获取申请开票信息")
    @PostMapping(value = "/get")
    public ResultVo get(@RequestBody List<String> taskNo){
        return financeService.getSettlementPayable(taskNo);
    }

    @ApiOperation(value = "申请开票")
    @PostMapping(value = "/apply")
    public ResultVo apply(@RequestBody AppSettlementPayableDto appSettlementPayableDto){
        return financeService.apply(appSettlementPayableDto);
    }

    @ApiOperation(value = "等待收票列表")
    @PostMapping(value = "/collect")
    public ResultVo<PageVo<SettlementVo>> collect(@RequestBody WaitTicketCollectDto waitTicketCollectDto){
        return financeService.collect(waitTicketCollectDto);
    }
}
