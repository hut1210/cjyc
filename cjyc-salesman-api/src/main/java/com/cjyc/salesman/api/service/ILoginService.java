package com.cjyc.salesman.api.service;

import com.cjyc.common.model.dto.salesman.sms.CaptchaValidatedDto;
import com.cjyc.common.model.dto.salesman.login.LoginByPasswordDto;
import com.cjyc.common.model.vo.ResultVo;
import com.cjyc.common.model.vo.salesman.login.SalemanLoginVo;

public interface ILoginService {
    /**
     * 手机号验证码登录
     * @author JPG
     * @since 2019/10/12 17:34
     * @param captchaValidatedDto
     */
    ResultVo<SalemanLoginVo> loginByCaptcha(CaptchaValidatedDto captchaValidatedDto);

    /**
     * 用户名密码登录
     * @author JPG
     * @since 2019/10/12 17:34
     * @param loginByPasswordDto
     */
    ResultVo<SalemanLoginVo> loginBypassword(LoginByPasswordDto loginByPasswordDto);
}
