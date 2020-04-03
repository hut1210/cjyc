package com.cjyc.customer.api.controller;

import com.alibaba.fastjson.JSON;
import com.cjyc.common.model.dto.web.waybill.TrunkMidwayUnload;
import io.swagger.annotations.Api;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Api(tags = "test")
public class testController {
    @RequestMapping("/test/ds")
    public String test(@Validated @RequestBody TrunkMidwayUnload trunkMidwayUnload){
        System.out.println(JSON.toJSONString(trunkMidwayUnload));
        return "成功";
    }

}
