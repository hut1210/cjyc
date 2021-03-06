package com.cjyc.common.model.dto.web.pingxx;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class WebPrePayDto {
    @ApiModelProperty(value = "登录人ID")
    private String uid;
    @ApiModelProperty(value = "订单No")
    private String orderNo;
    @ApiModelProperty(value = "支付渠道 微信：wx_pub_qr，支付宝：alipay_qr")
    private String channel;

    @ApiModelProperty(value = "IP（不用传）")
    private String ip;

    @ApiModelProperty(value = "2为业务员端出示 1为web端", required = true)
    private int clientType;
}
