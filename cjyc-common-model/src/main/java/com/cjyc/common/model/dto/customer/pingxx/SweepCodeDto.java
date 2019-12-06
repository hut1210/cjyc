package com.cjyc.common.model.dto.customer.pingxx;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class SweepCodeDto {

    @ApiModelProperty(value = "支付渠道")
    private String channel;

    @ApiModelProperty(value = "收款人Id")
    private String payeeId;

    @ApiModelProperty(value = "任务Id")
    private Long taskId;

    @ApiModelProperty(value = "车辆Id")
    private String orderCarIds;

    @ApiModelProperty(value = "IP地址（不用传）")
    private String ip;

}
