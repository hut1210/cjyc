package com.cjyc.common.system.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class CarrierProperty {

    public static List<Long> carriesMenuIds;
    public static Integer expires;
    public static Integer daylimit;

    @Value("${cjkj.carries_menu_ids}")
    public void setCarriesMenuIds(List<Long> carriesMenuIds) {
        CarrierProperty.carriesMenuIds = carriesMenuIds;
    }

    @Value("${cjyc.sms.expires}")
    public void setExpires(Integer expires) {
        CarrierProperty.expires = expires;
    }
    @Value("${cjyc.sms.daylimit}")
    public void setDaylimit(Integer daylimit) {
        CarrierProperty.daylimit = daylimit;
    }

}
