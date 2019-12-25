package com.cjyc.web.api.controller;

import com.cjyc.common.model.dto.web.order.*;
import com.cjyc.common.model.vo.ListVo;
import com.cjyc.common.model.vo.PageVo;
import com.cjyc.common.model.vo.ResultVo;
import com.cjyc.common.model.vo.web.order.DispatchAddCarVo;
import com.cjyc.common.model.vo.web.order.OrderCarWaitDispatchVo;
import com.cjyc.common.system.service.ICsOrderService;
import com.cjyc.web.api.service.IOrderService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Map;


/**
 * 订单
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
     * 按地级市查询待调度车辆统计（统计列表）
     * @author JPG
     */
    @ApiOperation(value = "查询待调度车辆统计")
    @PostMapping(value = "/wait/count/list/{userId}")
    public ResultVo<ListVo<Map<String, Object>>> waitDispatchCarCountList(@ApiParam(value = "用户userId", required = true)
                                                                          @PathVariable Long userId) {
        return orderService.waitDispatchCarCountList();
    }


    /**
     * 按线路统计待调度车辆（统计列表）
     * @author JPG
     */
    @ApiOperation(value = "按线路统计待调度车辆（统计列表）")
    @PostMapping(value = "/line/wait/count/list")
    public ResultVo<ListVo<Map<String, Object>>> lineWaitDispatchCarCountList(@RequestBody LineWaitDispatchCountDto reqDto) {
        return orderService.lineWaitDispatchCarCountList(reqDto,null);
    }

    /**
     * 查询待调度车辆列表（数据列表）
     * @author JPG
     */
    @ApiOperation(value = "查询待调度车辆列表")
    @PostMapping(value = "/wait/list")
    public ResultVo<PageVo<OrderCarWaitDispatchVo>> waitDispatchCarList(@RequestBody WaitDispatchListOrderCarDto reqDto) {
        return orderService.waitDispatchCarList(reqDto);
    }


    /**
     * 查询待调度车辆列表（数据列表）
     * @author JPG
     */
    @ApiOperation(value = "查询待调度车辆列表")
    @PostMapping(value = "/wait/trunk/count/list")
    public ResultVo<ListVo<Map<String, Object>>> waitDispatchTrunkCarCountList(@RequestBody WaitDispatchCountDto reqDto) {
        return orderService.waitDispatchTrunkCarCountList(reqDto);
    }
    /**
     * 查询待调度车辆列表（数据列表）
     * @author JPG
     */
    @ApiOperation(value = "查询待调度车辆列表")
    @PostMapping(value = "/line/wait/trunk/count/list")
    public ResultVo<ListVo<Map<String, Object>>> lineWaitDispatchTrunkCarCountList(@RequestBody LineWaitDispatchCountDto reqDto) {
        return orderService.lineWaitDispatchTrunkCarCountList(reqDto);
    }
    /**
     * 查询待调度车辆列表（数据列表）
     * @author JPG
     */
    @ApiOperation(value = "查询待调度车辆列表")
    @PostMapping(value = "/trunk/wait/list")
    public ResultVo<PageVo<OrderCarWaitDispatchVo>> waitDispatchTrunkCarList(@RequestBody WaitDispatchTrunkDto reqDto) {
        return orderService.waitDispatchTrunkCarList(reqDto);
    }



    /**
     * 根据订单车辆ID查询可调度起始地和目的地
     */
    @ApiOperation(value = "根据订单车辆ID查询可调度起始地和目的地")
    @PostMapping(value = "/car/from/to/get")
    public ResultVo<DispatchAddCarVo> computerCarEndpoint(@RequestBody ComputeCarEndpointDto reqDto) {
        return csOrderService.computerCarEndpoint(reqDto);
    }

}
