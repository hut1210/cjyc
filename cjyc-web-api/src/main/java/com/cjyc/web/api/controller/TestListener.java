package com.cjyc.web.api.controller;

import com.alibaba.fastjson.JSON;
import com.cjyc.common.model.entity.defined.OrderStateEntity;
import com.cjyc.common.system.config.AmqpProperty;
import com.rabbitmq.client.Channel;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.*;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@Api(tags = "rabbit")
@RequestMapping("/mq")
@RestController
@RabbitListener(queues = "${cjkj.amqp.routing-key.order-state}")
@Slf4j
public class TestListener {

    @Resource
    private AmqpTemplate rabbitTemplate;

    @RabbitListener(bindings = {@QueueBinding(value = @Queue(value = "${cjkj.amqp.routing-key.order-state}"), exchange = @Exchange(value = "${cjkj.amqp.exchange.topic}"), key = "${cjkj.amqp.routing-key.order-state}")})
    public void testStateListener(String s) {
        log.info("消费信息----------------------》》》》》》" + s);

    }
    @RabbitListener(bindings = {@QueueBinding(value = @Queue(value = "${cjkj.amqp.routing-key.order-confirm}"), exchange = @Exchange(value = "${cjkj.amqp.exchange.topic}"), key = "${cjkj.amqp.routing-key.order-confirm}")})
    public void testConfirmListener(String s) {
        log.info("消费信息----------------------》》》》》》" + s);

    }

    @GetMapping("/send")
    public String send(String s){
        System.out.println("----------------------》》》》》》" + AmqpProperty.topicExchange);
        System.out.println("----------------------》》》》》》" + AmqpProperty.orderStateRoutingKey);
        System.out.println("----------------------》》》》》》" + AmqpProperty.orderConfirmRoutingKey);
        System.out.println("----------------------》》》》》》" + AmqpProperty.enabled);
        rabbitTemplate.convertAndSend(AmqpProperty.topicExchange, AmqpProperty.orderStateRoutingKey, JSON.toJSONString(new OrderStateEntity(s, 1, 1214354567689L)));
        return "成功";
    }


}
