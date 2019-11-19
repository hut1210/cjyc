package com.cjyc.driver.api.controller;

import com.cjyc.common.model.dto.driver.BaseConditionDto;
import com.cjyc.common.model.vo.ResultVo;
import com.cjyc.common.model.vo.driver.task.FinishTaskVo;
import com.cjyc.driver.api.service.ITaskService;
import com.github.pagehelper.PageInfo;
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
@Api(tags = "已交付")
@RestController
@RequestMapping("/task")
public class TaskController {
    @Autowired
    private ITaskService taskService;

    @ApiOperation(value = "分页查询已交付运单列表", notes = "\t 请求接口为json格式")
    @PostMapping("/getPage")
    public ResultVo<PageInfo<FinishTaskVo>> getPage(@RequestBody @Validated BaseConditionDto dto) {
        return taskService.getPage(dto);
    }

}
