package com.cjyc.common.system.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class CarrierProperty {

    public static List<Long> carriesMenuIds;

    @Value("${cjkj.carries_menu_ids}")
    public void setCarriesMenuIds(List<Long> carriesMenuIds) {
        CarrierProperty.carriesMenuIds = carriesMenuIds;
    }

}
