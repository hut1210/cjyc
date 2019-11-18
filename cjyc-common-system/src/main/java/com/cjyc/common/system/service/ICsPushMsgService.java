package com.cjyc.common.system.service;

public interface ICsPushMsgService {
    /**
     * 发送消息
     * @author JPG
     * @since 2019/11/13 13:20
     * @param phone
     * @param msg
     */
    void send(String phone, String msg);

    /**
     * 发送消息
     * @author JPG
     * @since 2019/11/13 13:20
     * @param phone
     * @param msg
     */
    void send(String phone, String msg, String... args);
}
