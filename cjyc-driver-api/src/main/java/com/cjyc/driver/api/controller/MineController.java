package com.cjyc.driver.api.controller;

import com.cjyc.common.model.dto.CarrierDriverDto;
import com.cjyc.common.model.dto.driver.AppDriverDto;
import com.cjyc.common.model.dto.driver.BaseDriverDto;
import com.cjyc.common.model.dto.driver.mine.*;
import com.cjyc.common.model.dto.salesman.sms.CaptchaSendDto;
import com.cjyc.common.model.enums.CaptchaTypeEnum;
import com.cjyc.common.model.enums.ClientEnum;
import com.cjyc.common.model.vo.PageVo;
import com.cjyc.common.model.vo.ResultVo;
import com.cjyc.common.model.vo.driver.mine.*;
import com.cjyc.common.system.service.ICsDriverService;
import com.cjyc.common.system.service.ICsSmsService;
import com.cjyc.common.system.service.ICsVehicleService;
import com.cjyc.driver.api.service.IMineService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * 司机端
 * @author Zj
 */
@Api(tags = "我的")
@RestController
@RequestMapping(value = "/mine",
        consumes = MediaType.APPLICATION_JSON_UTF8_VALUE,
        produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
public class MineController {

    @Resource
    private IMineService mineService;
    @Resource
    private ICsVehicleService csVehicleService;
    @Resource
    private ICsDriverService csDriverService;
    @Resource
    private ICsSmsService csSmsService;

    @ApiOperation(value = "司机的银行卡信息")
    @PostMapping(value = "/findBinkCard")
    public ResultVo<BankCardVos> findBinkCard(@Validated @RequestBody AppDriverDto dto) {
        return mineService.findBinkCard(dto);
    }

    @ApiOperation(value = "司机管理信息(管理员中的)")
    @PostMapping(value = "/findDriver")
    public ResultVo<PageVo<DriverInfoVo>> findDriver(@Validated @RequestBody BaseDriverDto dto) {
        return mineService.findDriver(dto);
    }

    @ApiOperation(value = "该承运商下空闲车辆(管理员中的司机管理的新增/修改)")
    @PostMapping(value = "/findCompanyFreeVehicle")
    public ResultVo<CarrierVehicleVo> findCompanyFreeVehicle(@Validated @RequestBody CarrierVehicleNoDto dto){
        return csVehicleService.findCompanyFreeVehicle(dto);
    }

    @ApiOperation(value = "该承运商下空闲司机(管理员中的车辆管理中新增/修改车辆)")
    @PostMapping(value = "/findCompanyFreeDriver")
    public ResultVo<CarrierDriverVo> findCompanyFreeDriver(@Validated @RequestBody CarrierDriverNameDto dto){
        return csDriverService.findCompanyFreeDriver(dto);
    }

    @ApiOperation(value = "新增/修改承运商下司机")
    @PostMapping(value = "/saveOrModifyDriver")
    public ResultVo saveOrModifyDriver(@Validated @RequestBody CarrierDriverDto dto){
        return csDriverService.saveOrModifyDriver(dto);
    }

    @ApiOperation(value = "新增/修改车辆")
    @PostMapping(value = "/saveOrModifyVehicle")
    public ResultVo saveOrModifyVehicle(@Validated @RequestBody AppCarrierVehicleDto dto){
        return mineService.saveOrModifyVehicle(dto);
    }

    @ApiOperation(value = "删除(冻结)承运商下司机")
    @PostMapping(value = "/frozenDriver")
    public ResultVo frozenDriver(@Validated @RequestBody FrozenDriverDto dto){
        return mineService.frozenDriver(dto);
    }

    @ApiOperation(value = "查询没有被绑定的社会车辆信息")
    @PostMapping(value = "/findSocietyFreeVehicle")
    public ResultVo<SocietyVehicleVo> findSocietyFreeVehicle(@RequestBody CarrierVehicleNoDto dto){
        return csVehicleService.findSocietyFreeVehicle(dto);
    }

    @ApiOperation(value = "个人司机删除车辆绑定关系")
    @PostMapping(value = "/removeVehicle")
    public ResultVo removeVehicle(@Validated @RequestBody RemoveVehicleDto dto) {
        return mineService.removeVehicle(dto);
    }

    @ApiOperation(value = "车辆管理(我的车辆)")
    @PostMapping(value = "/findVehicle")
    public ResultVo<PageVo<DriverVehicleVo>> findVehicle(@Validated @RequestBody BaseDriverDto dto) {
        return mineService.findVehicle(dto);
    }

    @ApiOperation(value = "个人司机认证/修改个人信息")
    @PostMapping(value = "/authOrModifyInfo")
    public ResultVo authOrModifyInfo(@Validated @RequestBody SocietyDriverDto dto) {
        return mineService.authOrModifyInfo(dto);
    }

    @ApiOperation(value = "获取司机最新信息")
    @PostMapping(value = "/findNewDriverInfo")
    public ResultVo<AppDriverInfoVo> findNewDriverInfo(@Validated @RequestBody AppDriverDto dto) {
        return mineService.findNewDriverInfo(dto);
    }

    @ApiOperation(value = "更新司机状态")
    @PostMapping(value = "/updateDriverState")
    public ResultVo updateDriverState(@Validated @RequestBody DriverStateDto dto) {
        return mineService.updateDriverState(dto);
    }


    @ApiOperation(value = "个人司机信息(认证通过后查看司机信息)")
    @PostMapping(value = "/showDriverInfo")
    public ResultVo<SocietyDriverVo> showDriverInfo(@Validated @RequestBody AppDriverDto dto) {
        return mineService.showDriverInfo(dto);
    }

    @ApiOperation(value = "个人司机添加银行卡")
    @PostMapping(value = "/addBankCard")
    public ResultVo addBankCard(@Validated @RequestBody BankCardDto dto) {
        return mineService.addBankCard(dto);
    }

    @ApiOperation(value = "获取韵车验证码(非登录)")
    @PostMapping(value = "/send")
    public ResultVo send(@Validated @RequestBody CaptchaSendDto dto) {
        return csSmsService.send(dto.getPhone(), CaptchaTypeEnum.valueOf(dto.getType()), ClientEnum.APP_DRIVER);
    }

    @ApiOperation(value = "删除银行卡")
    @PostMapping(value = "/removeBankCard")
    public ResultVo removeBankCard(@Validated @RequestBody RemoveBankCardDto dto) {
        return mineService.removeBankCard(dto);
    }



    /************************************韵车集成改版 st***********************************/

    @ApiOperation(value = "司机的银行卡信息_改版")
    @PostMapping(value = "/findBinkCardNew")
    public ResultVo<BankCardVos> findBinkCardNew(@Validated @RequestBody AppDriverDto dto) {
        return mineService.findBinkCardNew(dto);
    }

    @ApiOperation(value = "司机管理信息(管理员中的)_改版")
    @PostMapping(value = "/findDriverNew")
    public ResultVo<PageVo<DriverInfoVo>> findDriverNew(@Validated @RequestBody BaseDriverDto dto) {
        return mineService.findDriverNew(dto);
    }

    @ApiOperation(value = "该承运商下空闲车辆(管理员中的司机管理的新增/修改)_改版")
    @PostMapping(value = "/findCompanyFreeVehicleNew")
    public ResultVo<CarrierVehicleVo> findCompanyFreeVehicleNew(@Validated @RequestBody CarrierVehicleNoDto dto){
        return csVehicleService.findCompanyFreeVehicleNew(dto);
    }

    @ApiOperation(value = "该承运商下空闲司机(管理员中的车辆管理中新增/修改车辆)_改版")
    @PostMapping(value = "/findCompanyFreeDriverNew")
    public ResultVo<CarrierDriverVo> findCompanyFreeDriverNew(@Validated @RequestBody CarrierDriverNameDto dto){
        return csDriverService.findCompanyFreeDriverNew(dto);
    }

    @ApiOperation(value = "新增/修改承运商下司机_改版")
    @PostMapping(value = "/saveOrModifyDriverNew")
    public ResultVo saveOrModifyDriverNew(@Validated @RequestBody CarrierDriverDto dto){
        return csDriverService.saveOrModifyDriverAppNew(dto);
    }

    @ApiOperation(value = "新增/修改车辆_改版")
    @PostMapping(value = "/saveOrModifyVehicleNew")
    public ResultVo saveOrModifyVehicleNew(@Validated @RequestBody AppCarrierVehicleDto dto){
        return mineService.saveOrModifyVehicleNew(dto);
    }

    @ApiOperation(value = "删除(冻结)承运商下司机_改版")
    @PostMapping(value = "/frozenDriverNew")
    public ResultVo frozenDriverNew(@Validated @RequestBody FrozenDriverDto dto){
        return mineService.frozenDriverNew(dto);
    }

    @ApiOperation(value = "查询没有被绑定的社会车辆信息_改版")
    @PostMapping(value = "/findSocietyFreeVehicleNew")
    public ResultVo<SocietyVehicleVo> findSocietyFreeVehicleNew(@RequestBody CarrierVehicleNoDto dto){
        return csVehicleService.findSocietyFreeVehicleNew(dto);
    }

    @ApiOperation(value = "个人司机删除车辆绑定关系_改版")
    @PostMapping(value = "/removeVehicleNew")
    public ResultVo removeVehicleNew(@Validated @RequestBody RemoveVehicleDto dto) {
        return mineService.removeVehicleNew(dto);
    }

    @ApiOperation(value = "车辆管理(我的车辆)_改版")
    @PostMapping(value = "/findVehicleNew")
    public ResultVo<PageVo<DriverVehicleVo>> findVehicleNew(@Validated @RequestBody BaseDriverDto dto) {
        return mineService.findVehicleNew(dto);
    }

    @ApiOperation(value = "个人司机认证/修改个人信息_改版")
    @PostMapping(value = "/authOrModifyInfoNew")
    public ResultVo authOrModifyInfoNew(@Validated @RequestBody SocietyDriverDto dto) {
        return mineService.authOrModifyInfoNew(dto);
    }

    @ApiOperation(value = "获取司机最新信息_改版")
    @PostMapping(value = "/findNewDriverInfoNew")
    public ResultVo<AppDriverInfoVo> findNewDriverInfoNew(@Validated @RequestBody AppDriverDto dto) {
        return mineService.findNewDriverInfoNew(dto);
    }

    @ApiOperation(value = "个人司机信息(认证通过后查看司机信息)_改版")
    @PostMapping(value = "/showDriverInfoNew")
    public ResultVo<SocietyDriverVo> showDriverInfoNew(@Validated @RequestBody AppDriverDto dto) {
        return mineService.showDriverInfoNew(dto);
    }

    @ApiOperation(value = "个人司机添加银行卡_改版")
    @PostMapping(value = "/addBankCardNew")
    public ResultVo addBankCardNew(@Validated @RequestBody BankCardDto dto) {
        return mineService.addBankCardNew(dto);
    }

    @ApiOperation(value = "删除银行卡_改版")
    @PostMapping(value = "/removeBankCardNew")
    public ResultVo removeBankCardNew(@Validated @RequestBody RemoveBankCardDto dto) {
        return mineService.removeBankCardNew(dto);
    }
}