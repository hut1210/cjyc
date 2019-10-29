package com.cjyc.web.api.controller;

import com.cjyc.common.model.dto.web.task.AllotTaskDto;
import com.cjyc.common.model.dto.web.task.LoadTaskDto;
import com.cjyc.common.model.dto.web.task.UnLoadTaskDto;
import com.cjyc.common.model.entity.Admin;
import com.cjyc.common.model.entity.Driver;
import com.cjyc.common.model.enums.AdminStateEnum;
import com.cjyc.common.model.util.BaseResultUtil;
import com.cjyc.common.model.vo.ResultVo;
import com.cjyc.web.api.service.IAdminService;
import com.cjyc.web.api.service.IDriverService;
import com.cjyc.web.api.service.ITaskService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * 任务
 * @author JPG
 */
@RestController
@RequestMapping("/task")
@Api(tags = "任务")
public class TaskController {

    @Resource
    private ITaskService taskService;
    @Resource
    private IAdminService adminService;
    @Resource
    private IDriverService driverService;


    /**
     * 分配任务
     */
    @ApiOperation(value = "分配任务")
    @PostMapping(value = "/allot")
    public ResultVo allot(@RequestBody AllotTaskDto reqDto) {
        //验证用户
        Driver driver = driverService.getByUserId(reqDto.getUserId());
        if (driver == null || driver.getState() != AdminStateEnum.CHECKED.code) {
            return BaseResultUtil.fail("当前用户不能登录");
        }
        reqDto.setUserName(driver.getName());
        return taskService.allot(reqDto);
    }



    /**
     * 装车
     */
    @ApiOperation(value = "装车")
    @PostMapping(value = "/load")
    public ResultVo load(@Validated @RequestBody LoadTaskDto reqDto) {
        //验证用户
        Driver driver = driverService.getByUserId(reqDto.getUserId());
        if (driver == null || driver.getState() != AdminStateEnum.CHECKED.code) {
            return BaseResultUtil.fail("当前用户，不能执行操作");
        }
        reqDto.setUserName(driver.getName());
        return taskService.load(reqDto);
    }


    /**
     * 卸车
     */
    @ApiOperation(value = "卸车")
    @PostMapping(value = "/unload")
    public ResultVo unload(@RequestBody UnLoadTaskDto reqDto) {
        //验证用户
        Driver driver = driverService.getByUserId(reqDto.getUserId());
        if (driver == null || driver.getState() != AdminStateEnum.CHECKED.code) {
            return BaseResultUtil.fail("当前用户，不能执行操作");
        }
        reqDto.setUserName(driver.getName());
        return taskService.unload(reqDto);
    }


    /**
     * 确认入库
     */
    @ApiOperation(value = "确认入库")
    @PostMapping(value = "/inhour")
    public ResultVo uno(@RequestBody UnLoadTaskDto reqDto) {
        //验证用户
        Driver driver = driverService.getByUserId(reqDto.getUserId());
        if (driver == null || driver.getState() != AdminStateEnum.CHECKED.code) {
            return BaseResultUtil.fail("当前用户，不能执行操作");
        }
        reqDto.setUserName(driver.getName());
        return taskService.unload(reqDto);
    }








}
