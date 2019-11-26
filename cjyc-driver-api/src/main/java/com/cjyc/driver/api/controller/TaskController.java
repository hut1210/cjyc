package com.cjyc.driver.api.controller;

import com.cjyc.common.model.dto.driver.BaseDriverDto;
import com.cjyc.common.model.dto.driver.task.*;
import com.cjyc.common.model.vo.PageVo;
import com.cjyc.common.model.vo.ResultVo;
import com.cjyc.common.model.vo.driver.task.TaskDetailVo;
import com.cjyc.common.model.vo.driver.task.TaskDriverVo;
import com.cjyc.common.model.vo.driver.task.WaybillTaskVo;
import com.cjyc.common.system.service.ICsTaskService;
import com.cjyc.driver.api.service.ITaskService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Description 已交付任务接口控制层
 * @Author Liu Xing Xiang
 * @Date 2019/11/19 10:20
 **/
@Api(tags = "任务")
@RestController
@RequestMapping("/task")
public class TaskController {
    @Autowired
    private ITaskService taskService;

    @ApiOperation(value = "分页查询待分配任务列表", notes = "\t 请求接口为json格式")
    @PostMapping("/getWaitHandleTaskPage")
    public ResultVo<PageVo<WaybillTaskVo>> getWaitHandleTaskPage(@RequestBody @Validated BaseDriverDto dto) {
        return taskService.getWaitHandleTaskPage(dto);
    }

    @ApiOperation(value = "分页查询提车，交车任务列表", notes = "\t 请求接口为json格式")
    @PostMapping("/getNoFinishTaskPage")
    public ResultVo<PageVo<WaybillTaskVo>> getNoFinishTaskPage(@RequestBody @Validated NoFinishTaskQueryDto dto) {
        return taskService.getNoFinishTaskPage(dto);
    }

    @ApiOperation(value = "分页查询已交付任务列表", notes = "\t 请求接口为json格式")
    @PostMapping("/getFinishTaskPage")
    public ResultVo<PageVo<WaybillTaskVo>> getFinishTaskPage(@RequestBody @Validated TaskQueryDto dto) {
        return taskService.getFinishTaskPage(dto);
    }

    @ApiOperation(value = "查询待分配任务明细", notes = "\t 请求接口为json格式")
    @PostMapping("/getNoHandleDetail")
    public ResultVo<TaskDetailVo> getNoHandleDetail(@RequestBody @Validated({DetailQueryDto.GetNoHandleDetail.class}) DetailQueryDto dto) {
        return taskService.getDetail(dto);
    }

    @ApiOperation(value = "查询已分配任务明细", notes = "\t 请求接口为json格式")
    @PostMapping("/getHistoryDetail")
    public ResultVo<TaskDetailVo> getHistoryDetail(@RequestBody @Validated({DetailQueryDto.GetHistoryDetail.class}) DetailQueryDto dto) {
        return taskService.getDetail(dto);
    }

    @ApiOperation(value = "分页查询司机列表", notes = "\t 请求接口为json格式")
    @PostMapping("/getDriverPage")
    public ResultVo<PageVo<TaskDriverVo>> getDriverPage(@RequestBody @Validated DriverQueryDto dto) {
        return taskService.getDriverPage(dto);
    }

    @ApiOperation(value = "分页查询历史记录列表", notes = "\t 请求接口为json格式")
    @PostMapping("/getHistoryTaskPage")
    public ResultVo<PageVo<WaybillTaskVo>> getHistoryTaskPage(@RequestBody @Validated TaskQueryDto dto) {
        return taskService.getHistoryTaskPage(dto);
    }


}
