package com.cjyc.common.model.enums.message;

import java.text.MessageFormat;

/**
 * 短信内容模板
 * @author JPG
 */
public enum SmsMessageEnum {

    /***/
    SMS_CAPTCHA("【韵车物流】验证码：{0}。请在5分钟内填写。如非本人操作，请忽略本短信。要运车，找韵车，韵车物流为您提供最优质的的车辆运输服务"),
    /**
     * 客户自提短信通知
     * {1} 订单号
     * {2} XX市
     * {3} 业务中心地点
     * {4} 联系人名称
     * {5} 联系人电话
     */
    SELF_PICK("【韵车物流】您的订单：{0}已到达{1}，请您到【{2}】提车，收车点联系电话【{3}，{4}】。");

    private String msg;

    SmsMessageEnum(String msg) {
        this.msg = msg;
    }

    public String getMsg() {
        return msg;
    }

    public String getMsg(String... args) {
        return MessageFormat.format(msg, args);
    }


}
