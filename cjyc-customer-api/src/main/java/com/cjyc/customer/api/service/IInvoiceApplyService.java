package com.cjyc.customer.api.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.cjyc.common.model.dto.customer.invoice.CustomerInvoiceAddDto;
import com.cjyc.common.model.dto.customer.invoice.InvoiceApplyQueryDto;
import com.cjyc.common.model.entity.InvoiceApply;
import com.cjyc.common.model.vo.ResultVo;

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
     * 查询已申请发票信息
     * @param dto
     * @return
     */
    ResultVo getInvoiceApplyPage(InvoiceApplyQueryDto dto);
    /**
     * 新增发票信息
     * @param dto
     * @return
     */
    ResultVo applyInvoice(CustomerInvoiceAddDto dto) throws Exception;
}
