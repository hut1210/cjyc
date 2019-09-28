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
@RequestMapping("/fiance")
@Api(tags = "index",description = "web端财务相关接口,包含收付款单等")
public class FianceController {
}
