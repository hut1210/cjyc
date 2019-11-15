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
import com.cjyc.common.system.service.ICsAdminService;
import com.cjyc.web.api.annotations.RequestHeaderAndBody;
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
    private ICsAdminService csAdminService;

    /**
     * 提送车调度
     *
     * @author JPG
     * @since 2019/10/15 11:53
     */
    @ApiOperation("提送车调度")
    @PostMapping("/local/save")
    public ResultVo saveLocal(@RequestHeaderAndBody SaveLocalDto reqDto) {
        //验证用户
        Admin admin = csAdminService.getByUserId(reqDto.getUserId(), true);
        if (admin == null || admin.getState() != AdminStateEnum.CHECKED.code) {
            return BaseResultUtil.fail("当前业务员，不在职");
        }
        reqDto.setUserName(admin.getName());
        return waybillService.saveLocal(reqDto);
    }

    /**
     * 修改同城调度
     *
     * @author JPG
     * @since 2019/10/15 11:53
     */
    @ApiOperation("修改同城调度")
    @PostMapping("/local/update")
    public ResultVo updateLocal(@RequestBody UpdateLocalDto reqDto) {
        return waybillService.updateLocal(reqDto);
    }


    /**
     * 干线调度
     *
     * @author JPG
     * @since 2019/10/15 11:53
     */
    @ApiOperation("干线调度")
    @PostMapping("/trunk/save")
    public ResultVo saveTrunk(@RequestBody SaveTrunkWaybillDto reqDto) {
        return waybillService.saveTrunk(reqDto);
    }

    /**
     * 修改干线运单
     *
     * @author JPG
     * @since 2019/10/15 11:53
     */
    @ApiOperation("修改干线调度")
    @PostMapping("/trunk/update")
    public ResultVo updateTrunk(@RequestBody UpdateTrunkWaybillDto reqDto) {
        return waybillService.updateTrunk(reqDto);
    }


    /**
     * 中途卸载车辆
     *
     * @author JPG
     * @since 2019/10/15 11:53
     */
    @ApiOperation("中途卸载车辆")
    @PostMapping("/trunk/midway/unload")
    public ResultVo trunkMidwayUnload(@RequestBody TrunkMidwayUnload reqDto) {
        return waybillService.trunkMidwayUnload(reqDto);
    }
    /**
     * 中止干线运单并结算
     *
     * @author JPG
     * @since 2019/10/15 11:53
     */
    @Deprecated
    @ApiOperation("中止干线运单并结算")
    @PostMapping("/trunk/midway/finish")
    public ResultVo updateTrunkMidwayFinish(@RequestBody UpdateTrunkMidwayFinishDto reqDto) {
        return waybillService.updateTrunkMidwayFinish(reqDto);
    }



    /**
     * 取消调度
     *
     * @author JPG
     * @since 2019/10/15 11:53
     */
    @ApiOperation("取消调度")
    @PostMapping("/cancel")
    public ResultVo<ListVo<BaseTipVo>> cancel(@RequestBody CancelWaybillDto reqDto) {
        return waybillService.cancel(reqDto);
    }



    /**
     * 根据订单车辆ID查询历史运单
     */
    @ApiOperation(value = "根据订单车辆ID查询历史运单")
    @PostMapping(value = "/car/history/list")
    public ResultVo<List<HistoryListWaybillVo>> getCarHistoryList(@RequestBody HistoryListDto reqDto) {
        return waybillService.historyList(reqDto);
    }


    /**
     * 查询同城运单列表
     */
    @ApiOperation(value = "查询同城运单列表")
    @PostMapping(value = "/local/list")
    public ResultVo<PageVo<LocalListWaybillCarVo>> getLocalList(@RequestBody LocalListWaybillCarDto reqDto) {
        return waybillService.locallist(reqDto);
    }

    /**
     * 查询干线主运单列表
     */
    @ApiOperation(value = "查询干线主运单列表")
    @PostMapping(value = "/trunk/main/list")
    public ResultVo<PageVo<TrunkMainListWaybillVo>> getTrunkMainList(@RequestBody TrunkMainListWaybillDto reqDto) {
        return waybillService.getTrunkMainList(reqDto);
    }

    /**
     * 查询干线子运单（任务）列表
     */
    @ApiOperation(value = "查询干线子运单（任务）列表")
    @PostMapping(value = "/trunk/sub/list")
    public ResultVo<PageVo<TrunkSubListWaybillVo>> getTrunkSubList(@RequestBody TrunkSubListWaybillDto reqDto) {
        return waybillService.getTrunkSubList(reqDto);
    }



    /**
     * 查询干线运单主单列表
     */
    @Deprecated
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
    public ResultVo<PageVo<TrunkCarListWaybillCarVo>> getCarTrunklist(@RequestBody TrunkListWaybillCarDto reqDto) {
        return waybillService.trunkCarlist(reqDto);
    }

    /**
     * 查询运单（含车辆）
     */
    @ApiOperation(value = "查询运单车辆列表")
    @PostMapping(value = "/get/{waybillId}")
    public ResultVo<WaybillVo> get(@ApiParam(value = "运单ID") @PathVariable Long waybillId) {
        return waybillService.get(waybillId);
    }

    /**
     * 分类根据车辆ID查询车辆运单
     */
    @ApiOperation(value = "分类根据车辆ID查询车辆运单")
    @PostMapping(value = "/car/get/{orderCarId}/{waybillType}")
    public ResultVo<List<WaybillCarVo>> getByType(@ApiParam(value = "运单车辆ID") @PathVariable Long orderCarId,
                                                  @ApiParam(value = "运单类型：1提车运单，2干线运单，3送车运单") @PathVariable Integer waybillType) {
        return waybillService.getCarByType(orderCarId, waybillType);
    }
















    /**
     * 我的运单-承运商
     */
    @ApiOperation(value = "我的运单-承运商")
    @PostMapping(value = "/cr/list")
    public ResultVo<List<CrWaybillVo>> crList(@RequestBody CrWaybillDto reqDto) {
        return waybillService.crList(reqDto);
    }


















}
