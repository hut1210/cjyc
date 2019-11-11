package com.cjyc.web.api.controller;

import com.cjyc.common.model.dto.web.OperateDto;
import com.cjyc.common.model.dto.web.carrier.CarrierDto;
import com.cjyc.common.model.dto.web.carrier.DispatchCarrierDto;
import com.cjyc.common.model.dto.web.carrier.SeleCarrierDto;
import com.cjyc.common.model.dto.web.carrier.SeleVehicleDriverDto;
import com.cjyc.common.model.enums.ResultEnum;
import com.cjyc.common.model.util.BasePageUtil;
import com.cjyc.common.model.util.BaseResultUtil;
import com.cjyc.common.model.vo.PageVo;
import com.cjyc.common.model.vo.ResultVo;
import com.cjyc.common.model.vo.web.carrier.BaseVehicleVo;
import com.cjyc.common.model.vo.web.carrier.CarrierVo;
import com.cjyc.common.model.vo.web.carrier.BaseCarrierVo;
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
@Api(tags = "承运商管理")
@CrossOrigin
@RestController
@RequestMapping("/carrier")
public class CarrierController {

    @Resource
    private ICarrierService carrierService;

    @ApiOperation(value = "新增承运商")
    @PostMapping(value = "/saveCarrier")
    public ResultVo saveCarrier(@Validated({ CarrierDto.SaveCarrierDto.class }) @RequestBody CarrierDto dto){
        return carrierService.saveCarrier(dto);
    }

    @ApiOperation(value = "更新承运商")
    @PostMapping(value = "/modifyCarrier")
    public ResultVo modifyCarrier(@Validated({ CarrierDto.UpdateCarrierDto.class }) @RequestBody CarrierDto dto){
        boolean result = carrierService.modifyCarrier(dto);
        return result ? BaseResultUtil.success():BaseResultUtil.fail(ResultEnum.FAIL.getMsg());
    }

    @ApiOperation(value = "根据条件查询承运商")
    @PostMapping(value = "/findCarrier")
    public ResultVo<PageVo<CarrierVo>> findCarrier(@RequestBody SeleCarrierDto dto){
        BasePageUtil.initPage(dto);
        return carrierService.findCarrier(dto);
    }

    @ApiOperation(value = "根据id审核/冻结/解冻承运商")
    @PostMapping(value = "/verifyCarrier")
    public ResultVo verifyCarrier(@RequestBody OperateDto dto){
        boolean result = carrierService.verifyCarrier(dto);
        return result ? BaseResultUtil.success():BaseResultUtil.fail(ResultEnum.FAIL.getMsg());
    }

    @ApiOperation(value = "根据carrierId查看基本承运商信息")
    @PostMapping(value = "/showBaseCarrier/{carrierId}")
    public ResultVo<BaseCarrierVo> showBaseCarrier(@PathVariable @ApiParam(value = "承运商id",required = true) Long carrierId){
        return carrierService.showBaseCarrier(carrierId);
    }

    @ApiOperation(value = "根据条件查看该承运商下车辆信息")
    @PostMapping(value = "/findBaseVehicle")
    public ResultVo<PageVo<BaseVehicleVo>> findBaseVehicle(@Validated({ SeleVehicleDriverDto.SelectVehicleDto.class })@RequestBody SeleVehicleDriverDto dto){
        BasePageUtil.initPage(dto);
        return carrierService.findBaseVehicle(dto);
    }

    @ApiOperation(value = "根据条件查看承运商下司机信息")
    @PostMapping(value = "/findBaseDriver")
    public ResultVo findBaseDriver(@Validated({ SeleVehicleDriverDto.SelectVehicleDto.class })@RequestBody SeleVehicleDriverDto dto){
        BasePageUtil.initPage(dto);
        return carrierService.findBaseDriver(dto);
    }

    @PostMapping("/resetPwd/{id}")
    @ApiOperation(value = "承运商机构超级管理员账户重置密码")
    public ResultVo resetPwd(@ApiParam(name = "id", value = "机构标识", required = true)
                             @PathVariable Long id) {
        //重置机构超级管理员用户密码
        return carrierService.resetPwd(id);
    }

    @ApiOperation(value = "调度承运商信息")
    @PostMapping(value = "/dispatchCarrier")
    public ResultVo dispatchCarrier(@RequestBody DispatchCarrierDto dto){
        BasePageUtil.initPage(dto);
        return carrierService.dispatchCarrier(dto);
    }
}