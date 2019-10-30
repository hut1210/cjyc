package com.cjyc.web.api.controller;

import com.cjyc.common.model.dto.web.customer.CustomerShareDto;
import com.cjyc.common.model.vo.PageVo;
import com.cjyc.common.model.vo.ResultVo;
import com.cjyc.web.api.service.ICustomerShareService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.Map;

/**
 * @auther litan
 * @description: com.cjyc.web.api.controller
 * @date:2019/10/28
 */
@RestController
@Api(tags = "分享管理")
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
    @PostMapping(value = "/list")
    public ResultVo<PageVo<Map<String,Object>>> getShareList(@RequestBody CustomerShareDto reqDto) {
        return shareService.getShareList(reqDto);
    }*/
}
