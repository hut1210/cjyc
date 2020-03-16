package com.cjyc.common.system.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnProperty(prefix = "cjkj.amqp", name = "enabled", havingValue = "true")
public class AmqpProperty {

    public static Boolean enabled;
    public static String topicExchange;
    public static String commonRoutingKey;

    @Value("${cjkj.amqp.enabled}")
    public void setEnabled(Boolean enabled) {
        AmqpProperty.enabled = enabled;
    }

    @Value("${cjkj.amqp.exchange.topic}")
    public void setTopicExchange(String topicExchange) {
        AmqpProperty.topicExchange = topicExchange;
    }

    @Value("${cjkj.amqp.routing-key.common}")
    public void setCommonRoutingKey(String commonRoutingKey) {
        AmqpProperty.commonRoutingKey = commonRoutingKey;
    }
}