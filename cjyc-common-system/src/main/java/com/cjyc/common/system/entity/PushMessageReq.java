package com.cjyc.common.system.entity;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Map;

/**
 * 极光推送：消息推送
 * 
 * @author Liuk
 * @email cjwl56@gmail.com
 * @date 2019-05-20 22:49:28
 */
@Data
public class PushMessageReq {

	/**
	 * apiKey标识
	 */
	@ApiModelProperty(value = "apiKey标识")
	private String apiKey;

	/**
	 * 目标
	 */
	@ApiModelProperty(value = "目标")
	private String dest;
	/**
	 * 模板编号
	 */
	@ApiModelProperty(value = "模板编号")
	private Long templateId;
	/**
	 * 签名ID
	 */
	@ApiModelProperty(value = "签名ID")
	private Long signnameId;

	/**
	 * 发送IP
	 */
	@ApiModelProperty(value = "发送IP")
	private String sendIp;

	/**
	 * 参数
	 */
	@ApiModelProperty(value = "参数")
	private Map<String,Object> params;

}
