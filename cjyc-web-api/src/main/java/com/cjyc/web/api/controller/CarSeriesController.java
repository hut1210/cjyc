package com.cjyc.web.api.controller;

import com.cjyc.common.model.dto.web.carSeries.CarSeriesAddDto;
import com.cjyc.common.model.dto.web.carrier.CarrierDto;
import com.cjyc.common.model.enums.ResultEnum;
import com.cjyc.common.model.util.BaseResultUtil;
import com.cjyc.common.model.vo.ResultVo;
import com.cjyc.web.api.service.ICarSeriesService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * @Description 品牌车系管理
 * @Author LiuXingXiang
 * @Date 2019/10/28 15:58
 **/
@Api(tags = "品牌车系管理")
@CrossOrigin
@RestController
@RequestMapping("/carSeries")
public class CarSeriesController {
    @Autowired
    private ICarSeriesService carSeriesService;

    @ApiOperation(value = "新增", notes = "\t 请求接口为json格式")
    @PostMapping(value = "/add")
    public ResultVo save(@RequestBody @Validated CarSeriesAddDto carSeriesAddDto){
        return carSeriesService.add(carSeriesAddDto);
    }


}
