package com.cjyc.customer.api.feign;

import com.cjkj.common.constant.ServiceNameConstants;
import com.cjkj.common.feign.fallback.UserServiceFallbackFactory;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@FeignClient(value = ServiceNameConstants.USER_SERVICE, fallbackFactory = UserServiceFallbackFactory.class, decode404 = true)
public interface ISysLoginService {

    @PostMapping("/sys/login/{username}/{password}")
    String getAuthentication(@PathVariable("username") String phone, @PathVariable("password") String fixedPwd);
}
