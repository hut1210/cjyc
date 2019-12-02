package com.cjyc.driver.api.controller;

import com.cjyc.common.model.dto.LoginDto;
import com.cjyc.common.model.dto.VerifyCodeDto;
import com.cjyc.common.model.vo.ResultVo;
import com.cjyc.common.model.vo.driver.login.DriverLoginVo;
import com.cjyc.common.system.service.ICsLoginService;
import com.cjyc.driver.api.service.ILoginService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@Api(tags = "司机注册登录")
@Slf4j
@RestController
@RequestMapping(value = "/login",
        produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
public class LoginController {

    @Resource
    private ILoginService loginService;
    @Resource
    private ICsLoginService csLoginService;

    @ApiOperation(value = "获取验证码")
    @PostMapping("/verifyCode")
    public ResultVo verifyCode(@Validated @RequestBody VerifyCodeDto dto) {
        return csLoginService.verifyCode(dto);
    }

    @ApiOperation(value = "司机注册登陆")
    @PostMapping("/login")
    public ResultVo<DriverLoginVo> login(@Validated @RequestBody LoginDto dto) {
        return loginService.login(dto);
    }
}