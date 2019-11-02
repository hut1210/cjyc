package com.cjyc.customer.api.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.cjyc.common.model.dto.customer.invoice.CustomerInvoiceAddDto;
import com.cjyc.common.model.entity.CustomerInvoice;
import com.cjyc.common.model.vo.ResultVo;

/**
 * @Description 发票信息业务接口
 * @Author LiuXingXiang
 * @Date 2019/11/2 10:24
 **/
public interface ICustomerInvoiceService extends IService<CustomerInvoice> {

    /**
     * 新增发票信息
     * @param dto
     * @return
     */
    ResultVo applyInvoice(CustomerInvoiceAddDto dto) throws Exception;
}
