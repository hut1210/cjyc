package com.cjyc.common.system.service;

import com.cjyc.common.model.enums.CaptchaTypeEnum;
import com.cjyc.common.model.enums.ClientEnum;
import com.cjyc.common.model.vo.ResultVo;

public interface ICsSmsService {
    /**
     * 验证验证码
     * @author JPG
     * @since 2019/11/25 17:24
     * @param phone 手机号
     * @param captcha 验证码
     * @param captchaTypeEnum 验证码类型
     */
    boolean validateCaptcha(String phone, String captcha, CaptchaTypeEnum captchaTypeEnum, ClientEnum clientEnum);

    /**
     * 发送验证码
     * @author JPG
     * @since 2019/11/26 8:49
     * @param phone 手机号
     * @param captchaTypeEnum 验证码类型
     * @param clientEnum 客户端类型
     */
    ResultVo send(String phone, CaptchaTypeEnum captchaTypeEnum, ClientEnum clientEnum);
    void send(String phone, String msg, Object... args);
}
