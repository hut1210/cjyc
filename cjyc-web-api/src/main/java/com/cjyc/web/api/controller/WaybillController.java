package com.cjyc.web.api.controller;

import com.cjyc.common.model.dto.web.waybill.CancelDispatchDto;
import com.cjyc.common.model.dto.web.waybill.WaybillPickDispatchListDto;
import com.cjyc.common.model.dto.web.waybill.WaybillTrunkDispatchListListDto;
import com.cjyc.common.model.vo.BaseTipVo;
import com.cjyc.common.model.vo.ListVo;
import com.cjyc.common.model.vo.ResultVo;
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

    /**
     * 提送车调度
     *
     * @author JPG
     * @since 2019/10/15 11:53
     */
    @ApiOperation("提送车调度")
    @PostMapping("/pickAndBack/dispatch")
    public ResultVo pickAndBackDispatch(@RequestBody WaybillPickDispatchListDto reqDto) {
        return waybillService.pickAndBackDispatch(reqDto);
    }


    /**
     * 干线调度
     *
     * @author JPG
     * @since 2019/10/15 11:53
     */
    @ApiOperation("干线调度")
    @PostMapping("/trunk/dispatch")
    public ResultVo trunkDispatch(@RequestBody WaybillTrunkDispatchListListDto reqDto) {
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
    public ResultVo updateTrunkDispatch(@RequestBody WaybillTrunkDispatchListListDto reqDto) {
        return waybillService.trunkDispatch(reqDto);
    }



    /**
     * TODO 取消调度
     *
     * @author JPG
     * @since 2019/10/15 11:53
     */
    @ApiOperation("取消调度")
    @PostMapping("/dispatch/cancel")
    public ResultVo<ListVo<BaseTipVo>> cancelDispatch(@RequestBody CancelDispatchDto reqDto) {
        return waybillService.cancelDispatch(reqDto);
    }




}
