package com.cjyc.common.system.service.impl;

import com.cjyc.common.system.service.ICsPushMsgService;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.text.MessageFormat;

@Service
public class CsPushMsgServiceImpl implements ICsPushMsgService {

    /**
     * 发送消息
     *
     * @param phone
     * @param msg
     * @author JPG
     * @since 2019/11/13 13:20
     */
    @Async
    @Override
    public void send(String phone, String msg) {

    }

    /**
     * 发送消息
     *
     * @param phone
     * @param msg
     * @param args
     * @author JPG
     * @since 2019/11/13 13:20
     */
    @Async
    @Override
    public void send(String phone, String msg, String... args) {
        msg = MessageFormat.format(msg, args);
    }
}
