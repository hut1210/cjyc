package com.cjyc.web.api.controller;

import com.cjyc.common.model.dao.ITaskDao;
import com.cjyc.common.model.dto.web.task.AllotTaskDto;
import com.cjyc.common.model.dto.web.waybill.HistoryListWaybillDto;
import com.cjyc.common.model.entity.Admin;
import com.cjyc.common.model.enums.AdminStateEnum;
import com.cjyc.common.model.util.BaseResultUtil;
import com.cjyc.common.model.vo.ResultVo;
import com.cjyc.common.model.vo.web.waybill.HistoryListWaybillVo;
import com.cjyc.web.api.service.IAdminService;
import com.cjyc.web.api.service.ITaskService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

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


    /**
     * 分配任务
     */
    @ApiOperation(value = "分配任务")
    @PostMapping(value = "/allot")
    public ResultVo allot(@RequestBody AllotTaskDto reqDto) {
        //验证用户
        Admin admin = adminService.getByUserId(reqDto.getUserId());
        if (admin == null || admin.getState() != AdminStateEnum.CHECKED.code) {
            return BaseResultUtil.fail("当前用户不能登录");
        }
        reqDto.setUserName(admin.getName());
        return taskService.allot(reqDto);
    }





}
