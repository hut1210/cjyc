package com.cjyc.web.api.controller;

import com.alibaba.fastjson.JSON;
import com.cjkj.log.monitor.LogUtil;
import com.cjyc.common.model.dto.web.BaseWebDto;
import com.cjyc.common.model.dto.web.dispatch.LineWaitCountDto;
import com.cjyc.common.model.dto.web.order.*;
import com.cjyc.common.model.enums.ResultEnum;
import com.cjyc.common.model.vo.ListVo;
import com.cjyc.common.model.vo.PageVo;
import com.cjyc.common.model.vo.ResultVo;
import com.cjyc.common.model.vo.web.order.DispatchAddCarVo;
import com.cjyc.common.model.vo.web.order.OrderCarWaitDispatchVo;
import com.cjyc.common.system.service.ICsOrderService;
import com.cjyc.web.api.service.IOrderService;
import com.cjyc.web.api.util.ExcelUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.http.MediaType;
import org.springframework.util.CollectionUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;


/**
 * 订单
 *
 * @author JPG
 */
@RestController
@Api(tags = "运单-调度池")
@RequestMapping(value = "/dispatch",
        produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
public class DispatchController {


    @Resource
    private IOrderService orderService;
    @Resource
    private ICsOrderService csOrderService;

    /**
     * @author JPG
     */
    @ApiOperation(value = "查询待调度车辆列表")
    @PostMapping(value = "/wait/list")
    public ResultVo<PageVo<OrderCarWaitDispatchVo>> waitDispatchCarList(@RequestBody WaitDispatchListOrderCarDto reqDto) {
        return orderService.waitDispatchCarList(reqDto);
    }

    @ApiOperation(value = "导出全部调度池列表")
    @GetMapping(value = "/wait/exportAllList")
    public void exportAllList(String param, HttpServletResponse response) {
        WaitDispatchListOrderCarDto reqDto = JSON.parseObject(param, WaitDispatchListOrderCarDto.class);
        ResultVo<List<OrderCarWaitDispatchVo>> dispatchRs = orderService.waitDispatchCarAllList(reqDto);
        if (!isResultSuccess(dispatchRs)) {
            ExcelUtil.printExcelResult(ExcelUtil.getWorkBookForShowMsg("提示信息", dispatchRs.getMsg()),
                    "导出异常.xls", response);
            return;
        }
        List<OrderCarWaitDispatchVo> dispatchList = dispatchRs.getData();
        if (CollectionUtils.isEmpty(dispatchList)) {
            ExcelUtil.printExcelResult(ExcelUtil.getWorkBookForShowMsg("提示信息", "未查询到结果信息"),
                    "结果为空.xls", response);
            return;
        }
        dispatchList = dispatchList.stream().filter(Objects::nonNull).collect(Collectors.toList());
        try {
            ExcelUtil.exportExcel(dispatchList, "调度信息", "调度信息",
                    OrderCarWaitDispatchVo.class, System.currentTimeMillis() + "调度信息.xls", response);
            return;
        } catch (Exception e) {
            LogUtil.error("导出订单信息异常", e);
            ExcelUtil.printExcelResult(ExcelUtil.getWorkBookForShowMsg("提示信息", "导出调度池信息异常: " + e.getMessage()),
                    "导出异常.xls", response);
        }
        ExcelUtil.print(dispatchRs, "调度池信息", response);
    }

    /**
     * V2.0
     *
     * @author JPG
     */
    @ApiOperation(value = "查询待调度车辆统计")
    @PostMapping(value = "/wait/count/list/v2")
    public ResultVo<ListVo<Map<String, Object>>> waitDispatchCarCountListV2(@RequestBody BaseWebDto reqDto) {
        return orderService.waitDispatchCarCountListV2(reqDto);
    }

    /**
     * @author JPG
     * @deprecated replace by {@link DispatchController#waitDispatchCarCountListV2}
     */
    @ApiOperation(value = "查询待调度车辆统计")
    @PostMapping(value = "/wait/count/list/{roleId}/{loginId}")
    public ResultVo<ListVo<Map<String, Object>>> waitDispatchCarCountList(@PathVariable Long loginId,
                                                                          @PathVariable Long roleId) {
        return orderService.waitDispatchCarCountListV2(new BaseWebDto(loginId, roleId));
    }

    /**
     * @author JPG
     * @deprecated replace by {@link DispatchController#waitDispatchCarCountListV2}
     */
    @Deprecated
    @ApiOperation(value = "查询待调度车辆统计")
    @PostMapping(value = "/wait/count/list/{userId}")
    public ResultVo<ListVo<Map<String, Object>>> waitDispatchCarCountList(@ApiParam(value = "用户userId", required = true)
                                                                          @PathVariable Long userId) {
        return orderService.waitDispatchCarCountList();
    }

    /**
     * V2.0
     *
     * @author JPG
     */
    @ApiOperation(value = "按线路统计待调度车辆（统计列表）")
    @PostMapping(value = "/line/wait/count/list")
    public ResultVo<ListVo<Map<String, Object>>> lineWaitDispatchCarCountListV2(@RequestBody LineWaitCountDto reqDto) {
        return orderService.lineWaitDispatchCarCountListV2(reqDto);
    }

    /**
     * @author JPG
     */
    @ApiOperation(value = "查询干线待调度车辆列表")
    @PostMapping(value = "/trunk/wait/list")
    public ResultVo<PageVo<OrderCarWaitDispatchVo>> waitDispatchTrunkCarList(@RequestBody WaitDispatchTrunkDto reqDto) {
        return orderService.waitDispatchTrunkCarList(reqDto);
    }

    /**
     * @author JPG
     */
    @ApiOperation(value = "查询干线待调度车辆按城市统计")
    @PostMapping(value = "/wait/trunk/count/list")
    public ResultVo<ListVo<Map<String, Object>>> waitDispatchTrunkCarCountList(@RequestBody BaseWebDto reqDto) {
        return orderService.waitDispatchTrunkCarCountList(reqDto);
    }

    /**
     * @author JPG
     */
    @ApiOperation(value = "查询干线待调度按线路统计列表")
    @PostMapping(value = "/line/wait/trunk/count/list")
    public ResultVo<ListVo<Map<String, Object>>> lineWaitDispatchTrunkCarCountList(@RequestBody LineWaitDispatchCountDto reqDto) {
        return orderService.lineWaitDispatchTrunkCarCountList(reqDto);
    }

    /**
     * @author JPG
     */
    @ApiOperation(value = "根据订单车辆ID查询可调度起始地和目的地")
    @PostMapping(value = "/car/from/to/get")
    public ResultVo<DispatchAddCarVo> computerCarEndpoint(@Validated @RequestBody ComputeCarEndpointDto reqDto) {
        return csOrderService.computerCarEndpoint(reqDto);
    }

    /**
     * @author JPG
     */
    @ApiOperation(value = "根据订单车辆ID查询可调度起始地和目的地")
    @PostMapping(value = "/car/carry/type/update")
    public ResultVo changeOrderCarCarryType(@Validated @RequestBody ChangeCarryTypeDto dtoList) {
        return csOrderService.changeOrderCarCarryType(dtoList);
    }

    /**
     * 检查返回结果是否成功
     *
     * @param resultVo
     * @return
     */
    private boolean isResultSuccess(ResultVo resultVo) {
        if (null == resultVo) {
            return false;
        }
        return resultVo.getCode() == ResultEnum.SUCCESS.getCode();
    }


}
