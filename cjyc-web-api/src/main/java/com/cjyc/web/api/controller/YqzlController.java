package com.cjyc.web.api.controller;

import com.cjyc.common.model.vo.ResultVo;
import com.cjyc.web.api.config.YQZLProperty;
import com.yqzl.constant.EnumBankProCode;
import com.yqzl.model.request.FundAccountQueryRequest;
import com.yqzl.model.request.FundTransferQueryRequest;
import com.yqzl.model.request.FundTransferRequest;
import com.yqzl.service.FundBankClient;
import com.yqzl.service.FundBankClientFactory;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 银企直联：提供银行转账，账户查询，交易流水查询等功能
 *
 * @author RenPL
 */
@RestController
@Api(tags = "银企直联")
@RequestMapping(value = "/yqzl", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
@Slf4j
public class YqzlController {

    @Autowired
    private FundBankClientFactory fundBankClientFactory;

    @ApiOperation(value = "银企直联-转账交易")
    @PostMapping(value = "/transfer")
    public ResultVo doTransfer(FundTransferRequest fundTransferRequest) {
        // 根据银行渠道信息找银行产品实现类，找不到就异常, 默认：交通银行
        String bankProCode = fundTransferRequest.getBankProCode();
        FundBankClient bankOperations = fundBankClientFactory.get(StringUtils.isEmpty(bankProCode) ? EnumBankProCode.COMM_BANK.name() : bankProCode);
        // 企业代码
        fundTransferRequest.setCorpNo(YQZLProperty.corpNo);
        // 企业用户号
        fundTransferRequest.setUserNo(YQZLProperty.userNo);
        return bankOperations.doTransfer(fundTransferRequest);
    }

    @ApiOperation(value = "银企直联-转账交易查询")
    @PostMapping(value = "/transferQuery")
    public ResultVo doTransferQuery(FundTransferQueryRequest fundTransferQueryRequest) {
        // 根据银行渠道信息找银行产品实现类，找不到就异常, 默认：交通银行
        String bankProCode = fundTransferQueryRequest.getBankProCode();
        FundBankClient bankOperations = fundBankClientFactory.get(StringUtils.isEmpty(bankProCode) ? EnumBankProCode.COMM_BANK.name() : bankProCode);
        // 企业代码
        fundTransferQueryRequest.setCorpNo(YQZLProperty.corpNo);
        // 企业用户号
        fundTransferQueryRequest.setUserNo(YQZLProperty.userNo);
        return bankOperations.doTransferQuery(fundTransferQueryRequest);
    }

    @ApiOperation(value = "银企直联-账户信息查询")
    @PostMapping(value = "/accountQuery")
    public ResultVo doAccountQuery(FundAccountQueryRequest fundAccountQueryRequest) {
        // 根据银行渠道信息找银行产品实现类，找不到就异常, 默认：交通银行
        String bankProCode = fundAccountQueryRequest.getBankProCode();
        FundBankClient bankOperations = fundBankClientFactory.get(StringUtils.isEmpty(bankProCode) ? EnumBankProCode.COMM_BANK.name() : bankProCode);
        // 企业代码
        fundAccountQueryRequest.setCorpNo(YQZLProperty.corpNo);
        // 企业用户号
        fundAccountQueryRequest.setUserNo(YQZLProperty.userNo);
        return bankOperations.doAccountQuery(fundAccountQueryRequest);
    }
}
