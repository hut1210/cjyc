package com.cjyc.common.model.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cjyc.common.model.dto.web.finance.FinanceQueryDto;
import com.cjyc.common.model.vo.web.finance.FinanceReceiptVo;
import com.cjyc.common.model.vo.web.finance.FinanceVo;
import com.cjyc.common.model.vo.web.finance.PaymentVo;
import com.cjyc.common.model.vo.web.finance.TrunkLineVo;

import java.util.List;

/**
 * @Author:Hut
 * @Date:2019/11/19 14:41
 */
public interface IFinanceDao extends BaseMapper {

    List<FinanceVo> getFinanceList(FinanceQueryDto financeQueryDto);

    List<TrunkLineVo> getTrunkCostList(String orderCarNo);

    List<FinanceReceiptVo> getFinanceReceiptList(FinanceQueryDto financeQueryDto);

    List<PaymentVo> getPaymentList(FinanceQueryDto financeQueryDto);
}
