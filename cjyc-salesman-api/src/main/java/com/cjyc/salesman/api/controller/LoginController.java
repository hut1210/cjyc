package com.cjyc.salesman.api.controller;

import com.cjyc.common.model.dto.salesman.sms.CaptchaValidatedDto;
import com.cjyc.common.model.dto.salesman.login.LoginByPasswordDto;
import com.cjyc.common.model.vo.ResultVo;
import com.cjyc.common.model.vo.salesman.login.SalemanLoginVo;
import com.cjyc.salesman.api.service.ILoginService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Api(tags = "登录")
@Slf4j
@RestController
@RequestMapping(value = "/login",
        consumes = MediaType.APPLICATION_JSON_UTF8_VALUE,
        produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
public class LoginController {

    @Autowired
    private ILoginService loginService;


    @ApiOperation(value = "手机号验证码登录", notes = " ")
    @PostMapping("/Captcha")
    public ResultVo<SalemanLoginVo> login(@Validated @RequestBody CaptchaValidatedDto reqDto){
        return loginService.loginByCaptcha(reqDto);
    }


    @ApiOperation(value = "用户名密码登录", notes = " ")
    @PostMapping("/password")
    public ResultVo<SalemanLoginVo> login(@Validated @RequestBody LoginByPasswordDto reqDto){
        return loginService.loginBypassword(reqDto);
    }
}
