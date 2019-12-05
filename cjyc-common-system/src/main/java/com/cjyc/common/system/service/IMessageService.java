package com.cjyc.common.system.service;

import com.cjyc.common.model.dto.SmsDto;
import com.cjyc.common.model.vo.ResultVo;

/**
 * 调用消息服务组件
 */
public interface IMessageService {
    /**
     * 手机短信码发送
     * @return
     */
    ResultVo sendSms(SmsDto dto);
}
