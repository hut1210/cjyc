package com.yqzl.service;

import com.cjyc.common.model.vo.ResultVo;
import com.yqzl.model.request.FundTransferRequest;
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
}
