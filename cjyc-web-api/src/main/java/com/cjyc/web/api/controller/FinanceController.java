package com.cjyc.web.api.controller;

import io.swagger.annotations.Api;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 财务
 * @author JPG
 */
@RestController
@Api(tags = "资金-财务")
@RequestMapping(value = "/finance",
        produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
public class FinanceController {


}
