package com.cjyc.salesman.api.controller;

import com.cjkj.common.model.SysUser;
import com.cjyc.common.model.util.BaseResultUtil;
import com.cjyc.common.model.vo.ResultVo;
import com.cjyc.salesman.api.fegin.IUserService;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Api(tags = "用户")
@RestController
@RequestMapping("/user")
@Slf4j
@RefreshScope
public class UserController {

    @Autowired
    private IUserService userService;


    @GetMapping("/get/{phone}")
    public ResultVo<SysUser> get(@PathVariable String phone){
        SysUser user = userService.selectByUsername(phone);
        return BaseResultUtil.success(user);
    }
}
