package com.cjyc.customer.api.service;

import com.cjyc.common.model.dto.customer.invoice.InvoiceOrderQueryDto;
import com.cjyc.common.model.entity.CustomerLine;
import com.baomidou.mybatisplus.extension.service.IService;
import com.cjyc.common.model.vo.ResultVo;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author JPG
 * @since 2019-11-01
 */
public interface ICustomerLineService extends IService<CustomerLine> {

    /**
     *
     * @param dto
     * @return
     */
    ResultVo getCustomerLine(InvoiceOrderQueryDto dto);

}
