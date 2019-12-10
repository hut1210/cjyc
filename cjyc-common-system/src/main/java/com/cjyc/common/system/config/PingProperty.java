package com.cjyc.common.system.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Configuration;

/**
 * @Author: Hut
 * @Date: 2019/12/9 19:37
 */
@Configuration
@RefreshScope
public class PingProperty {

    public static String apiKey;

    public static String userAppId;

    public static String driverAppId;

    public static String customerAppId;

    @Value("${cjyc.pay.ping++.apikey}")
    public void setApiKey(String apiKey){
        PingProperty.apiKey = apiKey;
    }

    @Value("${cjyc.pay.ping++.salesman.appid}")
    public void setUserAppId(String userAppId) {
        PingProperty.userAppId = userAppId;
    }

    @Value("${cjyc.pay.ping++.customer.appid}")
    public void setCustomerAppId(String customerAppId) {
        PingProperty.customerAppId = customerAppId;
    }

    @Value("${cjyc.pay.ping++.driver.appid}")
    public void setDriverAppId(String driverAppId) {
        PingProperty.driverAppId = driverAppId;
    }
}
