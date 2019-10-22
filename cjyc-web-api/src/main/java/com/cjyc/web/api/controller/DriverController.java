package com.cjyc.web.api.controller;

import com.cjyc.common.model.dto.web.driver.DriverDto;
import com.cjyc.common.model.dto.web.driver.SelectDriverDto;
import com.cjyc.common.model.dto.web.user.DriverListDto;
import com.cjyc.common.model.dto.web.vehicle.SelectVehicleDto;
import com.cjyc.common.model.enums.ResultEnum;
import com.cjyc.common.model.util.BasePageUtil;
import com.cjyc.common.model.util.BaseResultUtil;
import com.cjyc.common.model.vo.PageVo;
import com.cjyc.common.model.vo.ResultVo;
import com.cjyc.common.model.vo.web.driver.DriverVo;
import com.cjyc.common.model.vo.web.user.DriverListVo;
import com.cjyc.common.model.vo.web.vehicle.VehicleVo;
import com.cjyc.web.api.service.IDriverService;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
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
    @GetMapping(value = "/list")
    public ResultVo<PageVo<DriverListVo>> getDriverList(@RequestBody DriverListDto reqDto) {
        return driverService.lineWaitDispatchCarCountList(reqDto);
    }

    @ApiOperation(value = "新增散户司机", notes = "新增散户司机", httpMethod = "POST")
    @RequestMapping(value = "/saveDriver", method = RequestMethod.POST)
    public ResultVo saveDriver(@Validated({ DriverDto.SaveDriverDto.class }) @RequestBody DriverDto dto){
        boolean result = driverService.saveDriver(dto);
        return result ? BaseResultUtil.getVo(ResultEnum.SUCCESS.getCode(),ResultEnum.SUCCESS.getMsg())
                : BaseResultUtil.getVo(ResultEnum.FAIL.getCode(),ResultEnum.FAIL.getMsg());
    }

    @ApiOperation(value = "根据查询条件查看司机信息", notes = "根据查询条件查看司机信息", httpMethod = "POST")
    @RequestMapping(value = "/getDriverByTerm", method = RequestMethod.POST)
    public ResultVo<PageVo<DriverVo>> getDriverByTerm(@RequestBody SelectDriverDto dto){
        BasePageUtil.initPage(dto.getCurrentPage(),dto.getPageSize());
        PageInfo<DriverVo> pageInfo = driverService.getDriverByTerm(dto);
        return BaseResultUtil.getPageVo(ResultEnum.SUCCESS.getCode(),ResultEnum.SUCCESS.getMsg(),pageInfo);
    }

    @ApiOperation(value = "根据司机userId进行审核通过/拒绝", notes = "根据司机userId进行审核通过/拒绝", httpMethod = "POST")
    @RequestMapping(value = "/examineDriById/{id}", method = RequestMethod.POST)
    public ResultVo examineDriById(@PathVariable Long userId){
        boolean result = driverService.examineDriById(userId);
        return BaseResultUtil.getVo(ResultEnum.SUCCESS.getCode(),ResultEnum.SUCCESS.getMsg());
    }

}
