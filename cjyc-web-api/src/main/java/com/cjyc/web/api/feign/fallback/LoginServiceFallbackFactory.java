package com.cjyc.web.api.feign.fallback;

import com.cjkj.common.model.ResultData;
import com.cjkj.usercenter.dto.common.auth.AuthLoginReq;
import com.cjkj.usercenter.dto.common.auth.AuthLoginResp;
import com.cjyc.web.api.feign.ISysLoginService;
import feign.hystrix.FallbackFactory;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;

@Component
@Log4j2
public class LoginServiceFallbackFactory implements FallbackFactory<ISysLoginService> {
    @Override
    public ISysLoginService create(Throwable throwable) {
        return new ISysLoginService() {
            @Override
            public ResultData<AuthLoginResp> getAuthentication(AuthLoginReq loginReq) {
                log.error("调用登录服务：降级");
                return ResultData.failed("网络异常，请稍后再试");
            }
        };
    }
}
