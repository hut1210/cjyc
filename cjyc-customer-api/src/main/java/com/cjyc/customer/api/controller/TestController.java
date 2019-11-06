package com.cjyc.customer.api.controller;

import com.alibaba.fastjson.JSON;
import com.cjkj.common.model.ResultData;
import com.cjkj.usercenter.dto.common.AddRoleResp;
import com.cjkj.usercenter.dto.common.SelectRoleResp;
import com.cjyc.common.system.feign.ISysRoleService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.spring.web.json.Json;

import javax.annotation.Resource;

/**
 * 测试
 * @author JPG
 */
@Api(tags = "Test")
@Slf4j
@RestController
@RequestMapping(value = "/test",
        produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
public class TestController {

    @Resource
    private ISysRoleService sysRoleService;

    @ApiOperation(value = "测试", notes = " ")
    @PostMapping("/get/{id}")
    public ResultData<SelectRoleResp> get(@PathVariable Integer id){
        ResultData<SelectRoleResp> resultData = sysRoleService.getById(id);
        System.out.println(JSON.toJSONString(resultData));

        return resultData;

    }
}
