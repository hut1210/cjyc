package com.cjyc.customer.api.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.cjyc.common.model.entity.InvoiceApply;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author JPG
 * @since 2019-10-31
 */
public interface IInvoiceApplyService extends IService<InvoiceApply> {

    /**
     * 信息发票申请信息，返回id
     * @param invoiceApply
     * @return
     */
    String addAndReturnId(InvoiceApply invoiceApply);
}
