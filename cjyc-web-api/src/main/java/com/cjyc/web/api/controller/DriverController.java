package com.cjyc.web.api.controller;

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
    @GetMapping(value = "/list")
    public ResultVo<PageVo<DriverListVo>> getDriverList(@RequestBody DriverListDto reqDto) {
        return driverService.lineWaitDispatchCarCountList(reqDto);
    }

    @ApiOperation(value = "新增散户司机")
    @PostMapping(value = "/saveDriver")
    public ResultVo saveDriver(@Validated({ DriverDto.SaveDriverDto.class }) @RequestBody DriverDto dto){
        boolean result = driverService.saveDriver(dto);
        return result ? BaseResultUtil.getVo(ResultEnum.SUCCESS.getCode(),ResultEnum.SUCCESS.getMsg())
                : BaseResultUtil.getVo(ResultEnum.FAIL.getCode(),ResultEnum.FAIL.getMsg());
    }

    @ApiOperation(value = "根据查询条件查看司机信息")
    @PostMapping(value = "/getDriverByTerm")
    public ResultVo<PageVo<DriverVo>> getDriverByTerm(@RequestBody SelectDriverDto dto){
        BasePageUtil.initPage(dto.getCurrentPage(),dto.getPageSize());
        PageInfo<DriverVo> pageInfo = driverService.getDriverByTerm(dto);
        return BaseResultUtil.getPageVo(ResultEnum.SUCCESS.getCode(),ResultEnum.SUCCESS.getMsg(),pageInfo);
    }

    @ApiOperation(value = "根据司机userId进行审核通过/拒绝")
    @PostMapping(value = "/examineDriById/{id}")
    public ResultVo examineDriById(@PathVariable @ApiParam(value = "用户id",required = true) Long id,
                                   @PathVariable @ApiParam(value = "标志 3：审核通过 4：审核拒绝 5:冻结 6:解除",required = true) Integer flag){
        if(id == null || flag == null){
            BaseResultUtil.getVo(ResultEnum.MOBILE_PARAM_ERROR.getCode(),ResultEnum.MOBILE_PARAM_ERROR.getMsg());
        }
        boolean result = driverService.examineDriById(id,flag);
        return result ? BaseResultUtil.getVo(ResultEnum.SUCCESS.getCode(),ResultEnum.SUCCESS.getMsg())
                : BaseResultUtil.getVo(ResultEnum.FAIL.getCode(),ResultEnum.FAIL.getMsg());
    }

    @ApiOperation(value = "根据司机userId/Id查看司机信息")
    @PostMapping(value = "/getDriverById/{id}/{userId}")
    public ResultVo<ShowDriverVo> getDriverById(@PathVariable @ApiParam(value = "司机id",required = true) Long id,
                                                @PathVariable @ApiParam(value = "司机UserId",required = true) Long userId){
        if(id == null || userId == null){
            BaseResultUtil.getVo(ResultEnum.MOBILE_PARAM_ERROR.getCode(),ResultEnum.MOBILE_PARAM_ERROR.getMsg());
        }
        ShowDriverVo showDriverVo = driverService.getDriverById(id,userId);
        return BaseResultUtil.getVo(ResultEnum.SUCCESS.getCode(),ResultEnum.SUCCESS.getMsg(),showDriverVo);
    }

    @ApiOperation(value = "根据司机Id更新司机信息")
    @PostMapping(value = "/updDriverById/{id}")
    public ResultVo updateDriver(@RequestBody DriverDto dto){
        boolean result = driverService.updateDriver(dto);
        return result ? BaseResultUtil.getVo(ResultEnum.SUCCESS.getCode(),ResultEnum.SUCCESS.getMsg())
                : BaseResultUtil.getVo(ResultEnum.FAIL.getCode(),ResultEnum.FAIL.getMsg());
    }

}
