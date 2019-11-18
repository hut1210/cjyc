package com.cjyc.salesman.api.controller;

import com.cjyc.common.model.dto.web.order.WaitDispatchListOrderCarDto;
import com.cjyc.common.model.util.BaseResultUtil;
import com.cjyc.common.model.vo.PageVo;
import com.cjyc.common.model.vo.ResultVo;
import com.cjyc.common.model.vo.web.order.OrderCarWaitDispatchVo;
import com.cjyc.salesman.api.dto.OrderDto;
import com.cjyc.salesman.api.service.IOrderService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

/**
 * 调度池
 * @author JPG
 */
@Api(tags = "调度池")
@Slf4j
@RestController
@RequestMapping(value = "/dispatch",
        produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
public class DispatchController {

    @Autowired
    IOrderService orderService;

    /**
     * 查询待调度车辆列表（数据列表）
     * @author JPG
     */
    @ApiOperation(value = "查询待调度车辆列表")
    @PostMapping(value = "/wait/list")
    public ResultVo<PageVo<OrderCarWaitDispatchVo>> waitDispatchCarList(@RequestBody WaitDispatchListOrderCarDto reqDto) {
        return orderService.waitDispatchCarList(reqDto, null);
    }


}
