package com.cjyc.web.api.service.impl;

import com.cjyc.common.model.dao.ITradeBillSummaryDao;
import com.cjyc.common.model.dto.web.finance.CooperatorSearchDto;
import com.cjyc.common.model.dto.web.finance.FinanceQueryDto;
import com.cjyc.common.model.dto.web.finance.PayMentQueryDto;
import com.cjyc.common.model.util.MoneyUtil;
import com.cjyc.common.model.vo.ResultVo;
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

    @Override
    public BigDecimal receiptSummary(FinanceQueryDto financeQueryDto) {
        return MoneyUtil.nullToZero(tradeBillSummaryDao.receiptSummary(financeQueryDto));
    }

    @Override
    public BigDecimal payToCarrierSummary(PayMentQueryDto payMentQueryDto) {
        return MoneyUtil.nullToZero(tradeBillSummaryDao.payToCarrierSummary(payMentQueryDto));
    }

    @Override
    public BigDecimal paidToCarrierSummary(PayMentQueryDto payMentQueryDto) {
        return MoneyUtil.nullToZero(tradeBillSummaryDao.paidToCarrierSummary(payMentQueryDto));
    }

    @Override
    public BigDecimal payToCooperatorSummary(CooperatorSearchDto cooperatorSearchDto) {
        return MoneyUtil.nullToZero(tradeBillSummaryDao.payToCooperatorSummary(cooperatorSearchDto)).subtract(cooperatorSummary(cooperatorSearchDto));
    }

    private BigDecimal cooperatorSummary(CooperatorSearchDto cooperatorSearchDto){
        return MoneyUtil.nullToZero(tradeBillSummaryDao.cooperatorSummary(cooperatorSearchDto));
    }

    @Override
    public BigDecimal paidToCooperatorSummary(CooperatorSearchDto cooperatorSearchDto) {
        return MoneyUtil.nullToZero(tradeBillSummaryDao.paidToCooperatorSummary(cooperatorSearchDto));
    }

    @Override
    public BigDecimal ActualReceiptSummary(FinanceQueryDto financeQueryDto) {
        return MoneyUtil.nullToZero(tradeBillSummaryDao.ActualReceiptSummary(financeQueryDto));
    }
}
