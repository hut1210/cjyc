package com.cjyc.web.api.controller;

import com.cjyc.common.model.dto.KeywordDto;
import com.cjyc.common.model.dto.web.vehicle.VehicleDto;
import com.cjyc.common.model.dto.web.vehicle.*;
import com.cjyc.common.model.enums.ResultEnum;
import com.cjyc.common.model.util.BaseResultUtil;
import com.cjyc.common.model.vo.PageVo;
import com.cjyc.common.model.vo.ResultVo;
import com.cjyc.common.model.vo.FreeVehicleVo;
import com.cjyc.common.model.vo.web.vehicle.VehicleVo;
import com.cjyc.common.system.service.ICsVehicleService;
import com.cjyc.web.api.service.IVehicleService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
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
    @Resource
    private ICsVehicleService csVehicleService;

    @ApiOperation(value = "新增运输车辆")
    @PostMapping(value = "/saveVehicle")
    public ResultVo saveVehicle(@Validated @RequestBody VehicleDto dto){
        return vehicleService.saveVehicle(dto);
    }

    @ApiOperation(value = "根据条件查询运输车辆信息")
    @PostMapping(value = "/findVehicle")
    public ResultVo<PageVo<VehicleVo>> findVehicle(@Validated @RequestBody SelectVehicleDto dto){
        return vehicleService.findVehicle(dto);
    }

    @ApiOperation(value = "删除(如果车辆与司机绑定关系则先解除关系后删除车辆，如果没有绑定关系则直接删除车辆)")
    @PostMapping(value = "/removeVehicle")
    public ResultVo removeVehicle(@Validated @RequestBody RemoveVehicleDto dto){
        return vehicleService.removeVehicle(dto);
    }

    @ApiOperation(value = "修改车辆车位数")
    @PostMapping(value = "/modifyVehicle")
    public ResultVo modifyVehicle(@Validated @RequestBody ModifyCarryNumDto dto){
        return vehicleService.modifyVehicle(dto);
    }

    @ApiOperation(value = "查询没有被绑定的社会车辆信息")
    @PostMapping(value = "/findPersonFreeVehicle")
    public ResultVo<List<FreeVehicleVo>> findPersonFreeVehicle(@RequestBody KeywordDto dto){
        return csVehicleService.findPersonFreeVehicle(dto);
    }

    @ApiOperation(value = "该承运商下空闲车辆")
    @PostMapping(value = "/findCarrierVehicle")
    public ResultVo<List<FreeVehicleVo>> findCarrierVehicle(@Validated @RequestBody FreeVehicleDto dto){
        return csVehicleService.findCarrierVehicleById(dto);
    }

    @ApiOperation(value = "车辆管理导出Excel", notes = "\t 请求接口为/vehicle/exportVehicleExcel?plateNo=车牌号&realName=司机姓名&phone=司机手机号")
    @GetMapping("/exportVehicleExcel")
    public void exportVehicleExcel(HttpServletRequest request, HttpServletResponse response){
        vehicleService.exportVehicleExcel(request,response);
    }

    @ApiOperation(value = "社会车辆导入Excel", notes = "\t 请求接口为/importVehicleExcel/loginId(导入用户ID)格式")
    @PostMapping("/importVehicleExcel/{loginId}")
    public ResultVo importVehicleExcel(@RequestParam("file") MultipartFile file, @PathVariable Long loginId){
        boolean result = vehicleService.importVehicleExcel(file, loginId);
        return result ? BaseResultUtil.success() : BaseResultUtil.fail(ResultEnum.FAIL.getMsg());
    }
}