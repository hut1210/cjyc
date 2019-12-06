package com.cjyc.web.api.controller;

import com.cjyc.common.model.dto.web.finance.ApplySettlementDto;
import com.cjyc.common.model.dto.web.finance.FinanceQueryDto;
import com.cjyc.common.model.dto.web.finance.WaitQueryDto;
import com.cjyc.common.model.util.BaseResultUtil;
import com.cjyc.common.model.vo.PageVo;
import com.cjyc.common.model.vo.ResultVo;
import com.cjyc.common.model.vo.web.finance.FinanceReceiptVo;
import com.cjyc.common.model.vo.web.finance.FinanceVo;
import com.cjyc.common.model.vo.web.finance.WaitInvoiceVo;
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
    private IFinanceService iFinanceService;

    @ApiOperation(value = "根据条件查询财务总流水列表")
    @PostMapping(value = "/getFinanceList")
    public ResultVo<PageVo<FinanceVo>> getFinanceList(@RequestBody FinanceQueryDto financeQueryDto){
        return iFinanceService.getFinanceList(financeQueryDto);
    }

    @ApiOperation(value = "导出Excel")
    @GetMapping("/exportExcel")
    public void exportExcel(HttpServletRequest request, HttpServletResponse response){
        iFinanceService.exportExcel(request,response);
    }

    @ApiOperation(value = "根据条件查询财务应收账款列表")
    @PostMapping(value = "/getFinanceReceiptList")
    public ResultVo<PageVo<FinanceReceiptVo>> getFinanceReceiptList(@RequestBody FinanceQueryDto financeQueryDto){
        return iFinanceService.getFinanceReceiptList(financeQueryDto);
    }

    @ApiOperation(value = "申请开票")
    @PostMapping(value = "/applySettlement")
    public ResultVo applySettlement(@RequestBody ApplySettlementDto applySettlementDto){
        iFinanceService.applySettlement(applySettlementDto);
        return BaseResultUtil.success();
    }

    @ApiOperation(value = "根据条件查询等待开票列表")
    @PostMapping(value = "/getWaitInvoiceList")
    public ResultVo<PageVo<WaitInvoiceVo>> getWaitInvoiceList(@RequestBody WaitQueryDto waitInvoiceQueryDto){
        return iFinanceService.getWaitInvoiceList(waitInvoiceQueryDto);
    }

    @ApiOperation(value = "确认开票")
    @PostMapping(value = "/confirmSettlement")
    public ResultVo confirmSettlement(@RequestBody String orderCarNo){
        iFinanceService.confirmSettlement(orderCarNo);
        return BaseResultUtil.success();
    }

    @ApiOperation(value = "根据条件查询等待回款列表")
    @PostMapping(value = "/getWaitReceiveList")
    public ResultVo<PageVo<WaitInvoiceVo>> getWaitReceiveList(@RequestBody WaitQueryDto waitInvoiceQueryDto){
        return iFinanceService.getWaitInvoiceList(waitInvoiceQueryDto);
    }

}
