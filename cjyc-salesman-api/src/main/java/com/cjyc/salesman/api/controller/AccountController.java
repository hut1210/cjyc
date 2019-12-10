package com.cjyc.salesman.api.controller;

import com.cjkj.common.model.ResultData;
import com.cjyc.common.model.dto.salesman.account.ForgetPwdDto;
import com.cjyc.common.model.entity.Admin;
import com.cjyc.common.model.enums.CaptchaTypeEnum;
import com.cjyc.common.model.enums.ClientEnum;
import com.cjyc.common.model.util.BaseResultUtil;
import com.cjyc.common.model.vo.ResultVo;
import com.cjyc.common.system.feign.ISysUserService;
import com.cjyc.common.system.service.ICsAdminService;
import com.cjyc.common.system.service.ICsSmsService;
import com.cjyc.common.system.util.ResultDataUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.experimental.Accessors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

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
}
