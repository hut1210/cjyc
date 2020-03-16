package com.cjyc.web.api.service;

import java.math.BigDecimal;

/**
 * @Author: Hut
 * @Date: 2020/03/16 9:34
 **/
public interface ITradeBillSummaryService {

    /**
     * 收入汇总
     * @return
     */
    BigDecimal incomeSummary();

    /**
     * 成本汇总
     * @return
     */
    BigDecimal costSummary();

    /**
     * 毛利
     * @return
     */
    BigDecimal grossProfit();

}
