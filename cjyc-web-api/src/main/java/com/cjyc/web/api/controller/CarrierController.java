package com.cjyc.web.api.controller;

import com.cjyc.common.model.dto.web.carrier.CarrierDto;
import com.cjyc.common.model.dto.web.carrier.SeleCarrierDto;
import com.cjyc.common.model.dto.web.carrier.SeleVehicleDto;
import com.cjyc.common.model.entity.BusinessCityCode;
import com.cjyc.common.model.enums.ResultEnum;
import com.cjyc.common.model.util.BasePageUtil;
import com.cjyc.common.model.util.BaseResultUtil;
import com.cjyc.common.model.vo.PageVo;
import com.cjyc.common.model.vo.ResultVo;
import com.cjyc.common.model.vo.web.carrier.BaseVehicleVo;
import com.cjyc.common.model.vo.web.carrier.CarrierVo;
import com.cjyc.common.model.vo.web.carrier.BaseCarrierVo;
import com.cjyc.web.api.service.ICarrierService;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.commons.lang3.StringUtils;
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

    @ApiOperation(value = "更新承运商", notes = "更新承运商", httpMethod = "POST")
    @RequestMapping(value = "/updateCarrier", method = RequestMethod.POST)
    public ResultVo updateCarrier(@Validated({ CarrierDto.UpdateCarrierDto.class }) @RequestBody CarrierDto dto){
        boolean result = iCarrierService.updateCarrier(dto);
        return result ? BaseResultUtil.getVo(ResultEnum.SUCCESS.getCode(),ResultEnum.SUCCESS.getMsg())
                : BaseResultUtil.getVo(ResultEnum.FAIL.getCode(),ResultEnum.FAIL.getMsg());
    }

    @ApiOperation(value = "根据条件查询承运商", notes = "根据条件查询承运商", httpMethod = "POST")
    @RequestMapping(value = "/getCarrierByTerm", method = RequestMethod.POST)
    public ResultVo<PageVo<CarrierVo>> getCarrierByTerm(@RequestBody SeleCarrierDto dto){
        BasePageUtil.initPage(dto.getCurrentPage(),dto.getPageSize());
        PageInfo<CarrierVo> pageInfo = iCarrierService.getCarrierByTerm(dto);
        return BaseResultUtil.getPageVo(ResultEnum.SUCCESS.getCode(),ResultEnum.SUCCESS.getMsg(),pageInfo);
    }

    @ApiOperation(value = "根据id审核承运商", notes = "根据id审核承运商", httpMethod = "POST")
    @PostMapping(value = "/verifyCarrierById/{id}/{state}")
    public ResultVo verifyCarrierById(@PathVariable @ApiParam(value = "承运商id",required = true) Long id,
                                     @PathVariable @ApiParam(value = "审核状态 1:审核通过 2:审核拒绝 3：冻结 4:解除",required = true) String state){
        if(id == null || StringUtils.isBlank(state)){
            return BaseResultUtil.getVo(ResultEnum.MOBILE_PARAM_ERROR.getCode(),ResultEnum.MOBILE_PARAM_ERROR.getMsg());
        }
        boolean result = iCarrierService.verifyCarrierById(id,state);
        return result ? BaseResultUtil.getVo(ResultEnum.SUCCESS.getCode(),ResultEnum.SUCCESS.getMsg())
                : BaseResultUtil.getVo(ResultEnum.FAIL.getCode(),ResultEnum.FAIL.getMsg());
    }

    @ApiOperation(value = "根据id查看基本承运商信息", notes = "根据id(修改)查看承运商", httpMethod = "POST")
    @PostMapping(value = "/getBaseCarrierById/{id}")
    public ResultVo<BaseCarrierVo> getBaseCarrierById(@PathVariable @ApiParam(value = "承运商id",required = true) Long id){
        if(id == null){
            return BaseResultUtil.getVo(ResultEnum.MOBILE_PARAM_ERROR.getCode(),ResultEnum.MOBILE_PARAM_ERROR.getMsg());
        }
        BaseCarrierVo vo = iCarrierService.getBaseCarrierById(id);
        return BaseResultUtil.getVo(ResultEnum.FAIL.getCode(),ResultEnum.FAIL.getMsg(),vo);
    }

    @ApiOperation(value = "根据条件查看车辆信息", notes = "根据条件查看车辆信息", httpMethod = "POST")
    @PostMapping(value = "/getBaseVehicleByTerm")
    public ResultVo<PageVo<BaseVehicleVo>> getBaseVehicleByTerm(@Validated({ SeleVehicleDto.SelectVehicleDto.class })@RequestBody SeleVehicleDto dto){
        BasePageUtil.initPage(dto.getCurrentPage(),dto.getPageSize());
        PageInfo<BaseVehicleVo> pageInfo = iCarrierService.getBaseVehicleByTerm(dto);
        return BaseResultUtil.getPageVo(ResultEnum.FAIL.getCode(),ResultEnum.FAIL.getMsg(),pageInfo);
    }

    @ApiOperation(value = "根据承运商id查看承运商业务范围", notes = "根据承运商id查看承运商业务范围", httpMethod = "POST")
    @PostMapping(value = "/getCarrierBusiById/{id}")
    public ResultVo<BusinessCityCode> getCarrierBusiById(@PathVariable @ApiParam(value = "承运商id",required = true) Long id){
        if(id == null){
            return BaseResultUtil.getVo(ResultEnum.MOBILE_PARAM_ERROR.getCode(),ResultEnum.MOBILE_PARAM_ERROR.getMsg());
        }
        BusinessCityCode businessCityCode = iCarrierService.getCarrierBusiById(id);
        return BaseResultUtil.getVo(ResultEnum.FAIL.getCode(),ResultEnum.FAIL.getMsg(),businessCityCode);
    }

}