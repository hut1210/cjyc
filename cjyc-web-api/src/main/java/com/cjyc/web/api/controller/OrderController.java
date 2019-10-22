package com.cjyc.web.api.controller;

import com.cjyc.common.model.dto.web.order.OrderCarLineWaitDispatchCountListDto;
import com.cjyc.common.model.dto.web.order.OrderCarWaitDispatchListDto;
import com.cjyc.common.model.entity.Admin;
import com.cjyc.common.model.enums.order.OrderStateEnum;
import com.cjyc.common.model.util.BaseResultUtil;
import com.cjyc.common.model.vo.BizScopeVo;
import com.cjyc.common.model.vo.ListVo;
import com.cjyc.common.model.vo.PageVo;
import com.cjyc.common.model.vo.ResultVo;
import com.cjyc.common.model.vo.web.order.OrderCarWaitDispatchVo;
import com.cjyc.web.api.dto.OrderCommitDto;
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
@Api(tags = "订单")
@RequestMapping(value = "/order",
        produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
public class OrderController {

    @Resource
    private IOrderService orderService;
    @Resource
    private IBizScopeService bizScopeService;
    @Resource
    private IAdminService adminService;

    /**
     * 提交订单
     */
    @ApiOperation(value = "客户端保存和提交接口")
    @PostMapping(value = "/commit")
    public ResultVo commit(@RequestBody OrderCommitDto orderCommitDto) {

        //验证用户存不存在
        Long userId = orderCommitDto.getCreateUserId();
        Admin admin = adminService.getByUserId(userId);
        if(admin == null){
            return BaseResultUtil.fail("用户不存在");
        }
        orderCommitDto.setCreateUserName(admin.getName());

        //验证业务中心
        //验证匹配线路

        //新增or更新
        ResultVo resultVo = null;
        Long orderId = orderCommitDto.getOrderId();
        orderCommitDto.setState(OrderStateEnum.WAIT_CHECK.code);
        if(orderId == null){
            resultVo = orderService.save(orderCommitDto);
        }else{

            resultVo = orderService.update(orderCommitDto);
        }

        //添加物流日志
        //发送推送信息
        return resultVo;
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

}
