package com.cjyc.web.api.service;

import com.cjyc.common.model.dto.web.finance.CooperatorSearchDto;
import com.cjyc.common.model.dto.web.finance.FinanceQueryDto;
import com.cjyc.common.model.dto.web.finance.PayMentQueryDto;

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

    /**
     * 应收账款汇总
     * @param financeQueryDto
     * @return
     */
    BigDecimal receiptSummary(FinanceQueryDto financeQueryDto);

    BigDecimal payToCarrierSummary(PayMentQueryDto payMentQueryDto);

    BigDecimal paidToCarrierSummary(PayMentQueryDto payMentQueryDto);

    BigDecimal payToCooperatorSummary(CooperatorSearchDto cooperatorSearchDto);

    BigDecimal paidToCooperatorSummary(CooperatorSearchDto cooperatorSearchDto);

    BigDecimal actualReceiptSummary(FinanceQueryDto financeQueryDto);

    /**
     * 预付未完结应收汇总
     * @param financeQueryDto
     * @return
     */
    BigDecimal advancePaymentSummary(FinanceQueryDto financeQueryDto);

    /**
     * 预付未完结已付汇总
     * @param financeQueryDto
     * @return
     */
    BigDecimal actualAdvancePaymentSummary(FinanceQueryDto financeQueryDto);

    /**
     * 应收账款收益汇总
     * @param financeQueryDto
     * @return
     */
    BigDecimal receiptIncomeSummary(FinanceQueryDto financeQueryDto);

    /**
     * 查询总流水实收汇总
     * @param financeQueryDto
     * @return
     */
    BigDecimal financeActualReceiptSummary(FinanceQueryDto financeQueryDto);
}
