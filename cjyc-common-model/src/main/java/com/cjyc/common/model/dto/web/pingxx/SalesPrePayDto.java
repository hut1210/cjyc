package com.cjyc.common.model.dto.web.pingxx;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Author: Hut
 * @Date: 2020/01/07 18:11
 **/
@Data
public class SalesPrePayDto {

    @ApiModelProperty(value = "登录人ID")
    private Long loginId;
    @ApiModelProperty(value = "订单No")
    private String orderNo;
    @ApiModelProperty(value = "支付渠道 微信：wx_pub_qr，支付宝：alipay_qr")
    private String channel;

    @ApiModelProperty(value = "IP（不用传）",hidden = true)
    private String ip;

    @ApiModelProperty(value = "8为业务员端预付款出示", required = true)
    private int clientType;
}
