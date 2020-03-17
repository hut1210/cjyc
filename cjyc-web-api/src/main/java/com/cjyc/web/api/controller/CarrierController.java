package com.cjyc.web.api.controller;

import com.cjyc.common.model.dto.CarrierDriverDto;
import com.cjyc.common.model.dto.KeywordDto;
import com.cjyc.common.model.dto.web.OperateDto;
import com.cjyc.common.model.dto.web.carrier.*;
import com.cjyc.common.model.enums.ResultEnum;
import com.cjyc.common.model.util.BaseResultUtil;
import com.cjyc.common.model.vo.PageVo;
import com.cjyc.common.model.vo.ResultVo;
import com.cjyc.common.model.vo.web.bankInfo.BankInfoVo;
import com.cjyc.common.model.vo.web.carrier.*;
import com.cjyc.common.system.service.ICsBankInfoService;
import com.cjyc.common.system.service.ICsCarrierService;
import com.cjyc.common.system.service.ICsDriverService;
import com.cjyc.web.api.service.ICarrierService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 *  @author: zj
 *  @Date: 2019/10/18 15:13
 *  @Description:承运商管理
 */
@Api(tags = "司机-承运商")
@CrossOrigin
@RestController
@RequestMapping("/carrier")
public class CarrierController {

    @Resource
    private ICarrierService carrierService;
    @Resource
    private ICsDriverService csDriverService;
    @Resource
    private ICsCarrierService csCarrierService;
    @Resource
    private ICsBankInfoService csBankInfoService;

    @ApiOperation(value = "新增/修改承运商")
//    @PostMapping(value = "/saveOrModifyCarrier")
    public ResultVo saveOrModifyCarrier(@Validated @RequestBody CarrierDto dto){
        return carrierService.saveOrModifyCarrier(dto);
    }

    @ApiOperation(value = "根据条件查询承运商")
//    @PostMapping(value = "/findCarrier")
    public ResultVo<PageVo<CarrierVo>> findCarrier(@RequestBody SeleCarrierDto dto){
        return carrierService.findCarrier(dto);
    }

    @ApiOperation(value = "根据id审核/冻结/解冻承运商")
//    @PostMapping(value = "/verifyCarrier")
    public ResultVo verifyCarrier(@RequestBody OperateDto dto){
        return carrierService.verifyCarrier(dto);
    }

    @ApiOperation(value = "根据carrierId查看基本承运商信息")
    @PostMapping(value = "/showBaseCarrier/{carrierId}")
    public ResultVo<BaseCarrierVo> showBaseCarrier(@PathVariable @ApiParam(value = "承运商id",required = true) Long carrierId){
        return carrierService.showBaseCarrier(carrierId);
    }

//    @PostMapping("/resetPwd/{id}")
    @ApiOperation(value = "承运商机构超级管理员账户重置密码")
    public ResultVo resetPwd(@ApiParam(name = "id", value = "机构标识", required = true)
                             @PathVariable Long id) {
        //重置机构超级管理员用户密码
        return carrierService.resetPwd(id);
    }

    @ApiOperation(value = "新增/修改承运商下司机")
    @PostMapping(value = "/saveOrModifyDriver")
    public ResultVo saveOrModifyDriver(@Validated @RequestBody CarrierDriverDto dto){
        return csDriverService.saveOrModifyDriver(dto);
    }

    @ApiOperation(value = "根据carrierId查看基本司机信息")
    //@PostMapping(value = "/transportDriver")
    public ResultVo<PageVo<TransportDriverVo>> transportDriver(@Validated @RequestBody TransportDto dto){
        return carrierService.transportDriver(dto);
    }

    @ApiOperation(value = "根据carrierId查看基本车辆信息")
    //@PostMapping(value = "/transportVehicle")
    public ResultVo<PageVo<TransportVehicleVo>> transportVehicle(@Validated @RequestBody TransportDto dto){
        return carrierService.transportVehicle(dto);
    }

    @ApiOperation(value = "调度中心中干线调度承运商信息")
    @PostMapping(value = "/dispatchCarrier")
    public ResultVo<PageVo<DispatchCarrierVo>> dispatchCarrier(@RequestBody DispatchCarrierDto dto){
        return csCarrierService.dispatchCarrier(dto);
    }

    @ApiOperation(value = "调度中心中提车/送车调度中代驾和拖车列表")
//    @PostMapping(value = "/traileDriver")
    public ResultVo<PageVo<TrailCarrierVo>> trailDriver(@RequestBody TrailCarrierDto dto){
        return csCarrierService.trailDriver(dto);
    }

