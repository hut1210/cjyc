package com.cjyc.web.api.controller;

import com.cjyc.common.model.dto.web.task.*;
import com.cjyc.common.model.entity.Admin;
import com.cjyc.common.model.entity.Driver;
import com.cjyc.common.model.enums.UserTypeEnum;
import com.cjyc.common.model.util.BaseResultUtil;
import com.cjyc.common.model.vo.PageVo;
import com.cjyc.common.model.vo.ResultReasonVo;
import com.cjyc.common.model.vo.ResultVo;
import com.cjyc.common.model.vo.web.task.CrTaskVo;
import com.cjyc.common.model.vo.web.task.ListByWaybillTaskVo;
import com.cjyc.common.model.vo.web.task.TaskPageVo;
import com.cjyc.common.model.vo.web.task.TaskVo;
import com.cjyc.common.system.service.ICsAdminService;
import com.cjyc.common.system.service.ICsDriverService;
import com.cjyc.web.api.service.ITaskService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * 任务
 * @author JPG
 */
@RestController
@RequestMapping("/task")
@Api(tags = "运单-任务")
public class TaskController {

    @Resource
    private ITaskService taskService;
    @Resource
    private ICsAdminService csAdminService;
    @Resource
    private ICsDriverService csDriverService;

    /**
     * 功能描述: 查询任务分页列表
     * @author liuxingxiang
     * @date 2019/12/20
     * @param dto
     * @return com.cjyc.common.model.vo.ResultVo
     */
    @ApiOperation(value = "查询任务分页列表(后加功能)")
    @PostMapping(value = "/getTaskPage")
    public ResultVo<PageVo<TaskPageVo>> getTaskPage(@RequestBody @Validated TaskPageDto dto) {
        return taskService.getTaskPage(dto);
    }

    /**
     * 分配任务
     * @author JPG
     */
    @ApiOperation(value = "分配任务")
    @PostMapping(value = "/allot")
    public ResultVo allot(@RequestBody AllotTaskDto reqDto) {
        //验证用户
        Driver driver = csDriverService.getById(reqDto.getLoginId(), true);
        if (driver == null) {
            return BaseResultUtil.fail("当前用户不存在");
        }
        reqDto.setLoginName(driver.getName());
        return taskService.allot(reqDto);
    }


    /**
     * 确认出库
     * @author JPG
     */
    @ApiOperation(value = "确认出库")
    @PostMapping(value = "/car/out/store")
    public ResultVo<ResultReasonVo> outStore(@RequestBody OutStoreTaskDto reqDto) {
        //验证用户
        Admin admin = csAdminService.validate(reqDto.getLoginId());
        reqDto.setLoginName(admin.getName());
        reqDto.setLoginPhone(admin.getPhone());
        reqDto.setLoginType(UserTypeEnum.ADMIN);
        return taskService.outStore(reqDto);
    }


    /**
     * 确认入库
     * @author JPG
     */
    @ApiOperation(value = "确认入库")
    @PostMapping(value = "/car/in/store")
    public ResultVo inStore(@Validated @RequestBody InStoreTaskDto reqDto) {
        //验证用户
        Admin admin = csAdminService.validate(reqDto.getLoginId());
        reqDto.setLoginName(admin.getName());
        reqDto.setLoginPhone(admin.getPhone());
        reqDto.setLoginType(UserTypeEnum.ADMIN);
        return taskService.inStore(reqDto);
    }

    /**
     * 签收-业务员
     * @author JPG
     */
    @ApiOperation(value = "签收")
    @PostMapping(value = "/car/receipt")
    public ResultVo receipt(@RequestBody ReceiptTaskDto reqDto) {
        Admin admin = csAdminService.validate(reqDto.getLoginId());
        reqDto.setLoginName(admin.getName());
        reqDto.setLoginPhone(admin.getPhone());
        return taskService.receipt(reqDto);
    }

    /**
     * 查询子运单（任务）列表
     */
    @ApiOperation(value = "查询子运单（任务）列表")
    @PostMapping(value = "/list/{waybillId}")
    public ResultVo<List<ListByWaybillTaskVo>> listByWaybillId(@PathVariable Long waybillId) {
        return taskService.getlistByWaybillId(waybillId);
    }

    /**
     * 查询子运单（任务）列表
     */
    @ApiOperation(value = "查询子运单（任务）列表")
    @PostMapping(value = "/get/{taskId}")
    public ResultVo<TaskVo> get(@PathVariable Long taskId) {
        return taskService.get(taskId);
    }

    /**----承运商模块------------------------------------------------------------------------------------------------------------*/


    /**
     * 我的运单-承运商
     */
    @ApiOperation(value = "我的运单-承运商")
    @PostMapping(value = "/cr/task/list")
    public ResultVo<PageVo<CrTaskVo>> crAllottedList(@RequestBody CrTaskDto reqDto) {
        return taskService.crTaskList(reqDto);
    }



}
