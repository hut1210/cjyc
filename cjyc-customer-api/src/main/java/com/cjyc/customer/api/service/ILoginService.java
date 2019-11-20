package com.cjyc.customer.api.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.cjyc.common.model.dto.LoginDto;
import com.cjyc.common.model.dto.customer.login.VerifyCodeDto;
import com.cjyc.common.model.dto.salesman.login.LoginByPhoneDto;
import com.cjyc.common.model.entity.Customer;
import com.cjyc.common.model.vo.ResultVo;
import com.cjyc.common.model.vo.customer.login.CustomerLoginVo;

/**
 * 登录
 * @author JPG
 */
public interface ILoginService extends IService<Customer> {

    /**
     * 根据手机号获取验证码
     * @param dto
     * @return
     */
    ResultVo verifyCode(VerifyCodeDto dto);

    /**
     * 手机号验证码登陆
     * @param dto
     * @return
     */
    ResultVo<CustomerLoginVo> login(LoginDto dto);

    /**
     * 手机号验证码登录
     * @author JPG
     * @since 2019/10/12 17:34
     * @param captchaValidatedDto
     */
    ResultVo<CustomerLoginVo> loginByPhone(LoginByPhoneDto captchaValidatedDto);


}
