package com.cjyc.common.system.service;

import com.cjyc.common.model.entity.Order;
import com.cjyc.common.model.entity.OrderCar;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.scheduling.annotation.Async;

import java.util.List;
import java.util.Set;

public interface ICsAmqpService extends RabbitTemplate.ConfirmCallback {

    @Async
    void sendOrderConfirm(Order order, List<OrderCar> ocList);
    @Async
    void send(String exchange, String routingKey, Object message);
    @Async
    void sendOrderState(Order order);
    @Async
    void sendOrderState(Set<Order> orderSet, String type);
}
