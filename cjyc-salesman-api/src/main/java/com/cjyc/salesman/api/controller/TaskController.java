package com.cjyc.salesman.api.controller;

<<<<<<< HEAD
import com.cjyc.common.model.dto.driver.task.ReplenishInfoDto;
import com.cjyc.common.model.dto.web.task.*;
import com.cjyc.common.model.entity.Admin;
import com.cjyc.common.model.enums.UserTypeEnum;
import com.cjyc.common.model.vo.ResultReasonVo;
import com.cjyc.common.model.vo.ResultVo;
import com.cjyc.common.system.service.ICsAdminService;
import com.cjyc.common.system.service.ICsTaskService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
=======
import com.cjyc.common.model.dto.driver.task.DetailQueryDto;
import com.cjyc.common.model.dto.salesman.task.TaskQueryConditionDto;
import com.cjyc.common.model.vo.PageVo;
import com.cjyc.common.model.vo.ResultVo;
import com.cjyc.common.model.vo.driver.task.TaskDetailVo;
import com.cjyc.common.model.vo.salesman.task.TaskBillVo;
import com.cjyc.salesman.api.service.ITaskService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
>>>>>>> origin/develop
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Description 任务控制层
 * @Author Liu Xing Xiang
 * @Date 2019/12/9 10:35
 **/
@Api(tags = "任务")
@RestController
@RequestMapping(value = "/task", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
public class TaskController {
    @Autowired
    private ITaskService taskService;

    /**
     * 功能描述: 查询提送车列表分页
     * @author liuxingxiang
     * @date 2019/12/9
     * @param dto
     * @return com.cjyc.common.model.vo.ResultVo<com.cjyc.common.model.vo.PageVo<com.cjyc.common.model.vo.salesman.task.TaskBillVo>>
     */
    @ApiOperation(value = "查询提送车列表分页")
    @PostMapping("/getCarryPage")
    public ResultVo<PageVo<TaskBillVo>> getCarryPage(@RequestBody @Validated TaskQueryConditionDto dto) {
       return taskService.getCarryPage(dto);
    }

    /**
     * 功能描述: 查询提送车任务详情
     * @author liuxingxiang
     * @date 2019/12/9
     * @param dto
     * @return com.cjyc.common.model.vo.ResultVo<com.cjyc.common.model.vo.driver.task.TaskDetailVo>
     */
    @ApiOperation(value = "查询提送车任务详情")
    @PostMapping("/getCarryDetail")
    public ResultVo<TaskDetailVo> getCarryDetail(@RequestBody @Validated({DetailQueryDto.GetHistoryDetail.class}) DetailQueryDto dto) {
        return taskService.getCarryDetail(dto);
    }


}
