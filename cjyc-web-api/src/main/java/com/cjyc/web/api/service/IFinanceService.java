package com.cjyc.web.api.service;

import com.cjyc.common.model.dto.web.finance.ApplySettlementDto;
import com.cjyc.common.model.dto.web.finance.FinanceQueryDto;
import com.cjyc.common.model.dto.web.finance.WaitQueryDto;
import com.cjyc.common.model.vo.PageVo;
import com.cjyc.common.model.vo.ResultVo;
import com.cjyc.common.model.vo.web.finance.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @Author:Hut
 * @Date:2019/11/21 15:04
 */
public interface IFinanceService {
    ResultVo<PageVo<FinanceVo>> getFinanceList(FinanceQueryDto financeQueryDto);

    void exportExcel(HttpServletRequest request, HttpServletResponse response);

    ResultVo<PageVo<FinanceReceiptVo>> getFinanceReceiptList(FinanceQueryDto financeQueryDto);

    void applySettlement(ApplySettlementDto applySettlementDto);

    void confirmSettlement(String serialNumber,String invoiceNo);

    ResultVo<PageVo<WaitInvoiceVo>> getWaitInvoiceList(WaitQueryDto waitInvoiceQueryDto);

    void cancelSettlement(String serialNumber);

    ResultVo<PageVo<WaitForBackVo>> getWaitForBackList(WaitQueryDto waitInvoiceQueryDto);

    void writeOff(String serialNumber,String invoiceNo);

    void detail(String serialNumber);

    ResultVo<PageVo<ReceivableVo>> getReceivableList(WaitQueryDto waitInvoiceQueryDto);

    ResultVo<PageVo<PaymentVo>> getPaymentList(FinanceQueryDto financeQueryDto);
}
