package com.cjyc.salesman.api.controller;

import com.cjkj.common.model.ResultData;
import com.cjkj.usercenter.dto.common.SelectRoleResp;
import com.cjyc.common.model.dto.salesman.account.ForgetPwdDto;
import com.cjyc.common.model.dto.salesman.login.RoleBtnReqDto;
import com.cjyc.common.model.entity.Admin;
import com.cjyc.common.model.entity.Role;
import com.cjyc.common.model.enums.CaptchaTypeEnum;
import com.cjyc.common.model.enums.ClientEnum;
import com.cjyc.common.model.enums.role.RoleLoginAppEnum;
import com.cjyc.common.model.util.BaseResultUtil;
import com.cjyc.common.model.vo.ResultVo;
import com.cjyc.common.model.vo.salesman.login.BtnListVo;
import com.cjyc.common.system.feign.ISysRoleService;
import com.cjyc.common.system.feign.ISysUserService;
import com.cjyc.common.system.service.ICsAdminService;
import com.cjyc.common.system.service.ICsRoleService;
import com.cjyc.common.system.service.ICsSmsService;
import com.cjyc.common.system.util.ResultDataUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.experimental.Accessors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;

@Api(tags = "业务员APP-账号相关")
@Accessors(chain = true)
@RestController
@RequestMapping("/account")
public class AccountController {
    @Autowired
    private ICsSmsService csSmsService;
    @Autowired
    private ISysUserService sysUserService;
    @Autowired
    private ICsAdminService csAdminService;
    @Autowired
    private ISysRoleService sysRoleService;
    @Resource
    private ICsRoleService csRoleService;


    @ApiOperation(value = "忘记密码")
    @PostMapping("/forgetPwd")
    public ResultVo forgetPwd(@Valid @RequestBody ForgetPwdDto dto){
        if (csSmsService.validateCaptcha(dto.getPhone(), dto.getCaptcha(),
                CaptchaTypeEnum.FORGET_LOGIN_PWD, ClientEnum.APP_SALESMAN)) {
            Admin admin = csAdminService.getAdminByPhone(dto.getPhone(), true);
            if (null == admin || admin.getUserId() == null || admin.getUserId() <= 0L) {
                BaseResultUtil.fail("业务员信息不正确，请检查");
            }
            ResultData resultData = sysUserService.resetPwd(admin.getUserId(), dto.getNewPwd());
            if (ResultDataUtil.isSuccess(resultData)) {
                return BaseResultUtil.success();
            }else {
                return BaseResultUtil.fail("密码修改失败，原因：" + resultData.getMsg());
            }
        }else {
            return BaseResultUtil.fail("验证码错误");
        }
    }

    @ApiOperation(value = "获取登录用户按钮列表")
    @PostMapping("/getBtnList")
    public ResultVo<BtnListVo> getRoleBtnList(@Valid @RequestBody RoleBtnReqDto dto) {
        Admin admin = csAdminService.getById(dto.getLoginId(), true);
        if (admin == null || admin.getUserId() == null || admin.getUserId() <= 0L) {
            return BaseResultUtil.fail("用户信息有误，请确认");
        }
        ResultData<List<SelectRoleResp>> roleRd = sysRoleService.getListByUserId(admin.getUserId());
        if (ResultDataUtil.isSuccess(roleRd)) {
            if (!CollectionUtils.isEmpty(roleRd.getData())) {
                List<String> roleNameList = roleRd.getData().stream()
                        .map(r -> r.getRoleName()).collect(Collectors.toList());
                List<String> btnNameList = new ArrayList<>();
                Set<String> btnSet = new HashSet<>();
                List<Role> ycRoleList = csRoleService.getValidInnerRoleList();
                //是否可登录业务员APP，只需当前用户中有一个角色可登录APP即可登录APP
                AtomicBoolean canLogin = new AtomicBoolean(false);
                roleNameList.forEach(name -> {
                    //判断是否有登录角色
                    List<Role> existList = ycRoleList.stream().filter(r ->
                            r.getRoleName().equals(name)).collect(Collectors.toList());
                    if (!CollectionUtils.isEmpty(existList)) {
                        Role r = existList.get(0);
                        if (RoleLoginAppEnum.CAN_LOGIN_APP.getFlag() == r.getLoginApp()) {
                            canLogin.set(true);
                            if (!StringUtils.isEmpty(r.getAppBtns())) {
                                String[] btns = r.getAppBtns().split(",");
                                for (String btn: btns) {
                                    btnSet.add(btn);
                                }
                            }
                        }
                    }
                });
                btnNameList.addAll(btnSet);
                if (!canLogin.get()) {
                    return BaseResultUtil.fail("此用户不可登录APP");
                }else {
                    BtnListVo vo = new BtnListVo();
                    StringBuilder sb = new StringBuilder("");
                    btnNameList.forEach(b -> {
                        if (sb.length() > 0) {
                            sb.append("," + b);
                        }else {
                            sb.append(b);
                        }
                    });
                    vo.setBtnList(sb.toString());
                    return BaseResultUtil.success(vo);
                }
            }else {
                return BaseResultUtil.fail("角色信息为空");
            }
        }else {
            return BaseResultUtil.fail("角色信息获取失败");
        }
    }
}
