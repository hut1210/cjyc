package com.cjyc.web.api.controller;

import io.swagger.annotations.Api;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @auther litan
 * @description: com.cjyc.web.api.controller
 * @date:2019/9/28
 */
@RestController
@RequestMapping("/waybill")
@Api(tags = "waybill",description = "web端基础接口,包含登录、登出、等")
public class WaybillController {
}
