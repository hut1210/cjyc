package com.cjyc.driver.api.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Configuration;

/**
 * 秒信配置
 * @author JPG
 */
@Configuration
@ConditionalOnProperty(name = "cjyc.sms.miaoxin.enabled", havingValue = "true")
@RefreshScope
public class MiaoxinProperty {

    public static String account;
    public static String secret;
    public static String server;

    @Value("${cjyc.sms.miaoxin.account}")
    public void setAccount(String account) {
        MiaoxinProperty.account = account;
    }

    @Value("${cjyc.sms.miaoxin.secret}")
    public void setSecret(String secret) {
        MiaoxinProperty.secret = secret;
    }

    @Value("${cjyc.sms.miaoxin.server}")
    public void setServer(String server) {
        MiaoxinProperty.server = server;
    }
}
