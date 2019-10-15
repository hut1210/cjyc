package com.cjyc.customer.api.service;

import com.cjyc.common.model.dto.salesman.login.LoginByPhoneDto;
import com.cjyc.common.model.dto.salesman.login.LoginByUserNameDto;
import com.cjyc.common.model.entity.Customer;
import com.cjyc.common.model.vo.ResultVo;

public interface ILoginService {
    /**
     * 手机号验证码登录
     * @author JPG
     * @since 2019/10/12 17:34
     * @param captchaValidatedDto
     */
    ResultVo<Customer> loginByCaptcha(LoginByPhoneDto captchaValidatedDto);

     /**
     * 用户名密码登录
     * @author JPG
     * @since 2019/10/12 17:34
     * @param loginByUserNameDto
     */
    ResultVo<Customer> loginBypassword(LoginByUserNameDto loginByUserNameDto);
}
