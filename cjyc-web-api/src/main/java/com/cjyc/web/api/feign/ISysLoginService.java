package com.cjyc.web.api.feign;

import com.cjkj.common.feign.fallback.UserServiceFallbackFactory;
import com.cjkj.common.model.ResultData;
import com.cjkj.usercenter.dto.common.auth.AuthLoginReq;
import com.cjkj.usercenter.dto.common.auth.AuthLoginResp;
import com.cjyc.web.api.constant.FeignServiceContant;
import org.springframework.cloud.openfeign.FeignClient;
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
}
