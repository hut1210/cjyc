package com.cjyc.common.model.dao;

import com.cjyc.common.model.dto.web.finance.FinanceQueryDto;

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
}
