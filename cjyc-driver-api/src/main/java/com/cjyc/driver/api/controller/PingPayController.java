package com.cjyc.driver.api.controller;

import com.alibaba.fastjson.JSONObject;
import com.cjkj.common.utils.IPUtil;
import com.cjyc.common.model.dto.customer.order.CarCollectPayDto;
import com.cjyc.common.model.dto.customer.pingxx.SweepCodeDto;
import com.cjyc.common.model.dto.customer.pingxx.ValidateSweepCodeDto;
import com.cjyc.common.model.entity.Customer;
import com.cjyc.common.model.entity.Driver;
import com.cjyc.common.model.enums.ClientEnum;
import com.cjyc.common.model.enums.UserTypeEnum;
import com.cjyc.common.model.util.BaseResultUtil;
import com.cjyc.common.model.util.StringUtil;
import com.cjyc.common.model.vo.ResultVo;
import com.cjyc.common.model.vo.customer.order.ValidateReceiptCarPayVo;
import com.cjyc.common.model.vo.customer.order.ValidateSweepCodePayVo;
import com.cjyc.common.system.service.ICsDriverService;
import com.cjyc.common.system.service.ICsPingPayService;
import com.cjyc.common.system.service.ICsTaskService;
import com.pingplusplus.model.Charge;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * @Author: Hut
 * @Date: 2019/12/9 19:25
 */
@RestController
@RequestMapping("/pay")
@Slf4j
@Api(tags = "Ping++支付")
public class PingPayController {

    @Autowired
    private ICsPingPayService pingPayService;
    @Resource
    private ICsDriverService csDriverService;

    @ApiOperation("司机出示二维码")
    @PostMapping("/sweepDriveCode")
    public ResultVo sweepDriveCode(HttpServletRequest request, @RequestBody SweepCodeDto sweepCodeDto){
        Driver driver = csDriverService.validate(sweepCodeDto.getLoginId());
        sweepCodeDto.setLoginName(driver.getName());
        sweepCodeDto.setLoginType(UserTypeEnum.DRIVER);
        sweepCodeDto.setIp(StringUtil.getIp(IPUtil.getIpAddr(request)));

        Charge charge;
        try {
            sweepCodeDto.setClientType(ClientEnum.APP_DRIVER.code);
            charge = pingPayService.sweepDriveCode(sweepCodeDto);
        }catch (Exception e){
            log.error(e.getMessage(),e);
            return BaseResultUtil.fail(500,e.getMessage());
        }
        return BaseResultUtil.success(JSONObject.parseObject(charge.toString()));
    }

    @ApiOperation(value = "收款码-验证支付状态")
    @PostMapping(value = "/validate")
    public ResultVo<ValidateSweepCodePayVo> validateCarPayState(@RequestBody ValidateSweepCodeDto validateSweepCodeDto) {
        return pingPayService.validateCarPayState(validateSweepCodeDto, false);
    }

}
