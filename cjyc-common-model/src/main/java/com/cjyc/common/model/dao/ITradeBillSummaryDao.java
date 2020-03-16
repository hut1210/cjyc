package com.cjyc.common.model.dao;

import java.math.BigDecimal;

/**
 * 金额汇总
 * @Author: Hut
 * @Date: 2020/03/16 9:41
 **/
public interface ITradeBillSummaryDao {
    BigDecimal incomeSummary();

    BigDecimal costSummary();
}
