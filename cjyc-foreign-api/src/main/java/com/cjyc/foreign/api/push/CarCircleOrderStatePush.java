package com.cjyc.foreign.api.push;

import com.alibaba.fastjson.JSONObject;
import com.cjyc.foreign.api.constant.MQConstant;
import lombok.Data;
import org.springframework.stereotype.Component;

/**
 * 99车圈订单状态推送
 */
@Data
@Component
public class CarCircleOrderStatePush implements IOrderStatePushable{
    private String flag;
    public CarCircleOrderStatePush() {
        setFlag(MQConstant.TOPIC_ORDER_STATE_99CC);
    }
    @Override
    public boolean push(JSONObject jo) {
        return false;
    }
}
