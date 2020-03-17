package com.cjyc.common.model.dao;

import com.cjyc.common.model.dto.web.publicPayBank.PayBankDto;
import com.cjyc.common.model.entity.PublicPayBank;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cjyc.common.model.vo.web.publicPay.PayBankVo;
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
public interface IPublicPayBankDao extends BaseMapper<PublicPayBank> {

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
    PublicPayBank findPayBank(@Param("subBankName") String subBankName);

}
