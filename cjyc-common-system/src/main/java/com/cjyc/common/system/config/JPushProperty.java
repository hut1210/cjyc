package com.cjyc.common.system.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Configuration;

/**
 * 极光推送配置(静态属性)
 * @author JPG
 */
@Configuration
@ConditionalOnProperty(prefix = "cjkj.push", name = "enabled", havingValue = "true")
@RefreshScope
public class JPushProperty {

    public static String apiKey;

    @Value("${cjkj.push.apikey}")
    public void setAppKey(String apiKey) {
        JPushProperty.apiKey = apiKey;
    }

}
