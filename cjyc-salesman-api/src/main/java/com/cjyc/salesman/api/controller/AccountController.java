package com.cjyc.salesman.api.controller;

import com.cjyc.common.model.vo.ResultVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.experimental.Accessors;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Api(tags = "业务员APP-账号相关")
@Accessors(chain = true)
@RestController
@RequestMapping("/account")
public class AccountController {

    @ApiOperation(value = "忘记密码")
    @PostMapping("/forgetPwd")
    public ResultVo forgetPwd(){
//        sss;
        return null;
    }
}
