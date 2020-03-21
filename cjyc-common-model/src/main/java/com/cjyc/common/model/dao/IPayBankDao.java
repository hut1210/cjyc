package com.cjyc.common.model.dao;

import com.cjyc.common.model.dto.web.payBank.PayBankDto;
import com.cjyc.common.model.entity.PayBank;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cjyc.common.model.vo.web.payBank.PayBankVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author JPG
 * @since 2020-03-16
 */
public interface IPayBankDao extends BaseMapper<PayBank> {

    /**
     * 获取对公支付银行信息
     * @param dto
     * @return
     */
    List<PayBankVo> findPayBankInfo(PayBankDto dto);

    /**
     * 根据支行名称获取对公支付银行信息
     * @param subBankName
     * @return
     */
    PayBank findPayBank(@Param("subBankName") String subBankName);

}
