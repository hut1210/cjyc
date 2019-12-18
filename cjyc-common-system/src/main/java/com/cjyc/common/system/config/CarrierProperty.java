package com.cjyc.common.system.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
@ConditionalOnProperty(prefix = "cjkj", name = "carries_menus.enable",
        havingValue = "true")
public class CarrierProperty {

    public static List<Long> carriesMenuIds;
    /**
     * 承运商普通管理员角色资源列表
     */
    public static List<Long> carriesMgrMenuIds;

    @Value("${cjkj.carries_menu_ids}")
    public void setCarriesMenuIds(List<Long> carriesMenuIds) {
        CarrierProperty.carriesMenuIds = carriesMenuIds;
    }

    @Value("${cjkj.carries_mgr_menu_ids}")
    public void setCarriesMgrMenuIds(List<Long> carriesMgrMenuIds) {
        CarrierProperty.carriesMgrMenuIds = carriesMgrMenuIds;
    }

}
