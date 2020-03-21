package com.cjyc.foreign.api.utils;

import com.cjyc.foreign.api.push.IPushable;
import org.springframework.util.CollectionUtils;

import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

/**
 * 推送工具类
 */
public class PushUtil {
    /**
     * 获取推送实例
     * @param topic
     * @return
     */
    public static IPushable determinePushable(String topic) {
        Map<String, IPushable> pushBeanMap = SpringContextUtil.getApplicationContext().getBeansOfType(IPushable.class);
        AtomicReference<IPushable> pushableAf = new AtomicReference<>();
        if (!CollectionUtils.isEmpty(pushBeanMap)) {
            pushBeanMap.forEach((key, value) -> {
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
