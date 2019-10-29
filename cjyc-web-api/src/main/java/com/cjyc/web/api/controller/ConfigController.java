package com.cjyc.web.api.controller;

import com.cjyc.common.model.dto.web.config.ConfigDto;
import com.cjyc.common.model.dto.web.coupon.CouponDto;
import com.cjyc.common.model.enums.ResultEnum;
import com.cjyc.common.model.util.BasePageUtil;
import com.cjyc.common.model.util.BaseResultUtil;
import com.cjyc.common.model.vo.ResultVo;
import com.cjyc.web.api.service.IConfigService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 *  @author: zj
 *  @Date: 2019/10/29 13:10
 *  @Description:系统开关设置
 */
@Api(tags = "系统开关设置")
@CrossOrigin
@RestController
@RequestMapping("/config")
public class ConfigController {

    @Resource
    private IConfigService configService;

    @ApiOperation(value = "查询系统配置")
    @PostMapping(value = "/queryConfig")
    public ResultVo queryConfig(@Validated({ ConfigDto.QueryConfigDto.class }) @RequestBody ConfigDto dto){
        BasePageUtil.initPage(dto.getCurrentPage(),dto.getPageSize());
        return configService.queryConfig(dto);
    }

    @ApiOperation(value = "更新系统配置")
    @PostMapping(value = "/updateConfig")
    public ResultVo updateConfig(@Validated({ ConfigDto.UpdateConfigDto.class }) @RequestBody ConfigDto dto){
        return configService.updateConfig(dto);
    }
}