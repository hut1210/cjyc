package com.cjyc.common.model.enums.message;

import java.text.MessageFormat;

public enum PushMsgEnum {

    /***/
    COMMIT_ORDER("下单提醒", "您的订单：{0}已成功下单，稍后将由工作人员与您确认发运信息，请保持手机畅通，如有疑问请咨询客户服务热线：4009199266", 28),
    PAY_ORDER("付款提醒", "您的订单：{0}已接单，请及时付款，，如有疑问请咨询客户服务热线：4009199266", 28),
    PILOT_PICK("代驾上门提醒", "您的订单：{0}由韵车司机（{1}）提车中，请保持手机畅通，如有疑问请咨询客户服务热线：4009199266", 30),
    CONSIGN_PICK("拖车上门提醒", "您的订单：{0}由韵车救援车({1}/{2})提车中，请保持手机畅通，如有疑问请咨询客户服务热线：4009199266", 31);

    private String title;
    private String msg;
    private long code;

    PushMsgEnum(String title, String msg, long code) {
        this.title = title;
        this.msg = msg;
        this.code = code;
    }

    public String getTitle() {
        return title;
    }

    public String getMsg() {
        return msg;
    }

    public long getCode() {
        return code;
    }

    public String getMsg(String... args) {
        return MessageFormat.format(msg, args);
    }
}
