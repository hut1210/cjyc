package com.cjyc.web.api.controller;

import com.cjyc.common.model.dto.web.carrier.CarrierDto;
import com.cjyc.common.model.dto.web.carrier.SeleCarrierDto;
import com.cjyc.common.model.dto.web.carrier.SeleVehicleDriverDto;
import com.cjyc.common.model.enums.ResultEnum;
import com.cjyc.common.model.util.BasePageUtil;
import com.cjyc.common.model.util.BaseResultUtil;
import com.cjyc.common.model.vo.PageVo;
import com.cjyc.common.model.vo.ResultVo;
import com.cjyc.common.model.vo.web.carrier.BaseDriverVo;
import com.cjyc.common.model.vo.web.carrier.BaseVehicleVo;
import com.cjyc.common.model.vo.web.carrier.CarrierVo;
import com.cjyc.common.model.vo.web.carrier.BaseCarrierVo;
import com.cjyc.web.api.service.ICarrierService;
import com.github.pagehelper.PageInfo;
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
        boolean result = carrierService.saveCarrier(dto);
        return result ? BaseResultUtil.getVo(ResultEnum.SUCCESS.getCode(),ResultEnum.SUCCESS.getMsg())
                : BaseResultUtil.getVo(ResultEnum.FAIL.getCode(),ResultEnum.FAIL.getMsg());
    }

    @ApiOperation(value = "更新承运商")
    @PostMapping(value = "/updateCarrier")
    public ResultVo updateCarrier(@Validated({ CarrierDto.UpdateCarrierDto.class }) @RequestBody CarrierDto dto){
        boolean result = carrierService.updateCarrier(dto);
        return result ? BaseResultUtil.getVo(ResultEnum.SUCCESS.getCode(),ResultEnum.SUCCESS.getMsg())
                : BaseResultUtil.getVo(ResultEnum.FAIL.getCode(),ResultEnum.FAIL.getMsg());
    }

    @ApiOperation(value = "根据条件查询承运商")
    @PostMapping(value = "/getCarrierByTerm")
    public ResultVo<PageVo<CarrierVo>> getCarrierByTerm(@RequestBody SeleCarrierDto dto){
        BasePageUtil.initPage(dto.getCurrentPage(),dto.getPageSize());
        PageInfo<CarrierVo> pageInfo = carrierService.getCarrierByTerm(dto);
        return BaseResultUtil.getPageVo(ResultEnum.SUCCESS.getCode(),ResultEnum.SUCCESS.getMsg(),pageInfo);
    }

    @ApiOperation(value = "根据id审核承运商")
    @PostMapping(value = "/verifyCarrierById/{id}/{state}")
    public ResultVo verifyCarrierById(@PathVariable @ApiParam(value = "承运商id",required = true) Long id,
                                     @PathVariable @ApiParam(value = "审核状态 3:审核通过 4:审核拒绝 5：冻结 6:解除",required = true) Integer state){
        if(id == null || state == null){
            return BaseResultUtil.getVo(ResultEnum.MOBILE_PARAM_ERROR.getCode(),ResultEnum.MOBILE_PARAM_ERROR.getMsg());
        }
        boolean result = carrierService.verifyCarrierById(id,state);
        return result ? BaseResultUtil.getVo(ResultEnum.SUCCESS.getCode(),ResultEnum.SUCCESS.getMsg())
                : BaseResultUtil.getVo(ResultEnum.FAIL.getCode(),ResultEnum.FAIL.getMsg());
    }

    @ApiOperation(value = "根据id查看基本承运商信息")
    @PostMapping(value = "/getBaseCarrierById/{id}")
    public ResultVo<BaseCarrierVo> getBaseCarrierById(@PathVariable @ApiParam(value = "承运商id",required = true) Long id){
        if(id == null){
            return BaseResultUtil.getVo(ResultEnum.MOBILE_PARAM_ERROR.getCode(),ResultEnum.MOBILE_PARAM_ERROR.getMsg());
        }
        BaseCarrierVo vo = carrierService.getBaseCarrierById(id);
        return BaseResultUtil.getVo(ResultEnum.FAIL.getCode(),ResultEnum.FAIL.getMsg(),vo);
    }

    @ApiOperation(value = "根据条件查看车辆信息")
    @PostMapping(value = "/getBaseVehicleByTerm")
    public ResultVo<PageVo<BaseVehicleVo>> getBaseVehicleByTerm(@Validated({ SeleVehicleDriverDto.SelectVehicleDto.class })@RequestBody SeleVehicleDriverDto dto){
        BasePageUtil.initPage(dto.getCurrentPage(),dto.getPageSize());
        PageInfo<BaseVehicleVo> pageInfo = carrierService.getBaseVehicleByTerm(dto);
        return BaseResultUtil.getPageVo(ResultEnum.FAIL.getCode(),ResultEnum.FAIL.getMsg(),pageInfo);
    }

    @ApiOperation(value = "根据条件查看承运商下司机信息")
    @PostMapping(value = "/getBaseDriverByTerm")
    public ResultVo getBaseDriverByTerm(@Validated({ SeleVehicleDriverDto.SelectVehicleDto.class })@RequestBody SeleVehicleDriverDto dto){
        BasePageUtil.initPage(dto.getCurrentPage(),dto.getPageSize());
        return carrierService.getBaseDriverByTerm(dto);
    }
}