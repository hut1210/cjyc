package com.cjyc.common.util;

import cn.jpush.api.JPushClient;
import cn.jpush.api.push.PushResult;
import cn.jpush.api.push.model.Message;
import cn.jpush.api.push.model.Options;
import cn.jpush.api.push.model.Platform;
import cn.jpush.api.push.model.PushPayload;
import cn.jpush.api.push.model.audience.Audience;
import cn.jpush.api.push.model.notification.IosNotification;
import cn.jpush.api.push.model.notification.Notification;
import org.springframework.beans.factory.annotation.Value;

import java.util.Map;


/**
 * 极光推送工具类
 * @author leo
 * @date 2019/7/27.
 */
public class JPushUtil {

    @Value("${JPush.masterSecret}")
    private static String masterSecret;

    @Value("${JPush.appKey")
    private static String appKey;


    public static PushResult sendPush(String[] alias, String content) throws Exception{
        JPushClient jpushClient = new JPushClient(masterSecret, appKey);

        PushPayload payload = buildPushObject_all_alias_alert(alias, content);

        return jpushClient.sendPush(payload);

    }


    /**
     * 推送所有平台，所有设备
     *
     * @param content 推送内容
     */
    public static PushPayload buildPushObject_all_all_alert(String content) {
        return PushPayload.alertAll(content);
    }

    /**
     * 按别名推送给所有平台
     *
     * @param alias 推送别名
     * @param content 推送内容
     */
    public static PushPayload buildPushObject_all_alias_alert(String[] alias, String content) {
        return PushPayload.newBuilder()
                .setPlatform(Platform.all())
                .setAudience(Audience.alias(alias))
                .setNotification(Notification.alert(content))
                .build();
    }

    /**
     * 按别名推送给 Android 平台
     *
     * @param alias 推送别名
     * @param content 推送内容
     * @param title 标题
     * @param extras 附加参数
     */
    public static PushPayload buildPushObject_android_alias_alertWithTitle(
            String[] alias, String title, String content, Map<String, String> extras) {
        return PushPayload.newBuilder()
                .setPlatform(Platform.android())
                .setAudience(Audience.alias(alias))
                .setNotification(Notification.android(content, title, extras))
                .build();
    }

    /**
     * 平台是 iOS，推送目标是 "tag1", "tag_all" 的交集，推送内容同时包括通知与消息 - 通知信息是 ALERT，
     * 角标数字为 5，通知声音为 "happy"，并且附加字段 from = "JPush"；
     * 消息内容是 MSG_CONTENT。通知是 APNs 推送通道的，消息是 JPush 应用内消息通道的。
     * APNs 的推送环境是“生产”（如果不显式设置的话，Library 会默认指定为开发）
     *
     * @param alias 推送别名
     * @param msgContent 通知内容
     * @param alertContent 消息内容
     * @param extras 自定义参数
     */
    public static PushPayload buildPushObject_ios_tagAnd_alertWithExtrasAndMessage(String[] alias,
                 String msgContent, String alertContent, String extras) {
        return PushPayload.newBuilder()
                .setPlatform(Platform.ios())
                .setAudience(Audience.alias(alias))
                .setNotification(Notification.newBuilder()
                        .addPlatformNotification(IosNotification.newBuilder()
                                .setAlert(alertContent)
                                .setBadge(1) //角标数字
                                .setSound("happy") //通知声音
                                .addExtra("extras", extras)
                                .build())
                        .build())
                .setMessage(Message.content(msgContent))
                .setOptions(Options.newBuilder()
                        .setApnsProduction(true) //苹果推送通知服务
                        .build())
                .build();
    }

    /**
     * 推送 Andorid 与 iOS 消息
     *
     * @param alias 推送别名
     * @param msgContent 消息内容
     * @param extras 自定义参数
     */
    public static PushPayload buildPushObject_ios_audienceMore_messageWithExtras(String[] alias,
                 String msgContent, String extras) {
        return PushPayload.newBuilder()
                .setPlatform(Platform.android_ios())
                .setAudience(Audience.alias(alias))
                .setMessage(Message.newBuilder()
                        .setMsgContent(msgContent)
                        .addExtra("extras", extras)
                        .build())
                .build();
    }
}
