package com.cjyc.web.api.controller;

import io.swagger.annotations.Api;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @auther litan
 * @description: com.cjyc.web.api.controller
 * @date:2019/10/28
 */
@RestController
@Api(tags = "分享")
@RequestMapping(value = "/share",
        produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
public class ShareController {
/*
    @Resource
    private ICustomerShareService shareService;

    *//**
     * 查询分享管理列表
     * @author litan
     *//*
    @ApiOperation(value = "查询分享管理列表(分页)")
    @PostMapping(value = "/locallist")
    public ResultVo<PageVo<Map<String,Object>>> getShareList(@RequestBody CustomerShareDto reqDto) {
        return shareService.getShareList(reqDto);
    }*/
}
