package com.cjyc.foreign.api.controller;

import com.cjkj.common.model.ResultData;
import com.cjyc.foreign.api.entity.Line;
import com.cjyc.foreign.api.service.ITestService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Api(tags = {"测试"})
@RequestMapping("/test")
public class TestController {
    @Autowired
    private ITestService testService;

    @GetMapping("/getMLine/{id}")
    public ResultData<Line> getMLine(@PathVariable Long id) {
        return ResultData.ok(testService.getById(id));
    }

    @GetMapping("/getILine/{id}")
    public ResultData<Line> getILine(@PathVariable Long id) {
        return ResultData.ok(testService.getLineById(id));
    }
}
