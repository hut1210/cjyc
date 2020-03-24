package com.yqzl.service;

import com.cjyc.common.model.vo.ResultVo;
import com.yqzl.model.request.FundAccountQueryRequest;
import com.yqzl.model.request.FundTransferQueryRequest;
import com.yqzl.model.request.FundTransferRequest;
import com.yqzl.model.response.FundAccountQueryResponse;
import com.yqzl.model.response.FundTransferQueryResponse;
import com.yqzl.model.response.FundTransferResponse;

/**
 * 将银行的各种功能抽象成一个接口，请求这里面的方法就等于调用银行了
 *
 * @author RenPL 2020-3-15
 */
public interface FundBankClient {

    /**
     * 请求银行转账
     *
     * @param fundTransferRequest 银行请求
     * @return 请求结果
     */
    ResultVo<FundTransferResponse> doTransfer(FundTransferRequest fundTransferRequest);

    /**
     * 转账交易结果查询
     *
     * @param fundTransferQueryRequest
     * @return
     */
    ResultVo<FundTransferQueryResponse> doTransferQuery(FundTransferQueryRequest fundTransferQueryRequest);

    /**
     * 账户信息查询
     *
     * @param fundAccountQueryRequest
     * @return
     */
    ResultVo<FundAccountQueryResponse> doAccountQuery(FundAccountQueryRequest fundAccountQueryRequest);

}
