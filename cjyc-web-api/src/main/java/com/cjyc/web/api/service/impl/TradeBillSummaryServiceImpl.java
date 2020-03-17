package com.cjyc.web.api.service.impl;

import com.cjyc.common.model.dao.ITradeBillSummaryDao;
import com.cjyc.common.model.dto.web.finance.FinanceQueryDto;
import com.cjyc.common.model.util.MoneyUtil;
import com.cjyc.web.api.service.ITradeBillSummaryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;

/**
 * @Author: Hut
 * @Date: 2020/03/16 9:35
 **/
@Service
@Slf4j
@Transactional(rollbackFor = RuntimeException.class)
public class TradeBillSummaryServiceImpl implements ITradeBillSummaryService {

    @Resource
    private ITradeBillSummaryDao tradeBillSummaryDao;

    @Override
    public BigDecimal incomeSummary(FinanceQueryDto financeQueryDto) {
        return MoneyUtil.nullToZero(tradeBillSummaryDao.incomeSummary(financeQueryDto));
    }

    @Override
    public BigDecimal refundSummary(FinanceQueryDto financeQueryDto) {
        return MoneyUtil.nullToZero(tradeBillSummaryDao.refundSummary(financeQueryDto));
    }

    @Override
    public BigDecimal costSummary(FinanceQueryDto financeQueryDto) {
        return MoneyUtil.nullToZero(tradeBillSummaryDao.costSummary(financeQueryDto));
    }

    @Override
    public BigDecimal grossProfit(FinanceQueryDto financeQueryDto) {
        return incomeSummary(financeQueryDto).subtract(costSummary(financeQueryDto));
    }
}
