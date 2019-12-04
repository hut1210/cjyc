package com.cjyc.common.model.dto.customer.pingxx;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class PrePayDto {
    @ApiModelProperty(value = "登录人ID")
    private Long uid;
    @ApiModelProperty(value = "订单No")
    private String orderNo;
    @ApiModelProperty(value = "支付渠道")
    private String channel;
}
