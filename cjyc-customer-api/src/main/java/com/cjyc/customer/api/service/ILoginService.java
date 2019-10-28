package com.cjyc.customer.api.service;

import com.cjyc.common.model.dto.salesman.login.LoginByPhoneDto;
import com.cjyc.common.model.vo.ResultVo;
import com.cjyc.common.model.vo.customer.login.CustomerLoginVo;

public interface ILoginService {
    /**
     * 手机号验证码登录
     * @author JPG
     * @since 2019/10/12 17:34
     * @param captchaValidatedDto
     */
    ResultVo<CustomerLoginVo> loginByCaptcha(LoginByPhoneDto captchaValidatedDto);

}
