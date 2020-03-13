package com.cjyc.foreign.api.mqhandler;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.cjkj.log.monitor.LogUtil;
import com.cjyc.foreign.api.push.IPushable;
import com.cjyc.foreign.api.utils.PushUtil;
import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 99车圈推送订单状态任务
 */
@Slf4j
@Component
public class YcPushTask {
    /**
     * 线程池
     */
    private static final ThreadPoolExecutor EXEC = new ThreadPoolExecutor(3, 10, 3, TimeUnit.SECONDS,
            new ArrayBlockingQueue<Runnable>(3), new ThreadPoolExecutor.DiscardOldestPolicy());

//    @RabbitHandler
//    @RabbitListener(queues = MQConstant.QUEUE_YC_PUSH)
    private void pushOrderStateFunc(Message message, Channel channel) {
        EXEC.execute(() -> {
            final DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            final String topic = message.getMessageProperties().getReceivedRoutingKey();
            String jsonMsg = new String(message.getBody(), StandardCharsets.UTF_8);
            LogUtil.info(fmt.format(LocalDateTime.now()) + "收到 Topic: {}, 消息: [{}]",
                    topic, jsonMsg);
            JSONObject obj = null;
            try {
                obj = JSON.parseObject(jsonMsg, JSONObject.class);
                IPushable pushable = PushUtil.determinePushable(topic);
                if (null == pushable) {
                    log.error("此消息没有对应推送器，请检查!");
                    dealNackMsg(message, channel, jsonMsg);
                }
                //TODO 调用状态回调接口
                if (pushable.push(obj)) {
                    //正常逻辑处理后在应答, 需要先调用推送接口并解析返回结果，成功后确认
                    channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
                    log.info("消费消息成功，id: {}", message.getMessageProperties().getDeliveryTag());
                }else {
                    //推送不成功
                    log.error("推送信息失败：id:{}", message.getMessageProperties().getDeliveryTag());
                    dealNackMsg(message, channel, jsonMsg);
                }
            }catch (Exception e) {
                dealNackMsg(message, channel, jsonMsg);
            }
        });
    }

    /**
     * 消息处理失败，回退处理
     * @param message
     * @param channel
     * @param jsonMsg
     */
    private void dealNackMsg(Message message, Channel channel, String jsonMsg) {
        log.error("消息处理失败：id:{}", message.getMessageProperties().getDeliveryTag());
        if (message.getMessageProperties().getRedelivered()) {
            log.info("消息已经回滚过，拒绝接收消息 ： {}", jsonMsg);
            try {
                channel.basicReject(message.getMessageProperties().getDeliveryTag(), false);
            }catch (IOException ex) {
                log.error("消息已经回滚过，拒绝接收消息异常", ex);
            }
        } else {
            log.info("消息即将返回队列重新处理 ：{}", jsonMsg);
            // 设置消息重新回到队列处理
            // requeue表示是否重新回到队列，true重新入队
            try {
                channel.basicNack(message.getMessageProperties().getDeliveryTag(), false, true);
            } catch (IOException ex) {
                log.error("消息即将返回队列重新处理异常", ex);
            }
        }
    }
}
