package com.cjyc.web.api.controller;

import com.cjyc.common.model.dto.web.order.OrderCarLineWaitDispatchCountListDto;
import com.cjyc.common.model.dto.web.order.OrderCarWaitDispatchListDto;
import com.cjyc.common.model.enums.ResultEnum;
import com.cjyc.common.model.util.BaseResultUtil;
import com.cjyc.common.model.vo.BizScopeVo;
import com.cjyc.common.model.vo.ListVo;
import com.cjyc.common.model.vo.PageVo;
import com.cjyc.common.model.vo.ResultVo;
import com.cjyc.common.model.vo.web.order.OrderCarWaitDispatchVo;
import com.cjyc.web.api.dto.OrderDto;
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
 * @auther litan
 * @description: com.cjyc.web.api.controller
 * @date:2019/9/28
 */
@RestController
@Api(tags = "订单")
@RequestMapping(value = "/order",
        consumes = MediaType.APPLICATION_JSON_UTF8_VALUE,
        produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
public class OrderController {

    @Resource
    private IOrderService orderService;
    @Resource
    private IBizScopeService bizScopeService;

    /**
     * web端下单
     */
    @ApiOperation(value = "客户端下单接口", notes = "客户端下单", httpMethod = "POST")
    @RequestMapping(value = "/commit", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResultVo commit(@RequestBody OrderDto orderDto) {
        boolean result = orderService.commitOrder(orderDto);
        return result ? BaseResultUtil.success(orderDto) : BaseResultUtil.fail();
    }

    /**
     * 按地级市查询待调度车辆统计（统计列表）
     * @author JPG
     */
    @ApiOperation(value = "查询待调度车辆统计")
    @GetMapping(value = "/car/wait/dispatch/count/list/{userId}")
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
    @GetMapping(value = "/car/line/wait/dispatch/count/list")
    public ResultVo<ListVo<Map<String, Object>>> lineWaitDispatchCarCountList(@RequestBody OrderCarLineWaitDispatchCountListDto reqDto) {
        BizScopeVo bizScope = bizScopeService.getBizScope(reqDto.getUserId());
        return orderService.lineWaitDispatchCarCountList(reqDto, bizScope.getBizScopeStoreIds());
    }

    /**
     * 查询待调度车辆列表（数据列表）
     * @author JPG
     */
    @ApiOperation(value = "查询待调度车辆列表")
    @GetMapping(value = "/car/wait/dispatch/list")
    public ResultVo<PageVo<OrderCarWaitDispatchVo>> waitDispatchCarList(@RequestBody OrderCarWaitDispatchListDto reqDto) {
        BizScopeVo bizScope = bizScopeService.getBizScope(reqDto.getUserId());
        return orderService.waitDispatchCarList(reqDto, bizScope.getBizScopeStoreIds());
    }



/*
    *//**
     * 测试分页
     * *//*
    @ApiOperation(value = "分页测试接口", notes = "分页测试", httpMethod = "POST")
    @RequestMapping(value = "/testPageList", method = RequestMethod.POST)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "pageNum", value = "当前页", required = true, dataType = "Integer", paramType = "query"),
            @ApiImplicitParam(name = "pageSize", value = "条数", required = true, dataType = "Integer", paramType = "query")
    })
    public ResultVo testPageList(Integer pageNum, Integer pageSize){

        PageInfo<Customer> customerPageInfo = customerServiceCom.pageList(pageNum,pageSize);
        return BaseResultUtil.getVo(ResultEnum.SUCCESS.getCode(), ResultEnum.SUCCESS.getMsg(),customerPageInfo);
    }

    *//**
     * 下单测试--dto接收
     * *//*
    @ApiOperation(value = "下单测试接口", notes = "下单测试", httpMethod = "POST")
    @RequestMapping(value = "/orderTest", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResultVo orderTest(@RequestBody OrderDto orderDto) {
        return BaseResultUtil.getVo(ResultEnum.SUCCESS.getCode(),ResultEnum.SUCCESS.getMsg(),orderDto);
    }

    *//**
     * 下单测试2--Map接收
     * *//*
    @ApiOperation(value = "下单测试接口", notes = "下单测试", httpMethod = "POST")
    @RequestMapping(value = "/orderTest2", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResultVo orderTest2(@RequestBody Map<String,Object> map) {
        return BaseResultUtil.getVo(ResultEnum.SUCCESS.getCode(),ResultEnum.SUCCESS.getMsg(),map);
    }*/

}
