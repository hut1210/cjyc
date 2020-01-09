package com.cjyc.driver.api.controller;

import com.cjyc.common.model.dto.driver.task.ReplenishInfoDto;
import com.cjyc.common.model.dto.web.task.AllotTaskDto;
import com.cjyc.common.model.dto.web.task.LoadTaskDto;
import com.cjyc.common.model.dto.web.task.ReceiptTaskDto;
import com.cjyc.common.model.dto.web.task.UnLoadTaskDto;
import com.cjyc.common.model.entity.Driver;
import com.cjyc.common.model.enums.ClientEnum;
import com.cjyc.common.model.enums.UserTypeEnum;
import com.cjyc.common.model.util.BaseResultUtil;
import com.cjyc.common.model.vo.ResultReasonVo;
import com.cjyc.common.model.vo.ResultVo;
import com.cjyc.common.system.service.ICsDriverService;
import com.cjyc.common.system.service.ICsTaskService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.validation.Valid;

@RestController
@Api(tags = "运单")
@RequestMapping(value = "/waybill", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
public class WaybillController {
    @Resource
    private ICsTaskService csTaskService;
    @Resource
    private ICsDriverService csDriverService;


    /**
     * 提车完善信息
     * @author JPG
     */
    @ApiOperation(value = "提车完善信息")
    @PostMapping(value = "/replenish/info/update")
    public ResultVo replenishInfo(@Valid @RequestBody ReplenishInfoDto reqDto) {
        //验证用户
        Driver driver = csDriverService.getById(reqDto.getLoginId(), true);
        if (driver == null) {
            return BaseResultUtil.fail("当前用户不存在");
        }
        reqDto.setLoginName(driver.getName());
        return csTaskService.replenishInfo(reqDto);
    }

    /**
     * 提车装车并完善信息
     * @author JPG
     */
    @ApiOperation(value = "同城装车")
    @PostMapping(value = "/load/for/local")
    public ResultVo<ResultReasonVo> loadForLocal(@RequestBody ReplenishInfoDto reqDto) {
        //验证用户
        Driver driver = csDriverService.validate(reqDto.getLoginId());
        reqDto.setLoginName(driver.getName());
        reqDto.setLoginType(UserTypeEnum.DRIVER);
        return csTaskService.loadForLocal(reqDto);
    }
    /**
     * 分配任务
     * @author JPG
     */
    @ApiOperation(value = "分配任务")
    @PostMapping(value = "/allot")
    public ResultVo allot(@RequestBody AllotTaskDto reqDto) {
        //验证用户
        Driver driver = csDriverService.getById(reqDto.getLoginId(), true);
        if (driver == null) {
            return BaseResultUtil.fail("当前用户不存在");
        }
        reqDto.setLoginName(driver.getName());
        return csTaskService.allot(reqDto);
    }

    /**
     * 装车
     * @author JPG
     */
    @ApiOperation(value = "装车")
    @PostMapping(value = "/car/load")
    public ResultVo<ResultReasonVo> load(@Validated @RequestBody LoadTaskDto reqDto) {
        //验证用户
        Driver driver = csDriverService.validate(reqDto.getLoginId());
        reqDto.setLoginName(driver.getName());
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
        Driver driver = csDriverService.validate(reqDto.getLoginId());
        reqDto.setLoginName(driver.getName());
        return csTaskService.unload(reqDto);
    }

    /**
     * 签收-司机
     * @author JPG
     */
    @ApiOperation(value = "签收-无需支付")
    @PostMapping(value = "/car/receipt")
    public ResultVo receipt(@RequestBody ReceiptTaskDto reqDto) {
        //验证用户
        Driver driver = csDriverService.validate(reqDto.getLoginId());
        reqDto.setLoginName(driver.getName());
        reqDto.setLoginPhone(driver.getPhone());
        reqDto.setClientEnum(ClientEnum.APP_DRIVER);
        return csTaskService.receipt(reqDto);
    }

}
