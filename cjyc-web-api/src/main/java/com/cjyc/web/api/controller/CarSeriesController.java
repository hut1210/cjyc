package com.cjyc.web.api.controller;

import com.cjyc.common.model.dto.web.carSeries.CarSeriesAddDto;
import com.cjyc.common.model.dto.web.carSeries.CarSeriesQueryDto;
import com.cjyc.common.model.dto.web.carSeries.CarSeriesUpdateDto;
import com.cjyc.common.model.entity.CarSeries;
import com.cjyc.common.model.enums.ResultEnum;
import com.cjyc.common.model.util.BaseResultUtil;
import com.cjyc.common.model.vo.ResultVo;
import com.cjyc.common.system.service.ICsCarSeriesService;
import com.cjyc.web.api.service.ICarSeriesService;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * @Description 品牌车系管理
 * @Author LiuXingXiang
 * @Date 2019/10/28 15:58
 **/
@Api(tags = "品牌车系管理")
@RestController
@RequestMapping("/carSeries")
public class CarSeriesController {
    @Autowired
    private ICarSeriesService carSeriesService;
    @Autowired
    private ICsCarSeriesService csCarSeriesService;

    @ApiOperation(value = "新增", notes = "\t 请求接口为json格式")
    @PostMapping("/add")
    public ResultVo add(@RequestBody @Validated CarSeriesAddDto carSeriesAddDto){
        boolean result = carSeriesService.add(carSeriesAddDto);
        return result ? BaseResultUtil.success() : BaseResultUtil.fail(ResultEnum.FAIL.getMsg());
    }

    @ApiOperation(value = "删除", notes = "\t 请求接口为/delete/id格式;只支持单条删除")
    @PostMapping("/remove/{id}")
    public ResultVo remove(@PathVariable Long id){
        boolean result = carSeriesService.removeById(id);
        return result ? BaseResultUtil.success() : BaseResultUtil.fail(ResultEnum.FAIL.getMsg());
    }

    @ApiOperation(value = "查询详情", notes = "\t 请求接口为/queryDetail/id格式")
    @PostMapping("/queryDetail/{id}")
    public ResultVo<CarSeries> queryDetail(@PathVariable Long id){
        return BaseResultUtil.success(carSeriesService.getById(id));
    }

    @ApiOperation(value = "查询所有品牌", notes = "\t 无参数")
    @PostMapping("/queryAll")
    public ResultVo<List<CarSeries>> queryAll(){
        return carSeriesService.queryAll();
    }

    @ApiOperation(value = "查询品牌下所有车系", notes = "\t 请求接口为/queryModel/brand格式")
    @PostMapping("/queryModel/{brand}")
    public ResultVo queryModel(@PathVariable String brand){
        return carSeriesService.getModelByBrand(brand);
    }

    @ApiOperation(value = "修改", notes = "\t 请求接口为json格式")
    @PostMapping("/modify")
    public ResultVo modify(@RequestBody @Validated CarSeriesUpdateDto carSeriesUpdateDto){
        boolean result = carSeriesService.modify(carSeriesUpdateDto);
        return result ? BaseResultUtil.success() : BaseResultUtil.fail(ResultEnum.FAIL.getMsg());
    }

    @ApiOperation(value = "分页查询", notes = "\t 请求接口为json格式")
    @PostMapping("/queryPage")
    public ResultVo<PageInfo<CarSeries>> queryPage(@RequestBody CarSeriesQueryDto carSeriesQueryDto){
        return carSeriesService.queryPage(carSeriesQueryDto);
    }

    @ApiOperation(value = "导入Excel", notes = "\t 请求接口为/importExcel/createUserId(导入用户ID)格式")
    @PostMapping("/importExcel/{createUserId}")
    public ResultVo importExcel(@RequestParam("file") MultipartFile file,@PathVariable Long createUserId){
        boolean result = carSeriesService.importExcel(file, createUserId);
        return result ? BaseResultUtil.success() : BaseResultUtil.fail(ResultEnum.FAIL.getMsg());
    }

    @ApiOperation(value = "导出Excel", notes = "\t 请求接口为/carSeries/exportExcel?currentPage=1&pageSize=6&brand=宝马&model=BM001")
    @GetMapping("/exportExcel")
    public void exportExcel(HttpServletRequest request, HttpServletResponse response){
        carSeriesService.exportExcel(request,response);
    }

}
