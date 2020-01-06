package com.cjyc.common.model.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cjyc.common.model.dto.web.finance.*;
import com.cjyc.common.model.vo.web.finance.*;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.util.List;

/**
 * @Author:Hut
 * @Date:2019/11/19 14:41
 */
public interface IFinanceDao extends BaseMapper {

    List<FinanceVo> getFinanceList(FinanceQueryDto financeQueryDto);

    List<TrunkLineVo> getTrunkCostList(@Param("orderCarNo") String orderCarNo,@Param("type") int type);

    List<FinanceReceiptVo> getFinanceReceiptList(FinanceQueryDto financeQueryDto);

    List<PaymentVo> getPaymentList(FinanceQueryDto financeQueryDto);

    List<PaidVo> getPaidList(PayMentQueryDto payMentQueryDto);

    List<CollectReceiveVo> getCollectReceiveList(CollectReceiveQueryDto collectReceiveQueryDto);

    BigDecimal getFee(@Param("orderCarNo") String orderCarNo,@Param("type") int type);

    Integer getCustomerContractById(Long customerContractId);

    Integer getCustomerPartnerById(Long customerId);

    List<FinancePayableVo> getFinancePayableList(PayableQueryDto payableQueryDto);

    List<SettlementPayableVo> getSettlementInfo(List<String> taskNoList);

    List<SettlementVo> getCollectTicketList(WaitTicketCollectDto waitTicketCollectDto);

    void apply(SettlementVo settlementVo);
}
