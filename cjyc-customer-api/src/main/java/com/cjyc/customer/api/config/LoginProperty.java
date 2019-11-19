package com.cjyc.customer.api.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Configuration;

/**
 * 登录配置参数
 * @author JPG
 */
@Configuration
@RefreshScope
public class LoginProperty {
    public static String password;
    public static String clientId;
    public static String clientSecret;
    public static String grantType;

    @Value("${cjkj.customer.clientId:app}")
    public void setClientId(String clientId) {
        LoginProperty.clientId = clientId;
    }

    @Value("${cjkj.customer.clientSecret:app}")
    public void setClientSecret(String clientSecret) {
        LoginProperty.clientSecret = clientSecret;
    }

    @Value("${cjkj.customer.password:1234567890}")
    public void setPassword(String password) {
        LoginProperty.password = password;
    }
    @Value("${cjkj.customer.grantType:mobile}")
    public void setGrantType(String grantType) {
        LoginProperty.grantType = grantType;
    }
}
