package com.cjyc.foreign.api.push;

import com.alibaba.fastjson.JSONObject;
import com.cjyc.foreign.api.constant.MQConstant;
import com.cjyc.foreign.api.utils.HttpClientUtil;
import com.cjyc.foreign.api.utils.MD5Util;
import com.google.common.collect.Lists;
import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * 99车圈订单确认推送
 */
@Data
@Component
public class CarCircleOrderConfirmPush extends AbstractCarCirclePush {
    private String flag;
    //授权clientId
    @Value("${yc.99cc.clientId}")
    private String clientId;
    //接口地址
    @Value("${yc.99cc.confirmInterUrl}")
    private String interUrl;
    public CarCircleOrderConfirmPush() {
        setFlag(MQConstant.TOPIC_ORDER_CONFIRM_99CC);
    }
    @Override
    public boolean push(JSONObject jo) {
        System.out.println("订单确认接收到数据为: " + jo.toJSONString());
        return doPush(interUrl, jo, clientId, null, null);
    }
}
