package com.cjyc.web.api.config;

import com.cjyc.common.system.config.AmqpProperty;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


/**
 * @author JPG
 */
@Configuration
@ConditionalOnProperty(prefix = "cjkj.amqp", name = "enabled", havingValue = "true")
public class RabbitConfiguration {

   /* @Bean
    public TopicExchange topicExchange() {
        return new TopicExchange(AmqpProperty.topicExchange, true, false);
    }

    @Bean
    public Queue orderStateQueue() {
        return new Queue(AmqpProperty.commonRoutingKey, true);
    }

    @Bean
    public Binding binds(TopicExchange topicExchange , Queue orderStateQueue) {
        //参考https://docs.spring.io/spring-amqp/docs/1.7.11.RELEASE/reference/html/_reference.html#collection-declaration
        //链式写法: 用指定的路由键将队列绑定到交换机
        return BindingBuilder.bind(orderStateQueue).to(topicExchange).with(AmqpProperty.commonRoutingKey);
    }*/

}
