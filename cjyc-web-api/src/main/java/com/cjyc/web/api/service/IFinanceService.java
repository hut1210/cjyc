package com.cjyc.web.api.service;

import com.cjyc.common.model.dto.web.finance.*;
import com.cjyc.common.model.vo.PageVo;
import com.cjyc.common.model.vo.ResultVo;
import com.cjyc.common.model.vo.web.finance.*;

import java.util.List;

/**
 * @Author:Hut
 * @Date:2019/11/21 15:04
 */
public interface IFinanceService {
    ResultVo<PageVo<FinanceVo>> getFinanceList(FinanceQueryDto financeQueryDto);

    List<ExportFinanceVo> exportExcel(FinanceQueryDto financeQueryDto);

    ResultVo<PageVo<FinanceReceiptVo>> getFinanceReceiptList(FinanceQueryDto financeQueryDto);

    void applySettlement(ApplySettlementDto applySettlementDto);

    void confirmSettlement(String serialNumber,String invoiceNo);

    ResultVo<PageVo<WaitInvoiceVo>> getWaitInvoiceList(WaitQueryDto waitInvoiceQueryDto);

    void cancelSettlement(String serialNumber);

    ResultVo<PageVo<WaitForBackVo>> getWaitForBackList(WaitQueryDto waitInvoiceQueryDto);

    void writeOff(String serialNumber,String invoiceNo);

    SettlementDetailVo detail(Long Id);

    ResultVo<PageVo<ReceivableVo>> getReceivableList(WaitQueryDto waitInvoiceQueryDto);

    ResultVo<PageVo<PaymentVo>> getPaymentList(FinanceQueryDto financeQueryDto);

    ResultVo<PageVo<PaidVo>> getPaidList(PayMentQueryDto payMentQueryDto);

    ResultVo<PageVo<CollectReceiveVo>> getCollectReceiveList(CollectReceiveQueryDto collectReceiveQueryDto);

    ResultVo<CashSettlementDetailVo> settleDetail(String wayBillNo);

    ResultVo updateBackState(String wayBillNo);

    ResultVo<PageVo<FinancePayableVo>> getFinancePayableList(PayableQueryDto payableQueryDto);

    ResultVo getSettlementPayable(List<String> taskNo);

    ResultVo apply(AppSettlementPayableDto appSettlementPayableDto);

    ResultVo<PageVo<SettlementVo>> collect(WaitTicketCollectDto waitTicketCollectDto);

    ResultVo getConfirmTicket(String serialNumber);

    ResultVo confirm(ConfirmTicketDto confirmTicketDto);

    ResultVo withdraw(String serialNumber);

    ResultVo<PageVo<SettlementVo>> payment(WaitPaymentDto waitPaymentDto);

    ResultVo getWriteOffTicket(String serialNumber);

    ResultVo writeOffPayable(WriteOffTicketDto writeOffTicketDto);

    ResultVo paid(PayablePaidQueryDto payablePaidQueryDto);

    ResultVo payableDetail(String serialNumber);

    ResultVo<PageVo<PaidNewVo>> getPaidListNew(PayMentQueryDto payMentQueryDto);

    ResultVo externalPayment(ExternalPaymentDto externalPaymentDto);

    List<PaymentVo> exportPaymentExcel(FinanceQueryDto financeQueryDto);

    List<FinancePayableVo> exportPayableAll(PayableQueryDto payableQueryDto);

    List<SettlementVo> exportPayableCollect(WaitTicketCollectDto waitTicketCollectDto);

    List<SettlementVo> exportPayment(WaitPaymentDto waitPaymentDto);

    List<PayablePaidVo> exportPaid(PayablePaidQueryDto payablePaidQueryDto);

    List<PaidNewVo> exportTimePaid(PayMentQueryDto payMentQueryDto);

    ResultVo<PageVo<CooperatorPaidVo>> getCooperatorPaidList(CooperatorSearchDto cooperatorSearchDto);

    ResultVo payToCooperator(CooperatorPaymentDto cooperatorPaymentDto);
}
