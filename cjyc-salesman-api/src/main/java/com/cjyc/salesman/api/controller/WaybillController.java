package com.cjyc.salesman.api.controller;

import com.cjyc.salesman.api.service.IWaybillService;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Api(tags = "组织机构")
@Slf4j
@RestController
@RequestMapping(value = "/organization",
        consumes = MediaType.APPLICATION_JSON_UTF8_VALUE,
        produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
public class WaybillController {

    @Autowired
    private IWaybillService waybillService;



}
