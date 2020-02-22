package com.cjyc.salesman.api.controller;

import com.cjyc.common.model.dto.CommonDto;
import com.cjyc.common.model.dto.salesman.dispatch.*;
import com.cjyc.common.model.dto.web.carrier.DispatchCarrierDto;
import com.cjyc.common.model.dto.web.carrier.TrailCarrierDto;
import com.cjyc.common.model.dto.web.dispatch.ValidateLineDto;
import com.cjyc.common.model.dto.web.dispatch.ValidateLineShellDto;
import com.cjyc.common.model.dto.web.driver.DispatchDriverDto;
import com.cjyc.common.model.dto.web.order.ComputeCarEndpointDto;
import com.cjyc.common.model.vo.ListVo;
import com.cjyc.common.model.vo.PageVo;
import com.cjyc.common.model.vo.ResultVo;
import com.cjyc.common.model.vo.salesman.dispatch.*;
import com.cjyc.common.model.vo.web.carrier.DispatchCarrierVo;
import com.cjyc.common.model.vo.web.carrier.TrailCarrierVo;
import com.cjyc.common.model.vo.web.dispatch.WaitCountVo;
import com.cjyc.common.model.vo.web.driver.DispatchDriverVo;
import com.cjyc.common.model.vo.web.order.DispatchAddCarVo;
import com.cjyc.common.system.service.ICsCarrierService;
import com.cjyc.common.system.service.ICsDriverService;
import com.cjyc.common.system.service.ICsLineService;
import com.cjyc.common.system.service.ICsOrderService;
import com.cjyc.salesman.api.service.IDispatchService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang.Validate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Enumeration;
import java.util.List;

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
    @Resource
    private ICsLineService csLineService;

    /**
     * 功能描述: 查询所有出发城市-目的地城市的车辆数量
     * @author liuxingxiang
     * @date 2019/12/9
     * @param dto
     * @return com.cjyc.common.model.vo.ResultVo<com.cjyc.common.model.vo.PageVo<com.cjyc.common.model.vo.salesman.task.TaskWaybillVo>>
     */
    @Deprecated
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
    @Deprecated
    @ApiOperation(value = "调度列表信息")
    @PostMapping("/list")
    public ResultVo<PageVo<DispatchListVo>> list(@Valid @RequestBody DispatchListDto dto) {
        return dispatchService.getPageList(dto);
    }

    /**
     * @author JPG
     */
    @ApiOperation(value = "调度池列表")
    @PostMapping(value = "/wait/list")
    public ResultVo<PageVo<WaitDispatchCarListVo>> waitList(@Validated @RequestBody DispatchListDto reqDto) {
        return dispatchService.waitList(reqDto);
    }
    /**
     * @author JPG
     */
    @ApiOperation(value = "调度池按城市分组统计数量列表")
    @PostMapping(value = "/wait/count/list")
    public ResultVo<ListVo<WaitCountVo>> waitCountList(@Validated @RequestBody WaitCountDto reqDto) {
        return dispatchService.waitCountList(reqDto);
    }
    /**
     * @author JPG
     */
    @ApiOperation(value = "根据订单车辆ID查询调度起始地和目的地")
    @PostMapping(value = "/car/from/to/get")
    public ResultVo<DispatchAddCarVo> computerCarEndpoint(@RequestBody ComputeCarEndpointDto reqDto) {
        return csOrderService.computerCarEndpoint(reqDto);
    }

    /**
     * @author JPG
     */
    @ApiOperation(value = "根据订单车辆ID查询调度起始地和目的地")
    @PostMapping(value = "/lines/validate")
    public ResultVo<ListVo<ValidateLineVo>> validateLines(@RequestBody ValidateLineShellDto reqDto) {
        return csLineService.validateLines(reqDto);
    }

    /**
     * 功能描述: 调度页面-根据车辆编号查询车辆明细
     * @author liuxingxiang
     * @date 2019/12/13
     * @param dto
     * @return com.cjyc.common.model.vo.ResultVo
     */
    @ApiOperation(value = "调度页面-根据车辆编号查询车辆明细")
    @PostMapping("/getCarDetail")
    public ResultVo<DispatchCarDetailVo> getCarDetail(@RequestBody @Validated CarDetailDto dto) {
        return dispatchService.getCarDetail(dto.getCarNo());
    }

    /**
     * 功能描述: 调度页面-查询历史调度记录列表分页
     * @author liuxingxiang
     * @date 2019/12/13
     * @param dto
     * @return com.cjyc.common.model.vo.ResultVo<com.cjyc.common.model.vo.salesman.dispatch.HistoryDispatchRecordVo>
     */
    @ApiOperation(value = "调度页面-查询历史记录列表分页")
    @PostMapping("/getHistoryRecord")
    public ResultVo<PageVo<HistoryDispatchRecordVo>> getHistoryRecord(@RequestBody @Validated HistoryDispatchRecordDto dto) {
        return dispatchService.getHistoryRecord(dto);
    }

    @ApiOperation(value = "调度中心中干线调度承运商信息")
    @PostMapping(value = "/dispatchCarrier")
    public ResultVo<PageVo<DispatchCarrierVo>> dispatchCarrier(@RequestBody DispatchCarrierDto dto){
        return csCarrierService.dispatchAppCarrier(dto);
    }

    @ApiOperation(value = "干线调度个人(承运商)司机信息")
    //@PostMapping(value = "/dispatchDriver")
    public ResultVo<PageVo<DispatchDriverVo>> dispatchDriver(@RequestBody DispatchDriverDto dto){
        return csDriverService.dispatchDriver(dto);
    }

    @ApiOperation(value = "调度中心中提车/送车调度中代驾和拖车列表")
    //@PostMapping(value = "/traileDriver")
    public ResultVo<PageVo<TrailCarrierVo>> trailDriver(@RequestBody TrailCarrierDto dto){
        return csCarrierService.trailDriver(dto);
    }

    /**
     * 功能描述: 历史记录页面-根据运单ID查询历史调度记录明细
     * @author liuxingxiang
     * @date 2019/12/16
     * @param dto
     * @return com.cjyc.common.model.vo.ResultVo
     */
    @ApiOperation(value = "历史记录页面-根据运单ID查询历史调度记录明细")
    @PostMapping(value = "/getWaybillDetail")
    public ResultVo<WaybillDetailVo> getWaybillDetail(@RequestBody @Validated WaybillDetailDto dto){
        return dispatchService.getWaybillDetail(dto.getWaybillId());
    }


    /************************************韵车集成改版 st***********************************/

    @ApiOperation(value = "干线调度个人(承运商)司机信息_改版")
    //@PostMapping(value = "/dispatchDriverNew")
    @PostMapping(value = "/dispatchDriver")
    public ResultVo<PageVo<DispatchDriverVo>> dispatchDriverNew(@RequestBody DispatchDriverDto dto){
        return csDriverService.dispatchAppDriverNew(dto);
    }

    @ApiOperation(value = "调度中心中提车/送车调度中代驾和拖车列表_改版")
    //@PostMapping(value = "/traileDriver")
    @PostMapping(value = "/traileDriver")
    public ResultVo<PageVo<TrailCarrierVo>> trailDriverNew(@RequestBody TrailCarrierDto dto){
        return csCarrierService.trailDriverNew(dto);
    }

}
