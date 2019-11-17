package com.cjyc.web.api.controller;

import com.cjyc.common.model.dto.web.OperateDto;
import com.cjyc.common.model.dto.web.driver.DispatchDriverDto;
import com.cjyc.common.model.dto.web.driver.DriverDto;
import com.cjyc.common.model.dto.web.driver.SelectDriverDto;
import com.cjyc.common.model.dto.web.user.DriverListDto;
import com.cjyc.common.model.vo.PageVo;
import com.cjyc.common.model.vo.ResultVo;
import com.cjyc.common.model.vo.web.driver.DriverVo;
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
    @PostMapping(value = "/showDriver/{carrierId}")
    public ResultVo<ShowDriverVo> showDriver(@PathVariable @ApiParam(value = "承运商id",required = true) Long carrierId){
        return driverService.showDriver(carrierId);
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
    public ResultVo dispatchDriver(@RequestBody DispatchDriverDto dto){
        return driverService.dispatchDriver(dto);
    }
}
