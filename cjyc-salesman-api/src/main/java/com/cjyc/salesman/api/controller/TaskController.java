package com.cjyc.salesman.api.controller;

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
@Api(tags = "运单-任务")
public class TaskController {

    @Resource
    private ICsTaskService csTaskService;
    @Resource
    private ICsAdminService csAdminService;


    /**
     * 提车完善信息
     * @author JPG
     */
    @ApiOperation(value = "提车完善信息")
    @PostMapping(value = "/replenish/info/update")
    public ResultVo replenishInfo(@RequestBody ReplenishInfoDto reqDto) {
        //验证用户
        Admin admin = csAdminService.validate(reqDto.getLoginId());
        reqDto.setLoginName(admin.getName());
        return csTaskService.replenishInfo(reqDto);
    }

    /**
     * 装车
     * @author JPG
     */
    @ApiOperation(value = "装车")
    @PostMapping(value = "/car/load")
    public ResultVo<ResultReasonVo> load(@Validated @RequestBody LoadTaskDto reqDto) {
        //验证用户
        Admin admin = csAdminService.validate(reqDto.getLoginId());
        reqDto.setLoginName(admin.getName());
        reqDto.setLoginPhone(admin.getPhone());
        reqDto.setLoginType(UserTypeEnum.ADMIN);
        return csTaskService.load(reqDto);
    }

    /**
     * 卸车
     * @author JPG
     */
    @ApiOperation(value = "卸车")
    @PostMapping(value = "/car/unload")
    public ResultVo<ResultReasonVo> unload(@RequestBody UnLoadTaskDto reqDto) {
        //验证用户
        Admin admin = csAdminService.validate(reqDto.getLoginId());
        reqDto.setLoginName(admin.getName());
        return csTaskService.unload(reqDto);
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
        return csTaskService.outStore(reqDto);
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
        return csTaskService.inStore(reqDto);
    }

    /**
     * 签收-业务员
     * @author JPG
     */
    @ApiOperation(value = "签收")
    @PostMapping(value = "/car/receipt")
    public ResultVo receipt(@RequestBody ReceiptTaskDto reqDto) {
        //验证用户
        Admin admin = csAdminService.validate(reqDto.getLoginId());
        reqDto.setLoginName(admin.getName());
        reqDto.setLoginPhone(admin.getPhone());
        return csTaskService.receipt(reqDto);
    }


}
