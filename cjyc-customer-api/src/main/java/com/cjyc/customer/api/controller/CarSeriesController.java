package com.cjyc.customer.api.controller;

import com.cjyc.common.model.dto.KeywordDto;
import com.cjyc.common.model.vo.ResultVo;
import com.cjyc.customer.api.service.ICarSeriesService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 *  @author: zj
 *  @Date: 2019/11/3 19:42
 *  @Description:品牌车系
 */
@Api(tags = "品牌车系")
@CrossOrigin
@RestController
@RequestMapping("/car")
public class CarSeriesController {

    @Resource
    private ICarSeriesService carSeriesService;

    @ApiOperation(value = "查看品牌车系")
    @PostMapping(value = "/queryCarSeries")
    public ResultVo queryCarSeries(@RequestBody KeywordDto dto){
        return carSeriesService.queryCarSeries(dto);
    }

}