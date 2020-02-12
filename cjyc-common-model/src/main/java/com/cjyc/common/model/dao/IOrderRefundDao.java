package com.cjyc.common.model.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cjyc.common.model.entity.Refund;

/**
 * @Author: Hut
 * @Date: 2020/02/12 13:44
 */
public interface IOrderRefundDao extends BaseMapper<Refund> {
    Refund selectByOrderCode(String orderCode);

    void updateRefund(String orderNo);
}
