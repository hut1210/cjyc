package com.cjyc.common.system.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

@Configuration
@ConditionalOnProperty(prefix = "cjkj.amqp", name = "enabled", havingValue = "true")
public class AmqpProperty {

    public static Boolean enabled;
    public static String directExchange;
    public static String topicExchange;
    public static String orderStateRoutingKey;
    public static String orderConfirmRoutingKey;

    @Value("${cjkj.amqp.enabled}")
    public void setEnabled(Boolean enabled) {
        AmqpProperty.enabled = enabled;
    }

    @Value("${cjkj.amqp.exchange.direct}")
    public void setDirectExchange(String directExchange) {
        AmqpProperty.directExchange = directExchange;
    }
    @Value("${cjkj.amqp.exchange.topic}")
    public void setTopicExchange(String topicExchange) {
        AmqpProperty.topicExchange = topicExchange;
    }
    @Value("${cjkj.amqp.routing-key.order-state}")
    public void setOrderStateRoutingKey(String orderStateRoutingKey) {
        AmqpProperty.orderStateRoutingKey = orderStateRoutingKey;
    }
    @Value("${cjkj.amqp.routing-key.order-confirm}")
    public void setOrderConfirmRoutingKey(String orderConfirmRoutingKey) {
        AmqpProperty.orderConfirmRoutingKey = orderConfirmRoutingKey;
    }
}