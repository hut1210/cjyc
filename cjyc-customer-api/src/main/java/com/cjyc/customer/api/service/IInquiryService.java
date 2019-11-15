package com.cjyc.customer.api.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.cjyc.common.model.dto.customer.freightBill.TransportDto;
import com.cjyc.common.model.entity.Inquiry;

import java.math.BigDecimal;

public interface IInquiryService extends IService<Inquiry> {

    /**
     * 保存询价
     * @param dto
     * @param defaultWlFee
     * @return
     */
    boolean saveInquiry(TransportDto dto, BigDecimal defaultWlFee);
}
