package com.cjyc.common.system.service.impl;

import com.alibaba.fastjson.JSON;
import com.cjkj.common.model.ResultData;
import com.cjkj.log.monitor.LogUtil;
import com.cjyc.common.model.enums.UserTypeEnum;
import com.cjyc.common.model.enums.message.PushMsgEnum;
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
     * @author JPG
     * @since 2019/11/13 13:20
     */
    @Async
    @Override
    public void send(Long userId, UserTypeEnum userTypeEnum, PushMsgEnum pushMsgEnum, String... args) {
        send(Lists.newArrayList(userId), userTypeEnum, pushMsgEnum, args);
    }

    /**
     * 发送消息
     *
     * @param userIds
     * @author JPG
     * @since 2019/11/13 13:20
     */
    @Async
    @Override
    public void send(List<Long> userIds, UserTypeEnum userTypeEnum, PushMsgEnum pushMsgEnum, String... args) {
        try {
            if (CollectionUtils.isEmpty(userIds)) {
                return;
            }
            Map<String, Object> params = Maps.newHashMap();
            for (int i = 0; i < args.length; i++) {
                params.put(String.valueOf(i), args[i]);
            }
            PushMessageReq pushMessageReq = new PushMessageReq();
            pushMessageReq.setDest(Joiner.on(",").join(userIds));
            pushMessageReq.setTemplateId(pushMsgEnum.getCode());
            pushMessageReq.setParams(params);
            switch (userTypeEnum.code) {
                case 1:
                    pushMessageReq.setApiKey(JPushProperty.apiKeySalesman);
                    break;
                case 2:
                    pushMessageReq.setApiKey(JPushProperty.apiKeyDriver);
                    break;
                case 3:
                    pushMessageReq.setApiKey(JPushProperty.apiKeyCustomer);
                    break;
                default:
            }
            if(pushMessageReq.getApiKey() == null){
                LogUtil.info("【推送消息】推送功能未启用");
                return;
            }
            LogUtil.debug("【推送消息】-------------->参数" + JSON.toJSONString(pushMessageReq));
            ResultData resultData = clpMessageService.pushMsg(pushMessageReq);
            if("00000".equals(resultData.getCode())){
                //记录消息内容
                LogUtil.debug("【推送消息】成功！，返回参数" + JSON.toJSONString(resultData));
            }else{
                LogUtil.debug("【推送消息】失败！，返回参数" + JSON.toJSONString(resultData));
            }
        } catch (Exception e) {
            LogUtil.error("【推送消息】异常");
            LogUtil.error(e.getMessage(), e);
        }
    }
}
