package com.cjyc.foreign.api.utils;

import com.cjyc.foreign.api.push.IOrderConfirmPushable;
import com.cjyc.foreign.api.push.IOrderStatePushable;
import com.cjyc.foreign.api.push.IPushable;
import org.springframework.util.CollectionUtils;

import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

/**
 * 推送工具类
 */
public class PushUtils {
    /**
     * 获取推送实例
     * @param topic
     * @return
     */
    public static IPushable determinePushable(String topic) {
        Map<String, IOrderStatePushable> orderStateMap = SpringContextUtil.getApplicationContext().getBeansOfType(IOrderStatePushable.class);
        AtomicReference<IPushable> pushableAf = new AtomicReference<>();
        if (!CollectionUtils.isEmpty(orderStateMap)) {
            orderStateMap.forEach((key, value) -> {
                if (value.getFlag().equals(topic)) {
                    pushableAf.set(value);
                    return;
                }
            });
        }
        if (pushableAf.get() != null) {
            return pushableAf.get();
        }
        Map<String, IOrderConfirmPushable> orderConfirmMap = SpringContextUtil.getApplicationContext().getBeansOfType(IOrderConfirmPushable.class);
        if (!CollectionUtils.isEmpty(orderConfirmMap)) {
            orderConfirmMap.forEach((key, value) -> {
                if (value.getFlag().equals(topic)) {
                    pushableAf.set(value);
                    return;
                }
            });
        }
        if (pushableAf.get() != null) {
            return pushableAf.get();
        }
        return null;
    }
}
