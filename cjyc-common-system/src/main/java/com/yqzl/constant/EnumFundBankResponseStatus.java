package com.yqzl.constant;

/**
 * 银行接口响应类型
 *
 * @author RenPL 2020-3-15
 */
public enum EnumFundBankResponseStatus {
    /** 明确成功 */
    SUCCESS,
    /** 明确失败 */
    FAIL,
    /** 已提交（状态不明确需要后续查询，或等待异步通知的接口，或需要后续再调用一次的） */
    SUBMITTED,
    ;
}