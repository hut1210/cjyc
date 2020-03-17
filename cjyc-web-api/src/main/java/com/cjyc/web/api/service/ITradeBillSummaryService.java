package com.cjyc.web.api.service;

import com.cjyc.common.model.dto.web.finance.FinanceQueryDto;

import java.math.BigDecimal;

/**
 * @Author: Hut
 * @Date: 2020/03/16 9:34
 **/
public interface ITradeBillSummaryService {

    /**
     * 收入汇总
     * @return
     * @param financeQueryDto
     */
    BigDecimal incomeSummary(FinanceQueryDto financeQueryDto);

    /**
     * 退款汇总
     * @return
     */
    BigDecimal refundSummary(FinanceQueryDto financeQueryDto);

    /**
     * 成本汇总
     * @return
     * @param financeQueryDto
     */
    BigDecimal costSummary(FinanceQueryDto financeQueryDto);

    /**
     * 毛利
     * @return
     * @param financeQueryDto
     */
    BigDecimal grossProfit(FinanceQueryDto financeQueryDto);

}
