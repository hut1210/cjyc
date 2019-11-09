package com.cjyc.web.api.controller;

import com.cjyc.common.model.dto.web.driver.DriverDto;
import com.cjyc.common.model.vo.ResultVo;
import com.cjyc.web.api.service.IMimeCarrierService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 *  @author: zj
 *  @Date: 2019/11/8 18:33
 *  @Description:我的承运商下司机与车辆
 */
@Api(tags = "我的承运商下司机与车辆")
@CrossOrigin
@RestController
@RequestMapping("/mimeCarrier")
public class MimeCarrierController {

    @Resource
    private IMimeCarrierService mimeCarrierService;

   /* @ApiOperation(value = "新增承运商下司机")
    @PostMapping(value = "/saveDriver")
    public ResultVo saveDriver(@Validated({ DriverDto.SaveDriverDto.class }) @RequestBody DriverDto dto){
        return mimeCarrierService.saveDriver(dto);
    }*/
}