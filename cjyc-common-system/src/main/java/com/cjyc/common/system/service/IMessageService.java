package com.cjyc.common.system.service;

import com.cjyc.common.model.dto.NoticeDto;
import com.cjyc.common.model.dto.SmsDto;
import com.cjyc.common.model.vo.ResultVo;
import com.cjyc.common.system.entity.PushMessageReq;

/**
 * 调用消息服务组件
 */
public interface IMessageService {
    /**
     * 手机短信码发送
     * @return
     */
    ResultVo sendSms(SmsDto dto);

    /**
     * 极光推送消息
     * @param req
     * @return
     */
    ResultVo push(PushMessageReq req);

    /**
     * 极光通知
     * @param dto
     * @return
     */
    ResultVo notice(NoticeDto dto);
}
