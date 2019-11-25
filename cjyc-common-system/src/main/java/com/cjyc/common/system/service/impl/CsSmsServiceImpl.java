package com.cjyc.common.system.service.impl;

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
import com.cjyc.common.model.vo.ResultVo;
import com.cjyc.common.system.config.CarrierProperty;
import com.cjyc.common.system.service.ICsSmsService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDateTime;
@Service
@Slf4j
public class CsSmsServiceImpl implements ICsSmsService {

    @Autowired
    private StringRedisUtil redisUtil;

    @Override
    public ResultVo send(CaptchaSendDto reqDto) {
        /*Integer expires = CarrierProperty.expires;
        String phone = reqDto.getPhone();
        Integer type = reqDto.getType();
        //生成随机验证码
        String captcha = String.valueOf((int) ((Math.random() * 9 + 1) * Math.pow(10, 6 - 1)));
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
        redisUtil.setExpire(countKey, TimeConstant.SEC_OF_ONE_DAY);*/
        return BaseResultUtil.success();
    }

    @Override
    public ResultVo validate(CaptchaValidatedDto reqDto) {
        String phone = reqDto.getPhone();
        String captcha = reqDto.getCaptcha();
        int type = reqDto.getType();

        String key = RedisKeys.getCaptchaKey(ClientEnum.APP_CUSTOMER, phone, CaptchaTypeEnum.valueOf(type));
        String captchaCached = redisUtil.getStrValue(key);
        if(StringUtils.isBlank(captchaCached)){
            return BaseResultUtil.fail("请重新获取校验码");
        }
        if(!captcha.equals(captchaCached)){
            return BaseResultUtil.fail("验证码错误");
        }
        return BaseResultUtil.success("验证码正确");
    }
}