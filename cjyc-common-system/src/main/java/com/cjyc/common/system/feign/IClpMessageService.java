package com.cjyc.common.system.feign;

import com.cjkj.common.constant.ServiceNameConstants;
import com.cjkj.common.model.ResultData;
import com.cjkj.common.model.SmsSendReq;
import com.cjyc.common.system.entity.NoticeReq;
import com.cjyc.common.system.entity.PushMessageReq;
import com.cjyc.common.system.feign.fallback.MessageServiceFallbackFactory;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * 物流平台：消息中心内部调用
 */
@FeignClient(name = ServiceNameConstants.MESSAGE_SERVICE,
        fallbackFactory = MessageServiceFallbackFactory.class)
public interface IClpMessageService {

    /**
     * 手机短信发送
     * @param req
     * @return
     */
    @PostMapping(value = "/message/sms/send")
    ResultData sendSms(@RequestBody SmsSendReq req);

    /**
     *极光推送：推送消息
     * @param req
     * @return
     */
    @PostMapping(value = "/message/push")
    ResultData pushMsg(@RequestBody PushMessageReq req);

    /**
     * 极光推送：推送通知
     * @param req
     * @return
     */
    @PostMapping(value = "/message/notice")
    ResultData pushNotice(@RequestBody NoticeReq req);
}
