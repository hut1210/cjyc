package com.cjyc.web.api.controller;

import io.swagger.annotations.Api;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 任务
 * @author JPG
 */
@RestController
@RequestMapping("/task")
@Api(tags = "task",description = "任务接口,包含任务分配查询等")
public class TaskController {
}
