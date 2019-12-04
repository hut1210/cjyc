package com.cjyc.driver.api.controller;

import com.cjyc.common.model.dto.CarrierDriverDto;
import com.cjyc.common.model.dto.KeywordDto;
import com.cjyc.common.model.dto.driver.AppDriverDto;
import com.cjyc.common.model.dto.driver.BaseDriverDto;
import com.cjyc.common.model.dto.driver.mine.*;
import com.cjyc.common.model.dto.salesman.sms.CaptchaSendDto;
import com.cjyc.common.model.enums.CaptchaTypeEnum;
import com.cjyc.common.model.enums.ClientEnum;
import com.cjyc.common.model.util.BaseResultUtil;
import com.cjyc.common.model.vo.FreeDriverVo;
import com.cjyc.common.model.vo.FreeVehicleVo;
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
import java.util.List;

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
    public ResultVo<BinkCardVo> findBinkCard(@Validated @RequestBody AppDriverDto dto) {
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

    @ApiOperation(value = "新增/修改承运商下车辆")
    @PostMapping(value = "/saveOrModifyCarrierVehicle")
    public ResultVo saveOrModifyCarrierVehicle(@Validated @RequestBody AppCarrierVehicleDto dto){
        return mineService.saveOrModifyCarrierVehicle(dto);
    }

    @ApiOperation(value = "删除(冻结)承运商下司机")
    @PostMapping(value = "/frozenDriver")
    public ResultVo frozenDriver(@Validated @RequestBody FrozenDriverDto dto){
        return mineService.frozenDriver(dto);
    }

    @ApiOperation(value = "查询没有被绑定的社会车辆信息")
    @PostMapping(value = "/findSocietyFreeVehicle")
    public ResultVo<SocietyVehicleVo> findSocietyFreeVehicle(@RequestBody KeywordDto dto){
        return csVehicleService.findSocietyFreeVehicle(dto);
    }

    @ApiOperation(value = "个人司机添加/修改车辆信息")
    @PostMapping(value = "/saveOrModifyVehicle")
    public ResultVo saveOrModifyVehicle(@Validated @RequestBody SocietyVehicleDto dto) {
        return mineService.saveOrModifyVehicle(dto);
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

    @ApiOperation(value = "个人司机信息(认证通过后)")
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

}