    @ApiOperation(value = "承运商管理导出Excel", notes = "\t 请求接口为/carrier/exportCarrierExcel?name=企业名称&linkman=联系人&linkmanPhone=联系电话" +
            "&cardNo=银行卡号&legalName=法人姓名&legalIdCard=法人身份证号&isInvoice=是否开发票&settleType=结算方式&state=状态&operateName=操作人")
    @GetMapping("/exportCarrierExcel")
    public void exportCarrierExcel(HttpServletRequest request, HttpServletResponse response){
        carrierService.exportCarrierExcel(request,response);
    }


    /*********************************韵车集成改版 st*****************************/
    @ApiOperation(value = "改版：新增/修改承运商")
//    @PostMapping(value = "/saveOrModifyCarrierNew")
    @PostMapping(value = "/saveOrModifyCarrier")
    public ResultVo saveOrModifyCarrierNew(@Validated @RequestBody CarrierDto dto){
        return carrierService.saveOrModifyCarrierNew(dto);
    }

    @ApiOperation(value = "根据条件查询承运商_改版")
//    @PostMapping(value = "/findCarrierNew")
    @PostMapping(value = "/findCarrier")
    public ResultVo<PageVo<CarrierVo>> findCarrierNew(@RequestBody SeleCarrierDto dto){
        return carrierService.findCarrierNew(dto);
    }

    @ApiOperation(value = "根据id审核/冻结/解冻承运商_改版")
//    @PostMapping(value = "/verifyCarrierNew")
    @PostMapping(value = "/verifyCarrier")
    public ResultVo verifyCarrierNew(@RequestBody OperateDto dto){
        return carrierService.verifyCarrierNew(dto);
    }

    @ApiOperation(value = "根据carrierId查看基本承运商信息_改版")
    @PostMapping(value = "/showBaseCarrierNew/{carrierId}")
    public ResultVo<BaseCarrierVo> showBaseCarrierNew(@PathVariable @ApiParam(value = "承运商id",required = true) Long carrierId){
        return carrierService.showBaseCarrierNew(carrierId);
    }

//    @PostMapping("/resetPwdNew/{id}")
    @PostMapping("/resetPwd/{id}")
    @ApiOperation(value = "承运商机构超级管理员账户重置密码_改版")
    public ResultVo resetPwdNew(@ApiParam(name = "id", value = "机构标识", required = true)
                             @PathVariable Long id) {
        return carrierService.resetPwdNew(id);
    }

    @ApiOperation(value = "调度中心中提车/送车调度中代驾和拖车列表_改版")
//    @PostMapping(value = "/traileDriverNew")
    @PostMapping(value = "/traileDriver")
    public ResultVo<PageVo<TrailCarrierVo>> trailDriverNew(@RequestBody TrailCarrierDto dto){
        return csCarrierService.trailDriverNew(dto);
    }

    @ApiOperation(value = "根据carrierId查看基本司机信息_改版")
    //@PostMapping(value = "/transportDriverNew")
    @PostMapping(value = "/transportDriver")
    public ResultVo<PageVo<TransportDriverVo>> transportDriverNew(@Validated @RequestBody TransportDto dto){
        return carrierService.transportDriverNew(dto);
    }

    @ApiOperation(value = "根据carrierId查看基本车辆信息_改版")
    //@PostMapping(value = "/transportVehicleNew")
    @PostMapping(value = "/transportVehicle")
    public ResultVo<PageVo<TransportVehicleVo>> transportVehicleNew(@Validated @RequestBody TransportDto dto){
        return carrierService.transportVehicleNew(dto);
    }


    @ApiOperation(value = "承运商导入Excel", notes = "\t 请求接口为/importCarrierExcel/loginId(导入用户ID)格式")
    @PostMapping("/importCarrierExcel/{loginId}")
    public ResultVo importCarrierExcel(@RequestParam("file") MultipartFile file, @PathVariable Long loginId){
        boolean result = carrierService.importCarrierExcel(file, loginId);
        return result ? BaseResultUtil.success() : BaseResultUtil.fail(ResultEnum.FAIL.getMsg());
    }

    @ApiOperation(value = "获取全部银行信息")
    @PostMapping(value = "/findBankInfo")
    public ResultVo<List<BankInfoVo>> findBankInfo(@Validated @RequestBody KeywordDto dto) {
        return csBankInfoService.findWebBankInfo(dto);
    }

    /*********************************韵车集成改版 ed*****************************/
}