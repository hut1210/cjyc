package com.cjyc.web.api.controller;

import com.cjyc.common.model.entity.CarSeries;
import com.cjyc.common.model.enums.ResultEnum;
import com.cjyc.common.model.util.BaseResultUtil;
import com.cjyc.common.model.vo.CityVo;
import com.cjyc.common.model.vo.ResultVo;
import com.cjyc.common.service.service.ICarSeriesService;
import com.cjyc.common.service.service.ICityService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @auther litan
 * @description: com.cjyc.web.api.controller
 * @date:2019/9/28
 */
@RestController
@RequestMapping("/index")
@Api(tags = "公共基础数据接口",description = "web端基础接口,包含城市、班线等")
public class IndexController {

    @Autowired
    ICityService cityService;
    @Autowired
    ICarSeriesService carSeriesService;

    /**
     * 获取车辆品牌
     * */
    @ApiOperation(value = "获取车辆品牌", notes = "获取车辆品牌", httpMethod = "POST")
    @RequestMapping(value = "/getBrandList", method = RequestMethod.POST,  consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResultVo<List<String>> getBrandList() {
        List<String> carSeriesList = carSeriesService.getBrand();
        return BaseResultUtil.success(carSeriesList);
    }

    /**
     * 获取品牌下车辆列表
     * */
    @ApiOperation(value = "获取品牌下车辆列表", notes = "获取品牌下车辆列表", httpMethod = "POST")
    @RequestMapping(value = "/getSeriesListByBrand", method = RequestMethod.POST,  consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "brand", value = "品牌",  dataType = "String", paramType = "query")
    })
    public ResultVo<List<String>> getSeriesListByBrand(String brand) {
        List<String> carSeriesList = carSeriesService.getSeriesByBrand(brand);
        return BaseResultUtil.success(carSeriesList);
    }

    /**
     * 获取城市列表
     * */
    @ApiOperation(value = "获取城市列表", notes = "获取城市列表", httpMethod = "POST")
    @RequestMapping(value = "/getCityList", method = RequestMethod.POST,  consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "cityCode", value = "城市code,空表示全国和大区", dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "keyword", value = "检索关键词",  dataType = "String", paramType = "query")
    })
    public ResultVo<List<CityVo>> getCityList(String cityCode, String keyword) {
        List<CityVo> cityList = cityService.getCityList(cityCode,keyword);
        return BaseResultUtil.success(cityList);
    }
}
