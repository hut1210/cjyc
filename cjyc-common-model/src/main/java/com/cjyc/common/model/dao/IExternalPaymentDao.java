package com.cjyc.common.model.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cjyc.common.model.entity.ExternalPayment;

/**
 * @Author: Hut
 * @Date: 2020/02/18 9:06
 */
public interface IExternalPaymentDao extends BaseMapper<ExternalPayment> {
    ExternalPayment getByWayBillId(Long waybillId);

    void updateByWayBillId(ExternalPayment externalPayment);
}
