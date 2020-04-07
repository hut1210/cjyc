package com.cjyc.web.api.controller;

import com.cjyc.common.model.util.BaseResultUtil;
import com.cjyc.common.model.vo.ResultVo;
import com.cjyc.web.api.service.IYcStatisticsService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@Api(tags = "处理数据")
@RestController
@RequestMapping("/handleData")
public class HandleDataController {

    @Resource
    private IYcStatisticsService ycStatisticsService;

    @ApiOperation(value = "新增或者修改每日韵车数量")
    @PostMapping(value = "/addOrUpdate")
    public ResultVo addOrUpdate() {
        //return ycStatisticsService.addOrUpdate();
        return BaseResultUtil.success();
    }
}