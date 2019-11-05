package com.cjyc.customer.api.service;

import com.cjyc.common.model.dto.customer.partner.ApplyPartnerDto;
import com.cjyc.common.model.vo.ResultVo;

public interface IApplyPartnerService {

    /**
     * C端用户申请合伙人
     * @param dto
     * @return
     */
    ResultVo applyPartner(ApplyPartnerDto dto);
}
