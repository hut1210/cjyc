package com.cjyc.common.system.service;

import com.cjyc.common.model.entity.defined.PushInfo;
import com.cjyc.common.model.enums.UserTypeEnum;
import com.cjyc.common.model.enums.message.PushMsgEnum;

import java.util.List;

public interface ICsPushMsgService {

    /**
     * 发送消息
     * @author JPG
     * @since 2019/11/13 13:20
     */
    void send(Long userId, UserTypeEnum userTypeEnum, PushMsgEnum pushMsgEnum, Object... args);

    /**
     * 发送消息
     * @author JPG
     * @since 2019/11/13 13:20
     */
    void send(List<Long> userIds, UserTypeEnum userTypeEnum, PushMsgEnum pushMsgEnum, Object... args);

    PushInfo getPushInfo(Long toUserId, UserTypeEnum userTypeEnum, PushMsgEnum pushMsgEnum, Object... args);

    void send(PushInfo p);

    void send(List<PushInfo> pushList);

}
