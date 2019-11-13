package com.cjyc.common.model.enums.message;

import java.text.MessageFormat;

public enum PushMessageEnum {

    /***/
    COMMIT_ORDER("下单提醒", "【韵车物流】您的订单{0}，下单成功，请到订单中心查看"),
    CHECK_ORDER("揽货提醒", "【韵车物流】您的订单{0}，已揽货");

    private String title;
    private String msg;

    PushMessageEnum(String title, String msg) {
        this.title = title;
        this.msg = msg;
    }

    public String getTitle() {
        return title;
    }

    public String getMsg() {
        return msg;
    }

    public String getMsg(String... args) {
        return MessageFormat.format(msg, args);
    }
}
