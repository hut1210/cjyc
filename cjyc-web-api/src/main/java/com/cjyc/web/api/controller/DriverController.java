package com.cjyc.web.api.controller;

import com.cjyc.common.model.dto.CarrierDriverDto;
import com.cjyc.common.model.dto.web.OperateDto;
import com.cjyc.common.model.dto.web.driver.*;
import com.cjyc.common.model.dto.web.user.DriverListDto;
import com.cjyc.common.model.vo.PageVo;
import com.cjyc.common.model.vo.ResultVo;
import com.cjyc.common.model.vo.web.driver.DispatchDriverVo;
import com.cjyc.common.model.vo.web.driver.DriverVo;
import com.cjyc.common.model.vo.web.driver.ExistDriverVo;
import com.cjyc.common.model.vo.web.driver.ShowDriverVo;
import com.cjyc.common.model.vo.web.user.DriverListVo;
import com.cjyc.web.api.service.IDriverService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * 用户
 * @author JPG
 */
@Api(tags = "司机")
@RestController
@RequestMapping(value = "/driver",
        consumes = MediaType.APPLICATION_JSON_UTF8_VALUE,
        produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
public class DriverController {

    @Resource
    private IDriverService driverService;

    /**
     * 查询司机列表
     * @author JPG
     */
    @ApiOperation(value = "按线路统计待调度车辆（统计列表）")
    @PostMapping(value = "/list")
    public ResultVo<PageVo<DriverListVo>> getDriverList(@RequestBody DriverListDto reqDto) {
        return driverService.lineWaitDispatchCarCountList(reqDto);
    }

    @ApiOperation(value = "新增/修改散户司机")
    @PostMapping(value = "/saveOrModifyDriver")
    public ResultVo saveOrModifyDriver(@Validated @RequestBody DriverDto dto){
        return driverService.saveOrModifyDriver(dto);
    }

    @ApiOperation(value = "根据查询条件查看司机信息")
    @PostMapping(value = "/findDriver")
    public ResultVo<PageVo<DriverVo>> findDriver(@RequestBody SelectDriverDto dto){
        return driverService.findDriver(dto);
    }

    @ApiOperation(value = "根据id进行审核通过/拒绝/冻结解冻")
    @PostMapping(value = "/verifyDriver")
    public ResultVo verifyDriver(@RequestBody OperateDto dto){
        return driverService.verifyDriver(dto);
    }

    @ApiOperation(value = "根据承运商id(carrierId)查看司机信息")
    @PostMapping(value = "/showDriver")
    public ResultVo<ShowDriverVo> showDriver(@Validated @RequestBody BaseCarrierIdDto dto){
        return driverService.showDriver(dto);
    }

    @ApiOperation(value = "app注册校验记录")
    @PostMapping(value = "/showExistDriver")
    public ResultVo<List<ExistDriverVo>> showExistDriver(){
        return driverService.showExistDriver();
    }

    @ApiOperation(value = "冻结/解除司机冻结状态")
    @PostMapping(value = "/resetState/{id}/{flag}")
    public ResultVo resetState(@ApiParam(name = "id", value = "司机表示", required = true)
                               @PathVariable Long id,
                               @ApiParam(name = "flag", value = "冻结/解除状态 1：冻结 2：解除", required = true)
                               @PathVariable Integer flag){
        return driverService.resetState(id, flag);
    }

    @ApiOperation(value = "调度个人司机信息")
    @PostMapping(value = "/dispatchDriver")
    public ResultVo<PageVo<DispatchDriverVo>> dispatchDriver(@RequestBody DispatchDriverDto dto){
        return driverService.dispatchDriver(dto);
    }

    @ApiOperation(value = "查询承运商下属司机")
    @PostMapping(value = "/carrier/driver/list")
    public ResultVo<PageVo<DispatchDriverVo>> dispatchDriver(@RequestBody CarrierDriverListDto dto){
        return driverService.carrierDrvierList(dto);
    }
}
