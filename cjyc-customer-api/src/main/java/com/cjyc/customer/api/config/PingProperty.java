package com.cjyc.customer.api.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Configuration;

/**
 * @Author:Hut
 * @Date:2019/11/20 13:52
 */
@Configuration
@RefreshScope
public class PingProperty {

    public static String apiKey;

    public static String userAppId;

    public static String driverAppId;

    @Value("${cjyc.pay.ping++.apikey}")
    public void setApiKey(String apiKey){
        PingProperty.apiKey = apiKey;
    }

    @Value("${cjyc.pay.ping++.salesman.appid}")
    public void setUserAppId(String userAppId) {
        PingProperty.userAppId = userAppId;
    }

    @Value("${cjyc.pay.ping++.driver.appid}")
    public void setDriverAppId(String driverAppId) {
        PingProperty.driverAppId = driverAppId;
    }
}
