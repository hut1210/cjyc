package com.yqzl.service;

import org.springframework.lang.NonNull;

/**
 * 获取银行对接实现类的工厂
 *
 * @author RenPL 2020-3-15
 */
public interface FundBankClientFactory {

    /**
     * 获取银行实现类
     *
     * @param bankProCode 银行代号
     * @return 银行抽象接口
     */
    @NonNull
    FundBankClient get(String bankProCode);

    /**
     * 无法找到银行产品的异常
     */
    class CannotFoundBankClientException extends RuntimeException {
        public CannotFoundBankClientException(final Throwable cause) {
            super("无法获取银行对接实现", cause);
        }
    }
}
