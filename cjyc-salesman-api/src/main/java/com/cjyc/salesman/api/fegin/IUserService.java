package com.cjyc.salesman.api.fegin;

import com.cjkj.common.constant.ServiceNameConstants;
import com.cjkj.common.feign.fallback.UserServiceFallbackFactory;
import com.cjkj.common.model.LoginAppUser;
import com.cjkj.common.model.ResultData;
import com.cjkj.common.model.SysLoginForm;
import com.cjkj.common.model.SysUser;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/** 提取获取
 */
@FeignClient(name = ServiceNameConstants.USER_SERVICE, fallbackFactory = UserServiceFallbackFactory.class, decode404 = true)
public interface IUserService {
    /**
     * feign rpc访问远程/users/{username}接口
     * 查询用户实体对象SysUser
     *
     * @param username
     * @return
     */
    @GetMapping(value = "/sys/user/getByUserName/{username}")
    SysUser selectByUsername(@PathVariable("username") String username);

    /**
     * 登录
     *
     * @param sysLoginForm
     * @return
     */
    @GetMapping(value = "/sys/user/login", params = "username")
    ResultData<SysUser> login(@RequestBody SysLoginForm sysLoginForm);

    /**
     * 登录
     *
     * @param username
     * @return
     */
    @GetMapping(value = "/sys/user/login", params = "username")
    LoginAppUser findByUsername(@RequestParam("username") String username);

    /**
     * 通过手机号查询用户、角色信息
     *
     * @param mobile 手机号
     */
    @GetMapping(value = "/sys/user/mobile", params = "mobile")
    LoginAppUser findByMobile(@RequestParam("mobile") String mobile);

    /**
     * 根据OpenId查询用户信息
     *
     * @param openId openId
     */
    @GetMapping(value = "/sys/user/openId", params = "openId")
    LoginAppUser findByOpenId(@RequestParam("openId") String openId);

    /**
     * feign rpc访问远程/sys/user/getAuthoriyByName/{username}接口
     * @param username
     * @return
     */
    @GetMapping(value = "/sys/user/getAuthoriyByName/{username}")
    List<String> findAuthorityByName(@PathVariable(value = "username") String username);

    /**
     * 记录登录次数
     * @param username
     * @param ip
     */
    @RequestMapping(value = "/sys/user/loginCount/{username}/{ip}")
    void loginCount(@PathVariable(value = "username")String username, @PathVariable(value = "ip")String ip);
}
