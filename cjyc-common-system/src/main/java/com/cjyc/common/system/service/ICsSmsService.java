package com.cjyc.common.system.service;

import com.cjyc.common.model.enums.CaptchaTypeEnum;

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
}
