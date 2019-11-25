package com.cjyc.common.system.service;

import com.cjyc.common.model.dto.salesman.sms.CaptchaSendDto;
import com.cjyc.common.model.dto.salesman.sms.CaptchaValidatedDto;
import com.cjyc.common.model.vo.ResultVo;

public interface ICsSmsService {

    /**
     * 获取短信验证码
     * @param reqDto
     * @return
     */
    ResultVo send(CaptchaSendDto reqDto);

    /**
     * 校验短信验证码
     * @param reqDto
     * @return
     */
    ResultVo validate(CaptchaValidatedDto reqDto);
}
