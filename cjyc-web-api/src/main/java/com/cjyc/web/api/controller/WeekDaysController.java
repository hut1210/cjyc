package com.cjyc.web.api.controller;

import com.cjyc.common.model.dto.WeekDaysDto;
import com.cjyc.common.model.vo.ResultVo;
import com.cjyc.common.system.service.IWeekDaysService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Package: com.cjyc.web.api.controller
 * @Description: 提供关于时间段或者是时间查询工作日天数或是否工作日的接口
 * @Author: Yang.yanfei
 * @Date: 2020/4/16
 * @Version: V1.0
 * @Copyright: 2019 - 2020 - ©长久科技
 */
@Api(tags = "工作日查询接口")
@RequestMapping("/weekDays")
@RestController
public class WeekDaysController {
    @Autowired
    private IWeekDaysService weekDaysService;

    @ApiOperation("根据两个日期段查询有多少个工作日")
    @PostMapping("/getDateNum")
    public ResultVo<Integer> getDateNum(@RequestParam(name = "startDate") String startDate, @RequestParam(name = "endDate") String endDate) {
        return weekDaysService.getDateNum(startDate, endDate);
    }

    @ApiOperation("根据一个日期段查询是否为工作日")
    @PostMapping("/getWeekDaysTypeInfo")
    public ResultVo<WeekDaysDto> getWeekDaysTypeInfo(@RequestParam(name = "date") String date) {
        return weekDaysService.getWeekDaysTypeInfo(date);
    }

}
