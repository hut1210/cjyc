package com.cjyc.common.system.service.impl;

import com.cjyc.common.model.constant.TimePatternConstant;
import com.cjyc.common.model.enums.CaptchaTypeEnum;
import com.cjyc.common.model.enums.ClientEnum;
import com.cjyc.common.model.enums.message.SmsMessageEnum;
import com.cjyc.common.model.keys.RedisKeys;
import com.cjyc.common.model.util.BaseResultUtil;
import com.cjyc.common.model.util.LocalDateTimeUtil;
import com.cjyc.common.model.util.RandomUtil;
import com.cjyc.common.model.vo.ResultVo;
import com.cjyc.common.system.service.ICsSmsService;
import com.cjyc.common.system.util.MiaoxinSmsUtil;
import com.cjyc.common.system.util.RedisUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
public class CsSmsServiceImpl implements ICsSmsService {

    @Resource
    private RedisUtils redisUtils;

    @Value("${cjyc.sms.expires:300}")
    private Integer expires;
    @Value("${cjyc.sms.daylimit:20}")
    private Integer daylimit;
    /**
     * 验证验证码
     *
     * @param phone
     * @param captcha
     * @param captchaTypeEnum
     * @author JPG
     * @since 2019/11/25 17:24
     */
    @Override
    public boolean validateCaptcha(String phone, String captcha, CaptchaTypeEnum captchaTypeEnum, ClientEnum clientEnum) {
        String key = RedisKeys.getCaptchaKey(clientEnum, phone, captchaTypeEnum);
        String captchaCached = redisUtils.get(key);
        if(StringUtils.isBlank(captchaCached) || !captcha.equals(captchaCached)){
            return false;
        }
        return true;
    }

    @Override
    public ResultVo send(String phone, CaptchaTypeEnum captchaTypeEnum, ClientEnum clientEnum) {
        //生成随机验证码
        String captcha = RandomUtil.getMathRandom(captchaTypeEnum.getLength());
        //验证短信限制
        String keyPrefix = LocalDateTimeUtil.formatLDT(LocalDateTime.now(), TimePatternConstant.SIMPLE_DATE);
        String countKey = RedisKeys.getSmsCountKey(keyPrefix, phone);
        String count = redisUtils.get(countKey);
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
        String key = RedisKeys.getCaptchaKey(clientEnum, phone, captchaTypeEnum);
        redisUtils.set(key, captcha);
        redisUtils.expire(key, 1, TimeUnit.DAYS);
        //计数缓存
        redisUtils.incrBy(countKey, 1);
        redisUtils.expire(countKey, 1, TimeUnit.DAYS);
        return BaseResultUtil.success("验证码已发送");
    }
}
