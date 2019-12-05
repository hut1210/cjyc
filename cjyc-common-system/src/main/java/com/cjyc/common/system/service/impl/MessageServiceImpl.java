package com.cjyc.common.system.service.impl;

import com.cjkj.common.model.ResultData;
import com.cjkj.common.model.SmsSendReq;
import com.cjyc.common.model.dto.NoticeDto;
import com.cjyc.common.model.dto.SmsDto;
import com.cjyc.common.model.util.BaseResultUtil;
import com.cjyc.common.model.vo.ResultVo;
import com.cjyc.common.system.entity.NoticeReq;
import com.cjyc.common.system.entity.PushMessageReq;
import com.cjyc.common.system.feign.IClpMessageService;
import com.cjyc.common.system.service.IMessageService;
import com.cjyc.common.system.util.ResultDataUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;

/**
 * 消息发送：调用消息服务完成短信、极光消息发送
 */
@Service
@Slf4j
public class MessageServiceImpl implements IMessageService {
    /**
     * 发送类型为：短信
     */
    private static final String SMS_CODE_CONTENT_TYPE = "2";
    /**
     * 模板id
     */
    @Value("${cjkj.sms.templateId}")
    private static Long templateId;
    @Value("${cjkj.sms.apiKey}")
    private static String apiKey;
    @Value("${cjkj.sms.signName:韵车}")
    private static String signName;

    @Resource
    private IClpMessageService clpMessageService;

    @Override
    public ResultVo sendSms(SmsDto dto) {
        SmsSendReq req = new SmsSendReq();
        BeanUtils.copyProperties(dto, req);
        req.setContentType(SMS_CODE_CONTENT_TYPE);
        req.setPhoneNumber(dto.getPhone());
        req.setSignName(dto.getSignName());
        if (StringUtils.isEmpty(req.getApiKey())) {
            req.setApiKey(apiKey);
        }
        if (StringUtils.isEmpty(req.getSignName())) {
            req.setSignName(signName);
        }
        if (!StringUtils.isEmpty(req.getContent())) {
            //内容发送
            req.setTemplateId(null);
            req.setParams(null);
        }else {
            //模板发送
            if (req.getTemplateId() == null || req.getTemplateId() <= 0L) {
                req.setTemplateId(templateId);
            }
        }
        ResultData resultData = clpMessageService.sendSms(req);
        if (ResultDataUtil.isSuccess(resultData)) {
            return BaseResultUtil.success("发送成功");
        }
        return BaseResultUtil.fail("失败，原因：" + resultData.getMsg());
    }

    @Override
    public ResultVo push(PushMessageReq req) {
        if (StringUtils.isEmpty(req.getApiKey())) {
            req.setApiKey(apiKey);
        }
        ResultData resultData = clpMessageService.pushMsg(req);
        if (ResultDataUtil.isSuccess(resultData)) {
            return BaseResultUtil.success("推送消息成功");
        }
        return BaseResultUtil.fail("失败，原因：" + resultData.getMsg());
    }

    @Override
    public ResultVo notice(NoticeDto dto) {
        NoticeReq req = new NoticeReq();
        BeanUtils.copyProperties(dto, req);
        if (StringUtils.isEmpty(req.getApiKey())) {
            req.setApiKey(apiKey);
        }
        ResultData resultData = clpMessageService.pushNotice(req);
        if (ResultDataUtil.isSuccess(resultData)) {
            return BaseResultUtil.success("推送消息成功");
        }
        return BaseResultUtil.fail("失败，原因：" + resultData.getMsg());
    }
}
