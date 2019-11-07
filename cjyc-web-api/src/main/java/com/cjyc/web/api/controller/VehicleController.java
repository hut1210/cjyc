package com.cjyc.web.api.controller;

import com.cjyc.common.model.dto.web.OperateDto;
import com.cjyc.common.model.dto.web.vehicle.DriverVehicleConDto;
import com.cjyc.common.model.dto.web.vehicle.SelectVehicleDto;
import com.cjyc.common.model.dto.web.vehicle.VehicleDto;
import com.cjyc.common.model.enums.ResultEnum;
import com.cjyc.common.model.util.BaseResultUtil;
import com.cjyc.common.model.vo.ResultVo;
import com.cjyc.web.api.service.IVehicleService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 *  @author: zj
 *  @Date: 2019/10/18 15:13
 *  @Description:运输车辆管理
 */
@Api(tags = "运输车辆管理")
@CrossOrigin
@RestController
@RequestMapping("/vehicle")
public class VehicleController {

    @Resource
    private IVehicleService vehicleService;

    @ApiOperation(value = "新增运输车辆")
    @PostMapping(value = "/saveVehicle")
    public ResultVo saveVehicle(@RequestBody VehicleDto dto){
        boolean result = vehicleService.saveVehicle(dto);
        return result ? BaseResultUtil.success():BaseResultUtil.fail(ResultEnum.FAIL.getMsg());
    }

    @ApiOperation(value = "根据条件查询运输车辆信息")
    @PostMapping(value = "/findVehicle")
    public ResultVo findVehicle(@RequestBody SelectVehicleDto dto){
        return vehicleService.findVehicle(dto);
    }

    @ApiOperation(value = "删除车辆与司机关系")
    @PostMapping(value = "/verifyVehicle")
    public ResultVo verifyVehicle(@RequestBody OperateDto dto){
        return vehicleService.verifyVehicle(dto);
    }

    @ApiOperation(value = "修改车辆与司机关系")
    @PostMapping(value = "/modifyVehicle")
    public ResultVo modifyVehicle(@RequestBody DriverVehicleConDto dto){
        return vehicleService.modifyVehicle(dto);
    }

}