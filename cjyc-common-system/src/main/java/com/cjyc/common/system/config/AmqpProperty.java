package com.cjyc.common.system.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;


@Configuration
@ConditionalOnProperty(prefix = "cjkj.amqp", name = "enabled", havingValue = "true")
public class AmqpProperty {

    public static String tplExchange;
    /**
     * 承运商普通管理员角色资源列表
     */
    public static String orderStateRoutingKey;
    public static String orderConfirmRoutingKey;

    @Value("${cjkj.carries_menu_ids}")
    public static String getTplExchange() {
        return tplExchange;
    }

    public static String getOrderStateRoutingKey() {
        return orderStateRoutingKey;
    }

    public static String getOrderConfirmRoutingKey() {
        return orderConfirmRoutingKey;
    }
}