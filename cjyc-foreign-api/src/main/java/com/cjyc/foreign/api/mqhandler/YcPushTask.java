package com.cjyc.foreign.api.mqhandler;

import com.alibaba.fastjson.JSONObject;
import com.cjkj.log.monitor.LogUtil;
import com.cjkj.mq.rabbit.bean.BasicRabbitMessage;
import com.cjkj.mq.rabbit.sender.MessageSender;
import com.cjyc.common.system.util.RedisUtils;
import com.cjyc.foreign.api.push.IPushable;
import com.cjyc.foreign.api.utils.PushUtil;
import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
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
    //发送失败后重发策略
    private static Map<Integer, Integer> retryRule = new HashMap<>();
    //重发唯一标识
    private static final String RETRY_FLAG = "uniqueSequence";
    //业务类型key
    private static final String BIZ_TYPE = "type";
    //业务数据key
    private static final String BIZ_DATA = "data";
    //失败次数key
    private static final String FAIL_TIMES = "fail:times:";
    static {
        retryRule.put(1, 600000);
        retryRule.put(2, 1800000);
        retryRule.put(3, 3600000);
    }
    @Autowired
    private MessageSender messageSender;
    @Autowired
    private RedisUtils redisUtils;
    /**
    * @Description: 韵车mq消息推送消费
    * @Param: [message, channel]
    * @return: void
    * @Author: zcm
    * @Date: 2020/3/13
    */
    @RabbitHandler
    @RabbitListener(queues = "${mq.rabbit.yc.push.queue}")
    private void pushOrderStateFunc(Message message, Channel channel) {
        EXEC.execute(() -> {
            final DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            final String topic = message.getMessageProperties().getReceivedRoutingKey();
            String jsonMsg = new String(message.getBody(), StandardCharsets.UTF_8);
            LogUtil.info(fmt.format(LocalDateTime.now()) + "收到 Topic: {}, 消息: [{}]",
                    topic, jsonMsg);
            JSONObject obj = null;
            try {
                obj = (JSONObject) JSONObject.parse(jsonMsg);
                if (obj == null || obj.isEmpty() || StringUtils.isEmpty(obj.getString(BIZ_TYPE))
                 || !obj.containsKey(BIZ_DATA) || obj.getJSONObject(BIZ_DATA).isEmpty()) {
                    log.error("业务数据有误，请检查，数据: {}", obj);
                    channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
                    return;
                }
                //判定消息业务类型
                IPushable pushable = PushUtil.determinePushable(obj.getString(BIZ_TYPE));
                if (null == pushable) {
                    log.error("此消息没有对应推送器，请检查! 数据为: {}", obj);
                    channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
                    return;
                }
                //调用状态回调接口
                if (pushable.push(obj.getJSONObject(BIZ_DATA))) {
                    //正常逻辑处理后在应答, 需要先调用推送接口并解析返回结果，成功后确认
                    channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
                    log.info("消费消息成功，id: {}", message.getMessageProperties().getDeliveryTag());
                    //失败统计信息清空
                    String retryFlag = obj.getString(RETRY_FLAG);
                    if (!StringUtils.isEmpty(retryFlag)) {
                        redisUtils.delete(FAIL_TIMES+retryFlag);
                    }
                }else {
                    //推送到对方接口失败，不是mq失败
                    onPushFail(message, channel, obj);
                }
            }catch (Exception e) {
                e.printStackTrace();
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
    
    /**
    * @Description: 给对方消息推送失败后，需延迟发送
    * @Param: [message, channel, msgJo]
    * @return: void
    * @Author: zcm
    * @Date: 2020/3/14
    */
    private void onPushFail(Message message, Channel channel, JSONObject msgJo) {
        String flag = msgJo.getString(RETRY_FLAG);
        if (StringUtils.isEmpty(flag)) {
            flag = UUID.randomUUID().toString();
            msgJo.put(RETRY_FLAG, flag);
        }
        redisUtils.incrBy(FAIL_TIMES+flag, 1);
        //确认消息
        try {
            channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
        } catch (IOException e) {
            e.printStackTrace();
        }
        //检查第几次失败，然后发送到延迟队列中
        Optional<Integer> max = retryRule.keySet().stream().max((o1, o2) -> o1.compareTo(o2));
        String timesStr = redisUtils.get(FAIL_TIMES + flag);
        if (!StringUtils.isEmpty(timesStr)) {
            if (max.get() >= Integer.parseInt(timesStr)) {
                //调用延迟重发
                Integer delayTime = retryRule.get(
                        Integer.parseInt(timesStr));
                log.info("第{}次重发, 延迟时间：{}, 标识: {}", timesStr, delayTime, flag);
                BasicRabbitMessage brm = new BasicRabbitMessage();
                brm.setExchangeName(message.getMessageProperties().getReceivedExchange());
                brm.setRoutingKey(message.getMessageProperties().getReceivedRoutingKey());
                brm.setMsg(msgJo);
                brm.setUniqueSequence(flag);
                brm.setBizCode("韵车推送");
                messageSender.sendDelayRabbit(brm, delayTime);
            }else {
                log.info("消息重发超过最大次数，标识：{}", flag);
                //不在重发
                redisUtils.delete(FAIL_TIMES+flag);
            }
        }
    }
}
