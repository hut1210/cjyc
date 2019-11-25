package com.cjyc.common.system.service.impl;

import com.cjyc.common.model.enums.CaptchaTypeEnum;
import com.cjyc.common.model.enums.ClientEnum;
import com.cjyc.common.model.keys.RedisKeys;
import com.cjyc.common.system.service.ICsSmsService;
import com.cjyc.common.system.util.RedisUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class ICsSmsServiceImpl implements ICsSmsService {

    @Resource
    private RedisUtils redisUtils;
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
    public boolean validateCaptcha(String phone, String captcha, CaptchaTypeEnum captchaTypeEnum) {
        String key = RedisKeys.getCaptchaKey(ClientEnum.APP_CUSTOMER, phone, captchaTypeEnum);
        String captchaCached = redisUtils.get(key);
        if(StringUtils.isBlank(captchaCached) || !captcha.equals(captchaCached)){
            return false;
        }
        return true;
    }
}
