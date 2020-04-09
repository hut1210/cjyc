package com.cjyc.driver.api.controller;

import com.cjyc.common.model.dto.driver.BaseDriverDto;
import com.cjyc.common.model.dto.driver.task.DetailQueryDto;
import com.cjyc.common.model.dto.driver.task.DriverQueryDto;
import com.cjyc.common.model.dto.driver.task.NoFinishTaskQueryDto;
import com.cjyc.common.model.dto.driver.task.TaskQueryDto;
import com.cjyc.common.model.vo.PageVo;
import com.cjyc.common.model.vo.ResultVo;
import com.cjyc.common.model.vo.driver.task.TaskBillVo;
import com.cjyc.common.model.vo.driver.task.TaskDetailVo;
import com.cjyc.common.model.vo.driver.task.TaskDriverVo;
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
 * @Description 任务接口控制层
 * @Author Liu Xing Xiang
 * @Date 2019/11/19 10:20
 **/
@Api(tags = "任务")
@RestController
@RequestMapping("/task")
public class TaskController {
    @Autowired
    private ITaskService taskService;

    /**
     * 功能描述: 分页查询待分配任务列表
     * @author liuxingxiang
     * @date 2019/11/28
     * @param dto
     * @return com.cjyc.common.model.vo.ResultVo<com.cjyc.common.model.vo.PageVo<com.cjyc.common.model.vo.driver.task.TaskBillVo>>
     */
    @ApiOperation(value = "分页查询待分配任务列表", notes = "\t 请求接口为json格式")
    @PostMapping("/getWaitHandleTaskPage")
    public ResultVo<PageVo<TaskBillVo>> getWaitHandleTaskPage(@RequestBody @Validated BaseDriverDto dto) {
        return taskService.getWaitHandleTaskPage(dto);
    }

    /**
     * 功能描述: 分页查询提车,交车任务列表
     * @author liuxingxiang
     * @date 2019/11/28
     * @param dto
     * @return com.cjyc.common.model.vo.ResultVo<com.cjyc.common.model.vo.PageVo<com.cjyc.common.model.vo.driver.task.TaskBillVo>>
     */
    @ApiOperation(value = "分页查询提车,交车任务列表", notes = "\t 请求接口为json格式")
    @PostMapping("/getNoFinishTaskPage")
    public ResultVo<PageVo<TaskBillVo>> getNoFinishTaskPage(@RequestBody @Validated NoFinishTaskQueryDto dto) {
        return taskService.getNoFinishTaskPage(dto);
    }

    /**
     * 功能描述: 分页查询已交付任务列表
     * @author liuxingxiang
     * @date 2019/11/28
     * @param dto
     * @return com.cjyc.common.model.vo.ResultVo<com.cjyc.common.model.vo.PageVo<com.cjyc.common.model.vo.driver.task.TaskBillVo>>
     */
    @ApiOperation(value = "分页查询已交付任务列表", notes = "\t 请求接口为json格式")
    @PostMapping("/getFinishTaskPage")
    public ResultVo<PageVo<TaskBillVo>> getFinishTaskPage(@RequestBody @Validated TaskQueryDto dto) {
        return taskService.getFinishTaskPage(dto);
    }

    /**
     * 功能描述: 查询待分配任务明细
     * @author liuxingxiang
     * @date 2019/11/28
     * @param dto
     * @return com.cjyc.common.model.vo.ResultVo<com.cjyc.common.model.vo.driver.task.TaskDetailVo>
     */
    @ApiOperation(value = "查询待分配任务明细", notes = "\t 请求接口为json格式")
    @PostMapping("/getNoHandleDetail")
    public ResultVo<TaskDetailVo> getNoHandleDetail(@RequestBody @Validated({DetailQueryDto.GetNoHandleDetail.class}) DetailQueryDto dto) {
        return taskService.getNoHandleDetail(dto);
    }

    /**
     * 功能描述: 查询已分配任务明细
     * @author liuxingxiang
     * @date 2019/11/28
     * @param dto
     * @return com.cjyc.common.model.vo.ResultVo<com.cjyc.common.model.vo.driver.task.TaskDetailVo>
     */
    @ApiOperation(value = "查询已分配任务明细", notes = "\t 请求接口为json格式")
    @PostMapping("/getHistoryDetail")
    public ResultVo<TaskDetailVo> getHistoryDetail(@RequestBody @Validated({DetailQueryDto.GetHistoryDetail.class}) DetailQueryDto dto) {
        return taskService.getHistoryDetail(dto);
    }

    /**
     * 功能描述: 分页查询司机列表
     * @author liuxingxiang
     * @date 2019/11/28
     * @param dto
     * @return com.cjyc.common.model.vo.ResultVo<com.cjyc.common.model.vo.PageVo<com.cjyc.common.model.vo.driver.task.TaskDriverVo>>
     */
    @ApiOperation(value = "分页查询司机列表", notes = "\t 请求接口为json格式")
    @PostMapping("/getDriverPage")
    public ResultVo<PageVo<TaskDriverVo>> getDriverPage(@RequestBody @Validated DriverQueryDto dto) {
        return taskService.getDriverPage(dto);
    }

    /**
     * 功能描述: 分页查询历史记录列表
     * @author liuxingxiang
     * @date 2019/11/28
     * @param dto
     * @return com.cjyc.common.model.vo.ResultVo<com.cjyc.common.model.vo.PageVo<com.cjyc.common.model.vo.driver.task.TaskBillVo>>
     */
    @ApiOperation(value = "分页查询历史记录列表", notes = "\t 请求接口为json格式")
    @PostMapping("/getHistoryTaskPage")
    public ResultVo<PageVo<TaskBillVo>> getHistoryTaskPage(@RequestBody @Validated TaskQueryDto dto) {
        return taskService.getHistoryTaskPage(dto);
    }
}
