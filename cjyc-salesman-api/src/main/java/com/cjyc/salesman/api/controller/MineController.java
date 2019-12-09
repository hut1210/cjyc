package com.cjyc.salesman.api.controller;

import com.cjyc.salesman.api.service.IMineService;
import io.swagger.annotations.Api;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@Api(tags = "业务员APP我的")
@RestController
@RequestMapping(value = "/mine",
        produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
public class MineController {

    @Resource
    private IMineService mineService;

}