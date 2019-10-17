package com.cjyc.web.api.controller;

import com.cjyc.common.model.dto.web.user.DriverListDto;
import com.cjyc.common.model.vo.PageVo;
import com.cjyc.common.model.vo.ResultVo;
import com.cjyc.common.model.vo.web.user.DriverListVo;
import com.cjyc.web.api.service.IBizScopeService;
import com.cjyc.web.api.service.IDriverService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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


}
