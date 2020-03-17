package com.cjyc.foreign.api.push;

import com.alibaba.fastjson.JSONObject;
import com.cjyc.foreign.api.constant.MQConstant;
import lombok.Data;
import org.springframework.stereotype.Component;

/**
 * 99车圈订单确认推送
 */
@Data
@Component
public class CarCircleOrderConfirmPush implements IOrderConfirmPushable{
    private String flag;
    public CarCircleOrderConfirmPush() {
        setFlag(MQConstant.TOPIC_ORDER_CONFIRM_99CC);
    }
    @Override
    public boolean push(JSONObject jo) {
        System.out.println("订单确认接收到数据为: " + jo.toJSONString());
        return false;
    }
}
