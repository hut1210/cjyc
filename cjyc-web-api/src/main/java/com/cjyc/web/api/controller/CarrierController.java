package com.cjyc.web.api.controller;

import com.cjyc.common.model.dto.CarrierDriverDto;
import com.cjyc.common.model.dto.web.OperateDto;
import com.cjyc.common.model.dto.web.carrier.*;
import com.cjyc.common.model.vo.PageVo;
import com.cjyc.common.model.vo.ResultVo;
import com.cjyc.common.model.vo.web.carrier.*;
import com.cjyc.common.system.service.ICsDriverService;
import com.cjyc.web.api.service.ICarrierService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 *  @author: zj
 *  @Date: 2019/10/18 15:13
 *  @Description:承运商管理
 */
@Api(tags = "司机-承运商")
@CrossOrigin
@RestController
@RequestMapping("/carrier")
public class CarrierController {

    @Resource
    private ICarrierService carrierService;
    @Resource
    private ICsDriverService csDriverService;

    @ApiOperation(value = "新增/修改承运商")
    @PostMapping(value = "/saveOrModifyCarrier")
    public ResultVo saveOrModifyCarrier(@Validated @RequestBody CarrierDto dto){
        return carrierService.saveOrModifyCarrier(dto);
    }

    @ApiOperation(value = "根据条件查询承运商")
    @PostMapping(value = "/findCarrier")
    public ResultVo<PageVo<CarrierVo>> findCarrier(@RequestBody SeleCarrierDto dto){
        return carrierService.findCarrier(dto);
    }

    @ApiOperation(value = "根据id审核/冻结/解冻承运商")
    @PostMapping(value = "/verifyCarrier")
    public ResultVo verifyCarrier(@RequestBody OperateDto dto){
        return carrierService.verifyCarrier(dto);
    }

    @ApiOperation(value = "根据carrierId查看基本承运商信息")
    @PostMapping(value = "/showBaseCarrier/{carrierId}")
    public ResultVo<BaseCarrierVo> showBaseCarrier(@PathVariable @ApiParam(value = "承运商id",required = true) Long carrierId){
        return carrierService.showBaseCarrier(carrierId);
    }

    @PostMapping("/resetPwd/{id}")
    @ApiOperation(value = "承运商机构超级管理员账户重置密码")
    public ResultVo resetPwd(@ApiParam(name = "id", value = "机构标识", required = true)
                             @PathVariable Long id) {
        //重置机构超级管理员用户密码
        return carrierService.resetPwd(id);
    }

    @ApiOperation(value = "新增/修改承运商下司机")
    @PostMapping(value = "/saveOrModifyDriver")
    public ResultVo saveOrModifyDriver(@Validated @RequestBody CarrierDriverDto dto){
        return csDriverService.saveOrModifyDriver(dto);
    }

    @ApiOperation(value = "根据carrierId查看基本司机信息")
    @PostMapping(value = "/transportDriver")
    public ResultVo<PageVo<TransportDriverVo>> transportDriver(@Validated @RequestBody TransportDto dto){
        return carrierService.transportDriver(dto);
    }

    @ApiOperation(value = "根据carrierId查看基本车辆信息")
    @PostMapping(value = "/transportVehicle")
    public ResultVo<PageVo<TransportVehicleVo>> transportVehicle(@Validated @RequestBody TransportDto dto){
        return carrierService.transportVehicle(dto);
    }

    @ApiOperation(value = "调度承运商信息")
    @PostMapping(value = "/dispatchCarrier")
    public ResultVo dispatchCarrier(@RequestBody DispatchCarrierDto dto){
        return carrierService.dispatchCarrier(dto);
    }

    @ApiOperation(value = "调度中心中提车干线调度中代驾和拖车列表")
    @PostMapping(value = "/traileDriver")
    public ResultVo<PageVo<TrailCarrierVo>> trailDriver(@RequestBody TrailCarrierDto dto){
        return carrierService.trailDriver(dto);
    }

}