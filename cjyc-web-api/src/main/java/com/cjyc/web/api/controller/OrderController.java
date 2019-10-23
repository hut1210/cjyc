package com.cjyc.web.api.controller;

import com.cjyc.common.model.dto.web.order.OrderAllotDto;
import com.cjyc.common.model.dto.web.order.OrderCarLineWaitDispatchCountListDto;
import com.cjyc.common.model.dto.web.order.OrderCarWaitDispatchListDto;
import com.cjyc.common.model.dto.web.order.OrderListDto;
import com.cjyc.common.model.entity.Admin;
import com.cjyc.common.model.entity.Order;
import com.cjyc.common.model.enums.AdminStateEnum;
import com.cjyc.common.model.enums.order.OrderSaveType;
import com.cjyc.common.model.enums.order.OrderStateEnum;
import com.cjyc.common.model.util.BaseResultUtil;
import com.cjyc.common.model.vo.BizScopeVo;
import com.cjyc.common.model.vo.ListVo;
import com.cjyc.common.model.vo.PageVo;
import com.cjyc.common.model.vo.ResultVo;
import com.cjyc.common.model.vo.web.order.OrderCarWaitDispatchVo;
import com.cjyc.common.model.vo.web.order.OrderVo;
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
import javax.validation.constraints.NotNull;
import java.util.List;
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
     * 保存/提交/审核
     * 下单时客户不存在自动生成还是无主数据
     */
    @ApiOperation(value = "客户端保存和提交接口")
    @PostMapping(value = "/save")
    public ResultVo saveOrUpdate(@RequestBody OrderCommitDto reqDto) {

        //验证用户存不存在
        Long userId = reqDto.getUserId();
        Admin admin = adminService.getByUserId(userId);
        if(admin == null){
            return BaseResultUtil.fail("用户不存在");
        }
        reqDto.setCreateUserId(admin.getUserId());
        reqDto.setCreateUserName(admin.getName());

        //验证提交方式
        @NotNull int saveType = reqDto.getSaveType();
        if(OrderSaveType.SAVE.code != saveType){
            //验证数据范围
            BizScopeVo bizScope = bizScopeService.getBizScope(userId);
            List<Long> bizScopeStoreIds = bizScope.getBizScopeStoreIds();
            if(bizScopeStoreIds != null && !bizScopeStoreIds.contains(reqDto.getInputStoreId())){
                BaseResultUtil.fail("订单所属业务中中心不再权限范围内");
            }
            //验证角色权限
            /*if(admin.getJob() != ){

            }*/

            //验证业务中心
            //验证匹配线路
            //验证金额
            if(reqDto.getWlTotalFee().compareTo(reqDto.getRealTotalFee()) > 0){
                BaseResultUtil.fail("实收金额不能超过总的物流费");
            }
        }

        //订单状态
        if(OrderSaveType.CHECK.code == saveType){
            //新增or更新
            reqDto.setState(OrderStateEnum.CHECKED.code);
        }else if(OrderSaveType.COMMIT.code == saveType){
            reqDto.setState(OrderStateEnum.SUBMITTED.code);
        }else{
            reqDto.setState(OrderStateEnum.WAIT_SUBMIT.code);
        }


        ResultVo resultVo = orderService.saveAndUpdate(reqDto);

        //添加物流日志

        //发送推送信息
        return resultVo;
    }

    /**
     * 查询订单-根据ID
     */
    @ApiOperation(value = "查询订单-根据ID")
    @PostMapping(value = "/get/{orderId}")
    public ResultVo<OrderVo> get(@PathVariable Long orderId) {
        OrderVo orderVo = orderService.getVoById(orderId);
        return BaseResultUtil.success(orderVo);
    }


    /**
     * 分配订单
     */
    @ApiOperation(value = "分配订单")
    @PostMapping(value = "/allot")
    public ResultVo allot(@RequestBody OrderAllotDto reqDto) {
        //验证操作人
        Admin admin = adminService.getByUserId(reqDto.getUserId());
        if(admin == null || admin.getState() != AdminStateEnum.CHECKED.code){
            BaseResultUtil.fail("操作用户不存在或者已离职");
        }
        reqDto.setUserName(admin.getName());
        //验证被分配人
        Admin toAdmin = adminService.getByUserId(reqDto.getToUserId());
        if(toAdmin == null){
            BaseResultUtil.fail("目标业务员不存在");
        }
        reqDto.setToUserName(toAdmin.getName());
        return orderService.allot(reqDto);
    }



    /**
     * 订单查询
     */
    @ApiOperation(value = "订单查询")
    @PostMapping(value = "/list")
    public ResultVo list(@RequestBody OrderListDto reqDto) {
        return orderService.selectList(reqDto);
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
