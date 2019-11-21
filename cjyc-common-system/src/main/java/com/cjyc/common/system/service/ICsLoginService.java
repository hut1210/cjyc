package com.cjyc.common.system.service;

import com.cjyc.common.model.dto.customer.login.VerifyCodeDto;
import com.cjyc.common.model.vo.ResultVo;

public interface ICsLoginService {

    /**
     * 短信验证码
     * @param dto
     * @return
     */
    ResultVo verifyCode(VerifyCodeDto dto);
}
