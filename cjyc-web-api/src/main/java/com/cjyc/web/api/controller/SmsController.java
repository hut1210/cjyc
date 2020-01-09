package com.cjyc.web.api.controller;

import com.cjyc.common.model.dto.salesman.sms.CaptchaSendDto;
import com.cjyc.common.model.dto.salesman.sms.CaptchaValidatedDto;
import com.cjyc.common.model.enums.CaptchaTypeEnum;
import com.cjyc.common.model.enums.ClientEnum;
import com.cjyc.common.model.util.BaseResultUtil;
import com.cjyc.common.model.vo.ResultVo;
import com.cjyc.common.system.service.ICsSmsService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.validation.constraints.NotNull;

/**
 * 短信
 * @author JPG
 */
@Api(tags = "短信")
@Slf4j
@RestController
@RequestMapping(value = "/sms",
        produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
public class SmsController {


    @Resource
    private ICsSmsService csSmsService;


    @ApiOperation(value = "发送短信验证码", notes = " ")
    @PostMapping("/captcha/send")
    public ResultVo send(@RequestBody CaptchaSendDto reqDto){
        @NotNull String phone = reqDto.getPhone();
        @NotNull Integer type = reqDto.getType();
        ResultVo resultVo = csSmsService.send(phone, CaptchaTypeEnum.valueOf(type), ClientEnum.APP_DRIVER);
        return BaseResultUtil.success();
    }

    @ApiOperation(value = "校验短信验证码", notes = " ")
    @PostMapping("/captcha/validate")
    public ResultVo validate(@RequestBody CaptchaValidatedDto reqDto){
        String phone = reqDto.getPhone();
        String captcha = reqDto.getCaptcha();
        Integer type = reqDto.getType();

        boolean flag = csSmsService.validateCaptcha(phone, captcha, CaptchaTypeEnum.valueOf(type), ClientEnum.APP_DRIVER);

        return flag ? BaseResultUtil.success("验证通过") : BaseResultUtil.fail("验证码错误");
    }

}
