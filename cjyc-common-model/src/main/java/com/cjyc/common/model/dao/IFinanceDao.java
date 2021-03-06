package com.cjyc.common.model.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cjyc.common.model.dto.web.finance.*;
import com.cjyc.common.model.vo.web.finance.*;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * @Author:Hut
 * @Date:2019/11/19 14:41
 */
public interface IFinanceDao extends BaseMapper {

    List<FinanceVo> getFinanceList(FinanceQueryDto financeQueryDto);

    List<TrunkLineVo> getTrunkCostList(@Param("orderCarNo") String orderCarNo,@Param("type") int type);

    List<FinanceReceiptVo> getFinanceReceiptList(FinanceQueryDto financeQueryDto);

    List<PaymentVo> getPaymentList(FinanceQueryDto financeQueryDto);

    /**
     * 账期应收账款列表查询
     *
     * @param financeQueryDto
     * @return
     */
    List<ReceiveOrderCarDto> listPaymentDaysInfo(FinanceQueryDto financeQueryDto);

    List<PaidVo> getPaidList(PayMentQueryDto payMentQueryDto);

    List<CollectReceiveVo> getCollectReceiveList(CollectReceiveQueryDto collectReceiveQueryDto);

    BigDecimal getFee(@Param("orderCarNo") String orderCarNo,@Param("type") int type);

    Integer getCustomerContractById(Long customerContractId);

    Integer getCustomerPartnerById(Long customerId);

    List<FinancePayableVo> getFinancePayableList(PayableQueryDto payableQueryDto);

    List<PayableTaskVo> getSettlementInfo(@Param("taskNoList")List<String> taskNoList);

    List<SettlementVo> getCollectTicketList(WaitTicketCollectDto waitTicketCollectDto);

    void apply(SettlementVo settlementVo);

    void applyDetail(SettlementVo settlement);

    List<String> getTaskNoList(String serialNumber);

    void confirm(SettlementVo settlementVo);

    void withdraw(String serialNumber);

    List<SettlementVo> getPayablePaymentList(WaitPaymentDto waitPaymentDto);

    SettlementVo getPayableSettlement(String serialNumber);

    List<PayablePaidVo> getPayablePaidList(PayablePaidQueryDto payablePaidQueryDto);

    void writeOffPayable(SettlementVo settlementVo);

    List<ExportFinanceVo> getAllFinanceList(FinanceQueryDto financeQueryDto);

    List<PaidNewVo> getPaidListNew(PayMentQueryDto payMentQueryDto);

    List<PaidNewVo> getAutoPaidList(PayMentQueryDto payMentQueryDto);

    List<CooperatorPaidVo> getCooperatorPaidList(CooperatorSearchDto cooperatorSearchDto);

    List<ExportFinanceDetailVo> getFinanceDetailList(String no);

    BigDecimal payableAccountSummary(PayableQueryDto payableQueryDto);

    BigDecimal waitCollectSummary(WaitTicketCollectDto waitTicketCollectDto);

    BigDecimal paymentSummary(WaitPaymentDto waitPaymentDto);

    Map payablePaidSummary(PayablePaidQueryDto payablePaidQueryDto);

    /**
     * 应收账款总额统计
     *
     * @param financeQueryDto
     * @return
     */
    BigDecimal receiptSummary(FinanceQueryDto financeQueryDto);

    List<AdvancePaymentVo> getAdvancePayment(FinanceQueryDto financeQueryDto);

    FinanceSettlementDetailVo getSettlementDetail(String no);

    AccountPeriodVo getAccountPeriodInfo(String wayBillNo);

    List<TrunkLineVo> getCostList(String orderCarNo);
}
