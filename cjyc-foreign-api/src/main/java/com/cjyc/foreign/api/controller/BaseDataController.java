package com.cjyc.foreign.api.controller;

import com.cjyc.common.model.entity.CarSeries;
import com.cjyc.common.model.entity.City;
import com.cjyc.common.model.util.BaseResultUtil;
import com.cjyc.common.model.vo.ResultVo;
import com.cjyc.foreign.api.service.IBaseDataService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author zcm
 * @date 2020/3/17 14:51
 */
@Api(tags = {"基础数据"})
@RestController
@RequestMapping("/basedata")
public class BaseDataController {
    @Autowired
    private IBaseDataService baseDataService;

    @ApiOperation(value = "查询所有城市信息")
    @PostMapping("/getAllCity")
    public ResultVo<List<City>> getAllCity() {
        return BaseResultUtil.success(baseDataService.getAllCity());
    }

    @ApiOperation(value = "查询所有车系信息")
    @PostMapping("/getAllCarSeries")
    public ResultVo<List<CarSeries>> getAllCarSeries() {
        return BaseResultUtil.success(baseDataService.getAllCarSeries());
    }
}
