package com.cjyc.salesman.api.controller;

import com.cjyc.common.model.dto.driver.task.DetailQueryDto;
import com.cjyc.common.model.dto.driver.task.ReplenishInfoDto;
import com.cjyc.common.model.dto.salesman.task.OutAndInStorageQueryDto;
import com.cjyc.common.model.dto.salesman.task.TaskWaybillQueryDto;
import com.cjyc.common.model.dto.web.task.*;
import com.cjyc.common.model.entity.Admin;
import com.cjyc.common.model.entity.Driver;
import com.cjyc.common.model.enums.UserTypeEnum;
import com.cjyc.common.model.util.BaseResultUtil;
import com.cjyc.common.model.vo.PageVo;
import com.cjyc.common.model.vo.ResultReasonVo;
import com.cjyc.common.model.vo.ResultVo;
import com.cjyc.common.model.vo.driver.task.TaskDetailVo;
import com.cjyc.common.model.vo.salesman.task.TaskWaybillVo;
import com.cjyc.common.system.service.ICsAdminService;
import com.cjyc.common.system.service.ICsTaskService;
import com.cjyc.salesman.api.service.ITaskService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.text.MessageFormat;

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
    @Resource
    private ICsTaskService csTaskService;
    @Resource
    private ICsAdminService csAdminService;

    /**
     * 提车装车并完善信息
     * @author JPG
     */
    @ApiOperation(value = "同城装车")
    @PostMapping(value = "/load/for/local")
    public ResultVo<ResultReasonVo> loadForLocal(@RequestBody ReplenishInfoDto reqDto) {
        //验证用户
        Admin admin = csAdminService.validate(reqDto.getLoginId());
        reqDto.setLoginName(admin.getName());
        reqDto.setLoginType(UserTypeEnum.ADMIN);
        return csTaskService.loadForLocal(reqDto);
    }
    /**
     * 提车完善信息
     * @author JPG
     */
    @ApiOperation(value = "提车完善信息")
    @PostMapping(value = "/replenish/info/update")
    public ResultVo replenishInfo(@Validated @RequestBody ReplenishInfoDto reqDto) {
        //验证用户
        Admin admin = csAdminService.validate(reqDto.getLoginId());
        reqDto.setLoginName(admin.getName());
        reqDto.setLoginType(UserTypeEnum.ADMIN);
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
    public ResultVo<ResultReasonVo> unload(@Validated @RequestBody UnLoadTaskDto reqDto) {
        //验证用户
        Admin admin = csAdminService.validate(reqDto.getLoginId());
        reqDto.setLoginName(admin.getName());
        reqDto.setLoginPhone(admin.getPhone());
        reqDto.setLoginType(UserTypeEnum.ADMIN);
        return csTaskService.unload(reqDto);
    }

    /**
     * 确认出库
     * @author JPG
     */
    @ApiOperation(value = "确认出库")
    @PostMapping(value = "/car/out/store")
    public ResultVo<ResultReasonVo> outStore(@Validated @RequestBody OutStoreTaskDto reqDto) {
        //验证用户
        Admin admin = csAdminService.validate(reqDto.getLoginId());
        reqDto.setLoginName(admin.getName());
        reqDto.setLoginPhone(admin.getPhone());
        reqDto.setLoginType(UserTypeEnum.ADMIN);
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
        reqDto.setLoginPhone(admin.getPhone());
        reqDto.setLoginType(UserTypeEnum.ADMIN);
        return csTaskService.inStore(reqDto);
    }
    /**
     * 提车完善信息
     * @author JPG
     */
    @ApiOperation(value = "入库完善信息")
    @PostMapping(value = "/unload/replenish/info")
    public ResultVo unloadReplenishInfo(@Valid @RequestBody ReplenishInfoDto reqDto) {
        //验证用户
        Admin admin = csAdminService.validate(reqDto.getLoginId());
        reqDto.setLoginName(admin.getName());
        reqDto.setType(2);
        return csTaskService.replenishInfo(reqDto);
    }
    /**
     * 确认入库
     * @author JPG
     */
    @ApiOperation(value = "确认入库")
    @PostMapping(value = "/car/in/store/For/local")
    public ResultVo inStoreForLocal(@Validated @RequestBody ReplenishInfoDto reqDto) {
        //验证用户
        Admin admin = csAdminService.validate(reqDto.getLoginId());
        reqDto.setLoginName(admin.getName());
        reqDto.setLoginPhone(admin.getPhone());
        reqDto.setLoginType(UserTypeEnum.ADMIN);
        reqDto.setType(2);
        return csTaskService.inStoreForLocal(reqDto);
    }


    /**
     * 签收-业务员
     * @author JPG
     */
    @ApiOperation(value = "签收")
    @PostMapping(value = "/car/receipt")
    public ResultVo receipt(@Validated @RequestBody ReceiptTaskDto reqDto) {
        //验证用户
        Admin admin = csAdminService.validate(reqDto.getLoginId());
        reqDto.setLoginName(admin.getName());
        reqDto.setLoginPhone(admin.getPhone());
        return csTaskService.receipt(reqDto);
    }

    /**
     * 功能描述: 查询提送车；提送车历史记录列表
     * @author liuxingxiang
     * @date 2019/12/9
     * @param dto
     * @return com.cjyc.common.model.vo.ResultVo<com.cjyc.common.model.vo.PageVo<com.cjyc.common.model.vo.salesman.task.TaskWaybillVo>>
     */
    @ApiOperation(value = "分页查询提送车；提送车历史记录列表")
    @PostMapping("/getCarryPage")
    public ResultVo<PageVo<TaskWaybillVo>> getCarryPage(@RequestBody @Validated TaskWaybillQueryDto dto) {
       return taskService.getCarryPage(dto);
    }

    /**
     * 功能描述: 查询提送车任务详情
     * @author liuxingxiang
     * @date 2019/12/9
     * @param dto
     * @return com.cjyc.common.model.vo.ResultVo<com.cjyc.common.model.vo.driver.task.TaskDetailVo>
     */
    @ApiOperation(value = "查询提送车,提送车历史记录任务详情；查询出入库,出入库历史记录任务详情")
    @PostMapping("/getCarryDetail")
    public ResultVo<TaskDetailVo> getCarryDetail(@RequestBody @Validated({DetailQueryDto.GetSalesmanWaybillDetail.class}) DetailQueryDto dto) {
        return taskService.getCarryDetail(dto);
    }

    /**
     * 功能描述: 查询出入库,出入库历史记录列表分页
     * @author liuxingxiang
     * @date 2019/12/9
     * @param dto
     * @return com.cjyc.common.model.vo.ResultVo<com.cjyc.common.model.vo.PageVo<com.cjyc.common.model.vo.salesman.task.TaskWaybillVo>>
     */
    @ApiOperation(value = "分页查询出入库；出入库历史记录列表")
    @PostMapping("/getOutAndInStoragePage")
    public ResultVo<PageVo<TaskWaybillVo>> getOutAndInStoragePage(@RequestBody @Validated OutAndInStorageQueryDto dto) {
        return taskService.getOutAndInStoragePage(dto);
    }


}
