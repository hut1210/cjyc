package com.cjyc.common.system.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Configuration;

/**
 * 秒信配置
 * @author JPG
 */
@Configuration
@RefreshScope
public class SmsProperty {

    public static Integer expires;
    public static Integer daylimit;

    @Value("${cjyc.sms.expires}")
    public void setExpires(Integer expires) {
        SmsProperty.expires = expires;
    }
    @Value("${cjyc.sms.daylimit}")
    public static void setDaylimit(Integer daylimit) {
        SmsProperty.daylimit = daylimit;
    }
}
