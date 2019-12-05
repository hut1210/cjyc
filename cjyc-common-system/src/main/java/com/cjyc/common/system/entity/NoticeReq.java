package com.cjyc.common.system.entity;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 极光推送：消息通知
 */
@Data
public class NoticeReq {
    /**
     * apiKey标识
     */
    @ApiModelProperty(value = "apiKey标识")
    private String apiKey;

    /**
     * 消息类型
     */
    @ApiModelProperty(value = "消息类型 1:消息，2：通知，3：验证码,默认为2")
    private String contentType = "2";

    /**
     * 标题
     */
    @ApiModelProperty(value = "标题")
    private String title;
    /**
     * 内容
     */
    @ApiModelProperty(value = "内容")
    private String content;

    /**
     * 链接
     */
    @ApiModelProperty(value="链接")
    private String attachUrl;

    /**
     * 发送IP
     */
    @ApiModelProperty(value = "发送IP")
    private String sendIp;

    @ApiModelProperty(value = "图片地址")
    private String imgUrl;

    @ApiModelProperty(value = "跳转链接")
    private String intentUrl;

}
