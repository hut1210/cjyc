package com.cjyc.customer.api.controller;

import com.cjyc.common.model.dto.salesman.login.LoginByPhoneDto;
import com.cjyc.common.model.dto.salesman.login.LoginByUserNameDto;
import com.cjyc.common.model.entity.Customer;
import com.cjyc.common.model.vo.ResultVo;
import com.cjyc.common.model.vo.salesman.login.SalemanLoginVo;
import com.cjyc.customer.api.service.ILoginService;
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

/**
 * 注册登录
 * @author JPG
 */
@Api(tags = "注册登录")
@Slf4j
@RestController
@RequestMapping(value = "/login",
        consumes = MediaType.APPLICATION_JSON_UTF8_VALUE,
        produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
public class LoginController {

    @Autowired
    private ILoginService loginService;


    @ApiOperation(value = "手机号验证码登录", notes = " ")
    @PostMapping("/phone")
    public ResultVo<Customer> login(@Validated @RequestBody LoginByPhoneDto reqDto){
        return loginService.loginByCaptcha(reqDto);
    }

    @ApiOperation(value = "用户名密码登录", notes = " ")
    @PostMapping("/username")
    public ResultVo<Customer> login(@Validated @RequestBody LoginByUserNameDto reqDto){
        return loginService.loginBypassword(reqDto);
    }
}
