package com.cjyc.web.api.controller;

import com.cjyc.common.model.dto.web.CustomerDto;
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
    private IVehicleService iVehicleService;

    @ApiOperation(value = "新增运输车辆", notes = "新增运输车辆", httpMethod = "POST")
    @RequestMapping(value = "/saveVehicle", method = RequestMethod.POST)
    public ResultVo saveVehicle(@Validated({ VehicleDto.SaveVehicleDto.class }) @RequestBody VehicleDto dto){
        boolean result = iVehicleService.saveVehicle(dto);
        return result ? BaseResultUtil.getVo(ResultEnum.SUCCESS.getCode(),ResultEnum.SUCCESS.getMsg())
                : BaseResultUtil.getVo(ResultEnum.FAIL.getCode(),ResultEnum.FAIL.getMsg());
    }

    @ApiOperation(value = "根据运输车辆ID查看信息", notes = "根据运输车辆ID查看信息", httpMethod = "POST")
    @RequestMapping(value = "/showVehicle/{vehicleId}", method = RequestMethod.POST)
    public ResultVo<VehicleVo> showVehicle(@PathVariable Long vehicleId){
        if(vehicleId == null){
            BaseResultUtil.getVo(ResultEnum.MOBILE_PARAM_ERROR.getCode(),ResultEnum.MOBILE_PARAM_ERROR.getMsg());
        }
        VehicleVo vehicleVo = iVehicleService.showVehicle(vehicleId);
        return BaseResultUtil.getVo(ResultEnum.SUCCESS.getCode(),ResultEnum.SUCCESS.getMsg(),vehicleVo);
    }

    @ApiOperation(value = "根据车辆id更新运输车辆", notes = "根据车辆id更新运输车辆", httpMethod = "POST")
    @RequestMapping(value = "/updateVehicle", method = RequestMethod.POST)
    public ResultVo updateVehicle(@Validated({ VehicleDto.UpdateVehicleDto.class }) @RequestBody VehicleDto dto){
        boolean result = iVehicleService.updateVehicle(dto);
        return result ? BaseResultUtil.getVo(ResultEnum.SUCCESS.getCode(),ResultEnum.SUCCESS.getMsg())
                : BaseResultUtil.getVo(ResultEnum.FAIL.getCode(),ResultEnum.FAIL.getMsg());
    }

    @ApiOperation(value = "根据查询条件查看车辆信息", notes = "根据查询条件查看车辆信息", httpMethod = "POST")
    @RequestMapping(value = "/getVehicleByTerm", method = RequestMethod.POST)
    public ResultVo<PageVo<VehicleVo>> getVehicleByTerm(@RequestBody SelectVehicleDto dto){
        BasePageUtil.initPage(dto.getCurrentPage(),dto.getPageSize());
        PageInfo<VehicleVo> pageInfo = iVehicleService.getVehicleByTerm(dto);
        return BaseResultUtil.getPageVo(ResultEnum.SUCCESS.getCode(),ResultEnum.SUCCESS.getMsg(),pageInfo);
    }

    @ApiOperation(value = "根据车辆ids删除车辆信息", notes = "根据车辆ids删除车辆信息", httpMethod = "POST")
    @PostMapping(value = "/delVehicleByIds")
    public ResultVo delVehicleByIds(@RequestBody List<Long> ids){
        if(ids == null || ids.size() == 0){
            return BaseResultUtil.getVo(ResultEnum.MOBILE_PARAM_ERROR.getCode(),ResultEnum.MOBILE_PARAM_ERROR.getMsg());
        }
        boolean result = iVehicleService.delVehicleByIds(ids);
        return result ? BaseResultUtil.getVo(ResultEnum.SUCCESS.getCode(),ResultEnum.SUCCESS.getMsg())
                : BaseResultUtil.getVo(ResultEnum.FAIL.getCode(),ResultEnum.FAIL.getMsg());
    }

}