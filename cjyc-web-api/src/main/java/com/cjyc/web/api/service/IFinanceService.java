package com.cjyc.web.api.service;

import com.cjyc.common.model.dto.web.finance.*;
import com.cjyc.common.model.vo.PageVo;
import com.cjyc.common.model.vo.ResultVo;
import com.cjyc.common.model.vo.web.finance.*;

import java.util.List;
import java.util.Map;

/**
 * @Author:Hut
 * @Date:2019/11/21 15:04
 */
public interface IFinanceService {
    ResultVo<PageVo<FinanceVo>> getFinanceList(FinanceQueryDto financeQueryDto);

    Map exportExcel(FinanceQueryDto financeQueryDto);

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

    /**
     * 账期应收账款列表查询
     *
     * @param paymentDaysQueryDto
     * @return
     */
    ResultVo<PageVo<PaymentVo>> listPaymentDaysInfo(PaymentDaysQueryDto paymentDaysQueryDto);

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

    List<CooperatorPaidVo> exportCooperator(CooperatorSearchDto cooperatorSearchDto);

    ResultVo<List<ExportFinanceDetailVo>> getFinanceDetailList(String no);

    /**
     * <p>根据运单号查看上游付款状态列表</p>
     *
     * @param waybillNo
     * @return
     */
    ResultVo<PageVo<DriverUpstreamPaidInfoVo>> listDriverUpstreamPaidInfo(String waybillNo);

    /**
     * <p>应收账款结算申请</p>
     * <ol>
     *     <li>新增发票</li>
     *     <li>新增应收账款</li>
     *     <li>新增应收账款明细</li>
     * </ol>
     *
     * @param applyReceiveSettlementVo
     * @return
     */
    ResultVo applyReceiveSettlement(ApplyReceiveSettlementVo applyReceiveSettlementVo);
}
