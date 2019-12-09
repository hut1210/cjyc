package com.cjyc.salesman.api.controller;

import com.cjyc.common.model.dto.salesman.task.TaskQueryConditionDto;
import com.cjyc.common.model.vo.ResultVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.MediaType;
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

    @ApiOperation(value = "提送车列表分页查询")
    @PostMapping("/getCarryPage")
    public ResultVo getCarryPage(@RequestBody @Validated TaskQueryConditionDto dto) {
       return null;
    }
}
