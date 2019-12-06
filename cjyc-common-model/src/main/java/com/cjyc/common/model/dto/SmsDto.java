package com.cjyc.common.model.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Map;

/**
 * 短信请求信息
 */
@Data
public class SmsDto {
    @ApiModelProperty(value = "模板标识")
    private Long templateId;
    @ApiModelProperty(value = "apiKey: 配置文件配置或参数代入")
    private String apiKey;
    @ApiModelProperty(value = "短信签名：配置文件配置或参数代入")
    private String signName;
    @ApiModelProperty(value = "目标手机号")
    private String phone;
    @ApiModelProperty(value = "模板参数：为模板代入参数")
    private Map<String, Object> params;
    @ApiModelProperty(value = "短信发送内容：如果内容发送templateId、params需要为空，" +
            "否则templateId、params不能为空")
    private String content;
}
