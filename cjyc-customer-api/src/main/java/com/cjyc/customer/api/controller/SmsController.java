package com.cjyc.customer.api.controller;

import com.cjkj.common.redis.template.StringRedisUtil;
import com.cjyc.common.model.constant.TimeConstant;
import com.cjyc.common.model.constant.TimePatternConstant;
import com.cjyc.common.model.dto.salesman.sms.CaptchaSendDto;
import com.cjyc.common.model.dto.salesman.sms.CaptchaValidatedDto;
import com.cjyc.common.model.enums.CaptchaTypeEnum;
import com.cjyc.common.model.enums.ClientEnum;
import com.cjyc.common.model.enums.message.SmsMessageEnum;
import com.cjyc.common.model.keys.RedisKeys;
import com.cjyc.common.model.util.BaseResultUtil;
import com.cjyc.common.model.util.LocalDateTimeUtil;
import com.cjyc.common.model.util.RandomUtil;
import com.cjyc.common.model.vo.ResultVo;
import com.cjyc.common.system.service.ICsSmsService;
import com.cjyc.customer.api.util.MiaoxinSmsUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.validation.constraints.NotNull;
import java.io.IOException;
import java.time.LocalDateTime;

/**
 * 短信
 * @author JPG
 */
@Api(tags = "短信")
@Slf4j
@RestController
@RequestMapping(value = "/sms",
        consumes = MediaType.APPLICATION_JSON_UTF8_VALUE,
        produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
public class SmsController {

    @Value("${cjyc.sms.expires:300}")
    private Integer expires;
    @Value("${cjyc.sms.daylimit:20}")
    private Integer daylimit;

    @Resource
    private ICsSmsService csSmsService;

    @Autowired
    private StringRedisUtil redisUtil;

    @ApiOperation(value = "发送短信验证码", notes = " ")
    @PostMapping("/captcha/send")
    public ResultVo send(@RequestBody CaptchaSendDto reqDto){
        @NotNull String phone = reqDto.getPhone();
        @NotNull Integer type = reqDto.getType();
        //生成随机验证码
        String captcha = RandomUtil.getMathRandom(4);
        //验证短信限制
        String keyPrefix = LocalDateTimeUtil.formatLDT(LocalDateTime.now(), TimePatternConstant.SIMPLE_DATE);
        String countKey = RedisKeys.getSmsCountKey(keyPrefix, phone);
        String count = redisUtil.getStrValue(countKey);
        if(count != null && Integer.valueOf(count) > 20){
            return BaseResultUtil.fail("发送短信数量超出数量限制，请明天再试");
        }

        //发送短信
        try {
            String message = SmsMessageEnum.SMS_CAPTCHA.getMsg(captcha);
            MiaoxinSmsUtil.send(phone, message);
            log.info("【发送短信】向手机{}发送验证码是:{}", phone, captcha);
        } catch (IOException e) {
            log.error(e.getMessage(), e);
            return BaseResultUtil.fail("短信验证码发送失败");
        }
        //放入缓存
        String key = RedisKeys.getCaptchaKey(ClientEnum.APP_CUSTOMER, phone, CaptchaTypeEnum.valueOf(type));
        redisUtil.set(key, captcha, expires);
        redisUtil.incr(countKey);
        redisUtil.setExpire(countKey, TimeConstant.SEC_OF_ONE_DAY);
        return BaseResultUtil.success();
    }

    @ApiOperation(value = "校验短信验证码", notes = " ")
    @PostMapping("/captcha/validate")
    public ResultVo validate(@RequestBody CaptchaValidatedDto reqDto){
        String phone = reqDto.getPhone();
        String captcha = reqDto.getCaptcha();
        Integer type = reqDto.getType();

        boolean flag = csSmsService.validateCaptcha(phone, captcha, CaptchaTypeEnum.valueOf(type));

        return flag ? BaseResultUtil.success("验证通过") : BaseResultUtil.fail("验证码错误");
    }

}
