package com.cjyc.customer.api.controller;

import com.cjyc.common.model.dto.customer.partner.ApplyPartnerDto;
import com.cjyc.common.model.vo.ResultVo;
import com.cjyc.customer.api.service.IApplyPartnerService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 *  @author: zj
 *  @Date: 2019/11/4 12:24
 *  @Description:申请合伙人
 */
@Api(tags = "申请合伙人")
@CrossOrigin
@RestController
@RequestMapping("/partner")
public class ApplyPartnerController {

    @Resource
    private IApplyPartnerService applyPartnerService;

    @ApiOperation(value = "申请合伙人")
    @PostMapping(value = "/applyPartner")
    public ResultVo applyPartner(@Validated @RequestBody ApplyPartnerDto dto) {
        return applyPartnerService.applyPartner(dto);
    }
}