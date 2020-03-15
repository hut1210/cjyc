package com.cjyc.common.system.service.impl;

import com.cjyc.common.model.entity.Order;
import com.cjyc.common.model.entity.defined.OrderStateEntity;
import com.cjyc.common.system.config.AmqpProperty;
import com.cjyc.common.system.service.ICsAmqpService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.UUID;

@Service
@Slf4j
public class CsAmqpServiceImpl implements ICsAmqpService {
    @Resource
    private RabbitTemplate rabbitTemplate;

    @Async
    @Override
    public void sendOrderState(Order order) {
        if(order == null){
            return;
        }
        if(!AmqpProperty.enabled){
            return;
        }
        send(AmqpProperty.topicExchange, AmqpProperty.orderStateRoutingKey, new OrderStateEntity(order.getNo(), order.getState(), System.currentTimeMillis()));
    }
    @Async
    @Override
    public void sendOrderConfirm(Order order) {
        if(order == null){
            return;
        }
        send(AmqpProperty.directExchange, AmqpProperty.orderStateRoutingKey, null);
    }
    @Async
    @Override
    public void send(String exchange, String routingKey, Object message) {
        if(message == null){
            return;
        }
        //设置回调对象
        rabbitTemplate.setConfirmCallback(this);
        //构建回调返回的数据
        CorrelationData correlationData = new CorrelationData(String.valueOf(UUID.randomUUID()));
        rabbitTemplate.convertAndSend(exchange, routingKey, message, correlationData);
        log.info("Amqp >>> 发送消息到RabbitMQ, 消息内容: " + message);
    }


    /**
     * 消息回调确认方法
     */
    @Override
    public void confirm(CorrelationData correlationData, boolean isSendSuccess, String s) {
        log.info("confirm回调方法>>>>>>>>>>>>>回调消息ID为: " + correlationData.getId());
        if (isSendSuccess) {
            log.info("confirm回调方法>>>>>>>>>>>>>消息发送成功");
        } else {
            log.info("confirm回调方法>>>>>>>>>>>>>消息发送失败" + s);
        }

    }
}
