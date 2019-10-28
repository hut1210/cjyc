package com.cjyc.salesman.api.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Configuration;

/**
 * 极光推送配置(静态属性)
 * @author JPG
 */
@Configuration
@ConditionalOnProperty(prefix = "cjyc.push.jiguang", name = "enabled", havingValue = "true")
@RefreshScope
public class JiGuangProperty {

    public static String appKey;
    public static String masterSecret;


    @Value("${cjyc.push.jiguang.appKey}")
    public void setAppKey(String appKey) {
        JiGuangProperty.appKey = appKey;
    }

    @Value("${cjyc.push.jiguang.masterSecret}")
    public void setMasterSecret(String masterSecret) {
        JiGuangProperty.masterSecret = masterSecret;
    }

}
