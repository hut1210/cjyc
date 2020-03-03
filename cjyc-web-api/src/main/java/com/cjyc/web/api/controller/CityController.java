package com.cjyc.web.api.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.cjyc.common.model.dto.KeywordDto;
import com.cjyc.common.model.dto.AdminDto;
import com.cjyc.common.model.dto.salesman.city.CityPageDto;
import com.cjyc.common.model.dto.web.city.CityQueryDto;
import com.cjyc.common.model.entity.City;
import com.cjyc.common.model.entity.defined.FullCity;
import com.cjyc.common.model.util.BaseResultUtil;
import com.cjyc.common.model.vo.CityTreeVo;
import com.cjyc.common.model.vo.PageVo;
import com.cjyc.common.model.vo.ResultVo;
import com.cjyc.common.model.vo.customer.city.CityVo;
import com.cjyc.common.system.config.CarrierProperty;
import com.cjyc.common.system.service.ICsCityService;
import com.cjyc.web.api.service.ICityService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

@Api(tags = "基础数据-城市")
@Slf4j
@RestController
@RequestMapping(value = "/city",
        produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
public class CityController {

    @Autowired
    private ICityService cityService;
    @Resource
    private ICsCityService csCityService;

    @ApiOperation(value = "根据code查询城市", notes = " ")
    @PostMapping(value = "/get/{code}")
    public ResultVo<City> get(@ApiParam(value = "城市编码", required = true)
                              @PathVariable String code) {
        City city = cityService.getById(code);
        return BaseResultUtil.success(city);
    }

    @ApiOperation(value = "查询城市列表", notes = "")
    @PostMapping(value = "/list/{level}")
    public ResultVo<Collection<City>> List(@ApiParam(value = "行政区级别：0大区，1省，2市，3区县", required = true)
                                           @PathVariable int level) {
        HashMap<String, Object> columnMap = new HashMap<>();
        columnMap.put("level", level);
        List<City> list = cityService.selectList(columnMap);
        return BaseResultUtil.success(list);
    }

    @ApiOperation(value = "查询城市列表（分页）", notes = "")
    @PostMapping(value = "/page")
    public ResultVo<PageVo<City>> page(@ApiParam(value = "查询条件", required = true)
                                       @Validated @RequestBody CityPageDto cityPageDto) {
        IPage<City> iPage = cityService.selectPage(cityPageDto);
        return BaseResultUtil.success(iPage);
    }

    @ApiOperation(value = "查询下属城市列表", notes = "")
    @PostMapping(value = "/child/list/{code}")
    public ResultVo<List<City>> childList(@ApiParam(value = "城市编码", required = true)
                                          @PathVariable String code) {
        List<City> list = cityService.selectChildList(code);
        return BaseResultUtil.success(list);
    }

    @ApiOperation(value = "查询树形结构", notes = "")
    @PostMapping(value = "/cityTree/{startLevel}/{endLevel}")
    public ResultVo<List<CityTreeVo>> cityTree(@PathVariable @ApiParam(value = "区域级别 最高级:-1 大区:0 省直辖市:1 城市:2 区县:3",required = true) Integer startLevel,
                                               @PathVariable @ApiParam(value = "区域级别 最高级:-1 大区:0 省直辖市:1 城市:2 区县:3",required = true) Integer endLevel) {
        return cityService.cityTree(startLevel,endLevel);
    }

    @ApiOperation(value = "根据关键字模糊搜索省/城市")
    @PostMapping(value = "/keywordCityTree/{keyword}")
    public ResultVo<List<CityTreeVo>> keywordCityTree(@PathVariable @ApiParam(value = "省直辖市/城市名称",required = true) String keyword) {
        return cityService.keywordCityTree(keyword);
    }

    @ApiOperation(value = "分页查询城市列表")
    @PostMapping("/getCityPage")
    public ResultVo<PageVo<List<FullCity>>> getCityPage(@RequestBody CityQueryDto dto) {
        return cityService.getCityPage(dto);
    }

    /**
     * 功能描述: 查询大区未覆盖的省列表
     * @author liuxingxiang
     * @date 2019/11/21
     * @param dto
     * @return com.cjyc.common.model.vo.ResultVo<java.util.List<com.cjyc.common.model.entity.City>>
     */
    @ApiOperation(value = "根据省名称查询大区未覆盖省列表")
    @PostMapping("/getProvinceList")
    public ResultVo<List<City>> getProvinceList(@RequestBody KeywordDto dto) {
        return cityService.getProvinceList(dto);
    }

    @ApiOperation(value = "根据roleId/loginId查询省/城市区树形结构")
    @PostMapping(value = "/findThreeCityByAdmin")
    public ResultVo<CityVo> findThreeCityByAdmin(@RequestBody AdminDto dto) {
        return csCityService.findThreeCityByAdminNew(dto);
    }

    @ApiOperation(value = "城市管理导出Excel", notes = "\t 请求接口为/city/exportCityListExcel?regionCode=大区编码&provinceCode=省编码" +
            "&cityCode=市编码&areaCode=区/县编码")
    @GetMapping("/exportCityListExcel")
    public void exportCityListExcel(HttpServletRequest request, HttpServletResponse response){
        cityService.exportCityListExcel(request,response);
    }
}
