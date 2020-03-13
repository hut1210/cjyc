package com.cjyc.foreign.api.push;

import com.alibaba.fastjson.JSONObject;

/**
 * mq 消息推送
 */
public interface IPushable {
    boolean push(JSONObject jo);
}
