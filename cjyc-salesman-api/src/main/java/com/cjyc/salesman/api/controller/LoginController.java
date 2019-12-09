package com.cjyc.salesman.api.controller;

import com.cjkj.common.model.ResultData;
import com.cjkj.usercenter.dto.common.SelectRoleResp;
import com.cjkj.usercenter.dto.common.auth.AuthLoginReq;
import com.cjkj.usercenter.dto.common.auth.AuthLoginResp;
import com.cjkj.usercenter.dto.common.auth.AuthMobileLoginReq;
import com.cjyc.common.model.dto.salesman.login.LoginByPhoneDto;
import com.cjyc.common.model.dto.salesman.login.LoginByUserNameDto;
import com.cjyc.common.model.entity.Admin;
import com.cjyc.common.model.entity.Role;
import com.cjyc.common.model.enums.role.RoleLoginAppEnum;
import com.cjyc.common.model.util.BaseResultUtil;
import com.cjyc.common.model.vo.ResultVo;
import com.cjyc.common.model.vo.salesman.login.SalesmanAppLoginVo;
import com.cjyc.common.system.feign.ISysLoginService;
import com.cjyc.common.system.feign.ISysRoleService;
import com.cjyc.common.system.service.ICsAdminService;
import com.cjyc.common.system.service.ICsRoleService;
import com.cjyc.common.system.util.ResultDataUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.util.CollectionUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Api(tags = "业务员APP登录")
@RestController
@RequestMapping(value = "/login",
        produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
public class LoginController {
    /**
     * 用户名密码授权方式
     */
    private static final String PWD_LOGIN_GRANT_TYPE = "password";
    @Autowired
    private ISysRoleService sysRoleService;
    @Resource
    private ICsAdminService csAdminService;
    @Resource
    private ICsRoleService csRoleService;
    @Resource
    private ISysLoginService sysLoginService;

    @Value("${cjyc.salesman.clientId}")
    private String clientId;
    @Value("${cjyc.salesman.clientSecret}")
    private String clientSecret;
    @Value("${cjyc.salesman.grantType}")
    private String grantType;

    @ApiOperation(value = "业务员APP登录")
    @PostMapping("/mobile")
    public ResultVo<SalesmanAppLoginVo> login(@Validated @RequestBody LoginByPhoneDto dto) {
        ResultData<List<String>> rd = doPreLogin(dto.getPhone());
        if (ResultDataUtil.isSuccess(rd)) {
            // 登录
            AuthMobileLoginReq req = new AuthMobileLoginReq();
            req.setSmsCode(dto.getCaptcha());
            req.setMobile(dto.getPhone());
            req.setClientId(clientId);
            req.setClientSecret(clientSecret);
            req.setGrantType(grantType);
            ResultData<AuthLoginResp> loginRd = sysLoginService.mobileLogin(req);
            if (ResultDataUtil.isSuccess(loginRd)) {
                SalesmanAppLoginVo vo = new SalesmanAppLoginVo();
                BeanUtils.copyProperties(loginRd.getData(), vo);
                vo.setRoleList(rd.getData());
                return BaseResultUtil.success(vo);
            }else {
                return BaseResultUtil.fail("登录失败，原因：" + loginRd.getMsg());
            }
        }else {
            return BaseResultUtil.fail(rd.getMsg());
        }
    }

    @ApiOperation(value = "账号密码登录")
    @PostMapping("/pwd")
    public ResultVo<SalesmanAppLoginVo> pwdLogin(@Validated @RequestBody LoginByUserNameDto dto) {
        ResultData<List<String>> rd = doPreLogin(dto.getUsername());
        if (ResultDataUtil.isSuccess(rd)) {
            AuthLoginReq req = new AuthLoginReq();
            req.setUsername(dto.getUsername());
            req.setPassword(dto.getPassword());
            req.setGrantType(PWD_LOGIN_GRANT_TYPE);
            req.setClientId(clientId);
            req.setClientSecret(clientSecret);
            ResultData<AuthLoginResp> loginRd = sysLoginService.getAuthentication(req);
            if (!ResultDataUtil.isSuccess(loginRd)) {
                return BaseResultUtil.fail("登录失败，原因：" + loginRd.getMsg());
            }else {
                SalesmanAppLoginVo vo = new SalesmanAppLoginVo();
                BeanUtils.copyProperties(loginRd.getData(), vo);
                vo.setRoleList(rd.getData());
                return BaseResultUtil.success(vo);
            }
        }else {
            return BaseResultUtil.fail(rd.getMsg());
        }
    }

    /**
     * 登录前操作
     * @param account
     * @return
     */
    private ResultData<List<String>> doPreLogin(String account) {
        Admin admin = csAdminService.getAdminByPhone(account, true);
        if (admin == null || admin.getUserId() == null || admin.getUserId() <= 0L) {
            return ResultData.failed("用户信息有误，请确认");
        }
        ResultData<List<SelectRoleResp>> roleRd = sysRoleService.getListByUserId(admin.getUserId());
        if (ResultDataUtil.isSuccess(roleRd)) {
            if (!CollectionUtils.isEmpty(roleRd.getData())) {
                List<String> roleNameList = roleRd.getData().stream()
                        .map(r -> r.getRoleName()).collect(Collectors.toList());
                List<String> salesmanRoleNameList = new ArrayList<>();
                Set<String> btnSet = new HashSet<>();
                List<Role> ycRoleList = csRoleService.getValidInnerRoleList();
                roleNameList.forEach(name -> {
                    //判断是否有登录角色
                    List<Role> existList = ycRoleList.stream().filter(r ->
                            r.getRoleName().equals(name)).collect(Collectors.toList());
                    if (!CollectionUtils.isEmpty(existList)) {
                        if (RoleLoginAppEnum.CAN_LOGIN_APP.getFlag() == existList.get(0).getLoginApp()) {
                            salesmanRoleNameList.add(name);
                        }
                    }
                });
                salesmanRoleNameList.addAll(btnSet);
                if (CollectionUtils.isEmpty(salesmanRoleNameList)) {
                    return ResultData.failed("此用户不可登录APP");
                }else {
                    return ResultData.ok(salesmanRoleNameList);
                }
            }else {
                return ResultData.failed("角色信息为空");
            }
        }else {
            return ResultData.failed("角色信息获取失败");
        }
    }
}
