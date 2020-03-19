package com.cjyc.foreign.api.push;

import com.alibaba.fastjson.JSONObject;
import com.cjyc.foreign.api.constant.MQConstant;
import com.cjyc.foreign.api.utils.HttpClientUtil;
import com.cjyc.foreign.api.utils.MD5Util;
import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

/**
 * 99车圈订单状态推送
 */
@Data
@Component
public class CarCircleOrderStatePush extends AbstractCarCirclePush {
    private String flag;
    //授权clientId
    @Value("${yc.99cc.clientId}")
    private String clientId;
    //接口地址
    @Value("${yc.99cc.stateInterUrl}")
    private String interUrl;
    //返回结果key
    private static final String RESULT_KEY = "success";
    public CarCircleOrderStatePush() {
        setFlag(MQConstant.TOPIC_ORDER_STATE_99CC);
    }
    @Override
    public boolean push(JSONObject jo) {
        System.out.println("订单状态接收到数据为: " + jo.toJSONString());
        return doPush(interUrl, jo, clientId, null, null);
    }
}
