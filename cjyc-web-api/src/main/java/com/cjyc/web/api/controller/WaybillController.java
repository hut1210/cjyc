package com.cjyc.web.api.controller;

import com.cjyc.common.model.dto.web.waybill.CancelDispatchDto;
import com.cjyc.common.model.dto.web.waybill.HistoryListWaybillDto;
import com.cjyc.common.model.dto.web.waybill.LocalDispatchListWaybillDto;
import com.cjyc.common.model.dto.web.waybill.TrunkDispatchListShellWaybillDto;
import com.cjyc.common.model.entity.Admin;
import com.cjyc.common.model.enums.AdminStateEnum;
import com.cjyc.common.model.util.BaseResultUtil;
import com.cjyc.common.model.vo.BaseTipVo;
import com.cjyc.common.model.vo.ListVo;
import com.cjyc.common.model.vo.ResultVo;
import com.cjyc.common.model.vo.web.waybill.HistoryListWaybillVo;
import com.cjyc.web.api.service.IAdminService;
import com.cjyc.web.api.service.IWaybillService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
     * 取消调度
     *
     * @author JPG
     * @since 2019/10/15 11:53
     */
    @ApiOperation("取消调度")
    @PostMapping("/dispatch/cancel")
    public ResultVo<ListVo<BaseTipVo>> cancelDispatch(@RequestBody CancelDispatchDto reqDto) {
        return waybillService.cancelDispatch(reqDto);
    }



    /**
     * 根据订单车辆ID查询历史运单
     */
    @ApiOperation(value = "根据订单车辆ID查询历史运单")
    @PostMapping(value = "/car/history/list")
    public ResultVo<List<HistoryListWaybillVo>> carHistoryList(@RequestBody HistoryListWaybillDto reqDto) {
        return waybillService.historyList(reqDto);
    }










}
