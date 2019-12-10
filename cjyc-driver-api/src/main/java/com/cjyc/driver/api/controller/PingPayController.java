package com.cjyc.driver.api.controller;

import com.alibaba.fastjson.JSONObject;
import com.cjkj.common.utils.IPUtil;
import com.cjyc.common.model.dto.customer.pingxx.SweepCodeDto;
import com.cjyc.common.model.util.BaseResultUtil;
import com.cjyc.common.model.vo.ResultVo;
import com.cjyc.common.system.service.ICsPingPayService;
import com.pingplusplus.model.Charge;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

    @ApiOperation("司机出示二维码")
    @PostMapping("/sweepDriveCode")
    public ResultVo sweepDriveCode(HttpServletRequest request, @RequestBody SweepCodeDto sweepCodeDto){
        sweepCodeDto.setIp(IPUtil.getIpAddr(request));
        Charge charge;
        try {
            charge = pingPayService.sweepDriveCode(sweepCodeDto);
        }catch (Exception e){
            log.error(e.getMessage(),e);
            return BaseResultUtil.fail(500,"司机二维码异常");
        }
        return BaseResultUtil.success(JSONObject.parseObject(charge.toString()));
    }
}
