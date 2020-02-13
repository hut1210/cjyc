package com.cjyc.customer.api.controller;

import com.cjyc.common.model.dto.BasePageDto;
import com.cjyc.common.model.dto.customer.partner.ApplyPartnerDto;
import com.cjyc.common.model.dto.driver.mine.BankInfoDto;
import com.cjyc.common.model.vo.PageVo;
import com.cjyc.common.model.vo.ResultVo;
import com.cjyc.common.model.vo.web.bankInfo.BankInfoVo;
import com.cjyc.common.system.service.ICsBankInfoService;
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
    @Resource
    private ICsBankInfoService csBankInfoService;

    @ApiOperation(value = "申请合伙人")
    //@PostMapping(value = "/applyPartner")
    public ResultVo applyPartner(@Validated @RequestBody ApplyPartnerDto dto) {
        return applyPartnerService.applyPartner(dto);
    }


    /************************************韵车集成改版 st***********************************/

    @ApiOperation(value = "申请合伙人")
    //@PostMapping(value = "/applyPartnerNew")
    @PostMapping(value = "/applyPartner")
    public ResultVo applyPartnerNew(@Validated @RequestBody ApplyPartnerDto dto) {
        return applyPartnerService.applyPartnerNew(dto);
    }

    @ApiOperation(value = "获取全部银行信息")
    @PostMapping(value = "/findBankInfo")
    public ResultVo<PageVo<BankInfoVo>> findBankInfo(@Validated @RequestBody BankInfoDto dto) {
        return csBankInfoService.findBankInfo(dto);
    }

}