package com.cjyc.web.api.controller;

import com.cjyc.common.model.dto.web.vehicle.SelectVehicleDto;
import com.cjyc.common.model.dto.web.vehicle.VehicleDto;
import com.cjyc.common.model.enums.ResultEnum;
import com.cjyc.common.model.util.BasePageUtil;
import com.cjyc.common.model.util.BaseResultUtil;
import com.cjyc.common.model.vo.PageVo;
import com.cjyc.common.model.vo.ResultVo;
import com.cjyc.common.model.vo.web.vehicle.VehicleVo;
import com.cjyc.web.api.service.IVehicleService;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

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
    public ResultVo saveVehicle(@Validated({ VehicleDto.SaveVehicleDto.class }) @RequestBody VehicleDto dto){
        boolean result = vehicleService.saveVehicle(dto);
        return result ? BaseResultUtil.getVo(ResultEnum.SUCCESS.getCode(),ResultEnum.SUCCESS.getMsg())
                : BaseResultUtil.getVo(ResultEnum.FAIL.getCode(),ResultEnum.FAIL.getMsg());
    }

    @ApiOperation(value = "根据运输车辆ID查看信息")
    @PostMapping(value = "/showVehicle/{vehicleId}")
    public ResultVo<VehicleVo> showVehicle(@PathVariable Long vehicleId){
        if(vehicleId == null){
            BaseResultUtil.getVo(ResultEnum.MOBILE_PARAM_ERROR.getCode(),ResultEnum.MOBILE_PARAM_ERROR.getMsg());
        }
        VehicleVo vehicleVo = vehicleService.showVehicle(vehicleId);
        return BaseResultUtil.getVo(ResultEnum.SUCCESS.getCode(),ResultEnum.SUCCESS.getMsg(),vehicleVo);
    }

    @ApiOperation(value = "根据车辆id更新运输车辆")
    @PostMapping(value = "/updateVehicle")
    public ResultVo updateVehicle(@Validated({ VehicleDto.UpdateVehicleDto.class }) @RequestBody VehicleDto dto){
        boolean result = vehicleService.updateVehicle(dto);
        return result ? BaseResultUtil.getVo(ResultEnum.SUCCESS.getCode(),ResultEnum.SUCCESS.getMsg())
                : BaseResultUtil.getVo(ResultEnum.FAIL.getCode(),ResultEnum.FAIL.getMsg());
    }

    @ApiOperation(value = "根据查询条件查看车辆信息")
    @PostMapping(value = "/getVehicleByTerm")
    public ResultVo<PageVo<VehicleVo>> getVehicleByTerm(@RequestBody SelectVehicleDto dto){
        BasePageUtil.initPage(dto.getCurrentPage(),dto.getPageSize());
        PageInfo<VehicleVo> pageInfo = vehicleService.getVehicleByTerm(dto);
        return BaseResultUtil.getPageVo(ResultEnum.SUCCESS.getCode(),ResultEnum.SUCCESS.getMsg(),pageInfo);
    }

    @ApiOperation(value = "根据车辆ids删除车辆信息")
    @PostMapping(value = "/delVehicleByIds")
    public ResultVo delVehicleByIds(@RequestBody List<Long> ids){
        if(ids == null || ids.size() == 0){
            return BaseResultUtil.getVo(ResultEnum.MOBILE_PARAM_ERROR.getCode(),ResultEnum.MOBILE_PARAM_ERROR.getMsg());
        }
        boolean result = vehicleService.delVehicleByIds(ids);
        return result ? BaseResultUtil.getVo(ResultEnum.SUCCESS.getCode(),ResultEnum.SUCCESS.getMsg())
                : BaseResultUtil.getVo(ResultEnum.FAIL.getCode(),ResultEnum.FAIL.getMsg());
    }

}