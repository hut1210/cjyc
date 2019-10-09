package com.cjyc.salesman.api.fegin.fallback;

import com.cjkj.common.feign.UserService;
import com.cjkj.common.model.LoginAppUser;
import com.cjkj.common.model.ResultData;
import com.cjkj.common.model.SysLoginForm;
import com.cjkj.common.model.SysUser;
import feign.hystrix.FallbackFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.ArrayList;
import java.util.List;

/**
 * userService降级工场
 *
 */
@Slf4j
@Component
public class UserServiceFallbackFactory implements FallbackFactory<UserService> {
    @Override
    public UserService create(Throwable throwable) {
        return new UserService() {
            @Override
            public SysUser selectByUsername(String username) {
                log.error("通过用户名查询用户异常:{}", username, throwable);
                return new SysUser();
            }

            @Override
            public LoginAppUser findByUsername(String username) {
                log.error("通过用户名查询用户异常:{}", username, throwable);
                return new LoginAppUser();
            }

            @Override
            public LoginAppUser findByMobile(String mobile) {
                log.error("通过手机号查询用户异常:{}", mobile, throwable);
                return new LoginAppUser();
            }

            @Override
            public LoginAppUser findByOpenId(String openId) {
                log.error("通过openId查询用户异常:{}", openId, throwable);
                return new LoginAppUser();
            }

            @Override
            public List<String> findAuthorityByName(@PathVariable(value = "username") String username) {
                log.error("通过username查询用户权限异常:{}", username, throwable);
                return new ArrayList<>();
            }

            @Override
            public void loginCount(@PathVariable(value = "username") String username, @PathVariable(value = "ip") String ip) {
                log.error("记录登录次数异常{}{}", username, ip);
            }

            @Override
            public ResultData<SysUser> login(@RequestBody SysLoginForm sysLoginForm){
                log.error("通过SysLoginForm查询用户异常:{}", sysLoginForm, throwable);
                return ResultData.ok(new SysUser());
            }
        };
    }
}
