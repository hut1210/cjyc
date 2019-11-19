package com.cjyc.customer.api.controller;

import com.cjyc.common.model.dto.customer.login.LoginDto;
import com.cjyc.common.model.dto.salesman.login.LoginByPhoneDto;
import com.cjyc.common.model.dto.web.customer.CustomerfuzzyListDto;
import com.cjyc.common.model.util.BaseResultUtil;
import com.cjyc.common.model.util.RegexUtil;
import com.cjyc.common.model.vo.ResultVo;
import com.cjyc.common.model.vo.customer.login.CustomerLoginVo;
import com.cjyc.customer.api.service.ILoginService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;

/**
 * 注册登录
 * @author JPG
 */
@Api(tags = "注册登录")
@Slf4j
@RestController
@RequestMapping(value = "/login",
        produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
public class LoginController {

    @Autowired
    private ILoginService loginService;

    @ApiOperation(value = "获取验证码")
    @PostMapping("/verifyCode/{phone}")
    public ResultVo verifyCode(@PathVariable @ApiParam(value = "手机号",required = true) String phone) {
        return loginService.verifyCode(phone);
    }

    @ApiOperation(value = "获取验证码")
    @PostMapping("/login")
    public ResultVo<CustomerLoginVo> login(@Validated @RequestBody LoginDto dto) {
        return loginService.login(dto);
    }

    @ApiOperation(value = "手机号验证码登录", notes = " ")
    @PostMapping("/phone")
    public ResultVo<CustomerLoginVo> login(@Validated @RequestBody LoginByPhoneDto reqDto){
        //验证手机号
        @NotNull String phone = reqDto.getPhone();
        boolean isMobile = RegexUtil.isMobileExact(phone);
        if(!isMobile){
            return BaseResultUtil.fail("请输入正确的手机号");
        }

        return loginService.loginByPhone(reqDto);
    }
}
