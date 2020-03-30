package com.cjyc.scheduler.biz.config;

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

    public static String apiKeyCustomer;
    public static String apiKeyDriver;
    public static String apiKeySalesman;

    @Value("${cjkj.push.apikey.customer}")
    public void setApiKeyCustomer(String apiKeyCustomer) {
        JPushProperty.apiKeyCustomer = apiKeyCustomer;
    }

    @Value("${cjkj.push.apikey.driver}")
    public void setApiKeyDriver(String apiKeyDriver) {
        JPushProperty.apiKeyDriver = apiKeyDriver;
    }

    @Value("${cjkj.push.apikey.salesman}")
    public void setApiKeySalesman(String apiKeySalesman) {
        JPushProperty.apiKeySalesman = apiKeySalesman;
    }

}
