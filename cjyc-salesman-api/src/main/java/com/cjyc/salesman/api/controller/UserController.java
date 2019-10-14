package com.cjyc.salesman.api.controller;

import com.cjyc.salesman.api.fegin.ISysUserService;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Api(tags = "用户")
@Slf4j
@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private ISysUserService sysUserService;


/*    @GetMapping("/get/{phone}")
    public ResultVo<SysUserEntity> get(@PathVariable String phone){
        ResultData<SysUserEntity> sysUserEntityResultData = sysUserService.selectByUsername(phone);
        return BaseResultUtil.success(sysUserEntityResultData.getData());
    }*/


}
