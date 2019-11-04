package com.cjyc.customer.api.service.impl;

import com.cjkj.common.service.impl.SuperServiceImpl;
import com.cjyc.common.model.dao.IInvoiceApplyDao;
import com.cjyc.common.model.dao.IOrderDao;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cjyc.common.model.entity.InvoiceApply;
import com.cjyc.customer.api.service.IInvoiceApplyService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author JPG
 * @since 2019-10-31
 */
@Service
@Slf4j
@Transactional(propagation = Propagation.REQUIRED, rollbackFor = RuntimeException.class)
public class InvoiceApplyServiceImpl extends SuperServiceImpl<IInvoiceApplyDao, InvoiceApply> implements IInvoiceApplyService {
    @Resource
    private IOrderDao orderDao;
    @Resource
    private IInvoiceApplyDao invoiceApplyDao;

    @Override
    public String addAndReturnId(InvoiceApply invoiceApply) {
        return super.saveAndReturnId(invoiceApply);
    }
}
