package com.cjyc.web.api.controller;

import com.cjyc.common.model.dto.web.OperateDto;
import com.cjyc.common.model.dto.web.mineCarrier.*;
import com.cjyc.common.model.vo.PageVo;
import com.cjyc.common.model.vo.ResultVo;
import com.cjyc.common.model.vo.web.mineCarrier.MyCarVo;
import com.cjyc.common.model.vo.web.mineCarrier.MyDriverVo;
import com.cjyc.common.model.vo.web.mineCarrier.MyFreeDriverVo;
import com.cjyc.web.api.service.IMineCarrierService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 *  @author: zj
 *  @Date: 2019/11/8 18:33
 *  @Description:我的承运商下司机与车辆
 */
@Api(tags = "我的承运商下司机与车辆")
@CrossOrigin
@RestController
@RequestMapping("/mineCarrier")
public class MineCarrierController {

    @Resource
    private IMineCarrierService mimeCarrierService;

    @ApiOperation(value = "新增/修改承运商下司机",notes = "如果承运商id和登陆人id相同则为承运商添加，否则为业务员添加")
    @PostMapping(value = "/saveOrModifyDriver")
    public ResultVo saveOrModifyDriver(@Validated @RequestBody MyDriverDto dto){
        return mimeCarrierService.saveOrModifyDriver(dto);
    }

    @ApiOperation(value = "查询承运商下司机")
    @PostMapping(value = "/findPageDriver")
    public ResultVo<PageVo<MyDriverVo>> findPageDriver(@RequestBody QueryMyDriverDto dto){
        return mimeCarrierService.findPageDriver(dto);
    }

    @ApiOperation(value = "操作承运商下司机")
    @PostMapping(value = "/verifyDriver")
    public ResultVo verifyDriver(@RequestBody OperateDto dto){
        return mimeCarrierService.verifyDriver(dto);
    }

    @ApiOperation(value = "新增/修改承运商下车辆",notes = "根据司机id判断时新增还是修改")
    @PostMapping(value = "/saveOrModifyVehicle")
    public ResultVo saveOrModifyVehicle(@RequestBody MyVehicleDto dto){
        return mimeCarrierService.saveOrModifyVehicle(dto);
    }

    @ApiOperation(value = "查询承运商下车辆",notes = "如果carrierId为空，则是承运商管理员登陆，需要把loginId转成carrierId,否则是韵车内部登陆")
    @PostMapping(value = "/findPageCar")
    public ResultVo<PageVo<MyCarVo>> findPageCar(@RequestBody QueryMyCarDto dto){
        return mimeCarrierService.findPageCar(dto);
    }

    @ApiOperation(value = "查询承运商下的空闲司机")
    @PostMapping(value = "/findFreeDriver")
    public ResultVo<List<MyFreeDriverVo>> findFreeDriver(@PathVariable @ApiParam(value = "该承运商id",required = true) Long carrierId,
                                                         @PathVariable(required = false) @ApiParam(value = "司机姓名") String realName){
        return mimeCarrierService.findFreeDriver(carrierId,realName);
    }

}