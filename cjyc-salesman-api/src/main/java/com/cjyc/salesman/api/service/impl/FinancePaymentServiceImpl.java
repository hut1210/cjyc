package com.cjyc.salesman.api.service.impl;

import com.cjyc.common.model.entity.FinancePayment;
import com.cjyc.common.model.dao.IFinancePaymentDao;
import com.cjyc.salesman.api.service.IFinancePaymentService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 财务付款单表 服务实现类
 * </p>
 *
 * @author JPG
 * @since 2019-10-09
 */
@Service
public class FinancePaymentServiceImpl extends ServiceImpl<IFinancePaymentDao, FinancePayment> implements IFinancePaymentService {

}
