package com.cjyc.web.api.controller;

import com.cjkj.log.monitor.LogUtil;
import com.cjyc.common.model.dto.web.waybill.*;
import com.cjyc.common.model.enums.ResultEnum;
import com.cjyc.common.model.util.BaseResultUtil;
import com.cjyc.common.model.util.ExcelUtil;
import com.cjyc.common.model.vo.BaseTipVo;
import com.cjyc.common.model.vo.ListVo;
import com.cjyc.common.model.vo.PageVo;
import com.cjyc.common.model.vo.ResultVo;
import com.cjyc.common.model.vo.web.waybill.*;
import com.cjyc.common.system.service.ICsAdminService;
import com.cjyc.common.system.service.ICsWaybillService;
import com.cjyc.web.api.service.IWaybillService;
import com.google.common.collect.Lists;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.util.CollectionUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

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
    private ICsWaybillService csWaybillService;
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
    public ResultVo saveLocal(@Valid @RequestBody SaveLocalDto reqDto) {
        //验证用户
        ResultVo<SaveLocalDto> resVo = csAdminService.validateEnabled(reqDto);
        if(ResultEnum.SUCCESS.getCode() != resVo.getCode()){
            return BaseResultUtil.fail(resVo.getMsg());
        }
        return csWaybillService.saveLocal(resVo.getData());
    }

    /**
     * 修改同城调度
     *
     * @author JPG
     * @since 2019/10/15 11:53
     */
    @ApiOperation(value = "修改同城调度", consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @PostMapping("/local/update")
    public ResultVo updateLocal(@Validated @RequestBody UpdateLocalDto reqDto) {
        //验证用户
        ResultVo<UpdateLocalDto> resVo = csAdminService.validateEnabled(reqDto);
        if(ResultEnum.SUCCESS.getCode() != resVo.getCode()){
            return BaseResultUtil.fail(resVo.getMsg());
        }
        return csWaybillService.updateLocal(resVo.getData());
    }

    /**
     * 干线调度
     *
     * @author JPG
     * @since 2019/10/15 11:53
     */
    @ApiOperation("干线调度")
    @PostMapping("/trunk/save")
    public ResultVo saveTrunk(@Validated @RequestBody SaveTrunkWaybillDto reqDto) {
        //验证用户
        ResultVo<SaveTrunkWaybillDto> resVo = csAdminService.validateEnabled(reqDto);
        if(ResultEnum.SUCCESS.getCode() != resVo.getCode()){
            return BaseResultUtil.fail(resVo.getMsg());
        }
        return csWaybillService.saveTrunk(resVo.getData());
    }

    /**
     * 修改干线运单
     *
     * @author JPG
     * @since 2019/10/15 11:53
     */
    @ApiOperation("修改干线调度")
    @PostMapping("/trunk/update")
    public ResultVo updateTrunk(@Validated @RequestBody UpdateTrunkWaybillDto reqDto) {
        //验证用户
        ResultVo<UpdateTrunkWaybillDto> resVo = csAdminService.validateEnabled(reqDto);
        if(ResultEnum.SUCCESS.getCode() != resVo.getCode()){
            return BaseResultUtil.fail(resVo.getMsg());
        }
        return csWaybillService.updateTrunk(resVo.getData());
    }

    /**
     * 中途卸载车辆
     *
     * @author JPG
     * @since 2019/10/15 11:53
     */
    @ApiOperation("中途卸载车辆")
    @PostMapping("/trunk/midway/unload")
    public ResultVo trunkMidwayUnload(@Validated @RequestBody TrunkMidwayUnload reqDto) {
        //验证用户
        ResultVo<TrunkMidwayUnload> resVo = csAdminService.validateEnabled(reqDto);
        if(ResultEnum.SUCCESS.getCode() != resVo.getCode()){
            return BaseResultUtil.fail(resVo.getMsg());
        }
        return csWaybillService.trunkMidwayUnload(resVo.getData());
    }


    /**
     * 取消调度
     *
     * @author JPG
     * @since 2019/10/15 11:53
     */
    @ApiOperation("取消调度")
    @PostMapping("/cancel")
    public ResultVo<ListVo<BaseTipVo>> cancel(@Validated @RequestBody CancelWaybillDto reqDto) {
        //验证用户
        ResultVo<CancelWaybillDto> resVo = csAdminService.validateEnabled(reqDto);
        if(ResultEnum.SUCCESS.getCode() != resVo.getCode()){
            return BaseResultUtil.fail(resVo.getMsg());
        }
        return csWaybillService.cancel(resVo.getData());
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

    @Deprecated
    @ApiOperation(value = "导出分页同城运单列表")
    @GetMapping(value = "/local/exportPageList")
    public ResultVo exportPageLocalList(LocalListWaybillCarDto reqDto, HttpServletResponse response) {
        ResultVo<PageVo<LocalListWaybillCarVo>> vo = waybillService.locallist(reqDto);
        if (!isResultSuccess(vo)) {
            return BaseResultUtil.fail("导出数据异常");
        }
        PageVo<LocalListWaybillCarVo> data = vo.getData();
        if (null == data || CollectionUtils.isEmpty(data.getList())) {
            return BaseResultUtil.success("未查询到数据");
        }
        try {
            ExcelUtil.exportExcel(data.getList(), "运单信息", "运单信息",
                    LocalListWaybillCarVo.class, System.currentTimeMillis() + "运单信息.xls", response);
            return null;
        } catch (Exception e) {
            LogUtil.error("导出分页同城运单列表信息异常", e);
            return BaseResultUtil.fail("导出分页同城运单列表信息异常: " + e.getMessage());
        }
    }

    @ApiOperation(value = "导出全部同城运单列表")
    @GetMapping(value = "/local/exportAllList")
    public void exportAllList(LocalListWaybillCarDto reqDto,
                              HttpServletResponse response) {
        List<LocalListWaybillCarVo> list = waybillService.localAllList(reqDto);
        if (CollectionUtils.isEmpty(list)) {
            com.cjyc.web.api.util.ExcelUtil.printExcelResult(com.cjyc.web.api.util.ExcelUtil.getWorkBookForShowMsg("提示信息", "结果为空"),
                    "结果为空.xls", response);
            return;
        }
        try {
            List<ExportLocalListWaybillCarVo> exportList = Lists.newArrayList();
            for (LocalListWaybillCarVo vo : list) {
                ExportLocalListWaybillCarVo excelVo = new ExportLocalListWaybillCarVo();
                BeanUtils.copyProperties(vo, excelVo);
                exportList.add(excelVo);
            }
            ExcelUtil.exportExcel(exportList, "运单信息", "运单信息",
                    ExportLocalListWaybillCarVo.class, System.currentTimeMillis() + "运单信息.xls", response);
            return;
        } catch (Exception e) {
            LogUtil.error("导出全部同城运单列表信息异常", e);
            com.cjyc.web.api.util.ExcelUtil.printExcelResult(com.cjyc.web.api.util.ExcelUtil.getWorkBookForShowMsg("提示信息", "导出全部同城运单列表信息异常: " + e.getMessage()),
                    "导出异常.xls", response);
        }
    }

    /**
     * 查询干线主运单列表
     */
    @ApiOperation(value = "查询干线主运单列表")
    @PostMapping(value = "/trunk/main/list")
    public ResultVo<PageVo<TrunkMainListWaybillVo>> getTrunkMainList(@RequestBody TrunkMainListWaybillDto reqDto) {
        return waybillService.getTrunkMainList(reqDto);
    }

    @Deprecated
    @ApiOperation(value = "导出分页干线主运单列表")
    @GetMapping(value = "/trunk/main/exportPageList")
    public ResultVo exportMainPageList(TrunkMainListWaybillDto reqDto, HttpServletResponse response) {
        ResultVo<PageVo<TrunkMainListWaybillVo>> vo = waybillService.getTrunkMainList(reqDto);
        if (!isResultSuccess(vo)) {
            return BaseResultUtil.fail("导出数据异常");
        }
        PageVo<TrunkMainListWaybillVo> data = vo.getData();
        if (null == data || CollectionUtils.isEmpty(data.getList())) {
            return BaseResultUtil.success("未查询到数据");
        }
        try {
            ExcelUtil.exportExcel(data.getList(), "运单信息", "运单信息",
                    TrunkMainListWaybillVo.class, System.currentTimeMillis() + "运单信息.xls", response);
            return null;
        } catch (Exception e) {
            LogUtil.error("导出分页干线主运单列表异常", e);
            return BaseResultUtil.fail("导出分页干线主运单列表异常: " + e.getMessage());
        }
    }

    @ApiOperation(value = "导出全部干线主运单列表")
    @GetMapping(value = "/trunk/main/exportAllList")
    public void exportMainAllList(TrunkMainListWaybillDto reqDto, HttpServletResponse response) {
        List<TrunkMainListWaybillVo> list = waybillService.getTrunkMainAllList(reqDto);
        if (CollectionUtils.isEmpty(list)) {
            com.cjyc.web.api.util.ExcelUtil.printExcelResult(com.cjyc.web.api.util.ExcelUtil.getWorkBookForShowMsg("提示信息", "未查询到结果信息"),
                    "结果为空.xls", response);
            return;
        }
        try {
            List<ExportTrunkMainListWaybillVo> exportList = Lists.newArrayList();
            for (TrunkMainListWaybillVo vo : list) {
                ExportTrunkMainListWaybillVo exportVo = new ExportTrunkMainListWaybillVo();
                BeanUtils.copyProperties(vo, exportVo);
                exportList.add(exportVo);
            }
            ExcelUtil.exportExcel(exportList, "运单信息-干线", "运单信息-干线",
                    ExportTrunkMainListWaybillVo.class, System.currentTimeMillis() + "运单信息-干线.xls", response);
            return;
        } catch (Exception e) {
            LogUtil.error("导出全部干线主运单列表信息异常", e);
            com.cjyc.web.api.util.ExcelUtil.printExcelResult(
                    com.cjyc.web.api.util.ExcelUtil.getWorkBookForShowMsg("提示信息",
                            "导出全部干线主运单列表信息异常: " + e.getMessage()),
                    "导出异常.xls", response);
        }
    }

    /**
     * 查询干线子运单（任务）列表
     */
    @ApiOperation(value = "查询干线子运单（任务）列表")
    @PostMapping(value = "/trunk/sub/list")
    public ResultVo<PageVo<TrunkSubListWaybillVo>> getTrunkSubList(@RequestBody TrunkSubListWaybillDto reqDto) {
        return waybillService.getTrunkSubList(reqDto);
    }

    @Deprecated
    @ApiOperation(value = "导出分页运单干线子运单列表信息")
    @GetMapping(value = "/trunk/sub/exportPageList")
    public ResultVo exportTrunkSubPageList(TrunkSubListWaybillDto reqDto, HttpServletResponse response) {
        ResultVo<PageVo<TrunkSubListWaybillVo>> vo = waybillService.getTrunkSubList(reqDto);
        if (!isResultSuccess(vo)) {
            return BaseResultUtil.fail("导出数据异常");
        }
        PageVo<TrunkSubListWaybillVo> data = vo.getData();
        if (null == data || CollectionUtils.isEmpty(data.getList())) {
            return BaseResultUtil.success("未查询到数据");
        }

        try {
            ExcelUtil.exportExcel(data.getList(), "运单干线子运单信息", "运单干线子运单信息",
                    TrunkSubListWaybillVo.class, System.currentTimeMillis() + "运单干线子运单信息.xls", response);
            return null;
        } catch (Exception e) {
            LogUtil.error("导出分页运单干线子运单列表异常", e);
            return BaseResultUtil.fail("导出分页运单干线子运单列表异常: " + e.getMessage());
        }
    }

    @ApiOperation(value = "导出全部运单干线子运单列表信息")
    @GetMapping(value = "/trunk/sub/exportAllList")
    public void exportTrunkSubAllList(TrunkSubListWaybillDto reqDto, HttpServletResponse response) {
        ResultVo<List<TrunkSubListExportVo>> listRs = waybillService.getTrunkSubAllList(reqDto);
        if (!isResultSuccess(listRs)) {
            com.cjyc.web.api.util.ExcelUtil.printExcelResult(
                    com.cjyc.web.api.util.ExcelUtil.getWorkBookForShowMsg("提示信息", listRs.getMsg()),
                    "导出异常.xls", response);
            return;
        }
        List<TrunkSubListExportVo> list = listRs.getData();
        if (CollectionUtils.isEmpty(list)) {
            com.cjyc.web.api.util.ExcelUtil.printExcelResult(com.cjyc.web.api.util.ExcelUtil.getWorkBookForShowMsg("提示信息", "未查询到结果信息"),
                    "结果为空.xls", response);
            return;
        }
        try {
//            List<ExportTrunkMainListWaybillVo> rsList = dealTrunkSubListForExport(list);
            List<ExportTrunkSubListWaybillVo> exportSubList = Lists.newArrayList();
            for (TrunkSubListExportVo vo : list) {
                ExportTrunkSubListWaybillVo exportSub = new ExportTrunkSubListWaybillVo();
                BeanUtils.copyProperties(vo, exportSub);
                exportSubList.add(exportSub);
            }
            ExcelUtil.exportExcel(exportSubList, "运单干线子运单信息", "运单干线子运单信息",
                    ExportTrunkSubListWaybillVo.class, System.currentTimeMillis() + "运单干线子运单信息.xls", response);
            return;
        } catch (Exception e) {
            LogUtil.error("导出全部运单干线子运单列表异常", e);
            com.cjyc.web.api.util.ExcelUtil.printExcelResult(com.cjyc.web.api.util.ExcelUtil.getWorkBookForShowMsg("提示信息", "导出全部运单干线子运单列表异常: " + e.getMessage()),
                    "导出异常.xls", response);
        }
    }

    /**
     * 查询干线运单车辆列表
     */
    @ApiOperation(value = "查询干线运单车辆列表")
    @PostMapping(value = "/trunk/car/list")
    public ResultVo<PageVo<TrunkCarListWaybillCarVo>> getCarTrunklist(@RequestBody TrunkListWaybillCarDto reqDto) {
        return waybillService.trunkCarlist(reqDto);
    }

    @ApiOperation(value = "干线运单明细导出")
    @GetMapping(value = "/trunk/car/exportAllList")
    public void exportAllList(TrunkListWaybillCarDto reqDto, HttpServletResponse response) {
        ResultVo<List<TrunkCarDetailExportVo>> listRs = waybillService.trunkCarAllList(reqDto);
        if (!isResultSuccess(listRs)) {
            com.cjyc.web.api.util.ExcelUtil.printExcelResult(
                    com.cjyc.web.api.util.ExcelUtil.getWorkBookForShowMsg("提示信息", listRs.getMsg()),
                    "导出异常.xls", response);
            return;
        }
        List<TrunkCarDetailExportVo> list = listRs.getData();
        if (CollectionUtils.isEmpty(list)) {
            com.cjyc.web.api.util.ExcelUtil.printExcelResult(
                    com.cjyc.web.api.util.ExcelUtil.getWorkBookForShowMsg("提示信息",
                            "未查询到结果信息"),
                    "结果为空.xls", response);
            return;
        }
        list = list.stream().filter(l -> l != null).collect(Collectors.toList());
        try {
            ExcelUtil.exportExcel(list, "运单干线明细", "运单干线明细",
                    TrunkCarDetailExportVo.class, System.currentTimeMillis() + "运单干线明细.xls", response);
            return;
        } catch (Exception e) {
            LogUtil.error("导出运单干线明细信息异常", e);
            com.cjyc.web.api.util.ExcelUtil.printExcelResult(
                    com.cjyc.web.api.util.ExcelUtil.getWorkBookForShowMsg("提示信息",
                            "导出运单干线明细信息异常: " + e.getMessage()),
                    "导出异常.xls", response);
        }
    }

    /**
     * 查询运单（含车辆）
     */
    @Deprecated
    @ApiOperation(value = "查询运单（含车辆）")
    @PostMapping(value = "/get/{waybillId}")
    public ResultVo<WaybillVo> get(@ApiParam(value = "运单ID") @PathVariable Long waybillId) {
        return waybillService.get(waybillId);
    }

    /**
     * 查询运单（含车辆）
     */
    @ApiOperation(value = "查询运单（含车辆）")
    @PostMapping(value = "/get")
    public ResultVo<WaybillVo> get(@RequestBody GetDto reqDto) {
        return waybillService.get(reqDto);
    }

    /**
     * 分类根据车辆ID查询车辆运单
     */
    @ApiOperation(value = "分类根据车辆ID查询车辆运单")
    @PostMapping(value = "/car/get/{orderCarId}/{waybillType}")
    public ResultVo<List<WaybillCarTransportVo>> getByType(@ApiParam(value = "运单车辆ID") @PathVariable Long orderCarId,
                                                           @ApiParam(value = "运单类型：1提车运单，2干线运单，3送车运单") @PathVariable Integer waybillType) {
        return waybillService.getCarByType(orderCarId, waybillType);
    }

    /**----承运商模块-------------------------------------------------------------------------------------*/

    /**
     * 我的运单-承运商
     */
    @ApiOperation(value = "我的运单-承运商")
    //@PostMapping(value = "/cr/list")
    public ResultVo<PageVo<CrWaybillVo>> crList(@RequestBody CrWaybillDto reqDto) {
        return waybillService.crListForMineCarrier(reqDto);
    }

    /**----我的业务中心-------------------------------------------------------------------------------------*/

    /**
     * 车辆入库列表
     * @author JPG
     */
/*    @ApiOperation(value = "车辆入库列表")
    @PostMapping(value = "/in/store/list")
    public ResultVo<PageVo<InStoreListVo>> inStoreList(@RequestBody storeListDto reqDto) {
        return waybillService.inStoreList(reqDto);
    }*/

    /**
     * 检查返回结果是否成功
     *
     * @param resultVo
     * @return
     */
    private boolean isResultSuccess(ResultVo resultVo) {
        if (null == resultVo) {
            return false;
        }
        return resultVo.getCode() == ResultEnum.SUCCESS.getCode();
    }


    /************************************韵车集成改版 st***********************************/

    /**
     * 我的运单-承运商
     */
    @ApiOperation(value = "我的运单-承运商_改版")
    //@PostMapping(value = "/cr/listNew")
    @PostMapping(value = "/cr/list")
    public ResultVo<PageVo<CrWaybillVo>> crListNew(@RequestBody CrWaybillDto reqDto) {
        return waybillService.crListForMineCarrierNew(reqDto);
    }

    @ApiOperation(value = "我的公司-我的运单导出Excel", notes = "\t 请求接口为/waybill/exportCrListExcel?waybillNo=运单编号&carrierId=承运商id")
    @GetMapping("/exportCrListExcel")
    public void exportCrListExcel(HttpServletRequest request, HttpServletResponse response) {
        waybillService.exportCrListExcel(request, response);
    }
}
