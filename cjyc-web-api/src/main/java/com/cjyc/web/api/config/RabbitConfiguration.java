package com.cjyc.web.api.config;

import com.cjyc.common.system.config.AmqpProperty;
import org.springframework.amqp.core.*;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;
import java.util.List;

/**
 * @author JPG
 */
@Configuration
@ConditionalOnProperty(prefix = "cjkj.amqp", name = "enabled", havingValue = "true")
public class RabbitConfiguration {

    @Bean
    public TopicExchange topicExchange() {
        return new TopicExchange(AmqpProperty.topicExchange, true, false);
    }

    @Bean
    public Queue orderStateQueue() {
        return new Queue(AmqpProperty.orderStateRoutingKey, true);
    }

    @Bean
    public Queue orderConfirmQueue() {
        return new Queue(AmqpProperty.orderConfirmRoutingKey, true);
    }

    @Bean
    public List<Binding> binds() {
        //参考https://docs.spring.io/spring-amqp/docs/1.7.11.RELEASE/reference/html/_reference.html#collection-declaration
        //链式写法: 用指定的路由键将队列绑定到交换机
        return Arrays.asList(
                BindingBuilder.bind(orderStateQueue()).to(topicExchange()).with(AmqpProperty.orderStateRoutingKey),
                BindingBuilder.bind(orderConfirmQueue()).to(topicExchange()).with(AmqpProperty.orderConfirmRoutingKey)
        );
    }

}
