package com.cjyc.common.until;

import org.springframework.stereotype.Component;

/**
 * 公共字典类
 *
 * Created by leo on 2019/7/25.
 */
@Component
public class Constants {

    //短信模板
    public static String MSG_SEND_TEMP = "您的验证码是%s,请勿泄露。%d分钟内有效。";
    public static String MSG_CUSTOMER_ORDER_CREATE_TEMP = "您的订单：从%s到%s的%d辆车已确认，我们会尽快处理。详情请登录韵车App进行查看";
    public static String MSG_CUSTOMER_ORDER_FINISH_TEMP = "您的订单已完成，感谢使用韵车物流。";
    public static String MSG_CUSTOMER_CONFIRM_CODE = "您的收车码是%s,请勿泄露。%d分钟内有效。";
    public static String MSG_DRIVER_BIDDING_SUCCESS = "您已成功竞抢韵车订单，请及时处理。";

    //app推送模板
    public static String PUSH_SALESMAN_ORDER_NEW = "你有新的订单需要处理";

}
