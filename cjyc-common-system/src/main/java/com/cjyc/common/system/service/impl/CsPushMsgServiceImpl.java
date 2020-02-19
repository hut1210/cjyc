package com.cjyc.common.system.service.impl;

import com.alibaba.fastjson.JSON;
import com.cjkj.common.model.ResultData;
import com.cjkj.log.monitor.LogUtil;
import com.cjyc.common.system.config.JPushProperty;
import com.cjyc.common.system.entity.PushMessageReq;
import com.cjyc.common.system.feign.IClpMessageService;
import com.cjyc.common.system.service.ICsPushMsgService;
import com.google.common.base.Joiner;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import jdk.nashorn.internal.scripts.JO;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestBody;

import javax.annotation.Resource;
import java.text.MessageFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class CsPushMsgServiceImpl implements ICsPushMsgService {

    @Resource
    private IClpMessageService clpMessageService;


    /**
     * 发送消息
     *
     * @param userId
     * @param templateId
     * @author JPG
     * @since 2019/11/13 13:20
     */
    @Async
    @Override
    public void send(Long userId, Long templateId, String... args) {
        send(Lists.newArrayList(userId),templateId, args);
    }

    /**
     * 发送消息
     *
     * @param userIds
     * @param templateId
     * @param args
     * @author JPG
     * @since 2019/11/13 13:20
     */
    @Override
    public void send(List<Long> userIds, Long templateId, String... args) {
        try {
            if(CollectionUtils.isEmpty(userIds)){
                return;
            }
            Map<String, Object> params = Maps.newHashMap();
            for (int i = 0; i < args.length - 1; i++) {
                params.put(String.valueOf(i), args[i]);
            }
            PushMessageReq pushMessageReq = new PushMessageReq();
            pushMessageReq.setApiKey(JPushProperty.apiKey);
            pushMessageReq.setDest(Joiner.on(",").join(userIds));
            pushMessageReq.setTemplateId(templateId);
            //pushMessageReq.setSignnameId(null);
            //            //pushMessageReq.setSendIp();
            pushMessageReq.setParams(params);
            ResultData resultData = clpMessageService.pushMsg(pushMessageReq);
            LogUtil.debug("【推送消息】" + JSON.toJSONString(resultData));
        } catch (Exception e) {
            LogUtil.error("【推送消息】失败");
            LogUtil.error(e.getMessage(), e);
        }
    }
}
