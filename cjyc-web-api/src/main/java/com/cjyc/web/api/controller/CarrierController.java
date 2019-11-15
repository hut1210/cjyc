package com.cjyc.web.api.controller;

import com.cjyc.common.model.dto.web.OperateDto;
import com.cjyc.common.model.dto.web.carrier.*;
import com.cjyc.common.model.vo.PageVo;
import com.cjyc.common.model.vo.ResultVo;
import com.cjyc.common.model.vo.web.carrier.CarrierVo;
import com.cjyc.common.model.vo.web.carrier.BaseCarrierVo;
import com.cjyc.common.model.vo.web.carrier.TrailCarrierVo;
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

    @ApiOperation(value = "承运商手机号保证在司机表中唯一")
    @PostMapping(value = "/existCarrier")
    public ResultVo existCarrier(@PathVariable @ApiParam(value = "承运商联系人手机号",required = true) String linkmanPhone){
        return carrierService.existCarrier(linkmanPhone);
    }

    @ApiOperation(value = "新增承运商")
    @PostMapping(value = "/saveCarrier")
    public ResultVo saveCarrier(@Validated({ CarrierDto.SaveCarrierDto.class }) @RequestBody CarrierDto dto){
        return carrierService.saveCarrier(dto);
    }

    @ApiOperation(value = "验证修改承运商输入的手机号是否为该承运商下面的司机")
    @PostMapping(value = "/existCarrierDriver")
    public ResultVo existCarrierDriver(@PathVariable @ApiParam(value = "承运商id",required = true) Long carrierId,
                                       @PathVariable @ApiParam(value = "承运商联系人手机号",required = true) String linkmanPhone){
        return carrierService.existCarrierDriver(carrierId,linkmanPhone);
    }

    @ApiOperation(value = "更新承运商")
    @PostMapping(value = "/modifyCarrier")
    public ResultVo modifyCarrier(@Validated({ CarrierDto.UpdateCarrierDto.class }) @RequestBody CarrierDto dto){
        return carrierService.modifyCarrier(dto);
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