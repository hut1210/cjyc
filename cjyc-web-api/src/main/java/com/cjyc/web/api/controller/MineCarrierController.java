package com.cjyc.web.api.controller;

import com.cjyc.common.model.dto.CarrierDriverDto;
import com.cjyc.common.model.dto.CarrierVehicleDto;
import com.cjyc.common.model.dto.FreeDto;
import com.cjyc.common.model.dto.web.OperateDto;
import com.cjyc.common.model.dto.web.mineCarrier.*;
import com.cjyc.common.model.vo.FreeVehicleVo;
import com.cjyc.common.model.vo.PageVo;
import com.cjyc.common.model.vo.ResultVo;
import com.cjyc.common.model.vo.web.mineCarrier.HandleDto;
import com.cjyc.common.model.vo.web.mineCarrier.MyCarVo;
import com.cjyc.common.model.vo.web.mineCarrier.MyDriverVo;
import com.cjyc.common.model.vo.FreeDriverVo;
import com.cjyc.common.model.vo.web.mineCarrier.MyWaybillVo;
import com.cjyc.common.system.service.ICsDriverService;
import com.cjyc.common.system.service.ICsVehicleService;
import com.cjyc.web.api.service.IMineCarrierService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 *  @author: zj
 *  @Date: 2019/11/8 18:33
 *  @Description:我的承运商下司机与车辆
 */
@Api(tags = "司机-我的承运商模块")
@CrossOrigin
@RestController
@RequestMapping("/mineCarrier")
public class MineCarrierController {

    @Resource
    private IMineCarrierService mimeCarrierService;
    @Resource
    private ICsDriverService csDriverService;
    @Resource
    private ICsVehicleService csVehicleService;

    @ApiOperation(value = "新增/修改承运商下司机")
    @PostMapping(value = "/saveOrModifyDriver")
    public ResultVo saveOrModifyDriver(@Validated @RequestBody CarrierDriverDto dto){
        return csDriverService.saveOrModifyDriver(dto);
    }

    @ApiOperation(value = "查询承运商下司机")
    @PostMapping(value = "/findPageDriver")
    public ResultVo<PageVo<MyDriverVo>> findPageDriver(@RequestBody QueryMyDriverDto dto){
        return mimeCarrierService.findPageDriver(dto);
    }

    @ApiOperation(value = "操作承运商下司机")
    @PostMapping(value = "/verifyDriver")
    public ResultVo verifyDriver(@RequestBody OperateDto dto){
        return mimeCarrierService.verifyDriver(dto);
    }

    @ApiOperation(value = "新增/修改承运商下车辆",notes = "根据司机id判断时新增还是修改")
    @PostMapping(value = "/saveOrModifyVehicle")
    public ResultVo saveOrModifyVehicle(@RequestBody CarrierVehicleDto dto){
        return mimeCarrierService.saveOrModifyVehicle(dto);
    }

    @ApiOperation(value = "查询承运商下车辆")
    @PostMapping(value = "/findPageCar")
    public ResultVo<PageVo<MyCarVo>> findPageCar(@RequestBody QueryMyCarDto dto){
        return mimeCarrierService.findPageCar(dto);
    }

    @ApiOperation(value = "查询承运商下的空闲司机")
    @PostMapping(value = "/findCarrierFreeDriver")
    public ResultVo<List<FreeDriverVo>> findCarrierFreeDriver(@RequestBody FreeDto dto){
        return csDriverService.findCarrierFreeDriver(dto);
    }

    @ApiOperation(value = "该承运商下空闲车辆(管理员中的)")
    @PostMapping(value = "/findCarrierFreeVehicle")
    public ResultVo<List<FreeVehicleVo>> findCarrierFreeVehicle(@RequestBody FreeDto dto){
        return csVehicleService.findCarrierFreeVehicle(dto);
    }

}