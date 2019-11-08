package com.cjyc.salesman.api.controller;

import com.cjkj.common.model.ResultData;
import com.cjkj.common.model.ReturnMsg;
import com.cjkj.usercenter.dto.common.auth.AuthLoginReq;
import com.cjkj.usercenter.dto.common.auth.AuthLoginResp;
import com.cjkj.usercenter.dto.common.auth.AuthMobileLoginReq;
import com.cjyc.common.model.dto.salesman.login.LoginByPhoneDto;
import com.cjyc.common.model.dto.salesman.login.LoginByUserNameDto;
import com.cjyc.common.model.util.BaseResultUtil;
import com.cjyc.common.model.vo.ResultVo;
import com.cjyc.common.model.vo.salesman.login.SalemanLoginVo;
import com.cjyc.common.system.feign.ISysLoginService;
import com.cjyc.salesman.api.service.ILoginService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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
    @Autowired
    private ISysLoginService sysLoginService;

    @Value("${cjkj.salesman.clientId}")
    private String clientId;
    @Value("${cjkj.salesman.clientSecret}")
    private String clientSecret;
    @Value("${cjkj.salesman.grantType}")
    private String grantType;



    @ApiOperation(value = "手机号验证码登录", notes = " ")
    @PostMapping("/phone")
    public ResultVo<SalemanLoginVo> login(@Validated @RequestBody LoginByPhoneDto reqDto){
        AuthMobileLoginReq req = new AuthMobileLoginReq();
        req.setClientId(clientId);
        req.setClientSecret(clientSecret);
        req.setGrantType(grantType);
        req.setMobile(reqDto.getPhone());
        req.setSmsCode(reqDto.getCaptcha());
        ResultData<AuthLoginResp> rd = sysLoginService.mobileLogin(req);
        if (!ReturnMsg.SUCCESS.getCode().equals(rd.getCode())) {
            return BaseResultUtil.fail("登录失败，原因：" + rd.getMsg());
        }
        log.info("token info :: " + rd.getData());
        return loginService.loginByCaptcha(reqDto);
    }

    @ApiOperation(value = "用户名密码登录", notes = " ")
    @PostMapping("/username")
    public ResultVo<SalemanLoginVo> login(@Validated @RequestBody LoginByUserNameDto reqDto){
        AuthLoginReq req = new AuthLoginReq();
        req.setClientId(clientId);
        req.setClientSecret(clientSecret);
        req.setGrantType(grantType);
        req.setUsername(reqDto.getUsername());
        req.setPassword(reqDto.getPassword());
        ResultData<AuthLoginResp> rd = sysLoginService.getAuthentication(req);
        if (!ReturnMsg.SUCCESS.getCode().equals(rd.getCode())) {
            return BaseResultUtil.fail("登录失败，原因：" + rd.getMsg());
        }
        log.info("token info :: " + rd.getData());
        return loginService.loginBypassword(reqDto);
    }
}
