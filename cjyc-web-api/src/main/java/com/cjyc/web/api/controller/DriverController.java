package com.cjyc.web.api.controller;

import com.cjyc.common.model.dto.web.OperateDto;
import com.cjyc.common.model.dto.web.driver.*;
import com.cjyc.common.model.dto.web.user.DriverListDto;
import com.cjyc.common.model.enums.ResultEnum;
import com.cjyc.common.model.util.BaseResultUtil;
import com.cjyc.common.model.vo.PageVo;
import com.cjyc.common.model.vo.ResultVo;
import com.cjyc.common.model.vo.web.driver.DispatchDriverVo;
import com.cjyc.common.model.vo.web.driver.DriverVo;
import com.cjyc.common.model.vo.web.driver.ExistDriverVo;
import com.cjyc.common.model.vo.web.driver.ShowDriverVo;
import com.cjyc.common.model.vo.web.user.DriverListVo;
import com.cjyc.common.system.service.ICsDriverService;
import com.cjyc.web.api.service.IDriverService;
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
 * 用户
 * @author JPG
 */
@Api(tags = "司机")
@RestController
@CrossOrigin
@RequestMapping(value = "/driver")
public class DriverController {

    @Resource
    private IDriverService driverService;
    @Resource
    private ICsDriverService csDriverService;

    /**
     * 查询司机列表
     * @author JPG
     */
    @ApiOperation(value = "按线路统计待调度车辆（统计列表）")
    @PostMapping(value = "/list")
    public ResultVo<PageVo<DriverListVo>> getDriverList(@RequestBody DriverListDto reqDto) {
        return driverService.lineWaitDispatchCarCountList(reqDto);
    }

    @ApiOperation(value = "新增/修改散户司机")
//    @PostMapping(value = "/saveOrModifyDriver")
    public ResultVo saveOrModifyDriver(@Validated @RequestBody DriverDto dto){
        return driverService.saveOrModifyDriver(dto);
    }

    @ApiOperation(value = "根据查询条件查看司机信息")
//    @PostMapping(value = "/findDriver")
    public ResultVo<PageVo<DriverVo>> findDriver(@RequestBody SelectDriverDto dto){
        return driverService.findDriver(dto);
    }

    @ApiOperation(value = "根据id进行审核通过/拒绝/冻结解冻")
//    @PostMapping(value = "/verifyDriver")
    public ResultVo verifyDriver(@Validated @RequestBody OperateDto dto){
        return driverService.verifyDriver(dto);
    }

    @ApiOperation(value = "根据承运商id(carrierId)查看司机信息")
//    @PostMapping(value = "/showDriver")
    public ResultVo<ShowDriverVo> showDriver(@Validated @RequestBody BaseCarrierIdDto dto){
        return driverService.showDriver(dto);
    }

    @ApiOperation(value = "app注册校验记录")
    @PostMapping(value = "/showExistDriver")
    public ResultVo<List<ExistDriverVo>> showExistDriver(){
        return driverService.showExistDriver();
    }

    @ApiOperation(value = "冻结/解除司机冻结状态")
    @PostMapping(value = "/resetState/{id}/{flag}")
    public ResultVo resetState(@ApiParam(name = "id", value = "司机表示", required = true)
                               @PathVariable Long id,
                               @ApiParam(name = "flag", value = "冻结/解除状态 1：冻结 2：解除", required = true)
                               @PathVariable Integer flag){
        return driverService.resetState(id, flag);
    }

    @ApiOperation(value = "调度个人司机信息")
//    @PostMapping(value = "/dispatchDriver")
    public ResultVo<PageVo<DispatchDriverVo>> dispatchDriver(@RequestBody DispatchDriverDto dto){
        return csDriverService.dispatchDriver(dto);
    }

    @ApiOperation(value = "查询承运商下属司机")
//    @PostMapping(value = "/carrier/driver/list")
    public ResultVo<PageVo<DispatchDriverVo>> dispatchDriver(@RequestBody CarrierDriverListDto dto){
        return driverService.carrierDrvierList(dto);
    }

    @ApiOperation(value = "司机管理导出Excel", notes = "\t 请求接口为/driver/exportDriverExcel?realName=司机姓名&phone=司机手机号&idCard=司机身份证号" +
            "&plateNo=车牌号&identity=身份&runningState=运行状态&state=状态&mode=承运方式")
    @GetMapping("/exportDriverExcel")
    public void exportDriverExcel(HttpServletRequest request, HttpServletResponse response){
        driverService.exportDriverExcel(request,response);
    }


    /************************************韵车集成改版 st***********************************/

    @ApiOperation(value = "新增/修改社会司机_改版")
//    @PostMapping(value = "/saveOrModifyDriverNew")
    @PostMapping(value = "/saveOrModifyDriver")
    public ResultVo saveOrModifyDriverNew(@Validated @RequestBody DriverDto dto){
        return driverService.saveOrModifyDriverNew(dto);
    }

    @ApiOperation(value = "根据查询条件查看司机信息_改版")
//    @PostMapping(value = "/findDriverNew")
    @PostMapping(value = "/findDriver")
    public ResultVo<PageVo<DriverVo>> findDriverNew(@RequestBody SelectDriverDto dto){
        return driverService.findDriverNew(dto);
    }

    @ApiOperation(value = "根据承运商id(carrierId)查看司机信息")
//    @PostMapping(value = "/showDriverNew")
    @PostMapping(value = "/showDriver")
    public ResultVo<ShowDriverVo> showDriverNew(@Validated @RequestBody BaseCarrierIdDto dto){
        return driverService.showDriverNew(dto);
    }
    
    @ApiOperation(value = "根据id进行审核通过/拒绝/冻结/解冻_改版")
//    @PostMapping(value = "/verifyDriverNew")
    @PostMapping(value = "/verifyDriver")
    public ResultVo verifyDriverNew(@Validated @RequestBody OperateDto dto){
        return driverService.verifyDriverNew(dto);
    }

    @ApiOperation(value = "调度社会司机信息")
//    @PostMapping(value = "/dispatchDriverNew")
    @PostMapping(value = "/dispatchDriver")
    public ResultVo<PageVo<DispatchDriverVo>> dispatchDriverNew(@RequestBody DispatchDriverDto dto){
        return csDriverService.dispatchDriverNew(dto);
    }

    @ApiOperation(value = "查询承运商下属司机")
//    @PostMapping(value = "/carrier/driver/listNew")
    @PostMapping(value = "/carrier/driver/list")
    public ResultVo<PageVo<DispatchDriverVo>> dispatchDriverNew(@RequestBody CarrierDriverListDto dto){
        return driverService.carrierDrvierListNew(dto);
    }

    @ApiOperation(value = "社会司机导入Excel", notes = "\t 请求接口为/importDriverExcel/loginId(导入用户ID)格式")
    @PostMapping("/importDriverExcel/{loginId}")
    public ResultVo importDriverExcel(@RequestParam("file") MultipartFile file, @PathVariable Long loginId){
        boolean result = driverService.importDriverExcel(file, loginId);
        return result ? BaseResultUtil.success() : BaseResultUtil.fail(ResultEnum.FAIL.getMsg());
    }

    @ApiOperation(value = "根据司机id,承运商id，手机号删除社会司机信息")
    @PostMapping(value = "/deleteDriverInfo")
    public ResultVo deleteDriverInfo(@Validated @RequestBody DeleteDriverDto dto){
        return driverService.deleteDriverInfo(dto);
    }
}
