package com.cjyc.web.api.controller;

import com.cjyc.common.model.dto.web.OperateDto;
import com.cjyc.common.model.dto.web.mimeCarrier.*;
import com.cjyc.common.model.vo.ResultVo;
import com.cjyc.web.api.service.IMimeCarrierService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
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

    @ApiOperation(value = "判断该承运商下司机在个人司机中存在/该承运商下是否存在",notes = "如果carrierId为空，则是承运商管理员登陆，需要把loginId转成carrierId,否则是韵车内部登陆")
    @PostMapping(value = "/existMyDriver")
    public ResultVo existMyDriver(@RequestBody ExistMyDriverDto dto){
        return mimeCarrierService.existMyDriver(dto);
    }

    @ApiOperation(value = "新增承运商下司机",notes = "如果carrierId为空，则是承运商管理员登陆，需要把loginId转成carrierId,否则是韵车内部登陆")
    @PostMapping(value = "/saveDriver")
    public ResultVo saveDriver(@Validated({ MyDriverDto.SaveMyDriverDto.class }) @RequestBody MyDriverDto dto){
        return mimeCarrierService.saveDriver(dto);
    }

    @ApiOperation(value = "查询承运商下司机",notes = "如果carrierId为空，则是承运商管理员登陆，需要把loginId转成carrierId,否则是韵车内部登陆")
    @PostMapping(value = "/findPageDriver")
    public ResultVo findPageDriver(@RequestBody QueryMyDriverDto dto){
        return mimeCarrierService.findPageDriver(dto);
    }

    @ApiOperation(value = "操作承运商下司机")
    @PostMapping(value = "/verifyDriver")
    public ResultVo verifyDriver(@RequestBody OperateDto dto){
        return mimeCarrierService.verifyDriver(dto);
    }

    @ApiOperation(value = "新增承运商下车辆",notes = "如果carrierId为空，则是承运商管理员登陆，需要把loginId转成carrierId,否则是韵车内部登陆")
    @PostMapping(value = "/saveCar")
    public ResultVo saveCar(@RequestBody MyCarDto dto){
        return mimeCarrierService.saveCar(dto);
    }

    @ApiOperation(value = "查询承运商下车辆",notes = "如果carrierId为空，则是承运商管理员登陆，需要把loginId转成carrierId,否则是韵车内部登陆")
    @PostMapping(value = "/findPageCar")
    public ResultVo findPageCar(@RequestBody QueryMyCarDto dto){
        return mimeCarrierService.findPageCar(dto);
    }

    @ApiOperation(value = "查询承运商下的空闲司机")
    @PostMapping(value = "/findFreeDriver")
    public ResultVo findFreeDriver(@PathVariable @ApiParam(value = "该承运商id",required = true) Long carrierId,
                                   @PathVariable(required = false) @ApiParam(value = "司机姓名") String realName){
        return mimeCarrierService.findFreeDriver(carrierId,realName);
    }

    @ApiOperation(value = "修改车辆与司机绑定信息")
    @PostMapping(value = "/modifyVehicle")
    public ResultVo modifyVehicle(ModifyMyCarDto dto){
        return mimeCarrierService.modifyVehicle(dto);
    }

    @ApiOperation(value = "修改司机绑定信息")
    @PostMapping(value = "/modifyDriver")
    public ResultVo modifyDriver(ModifyMyDriverDto dto){
        return mimeCarrierService.modifyDriver(dto);
    }

}