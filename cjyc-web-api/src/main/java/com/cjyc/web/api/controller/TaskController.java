package com.cjyc.web.api.controller;

import com.cjyc.common.model.dto.web.task.*;
import com.cjyc.common.model.dto.web.waybill.TrunkMainListWaybillDto;
import com.cjyc.common.model.entity.Admin;
import com.cjyc.common.model.entity.Driver;
import com.cjyc.common.model.enums.AdminStateEnum;
import com.cjyc.common.model.util.BaseResultUtil;
import com.cjyc.common.model.vo.PageVo;
import com.cjyc.common.model.vo.ResultVo;
import com.cjyc.common.model.vo.web.waybill.TrunkMainListWaybillVo;
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
     * @author JPG
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
     * @author JPG
     */
    @ApiOperation(value = "装车")
    @PostMapping(value = "/car/load")
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
     * @author JPG
     */
    @ApiOperation(value = "卸车")
    @PostMapping(value = "/car/unload")
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
     * @author JPG
     */
    @ApiOperation(value = "确认入库")
    @PostMapping(value = "/car/in/store")
    public ResultVo inStore(@Validated @RequestBody InStoreTaskDto reqDto) {
        //验证用户
        Admin admin = adminService.getByUserId(reqDto.getUserId());
        if (admin == null || admin.getState() != AdminStateEnum.CHECKED.code) {
            return BaseResultUtil.fail("当前业务员，不在职");
        }
        reqDto.setUserName(admin.getName());
        return taskService.inStore(reqDto);
    }

    /**
     * 确认出库
     * @author JPG
     */
    @ApiOperation(value = "确认出库")
    @PostMapping(value = "/car/out/store")
    public ResultVo outStore(@RequestBody OutStoreTaskDto reqDto) {
        //验证用户
        Admin admin = adminService.getByUserId(reqDto.getUserId());
        if (admin == null || admin.getState() != AdminStateEnum.CHECKED.code) {
            return BaseResultUtil.fail("当前业务员，不在职");
        }
        reqDto.setUserName(admin.getName());
        return taskService.outStore(reqDto);
    }



    /**
     * 交接-司机
     * @author JPG
     */
    @ApiOperation(value = "签收")
    @PostMapping(value = "/car/driver/sign")
    public ResultVo driverSign(@RequestBody SignTaskDto reqDto) {
        //验证用户
        Admin admin = adminService.getByUserId(reqDto.getUserId());
        if (admin == null || admin.getState() != AdminStateEnum.CHECKED.code) {
            return BaseResultUtil.fail("当前业务员，不在职");
        }
        reqDto.setUserName(admin.getName());
        return taskService.sign(reqDto);
    }




}
