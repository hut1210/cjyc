package com.cjyc.web.api.controller;

import com.cjyc.common.model.dto.web.task.*;
import com.cjyc.common.model.entity.Driver;
import com.cjyc.common.model.enums.ResultEnum;
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
import com.cjyc.common.system.service.ICsTaskService;
import com.cjyc.web.api.service.ITaskService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.List;

/**
 * 任务
 *
 * @author JPG
 */
@RestController
@RequestMapping("/task")
@Api(tags = "运单-任务")
public class TaskController {

    @Resource
    private ITaskService taskService;
    @Resource
    private ICsTaskService csTaskService;
    @Resource
    private ICsAdminService csAdminService;
    @Resource
    private ICsDriverService csDriverService;

    /**
     * 功能描述: 查询任务分页列表
     *
     * @param dto
     * @return com.cjyc.common.model.vo.ResultVo
     * @author liuxingxiang
     * @date 2019/12/20
     */
    @ApiOperation(value = "查询任务分页列表(后加功能)")
    @PostMapping(value = "/getTaskPage")
    public ResultVo<PageVo<TaskPageVo>> getTaskPage(@RequestBody @Validated TaskPageDto dto) {
        return taskService.getTaskPage(dto);
    }

    /**
     * 分配任务
     *
     * @author JPG
     */
    @ApiOperation(value = "分配任务")
    @PostMapping(value = "/allot")
    public ResultVo allot(@Valid @RequestBody AllotTaskDto reqDto) {
        //验证用户
        Driver driver = csDriverService.getById(reqDto.getLoginId(), true);
        if (driver == null) {
            return BaseResultUtil.fail("当前用户不存在");
        }
        reqDto.setLoginName(driver.getName());
        return csTaskService.allot(reqDto);
    }


    /**
     * 确认出库
     *
     * @author JPG
     */
    @ApiOperation(value = "确认出库")
    @PostMapping(value = "/car/out/store")
    public ResultVo<ResultReasonVo> outStore(@RequestBody BaseTaskDto reqDto) {
        //验证用户
        ResultVo<BaseTaskDto> resVo = csAdminService.validateEnabled(reqDto);
        if(ResultEnum.SUCCESS.getCode() != resVo.getCode()){
            return BaseResultUtil.fail(resVo.getMsg());
        }
        return csTaskService.outStore(resVo.getData());
    }


    /**
     * 确认入库
     *
     * @author JPG
     */
    @ApiOperation(value = "确认入库")
    @PostMapping(value = "/car/in/store")
    public ResultVo inStore(@Validated @RequestBody BaseTaskDto reqDto) {
        //验证用户
        ResultVo<BaseTaskDto> resVo = csAdminService.validateEnabled(reqDto);
        if(ResultEnum.SUCCESS.getCode() != resVo.getCode()){
            return BaseResultUtil.fail(resVo.getMsg());
        }
        return csTaskService.inStore(resVo.getData());
    }

    /**
     * 签收-业务员
     *
     * @author JPG
     */
    @ApiOperation(value = "签收")
    @PostMapping(value = "/car/receipt")
    public ResultVo receipt(@RequestBody BaseTaskDto reqDto) {
        ResultVo<BaseTaskDto> resVo = csAdminService.validateEnabled(reqDto);
        if(ResultEnum.SUCCESS.getCode() != resVo.getCode()){
            return BaseResultUtil.fail(resVo.getMsg());
        }
        return csTaskService.receipt(resVo.getData());
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
    //@PostMapping(value = "/cr/task/list")
    public ResultVo<PageVo<CrTaskVo>> crAllottedList(@RequestBody CrTaskDto reqDto) {
        return taskService.crTaskList(reqDto);
    }


    /************************************韵车集成改版 st***********************************/

    /**
     * 我的运单-承运商
     */
    @ApiOperation(value = "我的运单-承运商_改版")
    //@PostMapping(value = "/cr/task/listNew")
    @PostMapping(value = "/cr/task/list")
    public ResultVo<PageVo<CrTaskVo>> crAllottedListNew(@RequestBody CrTaskDto reqDto) {
        return taskService.crTaskListNew(reqDto);
    }

    @ApiOperation(value = "我的公司-已指派导出Excel", notes = "\t 请求接口为/task/exportCrAllottedListExcel?waybillNo=主运单编号&taskNo=运单号&" +
            "driverName=司机名称&driverPhone=司机电话&vehiclePlateNo=车牌号&carrierId=承运商id")
    @GetMapping("/exportCrAllottedListExcel")
    public void exportCrAllottedListExcel(HttpServletRequest request, HttpServletResponse response) {
        taskService.exportCrAllottedListExcel(request, response);
    }

}
