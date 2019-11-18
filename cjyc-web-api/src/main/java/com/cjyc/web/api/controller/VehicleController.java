package com.cjyc.web.api.controller;

import com.cjyc.common.model.dto.web.vehicle.*;
import com.cjyc.common.model.vo.PageVo;
import com.cjyc.common.model.vo.ResultVo;
import com.cjyc.common.model.vo.web.vehicle.FreeVehicleVo;
import com.cjyc.common.model.vo.web.vehicle.VehicleVo;
import com.cjyc.web.api.service.IVehicleService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 *  @author: zj
 *  @Date: 2019/10/18 15:13
 *  @Description:运输车辆管理
 */
@Api(tags = "司机-运力")
@CrossOrigin
@RestController
@RequestMapping("/vehicle")
public class VehicleController {

    @Resource
    private IVehicleService vehicleService;

    @ApiOperation(value = "新增运输车辆")
    @PostMapping(value = "/saveVehicle")
    public ResultVo saveVehicle(@RequestBody VehicleDto dto){
        return vehicleService.saveVehicle(dto);
    }

    @ApiOperation(value = "根据条件查询运输车辆信息")
    @PostMapping(value = "/findVehicle")
    public ResultVo<PageVo<VehicleVo>> findVehicle(@RequestBody SelectVehicleDto dto){
        return vehicleService.findVehicle(dto);
    }

    @ApiOperation(value = "删除(如果车辆与司机绑定关系则先解除关系后删除车辆，如果没有绑定关系则直接删除车辆)")
    @PostMapping(value = "/removeVehicle")
    public ResultVo removeVehicle(@RequestBody RemoveVehicleDto dto){
        return vehicleService.removeVehicle(dto);
    }

    @ApiOperation(value = "修改车辆车位数")
    @PostMapping(value = "/modifyVehicle")
    public ResultVo modifyVehicle(@RequestBody ModifyCarryNumDto dto){
        return vehicleService.modifyVehicle(dto);
    }

    @ApiOperation(value = "查询没有被绑定的车辆信息")
    @PostMapping(value = "/findFreeVehicle")
    public ResultVo<List<FreeVehicleVo>> findFreeVehicle(@RequestBody FreeVehicleDto dto){
        return vehicleService.findFreeVehicle(dto);
    }
}