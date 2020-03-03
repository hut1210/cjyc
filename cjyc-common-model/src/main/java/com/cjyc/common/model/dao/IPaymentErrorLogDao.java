package com.cjyc.common.model.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cjyc.common.model.entity.PaymentErrorLog;

/**
 * @Author: Hut
 * @Date: 2020/02/22 17:30
 */
public interface IPaymentErrorLogDao extends BaseMapper<PaymentErrorLog> {
    PaymentErrorLog getByWayBillNo(String waybillNo);
}
