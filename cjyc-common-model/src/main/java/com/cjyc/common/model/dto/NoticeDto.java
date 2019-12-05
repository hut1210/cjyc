package com.cjyc.common.model.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 极光通知Dto
 */
@Data
public class NoticeDto {
    /**
     * apiKey标识
     */
    @ApiModelProperty(value = "apiKey标识")
    private String apiKey;
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
