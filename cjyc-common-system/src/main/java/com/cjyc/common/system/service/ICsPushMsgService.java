package com.cjyc.common.system.service;

import java.util.List;

public interface ICsPushMsgService {

    /**
     * 发送消息
     * @author JPG
     * @since 2019/11/13 13:20
     */
    void send(Long userId, Long templateId, String... args);

    /**
     * 发送消息
     * @author JPG
     * @since 2019/11/13 13:20
     */
    void send(List<Long> userIds, Long templateId, String... args);
}
