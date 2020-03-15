package com.cjyc.common.system.service.impl;

import com.cjyc.common.model.entity.Order;
import com.cjyc.common.model.entity.OrderCar;
import com.cjyc.common.model.util.MoneyUtil;
import com.cjyc.common.system.config.AmqpProperty;
import com.cjyc.common.system.service.ICsAmqpService;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Set;
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
        sendOrderState(Sets.newHashSet(order));
    }
    @Async
    @Override
    public void sendOrderState(Set<Order> orderSet) {
        if(CollectionUtils.isEmpty(orderSet)){
            return;
        }
        orderSet.forEach(o -> {
            Map<Object, Object> map = Maps.newHashMap();
            map.put("orderNo", o.getNo());
            map.put("state", o.getState());
            map.put("createTime", System.currentTimeMillis());
            send(AmqpProperty.topicExchange, AmqpProperty.orderStateRoutingKey, map);
        });
    }

    @Async
    @Override
    public void sendOrderConfirm(Order order, List<OrderCar> ocList) {
        if(order == null){
            return;
        }
        BigDecimal pickTotalFee = BigDecimal.ZERO;
        BigDecimal trunkTotalFee = BigDecimal.ZERO;
        BigDecimal backTotalFee = BigDecimal.ZERO;
        BigDecimal addInsuranceTotalFee = BigDecimal.ZERO;
        for (OrderCar orderCar : ocList) {
            pickTotalFee = pickTotalFee.add(MoneyUtil.nullToZero(orderCar.getPickFee()));
            trunkTotalFee = trunkTotalFee.add(MoneyUtil.nullToZero(orderCar.getTrunkFee()));
            backTotalFee = backTotalFee.add(MoneyUtil.nullToZero(orderCar.getBackFee()));
            addInsuranceTotalFee = addInsuranceTotalFee.add(MoneyUtil.nullToZero(orderCar.getAddInsuranceFee()));

        }
        Map<Object, Object> map = Maps.newHashMap();
        map.put("orderNo", order.getNo());
        map.put("state", order.getState());
        map.put("pickFee", pickTotalFee);
        map.put("trunkFee", trunkTotalFee);
        map.put("backFee", backTotalFee);
        map.put("addInsuranceFee", addInsuranceTotalFee);
        map.put("createTime", System.currentTimeMillis());
        send(AmqpProperty.directExchange, AmqpProperty.orderStateRoutingKey, map);
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
