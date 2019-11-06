package com.cjyc.web.api.controller;

import com.cjyc.common.model.dto.web.waybill.*;
import com.cjyc.common.model.entity.Admin;
import com.cjyc.common.model.enums.AdminStateEnum;
import com.cjyc.common.model.util.BaseResultUtil;
import com.cjyc.common.model.vo.BaseTipVo;
import com.cjyc.common.model.vo.ListVo;
import com.cjyc.common.model.vo.PageVo;
import com.cjyc.common.model.vo.ResultVo;
import com.cjyc.common.model.vo.web.waybill.*;
import com.cjyc.web.api.service.IAdminService;
import com.cjyc.web.api.service.IWaybillService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 运单
 *
 * @author JPG
 */
@Api(tags = "运单")
@RestController
@RequestMapping(value = "/waybill",
        produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
public class WaybillController {

    @Autowired
    private IWaybillService waybillService;
    @Autowired
    private IAdminService adminService;

    /**
     * 提送车调度
     *
     * @author JPG
     * @since 2019/10/15 11:53
     */
    @ApiOperation("提送车调度")
    @PostMapping("/local/dispatch")
    public ResultVo LocalDispatch(@RequestBody LocalDispatchListWaybillDto reqDto) {
        //验证用户
        Admin admin = adminService.getByUserId(reqDto.getUserId());
        if (admin == null || admin.getState() != AdminStateEnum.CHECKED.code) {
            return BaseResultUtil.fail("当前业务员，不在职");
        }
        reqDto.setUserName(admin.getName());
        return waybillService.localDispatch(reqDto);
    }


    /**
     * 干线调度
     *
     * @author JPG
     * @since 2019/10/15 11:53
     */
    @ApiOperation("干线调度")
    @PostMapping("/trunk/dispatch")
    public ResultVo trunkDispatch(@RequestBody TrunkDispatchListShellWaybillDto reqDto) {
        return waybillService.trunkDispatch(reqDto);
    }

    /**
     * TODO 干线追加调度
     *
     * @author JPG
     * @since 2019/10/15 11:53
     */
    @ApiOperation("干线追加调度")
    @PostMapping("/trunk/dispatch/update")
    public ResultVo updateTrunkDispatch(@RequestBody TrunkDispatchListShellWaybillDto reqDto) {
        return null;
    }



    /**
     * 取消运单
     *
     * @author JPG
     * @since 2019/10/15 11:53
     */
    @ApiOperation("取消运单")
    @PostMapping("/dispatch/cancel")
    public ResultVo<ListVo<BaseTipVo>> cancelDispatch(@RequestBody CancelDispatchDto reqDto) {
        return waybillService.cancelDispatch(reqDto);
    }



    /**
     * 根据订单车辆ID查询历史运单
     */
    @ApiOperation(value = "根据订单车辆ID查询历史运单")
    @PostMapping(value = "/car/history/list")
    public ResultVo<List<HistoryListWaybillVo>> getCarHistoryList(@RequestBody HistoryListWaybillDto reqDto) {
        return waybillService.historyList(reqDto);
    }


    /**
     * 查询同城运单列表
     */
    @ApiOperation(value = "查询同城运单列表")
    @PostMapping(value = "/local/list")
    public ResultVo<PageVo<LocalListWaybillCarVo>> getLocalList(@RequestBody LocalListWaybillCarDto reqDto) {
        return waybillService.Locallist(reqDto);
    }

    /**
     * 查询干线运单列表
     */
    @ApiOperation(value = "查询干线运单列表")
    @PostMapping(value = "/trunk/list")
    public ResultVo<PageVo<TrunkListWaybillVo>> getTrunklist(@RequestBody TrunkListWaybillDto reqDto) {
        return waybillService.trunklist(reqDto);
    }


    /**
     * 查询干线运单车辆列表
     */
    @ApiOperation(value = "查询干线运单车辆列表")
    @PostMapping(value = "/trunk/car/list")
    public ResultVo<PageVo<TrunkListWaybillCarVo>> getCarTrunklist(@RequestBody TrunkListWaybillCarDto reqDto) {
        return waybillService.trunkCarlist(reqDto);
    }

    /**
     * 查询干线运单车辆列表
     */
    @ApiOperation(value = "查询干线运单车辆列表")
    @PostMapping(value = "/get/{waybillId}")
    public ResultVo<GetWaybillVo> get(@ApiParam(value = "运单ID") @PathVariable Long waybillId) {
        return waybillService.get(waybillId);
    }

    /**
     * 分类根据车辆ID查询车辆运单
     */
    @ApiOperation(value = "分类根据车辆ID查询车辆运单")
    @PostMapping(value = "/car/get/{orderCarId}/{waybillType}")
    public ResultVo<List<GetWaybillCarVo>> getByType(@ApiParam(value = "运单车辆ID") @PathVariable Long orderCarId,
    @ApiParam(value = "运单类型：1提车运单，2干线运单，3送车运单") @PathVariable Integer waybillType) {
        return waybillService.getCarByType(orderCarId, waybillType);
    }



    /**
     * 我的运单-承运商
     */
    @ApiOperation(value = "我的运单-承运商")
    @PostMapping(value = "/cys/list")
    public ResultVo<List<CysWaybillVo>> cysList(@RequestBody CysWaybillDto reqDto) {
        return waybillService.cysList(reqDto);
    }















}
