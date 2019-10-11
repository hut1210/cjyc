package com.cjyc.salesman.api.controller;

import com.alibaba.fastjson.JSONObject;
import com.cjyc.common.model.entity.City;
import com.cjyc.common.model.enums.ResultEnum;
import com.cjyc.common.model.util.BaseResultUtil;
import com.cjyc.common.model.vo.ResultVo;
import com.cjyc.salesman.api.fegin.ISysUserService;
import com.cjyc.salesman.api.service.ICityService;
import com.cjyc.salesman.api.util.JsObjParamUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@Api(tags = "城市")
@Slf4j
@RestController
@RequestMapping(value = "/city",
        consumes = MediaType.APPLICATION_JSON_UTF8_VALUE,
        produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
public class CityController {

    private ISysUserService userService;

    @Autowired
    private ICityService cityService;


    @ApiOperation(value = "获取城市1", notes = "接口详细描述")
    @GetMapping(value = "/get")
    public ResultVo<City> get(@RequestBody JSONObject json){
        String cityCode = JsObjParamUtil.getString(json,"cityCode", true);
        City city = cityService.getById(cityCode);
        return BaseResultUtil.success(city);
    }


    @ApiOperation(value = "获取城市1", notes = "接口详细描述")
    @GetMapping(value = "/get/1")
    public ResultVo<City> get1(@RequestBody(required = true) String cityCode){
        System.out.println(cityCode);
        City city = cityService.getById(cityCode);
        return BaseResultUtil.success(city);
    }

    @ApiOperation(value = "获取城市2", notes = "接口详细描述")
    @GetMapping(value = "/get/{cityCode}")
    public ResultVo<City> get(@PathVariable(required = true) String cityCode){
        City city = cityService.getById(cityCode);
        return BaseResultUtil.getVo(ResultEnum.SUCCESS.getCode(), ResultEnum.SUCCESS.getMsg(), city);
    }

    @ApiOperation(value = "获取城市3", notes = "接口详细描述")
    @GetMapping(value = "/get/one")
    public ResultVo<City> getOne(@RequestParam String cityCode){
        City city = cityService.findById(cityCode);
        return BaseResultUtil.getVo(ResultEnum.SUCCESS.getCode(), ResultEnum.SUCCESS.getMsg(), city);
    }

}
