package com.cjyc.common.system.feign.fallback;

import com.cjkj.common.model.ResultData;
import com.cjkj.common.model.SmsSendReq;
import com.cjyc.common.system.entity.NoticeReq;
import com.cjyc.common.system.entity.PushMessageReq;
import com.cjyc.common.system.feign.IClpMessageService;
import feign.hystrix.FallbackFactory;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;

/**
 * 消息中心 降级工厂
 */
@Component
@Log4j2
public class MessageServiceFallbackFactory implements FallbackFactory<IClpMessageService> {
    @Override
    public IClpMessageService create(Throwable throwable) {
        return new IClpMessageService() {

            @Override
            public ResultData sendSms(SmsSendReq req) {
                log.error("调用消息服务：降级");
                return ResultData.failed("网络异常，请稍后再试");
            }

            @Override
            public ResultData pushMsg(PushMessageReq req) {
                log.error("调用消息服务：降级");
                return ResultData.failed("网络异常，请稍后再试");
            }

            @Override
            public ResultData pushNotice(NoticeReq req) {
                log.error("调用消息服务：降级");
                return ResultData.failed("网络异常，请稍后再试");
            }
        };
    }
}
