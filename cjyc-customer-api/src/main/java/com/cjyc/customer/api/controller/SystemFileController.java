package com.cjyc.customer.api.controller;

import com.cjyc.customer.api.service.ISystemFileService;
import io.swagger.annotations.Api;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 *  @author: zj
 *  @Date: 2019/10/14 9:36
 *  @Description:处理系统文件
 */
@Api(tags = "系统文件")
@CrossOrigin
@RestController
@RequestMapping("/systemFile")
public class SystemFileController {

    @Resource
    private ISystemFileService iSystemFileService;

}