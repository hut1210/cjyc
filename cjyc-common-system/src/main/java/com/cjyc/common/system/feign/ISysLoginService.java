package com.cjyc.common.system.feign;

import com.cjkj.common.feign.fallback.UserServiceFallbackFactory;
import com.cjkj.common.model.ResultData;
import com.cjkj.usercenter.dto.common.SelectDeptResp;
import com.cjkj.usercenter.dto.common.auth.AuthLoginReq;
import com.cjkj.usercenter.dto.common.auth.AuthLoginResp;
import com.cjkj.usercenter.dto.common.auth.AuthMobileLoginReq;
import com.cjkj.usercenter.dto.common.auth.SendSmsCodeReq;
import com.cjyc.common.model.constant.FeignServiceContant;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * sys登录
 * @author JPG
 */
@FeignClient(value = FeignServiceContant.OAUTH_SERVICE, fallbackFactory = UserServiceFallbackFactory.class, decode404 = true)
public interface ISysLoginService {

    /**
     * 用户名密码获取token
     * @author JPG
     * @since 2019/10/21 9:33
     * @param authLoginReq 参数
     */
    @PostMapping("/feign/auth/login")
    ResultData<AuthLoginResp> getAuthentication(@RequestBody AuthLoginReq authLoginReq);

    /**
     * 手机号短信码登录
     * @param req
     * @return
     */
    @PostMapping("/feign/auth/mobile/login")
    ResultData<AuthLoginResp> mobileLogin(@RequestBody AuthMobileLoginReq req);

    /**
     * 根据手机号获取验证码
     * @param req
     * @return
     */
    @PostMapping("/sms/code")
    ResultData verifyCode(@RequestBody SendSmsCodeReq req);
}

