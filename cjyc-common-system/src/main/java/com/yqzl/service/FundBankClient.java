package com.yqzl.service;

import com.cjyc.common.model.vo.ResultVo;

/**
 * 将银行的各种功能抽象成一个接口，请求这里面的方法就等于调用银行了
 * yyftest
 * @author RenPL 2020-3-15
 */
public interface FundBankClient {

    /**
     * 请求银行转账
     *
     * @param obj 银行请求
     * @return 请求结果
     */
    ResultVo doTransfer(Object obj);

    /**
     * 转账交易结果查询
     *
     * @param obj
     * @return
     */
    ResultVo doTransferQuery(Object obj);

    /**
     * 账户信息查询
     *
     * @param obj
     * @return
     */
    ResultVo doAccountQuery(Object obj);

}
