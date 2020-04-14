package com.cjyc.web.api.service;

import com.cjyc.common.model.dto.web.finance.*;
import com.cjyc.common.model.vo.PageVo;
import com.cjyc.common.model.vo.ResultVo;
import com.cjyc.common.model.vo.web.finance.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

/**
 * @Author:Hut
 * @Date:2019/11/21 15:04
 */
public interface IFinanceService {
    ResultVo<PageVo<FinanceVo>> getFinanceList(FinanceQueryDto financeQueryDto);

    ResultVo exportExcel(HttpServletResponse response, FinanceQueryDto financeQueryDto);

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

    ResultVo<Object> exportPaymentExcel(HttpServletResponse response, FinanceQueryDto financeQueryDto);

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
     *     <li>新增结算信息</li>
     *     <li>新增结算明细信息</li>
     * </ol>
     *
     * @param applyReceiveSettlementVo
     * @return
     */
    ResultVo applyReceiveSettlement(ApplyReceiveSettlementVo applyReceiveSettlementVo);

    /**
     * <p>应收账款结算-待开票列表查询, 此时查询结算状态为已经申请的结算信息列表</p>
     *
     * @param receiveSettlementNeedInvoiceVo
     * @return
     */
    ResultVo<PageVo<ReceiveSettlementDto>> listReceiveSettlementNeedInvoice(ReceiveSettlementNeedInvoiceVo receiveSettlementNeedInvoiceVo);

    /**
     * <p>应收账款-待开票-撤回</p>
     * <ol>
     *     <li>删除结算信息</li>
     *     <li>删除结算明细信息</li>
     * </ol>
     *
     * @param cancelInvoiceVo
     * @return
     */
    ResultVo cancelReceiveSettlement(CancelInvoiceVo cancelInvoiceVo);

    /**
     *
     * <p>应收账款-待开票-确认开票</p>
     * <ol>
     *     <li>修改结算状态为已确认</li>
     * </ol>
     *
     * @param confirmInvoiceVo
     * @return
     */
    ResultVo confirmInvoice(ConfirmInvoiceVo confirmInvoiceVo);

    /**
     * <p>应收账款-待回款-核销</p>
     * <ol>
     *     <li>修改结算状态为已核销</li>
     * </ol>
     *
     * @param verificationReceiveSettlementVo
     * @return
     */
    ResultVo verificationReceiveSettlement(VerificationReceiveSettlementVo verificationReceiveSettlementVo);

    /**
     * 应收账款结算-结算明细查询
     *
     * @param serialNumber
     * @return
     */
    ResultVo<ReceiveSettlementInvoiceDetailDto> listReceiveSettlementDetail(String serialNumber);

    /**
     * 应收账款结算-待回款(账期)列表查询
     *
     * @param receiveSettlementNeedPayedVo
     * @return
     */
    ResultVo<PageVo<ReceiveSettlementDto>> listReceiveSettlementNeedPayed(ReceiveSettlementNeedPayedVo receiveSettlementNeedPayedVo);

    /**
     * 应收账款结算-已收款(账期)列表查询
     *
     * @param receiveSettlementNeedInvoiceVo
     * @return
     */
    ResultVo<PageVo<ReceiveSettlementDto>> listReceiveSettlementPayed(ReceiveSettlementNeedInvoiceVo receiveSettlementNeedInvoiceVo);

    /**
     * 账期应收账款列表查询
     *
     * @param financeQueryDto
     * @return
     */
    ResultVo<PageVo<ReceiveOrderCarDto>> listPaymentDaysInfo(FinanceQueryDto financeQueryDto);

    /**
     * 导出应收账款（账期）Excel
     *
     * @param financeQueryDto
     * @return
     */
    ResultVo exportPaymentDaysInfo(HttpServletResponse response, FinanceQueryDto financeQueryDto);

    /**
     * 导出应收账款-待开票（账期）Excel
     *
     * @param response
     * @param receiveSettlementNeedInvoiceVo
     * @return
     */
    ResultVo exportReceiveSettlementNeedInvoice(HttpServletResponse response, ReceiveSettlementNeedInvoiceVo receiveSettlementNeedInvoiceVo);

    /**
     * 导出应收账款-待回款（账期）Excel
     *
     * @param response
     * @param receiveSettlementNeedPayedVo
     * @return
     */
    ResultVo exportReceiveSettlementNeedPayed(HttpServletResponse response, ReceiveSettlementNeedPayedVo receiveSettlementNeedPayedVo);

    /**
     * 导出应收账款-已收款（账期）Excel
     *
     * @param response
     * @param receiveSettlementNeedInvoiceVo
     * @return
     */
    ResultVo exportReceiveSettlementPayed(HttpServletResponse response, ReceiveSettlementNeedInvoiceVo receiveSettlementNeedInvoiceVo);

    /**
     * 预付未完结列表
     * @param financeQueryDto
     * @return
     */
    ResultVo<PageVo<AdvancePaymentVo>> getAdvancePayment(FinanceQueryDto financeQueryDto);

    /**
     * 导出预付未完结 Excel
     * @param financeQueryDto
     * @return
     */
    ResultVo exportAdvancePaymentExcel(HttpServletResponse response, FinanceQueryDto financeQueryDto);
}
