package com.cjyc.common.model.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cjyc.common.model.entity.BankSubankRelation;

/**
 * <p>
 * 银行编码+支付行号对应关系表 Mapper 接口
 * </p>
 *
 * @author RenPL
 * @since 2020-4-3
 */
public interface IBankSubankRelationDao extends BaseMapper<BankSubankRelation> {

    /**
     * 根据支付行号查询银行编码
     *
     * @param payBankCode
     * @return
     */
    String getBankCodeByPayBankCode(String payBankCode);
}
