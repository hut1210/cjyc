package com.cjyc.web.api.controller;

import com.cjyc.common.model.dto.web.OperateDto;
import com.cjyc.common.model.dto.web.driver.DriverDto;
import com.cjyc.common.model.dto.web.driver.SelectDriverDto;
import com.cjyc.common.model.dto.web.user.DriverListDto;
import com.cjyc.common.model.enums.ResultEnum;
import com.cjyc.common.model.util.BasePageUtil;
import com.cjyc.common.model.util.BaseResultUtil;
import com.cjyc.common.model.vo.PageVo;
import com.cjyc.common.model.vo.ResultVo;
import com.cjyc.common.model.vo.web.driver.DriverVo;
import com.cjyc.common.model.vo.web.driver.ShowDriverVo;
import com.cjyc.common.model.vo.web.user.DriverListVo;
import com.cjyc.web.api.service.IDriverService;
import com.github.pagehelper.PageInfo;
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

    @ApiOperation(value = "新增散户司机")
    @PostMapping(value = "/saveDriver")
    public ResultVo saveDriver(@Validated({ DriverDto.SaveDriverDto.class }) @RequestBody DriverDto dto){
        boolean result = driverService.saveDriver(dto);
        return result ? BaseResultUtil.success():BaseResultUtil.fail(ResultEnum.FAIL.getMsg());
    }

    @ApiOperation(value = "根据查询条件查看司机信息")
    @PostMapping(value = "/findDriver")
    public ResultVo<PageVo<DriverVo>> findDriver(@RequestBody SelectDriverDto dto){
        BasePageUtil.initPage(dto);
        return driverService.findDriver(dto);
    }

    @ApiOperation(value = "根据id进行审核通过/拒绝/冻结解冻")
    @PostMapping(value = "/verifyDriver")
    public ResultVo verifyDriver(@RequestBody OperateDto dto){
        boolean result = driverService.verifyDriver(dto);
        return result ? BaseResultUtil.success():BaseResultUtil.fail(ResultEnum.FAIL.getMsg());
    }

    @ApiOperation(value = "根据司机userId/Id查看司机信息")
    @PostMapping(value = "/showDriver/{id}/{userId}")
    public ResultVo<ShowDriverVo> showDriver(@PathVariable @ApiParam(value = "司机id",required = true) Long id,
                                             @PathVariable @ApiParam(value = "司机UserId",required = true) Long userId){
        return driverService.showDriver(id,userId);
    }

    @ApiOperation(value = "根据司机Id更新司机信息")
    @PostMapping(value = "/modifyDriver/{id}")
    public ResultVo modifyDriver(@RequestBody DriverDto dto){
        boolean result = driverService.modifyDriver(dto);
        return result ? BaseResultUtil.success():BaseResultUtil.fail(ResultEnum.FAIL.getMsg());
    }

    @ApiOperation(value = "冻结/解除司机冻结状态")
    @PostMapping(value = "/resetState/{id}/{flag}")
    public ResultVo resetState(@ApiParam(name = "id", value = "司机表示", required = true)
                               @PathVariable Long id,
                               @ApiParam(name = "flag", value = "冻结/解除状态 1：冻结 2：解除", required = true)
                               @PathVariable Integer flag){
        return driverService.resetState(id, flag);
    }


}
