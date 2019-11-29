package com.cjyc.web.api.controller;

import com.cjkj.log.monitor.LogUtil;
import com.cjyc.common.model.dto.web.order.*;
import com.cjyc.common.model.entity.Admin;
import com.cjyc.common.model.enums.ResultEnum;
import com.cjyc.common.model.util.BaseResultUtil;
import com.cjyc.common.model.vo.PageVo;
import com.cjyc.common.model.vo.ResultVo;
import com.cjyc.common.model.vo.web.order.*;
import com.cjyc.common.system.service.ICsAdminService;
import com.cjyc.web.api.service.IOrderService;
import com.cjyc.web.api.util.ExcelUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.MediaType;
import org.springframework.util.CollectionUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.stream.Collectors;

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
    private ICsAdminService csAdminService;


    /**
     * 保存,只保存无验证
     * @author JPG
     */
    @ApiOperation(value = "订单保存")
    @PostMapping(value = "/save")
    public ResultVo save(@RequestBody SaveOrderDto reqDto) {

        //验证用户存不存在
        Admin admin = csAdminService.validate(reqDto.getLoginId());
        reqDto.setLoginName(admin.getName());
        reqDto.setCreateUserId(admin.getId());
        reqDto.setCreateUserName(admin.getName());

        ResultVo resultVo = orderService.save(reqDto);
        //发送推送信息
        return resultVo;
    }


    /**
     * 提交
     * @author JPG
     */
    @ApiOperation(value = "订单提交")
    @PostMapping(value = "/commit")
    public ResultVo commit(@Validated @RequestBody CommitOrderDto reqDto) {

        //验证用户存不存在
        Admin admin = csAdminService.validate(reqDto.getLoginId());
        reqDto.setLoginName(admin.getName());
        reqDto.setLoginPhone(admin.getPhone());
        reqDto.setCreateUserId(admin.getId());
        reqDto.setCreateUserName(admin.getName());

        ResultVo resultVo = orderService.commit(reqDto);

        //发送消息给用户
        //csPushMsgService.send(reqDto.getCustomerPhone(), PushMessageEnum.COMMIT_ORDER.getMsg(resultVo.getData().toString()));
        //发送短信
        return resultVo;
    }


    @ApiOperation(value = "导入订单")
    @PostMapping(value = "/batch/import")
    @Deprecated
    public ResultVo batchImport(@Validated @RequestBody BatchImportOrderDto reqDto) {
        return null;
    }

    /**
     * 完善订单信息
     * @author JPG
     */
    @ApiOperation(value = "完善订单信息")
    @PostMapping(value = "/replenish/info/update")
    public ResultVo replenishInfo(@RequestBody ReplenishOrderDto reqDto) {
        return orderService.replenishInfo(reqDto);
    }

    /**
     * 审核订单
     * @author JPG
     */
    @ApiOperation(value = "审核订单")
    @PostMapping(value = "/check")
    public ResultVo check(@RequestBody CheckOrderDto reqDto) {
        //验证用户存不存在
        Admin admin = csAdminService.validate(reqDto.getLoginId());
        reqDto.setLoginName(admin.getName());
        return orderService.check(reqDto);
    }

    /**
     * 驳回订单
     * @author JPG
     */
    @ApiOperation(value = "驳回订单")
    @PostMapping(value = "/reject")
    public ResultVo reject(@RequestBody RejectOrderDto reqDto) {
        //验证用户存不存在
        Admin admin = csAdminService.validate(reqDto.getLoginId());
        return orderService.reject(reqDto);
    }




    /**
     * 查询订单-根据ID
     * @author JPG
     */
    @ApiOperation(value = "查询订单-根据ID")
    @PostMapping(value = "/get/{orderId}")
    public ResultVo<OrderVo> get(@PathVariable Long orderId) {
        OrderVo orderVo = orderService.getVoById(orderId);
        return BaseResultUtil.success(orderVo);
    }


    /**
     * 查询订单取消记录-根据ID
     * @author JPG
     */
    @ApiOperation(value = "查询订单取消记录-根据ID")
    @PostMapping(value = "/change/log/list")
    public ResultVo<List<ListOrderChangeLogVo>> get(@RequestBody ListOrderChangeLogDto reqDto) {
        List<ListOrderChangeLogVo> list = orderService.getChangeLogVoById(reqDto);
        return BaseResultUtil.success(list);
    }

    /**
     * 查询订单列表
     * @author JPG
     */
    @ApiOperation(value = "查询订单列表")
    @PostMapping(value = "/list")
    public ResultVo<PageVo<ListOrderVo>> list(@RequestBody ListOrderDto reqDto) {
        return orderService.list(reqDto);
    }

    @ApiOperation(value = "分页导出订单列表")
    @GetMapping(value = "/exportPageList")
    public ResultVo exportPageList(ListOrderDto reqDto, HttpServletResponse response){
        ResultVo<PageVo<ListOrderVo>> resultVo = orderService.list(reqDto);
        if (!isResultSuccess(resultVo)) {
            return BaseResultUtil.fail("导出数据异常");
        }
        PageVo<ListOrderVo> data = resultVo.getData();
        if (data == null || CollectionUtils.isEmpty(data.getList())) {
            return BaseResultUtil.success("未查询到数据");
        }
        try{
            ExcelUtil.exportExcel(data.getList(), "订单信息", "订单信息",
                    ListOrderVo.class, System.currentTimeMillis()+"订单信息.xls", response);
            return null;
        }catch (Exception e) {
            LogUtil.error("导出订单信息异常", e);
            return BaseResultUtil.fail("导出订单信息异常: " + e.getMessage());
        }
    }

    @ApiOperation(value = "导出全部订单列表")
    @GetMapping(value = "/exportAllList")
    public ResultVo exportAllList(ListOrderDto reqDto, HttpServletResponse response) {
        List<ListOrderVo> orderList = orderService.listAll(reqDto);
        if (CollectionUtils.isEmpty(orderList)) {
            return BaseResultUtil.success("未查询到结果");
        }
        orderList = orderList.stream().filter(o -> o != null).collect(Collectors.toList());
        try{
            ExcelUtil.exportExcel(orderList, "订单信息", "订单信息",
                    ListOrderVo.class, System.currentTimeMillis()+"订单信息.xls", response);
            return null;
        }catch (Exception e) {
            LogUtil.error("导出订单信息异常", e);
            return BaseResultUtil.fail("导出订单信息异常: " + e.getMessage());
        }
    }

    /**
     * 查询订单车辆列表
     * @author JPG
     */
    @ApiOperation(value = "订单车辆查询")
    @PostMapping(value = "/car/list")
    public ResultVo<PageVo<ListOrderCarVo>> carlist(@RequestBody ListOrderCarDto reqDto) {
        return orderService.carlist(reqDto);
    }

    @ApiOperation(value = "分页导出车辆信息列表")
    @GetMapping(value = "/car/exportPageList")
    public ResultVo exportPageCarList(ListOrderCarDto reqDto, HttpServletResponse response) {
        ResultVo<PageVo<ListOrderCarVo>> resultVo = orderService.carlist(reqDto);
        if (!isResultSuccess(resultVo)) {
            return BaseResultUtil.fail("导出数据异常");
        }
        PageVo<ListOrderCarVo> data = resultVo.getData();
        if (data == null || CollectionUtils.isEmpty(data.getList())) {
            return BaseResultUtil.success("未查询到数据");
        }
        try{
            ExcelUtil.exportExcel(data.getList(), "车辆信息", "车辆信息",
                    ListOrderCarVo.class, System.currentTimeMillis()+"车辆信息.xls", response);
            return null;
        }catch (Exception e) {
            LogUtil.error("导出车辆信息异常", e);
            return BaseResultUtil.fail("导出车辆信息异常: " + e.getMessage());
        }
    }

    @ApiOperation(value = "导出全部车辆信息列表")
    @GetMapping(value = "/car/exportAllList")
    public ResultVo exportAllCarList(ListOrderCarDto reqDto, HttpServletResponse response) {
        List<ListOrderCarVo> carList = orderService.carListAll(reqDto);
        if (CollectionUtils.isEmpty(carList)) {
            return BaseResultUtil.success("未查询到结果");
        }
        carList = carList.stream().filter(c -> c != null).collect(Collectors.toList());
        try {
            ExcelUtil.exportExcel(carList, "车辆信息", "车辆信息",
                    ListOrderCarVo.class, System.currentTimeMillis()+"车辆信息.xls", response);
            return null;
        }catch (Exception e) {
            LogUtil.error("导出车辆信息异常", e);
            return BaseResultUtil.fail("导出车辆信息异常: " + e.getMessage());
        }
    }
    /**
     * 查询订单车辆运输信息-根据ID
     * @author JPG
     */
    @ApiOperation(value = "查询订单车辆运输状态-根据ID")
    @PostMapping(value = "/detail/info/{orderId}")
    public ResultVo<List<TransportInfoOrderCarVo>> detailInfo(@PathVariable Long orderId) {
        List<TransportInfoOrderCarVo> list = orderService.getTransportInfoVoById(orderId);
        return BaseResultUtil.success(list);
    }



    /**
     * 分配订单
     * @author JPG
     */
    @ApiOperation(value = "分配订单")
    @PostMapping(value = "/allot")
    public ResultVo allot(@Validated @RequestBody AllotOrderDto reqDto) {
        //验证用户存不存在
        Admin admin = csAdminService.validate(reqDto.getLoginId());
        reqDto.setLoginName(admin.getName());
        Admin toAdmin = csAdminService.validate(reqDto.getToAdminId());
        reqDto.setToAdminName(toAdmin.getName());
        return orderService.allot(reqDto);
    }


    /**
     * 订单改价
     * @author JPG
     */
    @ApiOperation(value = "订单改价")
    @PostMapping(value = "/change/price")
    public ResultVo changePrice(@RequestBody ChangePriceOrderDto reqDto) {
        Admin admin = csAdminService.validate(reqDto.getLoginId());
        reqDto.setLoginName(admin.getName());
        return orderService.changePrice(reqDto);
    }


    /**
     * 取消订单
     * @author JPG
     */
    @ApiOperation(value = "取消订单")
    @PostMapping(value = "/cancel")
    public ResultVo cancel(@RequestBody CancelOrderDto reqDto) {
        Admin admin = csAdminService.validate(reqDto.getLoginId());
        reqDto.setLoginName(admin.getName());
        return orderService.cancel(reqDto);
    }


    /**
     * 作废订单
     * @author JPG
     */
    @ApiOperation(value = "作废订单")
    @PostMapping(value = "/obsolete")
    public ResultVo obsolete(@RequestBody CancelOrderDto reqDto) {

        Admin admin = csAdminService.validate(reqDto.getLoginId());
        reqDto.setLoginName(admin.getName());

        return orderService.obsolete(reqDto);
    }

    /**
     * 检查返回结果是否成功
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
