package com.cjyc.common.model.dao;

import com.cjyc.common.model.dto.web.finance.CooperatorSearchDto;
import com.cjyc.common.model.dto.web.finance.FinanceQueryDto;
import com.cjyc.common.model.dto.web.finance.PayMentQueryDto;

import java.math.BigDecimal;

/**
 * 金额汇总
 * @Author: Hut
 * @Date: 2020/03/16 9:41
 **/
public interface ITradeBillSummaryDao {
    BigDecimal incomeSummary(FinanceQueryDto financeQueryDto);

    BigDecimal costSummary(FinanceQueryDto financeQueryDto);

    BigDecimal refundSummary(FinanceQueryDto financeQueryDto);

    BigDecimal receiptSummary(FinanceQueryDto financeQueryDto);

    BigDecimal payToCarrierSummary(PayMentQueryDto payMentQueryDto);

    BigDecimal paidToCarrierSummary(PayMentQueryDto payMentQueryDto);

    BigDecimal payToCooperatorSummary(CooperatorSearchDto cooperatorSearchDto);

    BigDecimal paidToCooperatorSummary(CooperatorSearchDto cooperatorSearchDto);

    BigDecimal cooperatorSummary(CooperatorSearchDto cooperatorSearchDto);

    BigDecimal ActualReceiptSummary(FinanceQueryDto financeQueryDto);
}
