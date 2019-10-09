package com.cjyc.salesman.api.controller;

import com.alibaba.fastjson.JSONObject;
import com.cjyc.common.model.entity.City;
import com.cjyc.common.model.util.BaseResultUtil;
import com.cjyc.common.model.util.ResultEnum;
import com.cjyc.common.model.vo.ResultVo;
import com.cjyc.salesman.api.service.ICityService;
import com.cjyc.salesman.api.util.JsonObjectParamUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.*;

@Api(tags = "城市")
@RestController
@RequestMapping("/city")
@Slf4j
@RefreshScope
public class CityController {

    @Autowired
    private ICityService cityService;


    @ApiOperation(value = "获取城市1", notes = "接口详细描述", produces = "application/json")
    @GetMapping(value = "/get")
    public ResultVo<City> get(@RequestBody JSONObject json){
        String cityCode = JsonObjectParamUtil.getString(json,"cityCode");
        City city = cityService.getById(cityCode);

        return BaseResultUtil.getVo(ResultEnum.SUCCESS.getCode(), ResultEnum.SUCCESS.getMsg(), city);
    }

    @ApiOperation(value = "获取城市2", notes = "接口详细描述", produces = "application/json")
    @GetMapping(value = "/get/{cityCode}")
        public ResultVo<City> get(@PathVariable String cityCode){
        City city = cityService.getById(cityCode);

        return BaseResultUtil.getVo(ResultEnum.SUCCESS.getCode(), ResultEnum.SUCCESS.getMsg(), city);
    }

    @ApiOperation(value = "获取城市3", notes = "接口详细描述", produces = "application/json")
    @GetMapping(value = "/get/one")
    public ResultVo<City> getOne(@RequestParam String cityCode){
        City city = cityService.findById(cityCode);

        return BaseResultUtil.getVo(ResultEnum.SUCCESS.getCode(), ResultEnum.SUCCESS.getMsg(), city);
    }

}
