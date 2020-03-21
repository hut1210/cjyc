package com.cjyc.common.model.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cjyc.common.model.dto.web.finance.ReceiveSettlementDto;
import com.cjyc.common.model.entity.ReceiveSettlement;
import com.cjyc.common.model.vo.web.finance.ReceiveSettlementNeedPayedVo;

import java.util.List;

/**
 * <p>
 * 应收账款结算表Mapper接口
 * </p>
 *
 * @author RenPL
 * @since 2020-03-20
 */
public interface IReceiveSettlementDao extends BaseMapper<ReceiveSettlement> {

    /**
     * 待开票列表查询
     *
     * @param receiveSettlement
     * @return
     */

    List<ReceiveSettlementDto> listReceiveSettlement(ReceiveSettlement receiveSettlement);

    /**
     * 应收账款结算-待回款列表查询
     *
     * @param receiveSettlementNeedPayedVo
     * @return
     */
    List<ReceiveSettlementDto> listReceiveSettlementNeedInvoice(ReceiveSettlementNeedPayedVo receiveSettlementNeedPayedVo);
}
