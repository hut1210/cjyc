package com.cjyc.web.api.controller;

import com.cjyc.common.model.dto.web.order.LineWaitDispatchCountListOrderCarDto;
import com.cjyc.common.model.dto.web.order.WaitDispatchListOrderCarDto;
import com.cjyc.common.model.vo.BizScopeVo;
import com.cjyc.common.model.vo.ListVo;
import com.cjyc.common.model.vo.PageVo;
import com.cjyc.common.model.vo.ResultVo;
import com.cjyc.common.model.vo.web.order.OrderCarWaitDispatchVo;
import com.cjyc.web.api.service.IAdminService;
import com.cjyc.web.api.service.IBizScopeService;
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
@Api(tags = "调度池")
@RequestMapping(value = "/dispatch",
        produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
public class DispatchController {


    @Resource
    private IOrderService orderService;
    @Resource
    private IBizScopeService bizScopeService;
    @Resource
    private IAdminService adminService;

    /**
     * 按地级市查询待调度车辆统计（统计列表）
     * @author JPG
     */
    @ApiOperation(value = "查询待调度车辆统计")
    @GetMapping(value = "/wait/count/list/{userId}")
    public ResultVo<ListVo<Map<String, Object>>> waitDispatchCarCountList(@ApiParam(value = "userId", required = true)
                                                                          @PathVariable Long userId) {
        BizScopeVo bizScope = bizScopeService.getBizScope(userId);
        return orderService.waitDispatchCarCountList();
    }

    /**
     * 按线路统计待调度车辆（统计列表）
     * @author JPG
     */
    @ApiOperation(value = "按线路统计待调度车辆（统计列表）")
    @GetMapping(value = "/line/wait/count/list")
    public ResultVo<ListVo<Map<String, Object>>> lineWaitDispatchCarCountList(@RequestBody LineWaitDispatchCountListOrderCarDto reqDto) {
        BizScopeVo bizScope = bizScopeService.getBizScope(reqDto.getUserId());
        return orderService.lineWaitDispatchCarCountList(reqDto, bizScope.getBizScopeStoreIds());
    }

    /**
     * 查询待调度车辆列表（数据列表）
     * @author JPG
     */
    @ApiOperation(value = "查询待调度车辆列表")
    @GetMapping(value = "/wait/list")
    public ResultVo<PageVo<OrderCarWaitDispatchVo>> waitDispatchCarList(@RequestBody WaitDispatchListOrderCarDto reqDto) {
        BizScopeVo bizScope = bizScopeService.getBizScope(reqDto.getUserId());
        return orderService.waitDispatchCarList(reqDto, bizScope.getBizScopeStoreIds());
    }

}
