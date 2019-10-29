package com.cjyc.web.api.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.cjyc.common.model.dto.salesman.city.CityPageDto;
import com.cjyc.common.model.entity.City;
import com.cjyc.common.model.util.BaseResultUtil;
import com.cjyc.common.model.vo.PageVo;
import com.cjyc.common.model.vo.ResultVo;
import com.cjyc.common.model.vo.web.city.ProvinceCityTreeVo;
import com.cjyc.common.model.dto.web.city.TreeCityDto;
import com.cjyc.common.model.vo.web.city.TreeCityVo;
import com.cjyc.web.api.service.ICityService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;

@Api(tags = "城市")
@Slf4j
@RestController
@RequestMapping(value = "/city",
        produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
public class CityController {

    @Autowired
    private ICityService cityService;

    @ApiOperation(value = "根据code查询城市", notes = " ")
    @GetMapping(value = "/get/{code}")
    public ResultVo<City> get(@ApiParam(value = "城市编码", required = true)
                              @PathVariable String code) {
        City city = cityService.getById(code);
        return BaseResultUtil.success(city);
    }

    @ApiOperation(value = "查询城市列表", notes = "")
    @GetMapping(value = "/list/{level}")
    public ResultVo<Collection<City>> List(@ApiParam(value = "行政区级别：0大区，1省，2市，3区县", required = true)
                                           @PathVariable int level) {
        HashMap<String, Object> columnMap = new HashMap<>();
        columnMap.put("level", level);
        List<City> list = cityService.selectList(columnMap);
        return BaseResultUtil.success(list);
    }

    @ApiOperation(value = "查询城市列表（分页）", notes = "")
    @GetMapping(value = "/page")
    public ResultVo<PageVo<City>> page(@ApiParam(value = "查询条件", required = true)
                                       @Validated @RequestBody CityPageDto cityPageDto) {
        IPage<City> iPage = cityService.selectPage(cityPageDto);
        return BaseResultUtil.success(iPage);
    }

    @ApiOperation(value = "查询下属城市列表", notes = "")
    @GetMapping(value = "/child/list/{code}")
    public ResultVo<List<City>> childList(@ApiParam(value = "城市编码", required = true)
                                          @PathVariable String code) {
        List<City> list = cityService.selectChildList(code);
        return BaseResultUtil.success(list);
    }

    @ApiOperation(value = "省城市树形结构", notes = "")
    @PostMapping(value = "/tree")
    public ResultVo<List<TreeCityVo>> tree(@RequestBody TreeCityDto treeCityDto) {
        return cityService.getTree(treeCityDto);
    }
}
