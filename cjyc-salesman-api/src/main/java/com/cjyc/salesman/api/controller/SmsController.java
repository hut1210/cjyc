package com.cjyc.salesman.api.controller;

import com.cjkj.common.redis.template.StringRedisUtil;
import com.cjyc.common.model.dto.salesman.sms.CaptchaSendDto;
import com.cjyc.common.model.dto.salesman.sms.CaptchaValidatedDto;
import com.cjyc.common.model.enums.message.SmsMessageEnum;
import com.cjyc.common.model.keys.RedisKeys;
import com.cjyc.common.model.util.BaseResultUtil;
import com.cjyc.common.model.vo.ResultVo;
import com.cjyc.salesman.api.util.MiaoxinSmsUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.constraints.NotNull;
import java.io.IOException;

@Api(tags = "验证码")
@Slf4j
@RestController
@RequestMapping(value = "/sms",
        consumes = MediaType.APPLICATION_JSON_UTF8_VALUE,
        produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
public class SmsController {

    @Autowired
    private StringRedisUtil redisUtil;
    @Resource
    private MiaoxinSmsUtil miaoxinSmsUtil;

    @ApiOperation(value = "发送短信验证码", notes = " ")
    @PostMapping("/captcha/send")
    public ResultVo send(@RequestBody CaptchaSendDto reqDto){
        @NotNull String phone = reqDto.getPhone();
        @NotNull Integer type = reqDto.getType();
        //生成随机验证码
        String captcha = String.valueOf((int) ((Math.random() * 9 + 1) * Math.pow(10, 6 - 1)));
        //发送短信
        try {
            String message = SmsMessageEnum.SMS_CAPTCHA.getMsg(captcha);
            miaoxinSmsUtil.send(phone, message);
            log.info("【发送短信】向手机 验证码是:{}", captcha);
        } catch (IOException e) {
            log.error(e.getMessage(), e);
            return BaseResultUtil.fail("短信验证码发送失败");
        }
        //放入缓存
        String key = RedisKeys.getSalesmanCaptchaKeyByPhone(phone, type);
        redisUtil.set(key, captcha);
        return BaseResultUtil.success();
    }

    @ApiOperation(value = "校验短信验证码", notes = " ")
    @PostMapping("/captcha/validate")
    public ResultVo validate(@RequestBody CaptchaValidatedDto reqDto){
        @NotNull String phone = reqDto.getPhone();
        @NotNull String captcha = reqDto.getCaptcha();
        int type = reqDto.getType();

        String key = RedisKeys.getSalesmanCaptchaKeyByPhone(phone, type);
        String captchaCached = redisUtil.getStrValue(key);
        if(StringUtils.isBlank(captchaCached)){
            return BaseResultUtil.fail("请重新获取校验码");
        }
        if(!captcha.equals(captchaCached)){
            return BaseResultUtil.fail("验证码错误");
        }
        return BaseResultUtil.success();
    }

}
