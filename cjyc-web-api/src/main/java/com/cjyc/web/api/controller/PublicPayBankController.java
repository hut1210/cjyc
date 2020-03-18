package com.cjyc.web.api.controller;

import com.cjyc.common.model.dto.web.publicPayBank.PayBankDto;
import com.cjyc.common.model.enums.ResultEnum;
import com.cjyc.common.model.util.BaseResultUtil;
import com.cjyc.common.model.vo.PageVo;
import com.cjyc.common.model.vo.ResultVo;
import com.cjyc.common.model.vo.web.publicPay.PayBankVo;
import com.cjyc.web.api.service.IPublicPayBankService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;

@Api(tags = "基础数据-对公支付银行信息")
@CrossOrigin
@RestController
@RequestMapping("/publicPayBank")
public class PublicPayBankController {

    @Resource
    private IPublicPayBankService payBankService;

    @ApiOperation(value = "对公支付行号导入Excel", notes = "\t 请求接口为/importPayBankExcel/loginId(登录用户ID)格式")
    @PostMapping("/importPayBankExcel/{loginId}")
    @Deprecated
    public ResultVo importPayBankExcel(@RequestParam("file") MultipartFile file, @PathVariable Long loginId){
        boolean result = payBankService.importPayBankExcel(file,loginId);
        return result ? BaseResultUtil.success() : BaseResultUtil.fail(ResultEnum.FAIL.getMsg());
    }

    @ApiOperation(value = "查询对公支付银行信息")
    @PostMapping(value = "/findPayBankInfo")
    public ResultVo<PageVo<PayBankVo>> findPayBankInfo(@RequestBody PayBankDto dto) {
        return payBankService.findPayBankInfo(true,dto);
    }

}