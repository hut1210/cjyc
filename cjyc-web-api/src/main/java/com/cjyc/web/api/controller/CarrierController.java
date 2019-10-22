package com.cjyc.web.api.controller;

import com.cjyc.common.model.dto.web.carrier.CarrierDto;
import com.cjyc.common.model.dto.web.vehicle.VehicleDto;
import com.cjyc.common.model.enums.ResultEnum;
import com.cjyc.common.model.util.BaseResultUtil;
import com.cjyc.common.model.vo.ResultVo;
import com.cjyc.web.api.service.ICarrierService;
import com.cjyc.web.api.service.IVehicleService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
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
    private ICarrierService iCarrierService;

    @ApiOperation(value = "新增承运商", notes = "新增承运商", httpMethod = "POST")
    @RequestMapping(value = "/saveCarrier", method = RequestMethod.POST)
    public ResultVo saveCarrier(@Validated({ CarrierDto.SaveCarrierDto.class }) @RequestBody CarrierDto dto){
        boolean result = iCarrierService.saveCarrier(dto);
        return result ? BaseResultUtil.getVo(ResultEnum.SUCCESS.getCode(),ResultEnum.SUCCESS.getMsg())
                : BaseResultUtil.getVo(ResultEnum.FAIL.getCode(),ResultEnum.FAIL.getMsg());
    }

}