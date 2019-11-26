package com.cjyc.common.system.service;

import com.cjyc.common.model.enums.CaptchaTypeEnum;
import com.cjyc.common.model.enums.ClientEnum;
import com.cjyc.common.model.vo.ResultVo;

public interface ICsSmsService {
    /**
     * 验证验证码
     * @author JPG
     * @since 2019/11/25 17:24
     * @param phone
     * @param captcha
     * @param captchaTypeEnum
     */
    boolean validateCaptcha(String phone, String captcha, CaptchaTypeEnum captchaTypeEnum);

    /**
     * 发送验证码
     * @author JPG
     * @since 2019/11/26 8:38
     * @param phone
     * @param captchaTypeEnum
     */
    ResultVo send(String phone, CaptchaTypeEnum captchaTypeEnum, ClientEnum clientEnum);
}
