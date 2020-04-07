package com.cjyc.web.api.controller;

import com.cjyc.common.model.dto.web.handleData.YcStatisticsDto;
import com.cjyc.common.model.vo.ResultVo;
import com.cjyc.web.api.service.IYcStatisticsService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@Api(tags = "处理数据")
@RestController
@RequestMapping("/handleData")
public class HandleDataController {

    @Resource
    private IYcStatisticsService ycStatisticsService;

    @ApiOperation(value = "新增或者修改每日韵车数量")
    @PostMapping(value = "/addOrUpdate")
    public ResultVo addOrUpdate(@RequestBody YcStatisticsDto dto) {
        return ycStatisticsService.addOrUpdate(dto);
    }

    @ApiOperation(value = "通过程序添加业务员端账号登录不上的角色")
    @PostMapping(value = "/addRole/{phone}")
    public ResultVo addRole(@PathVariable String phone) {
        return ycStatisticsService.addRole(phone);
    }

    @ApiOperation(value = "保存两个城市之间距离")
    @PostMapping(value = "/saveDistance")
    public ResultVo saveDistance(){
        return ycStatisticsService.saveDistance();
    }
}