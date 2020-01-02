package com.cjyc.salesman.api.controller;

import com.cjyc.common.model.dto.CommonDto;
import com.cjyc.common.model.dto.salesman.dispatch.CarDetailDto;
import com.cjyc.common.model.dto.salesman.dispatch.DispatchListDto;
import com.cjyc.common.model.dto.salesman.dispatch.HistoryDispatchRecordDto;
import com.cjyc.common.model.dto.salesman.dispatch.WaybillDetailDto;
import com.cjyc.common.model.dto.web.carrier.DispatchCarrierDto;
import com.cjyc.common.model.dto.web.carrier.TrailCarrierDto;
import com.cjyc.common.model.dto.web.driver.DispatchDriverDto;
import com.cjyc.common.model.dto.web.order.ComputeCarEndpointDto;
import com.cjyc.common.model.vo.PageVo;
import com.cjyc.common.model.vo.ResultVo;
import com.cjyc.common.model.vo.salesman.dispatch.*;
import com.cjyc.common.model.vo.web.carrier.DispatchCarrierVo;
import com.cjyc.common.model.vo.web.carrier.TrailCarrierVo;
import com.cjyc.common.model.vo.web.driver.DispatchDriverVo;
import com.cjyc.common.model.vo.web.order.DispatchAddCarVo;
import com.cjyc.common.system.service.ICsCarrierService;
import com.cjyc.common.system.service.ICsDriverService;
import com.cjyc.common.system.service.ICsOrderService;
import com.cjyc.salesman.api.service.IDispatchService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;

/**
 * @Description 调度模块接口控制层
 * @Author Liu Xing Xiang
 * @Date 2019/12/11 11:25
 **/
@Api(tags = "调度")
@RestController
@RequestMapping(value = "/dispatch", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
public class DispatchController {
    @Autowired
    private IDispatchService dispatchService;
    @Resource
    private ICsCarrierService csCarrierService;
    @Resource
    private ICsDriverService csDriverService;
    @Resource
    private ICsOrderService csOrderService;

    /**
     * 功能描述: 查询所有出发城市-目的地城市的车辆数量
     * @author liuxingxiang
     * @date 2019/12/9
     * @param dto
     * @return com.cjyc.common.model.vo.ResultVo<com.cjyc.common.model.vo.PageVo<com.cjyc.common.model.vo.salesman.task.TaskWaybillVo>>
     */
    @ApiOperation(value = "查询所有出发城市-目的地城市的车辆数量")
    @PostMapping("/getCityCarCount")
    public ResultVo<CityCarCountVo> getCityCarCount(@RequestBody @Validated CommonDto dto) {
        return dispatchService.getCityCarCount(dto.getLoginId());
    }

    /**
     * 功能描述: 调度列表信息
     * @author zhangcangman
     * @date 2019/12/13
     * @param dto
     * @return com.cjyc.common.model.vo.ResultVo<PageVo<DispatchListVo>>
     */
    @ApiOperation(value = "调度列表信息")
    @PostMapping("/list")
    public ResultVo<PageVo<DispatchListVo>> list(@Valid @RequestBody DispatchListDto dto) {
        return dispatchService.getPageList(dto);
    }

    /**
     * 查询待调度车辆列表（数据列表）
     * @author JPG
     */
    @Deprecated
    @ApiOperation(value = "查询待调度车辆列表")
    @PostMapping(value = "/wait/list")
    public ResultVo<PageVo<WaitDispatchCarListVo>> waitDispatchCarList(@RequestBody DispatchListDto reqDto) {
        return dispatchService.waitDispatchCarList(reqDto);
    }
    /**
     * 根据订单车辆ID查询可调度起始地和目的地
     */
    @ApiOperation(value = "根据订单车辆ID查询可调度起始地和目的地")
    @PostMapping(value = "/car/from/to/get")
    public ResultVo<DispatchAddCarVo> computerCarEndpoint(@RequestBody ComputeCarEndpointDto reqDto) {
        return csOrderService.computerCarEndpoint(reqDto);
    }

    /**
     * 功能描述: 根据车辆编号查询车辆明细
     * @author liuxingxiang
     * @date 2019/12/13
     * @param dto
     * @return com.cjyc.common.model.vo.ResultVo
     */
    @ApiOperation(value = "根据车辆编号查询车辆明细")
    @PostMapping("/getCarDetail")
    public ResultVo<DispatchCarDetailVo> getCarDetail(@RequestBody @Validated CarDetailDto dto) {
        return dispatchService.getCarDetail(dto.getCarNo());
    }

    /**
     * 功能描述: 查询历史调度记录列表分页
     * @author liuxingxiang
     * @date 2019/12/13
     * @param dto
     * @return com.cjyc.common.model.vo.ResultVo<com.cjyc.common.model.vo.salesman.dispatch.HistoryDispatchRecordVo>
     */
    @ApiOperation(value = "查询历史调度记录列表分页")
    @PostMapping("/getHistoryRecord")
    public ResultVo<HistoryDispatchRecordVo> getHistoryRecord(@RequestBody @Validated HistoryDispatchRecordDto dto) {
        return dispatchService.getHistoryRecord(dto);
    }

    @ApiOperation(value = "调度中心中干线调度承运商信息")
    @PostMapping(value = "/dispatchCarrier")
    public ResultVo<PageVo<DispatchCarrierVo>> dispatchCarrier(@RequestBody DispatchCarrierDto dto){
        return csCarrierService.dispatchCarrier(dto);
    }

    @ApiOperation(value = "干线调度个人(承运商)司机信息")
    @PostMapping(value = "/dispatchDriver")
    public ResultVo<PageVo<DispatchDriverVo>> dispatchDriver(@RequestBody DispatchDriverDto dto){
        return csDriverService.dispatchDriver(dto);
    }

    @ApiOperation(value = "调度中心中提车/送车调度中代驾和拖车列表")
    @PostMapping(value = "/traileDriver")
    public ResultVo<PageVo<TrailCarrierVo>> trailDriver(@RequestBody TrailCarrierDto dto){
        return csCarrierService.trailDriver(dto);
    }

    /**
     * 功能描述: 根据运单ID查询历史调度记录明细
     * @author liuxingxiang
     * @date 2019/12/16
     * @param dto
     * @return com.cjyc.common.model.vo.ResultVo
     */
    @ApiOperation(value = "根据运单ID查询历史调度记录明细")
    @PostMapping(value = "/getWaybillDetail")
    public ResultVo<WaybillDetailVo> getWaybillDetail(@RequestBody @Validated WaybillDetailDto dto){
        return dispatchService.getWaybillDetail(dto.getWaybillId());
    }

}
