package com.cjyc.web.api.controller;

import com.cjkj.common.model.ResultData;
import com.cjkj.common.model.ReturnMsg;
import com.cjkj.usercenter.dto.common.AddUserResp;
import com.cjkj.usercenter.dto.common.UpdatePwdReq;
import com.cjkj.usercenter.dto.common.auth.AuthLoginReq;
import com.cjkj.usercenter.dto.common.auth.AuthLoginResp;
import com.cjyc.common.model.dto.salesman.login.LoginByUserNameDto;
import com.cjyc.common.model.dto.web.login.UpdatePwdDto;
import com.cjyc.common.model.util.BaseResultUtil;
import com.cjyc.common.model.util.YmlProperty;
import com.cjyc.common.model.vo.ResultVo;
import com.cjyc.common.system.feign.ISysLoginService;
import com.cjyc.common.system.feign.ISysUserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Api(tags = "功能-登录")
@RestController
public class LoginController {

    @Autowired
    private ISysLoginService sysLoginService;

    @Autowired
    private ISysUserService sysUserService;

    @Value("${cjkj.web.clientId}")
    private String clientId;
    @Value("${cjkj.web.clientSecret}")
    private String clientSecret;
    @Value("${cjkj.web.grantType}")
    private String grantType;

    @ApiOperation(value = "用户名密码登录：针对传统标准的用户名密码流程")
    @PostMapping("/pwdLogin")
    public ResultVo<AuthLoginResp> pwdLogin(@Valid @RequestBody LoginByUserNameDto dto) {
        AuthLoginReq req = new AuthLoginReq();
        req.setClientId(clientId);
        req.setClientSecret(clientSecret);
        req.setGrantType(grantType);
        req.setUsername(dto.getUsername());
        req.setPassword(dto.getPassword());
        ResultData<AuthLoginResp> rd = sysLoginService.getAuthentication(req);
        if (!ReturnMsg.SUCCESS.getCode().equals(rd.getCode())) {
            return BaseResultUtil.fail("登录失败，原因：" + rd.getMsg());
        }
        return BaseResultUtil.success(rd.getData());
    }

    @ApiOperation(value = "用户名密码登录: 针对密码固定且在配置文件配置情况")
    @GetMapping("/pwdLogin/{account}")
    public ResultVo<AuthLoginResp> pwdLogin(
            @ApiParam(name = "account", value = "账号", required = true)
            @PathVariable String account) {
        AuthLoginReq req = new AuthLoginReq();
        req.setClientId(clientId);
        req.setClientSecret(clientSecret);
        req.setGrantType(grantType);
        req.setPassword(YmlProperty.get("cjkj.web.password"));
        req.setUsername(account);
        ResultData<AuthLoginResp> rd = sysLoginService.getAuthentication(req);
        if (!ReturnMsg.SUCCESS.getCode().equals(rd.getCode())) {
            return BaseResultUtil.fail("登录失败，原因：" + rd.getMsg());
        }
        return BaseResultUtil.success(rd.getData());
    }

    @ApiOperation(value = "修改密码")
    @PostMapping("/updatePwd")
    public ResultVo updatePwd(@Valid @RequestBody UpdatePwdDto dto){
        ResultData<AddUserResp> accountRd = sysUserService.getByAccount(dto.getAccount());
        if (!ReturnMsg.SUCCESS.getCode().equals(accountRd.getCode())) {
            return BaseResultUtil.fail("用户密码更新失败，原因：" + accountRd.getMsg());
        }
        Long userId = accountRd.getData().getUserId();
        UpdatePwdReq req = new UpdatePwdReq();
        BeanUtils.copyProperties(dto, req);
        req.setUserId(userId);
        ResultData rd = sysUserService.updatePwd(req);
        if (!ReturnMsg.SUCCESS.getCode().equals(rd.getCode())) {
            return BaseResultUtil.fail("用户密码更新失败，原因：" + rd.getMsg());
        }
        return BaseResultUtil.success();
    }
}
