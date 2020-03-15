package com.cjyc.common.system.service;

import com.cjyc.common.model.entity.Order;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.scheduling.annotation.Async;

public interface ICsAmqpService extends RabbitTemplate.ConfirmCallback {

    @Async
    void sendOrderConfirm(Order order);
    @Async
    void send(String exchange, String routingKey, Object message);
    @Async
    void sendOrderState(Order order);
}
