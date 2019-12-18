package com.cjyc.driver.api.controller;

import com.cjyc.common.model.dto.driver.task.PickLoadDto;
import com.cjyc.common.model.dto.driver.task.ReplenishInfoDto;
import com.cjyc.common.model.dto.web.task.*;
import com.cjyc.common.model.entity.Driver;
import com.cjyc.common.model.util.BaseResultUtil;
import com.cjyc.common.model.vo.ResultReasonVo;
import com.cjyc.common.model.vo.ResultVo;
import com.cjyc.common.system.service.ICsAdminService;
import com.cjyc.common.system.service.ICsDriverService;
import com.cjyc.common.system.service.ICsTaskService;
import com.cjyc.driver.api.service.IWaybillService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@Api(tags = "运单")
@RequestMapping(value = "/waybill", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
public class WaybillController {

    @Resource
    private IWaybillService waybillService;

    @Resource
    private ICsTaskService csTaskService;
    @Resource
    private ICsDriverService csDriverService;
    @Resource
    private ICsAdminService csAdminService;


/*    @ApiOperation(value = "分页查询待分配运单列表")
    @PostMapping("/wait/allot/page")
    public ResultVo<PageVo<WaitAllotVo>> getWaitAllot(@RequestBody WaitAllotDto dto) {
        return waybillService.getWaitAllotPage(dto);
    }*/

    /**
     * 提车完善信息
     * @author JPG
     */
    @ApiOperation(value = "提车完善信息")
    @PostMapping(value = "/replenish/info/update")
    public ResultVo replenishInfo(@RequestBody ReplenishInfoDto reqDto) {
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
    @ApiOperation(value = "提车完善信息")
    @PostMapping(value = "/replenish/info/update")
    public ResultVo<ResultReasonVo> replenishInfo(@RequestBody PickLoadDto reqDto) {
        //验证用户
        Driver driver = csDriverService.validate(reqDto.getLoginId());
        reqDto.setLoginName(driver.getName());
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
    public ResultVo<ResultReasonVo> unload(@RequestBody UnLoadTaskDto reqDto) {
        //验证用户
        Driver driver = csDriverService.validate(reqDto.getLoginId());
        reqDto.setLoginName(driver.getName());
        return csTaskService.unload(reqDto);
    }

    /**
     * 签收-司机
     * @author JPG
     */
    @ApiOperation(value = "签收")
    @PostMapping(value = "/car/receipt")
    public ResultVo receipt(@RequestBody ReceiptTaskDto reqDto) {
        //验证用户
        Driver driver = csDriverService.validate(reqDto.getLoginId());
        reqDto.setLoginName(driver.getName());
        reqDto.setLoginPhone(driver.getPhone());
        return csTaskService.receipt(reqDto);
    }

}
