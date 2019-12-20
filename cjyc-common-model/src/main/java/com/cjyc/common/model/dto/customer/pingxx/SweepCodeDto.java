package com.cjyc.common.model.dto.customer.pingxx;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * @Author: Hut
 * @Date: 2019/12/11 15:19
 */
@Data
public class SweepCodeDto {

    @ApiModelProperty(value = "支付渠道")
    private String channel;

    @ApiModelProperty(value = "用户ID",required = true)
    private Long loginId;

    @ApiModelProperty(value = "任务Id")
    private Long taskId;

    @ApiModelProperty(value = "ip (不用传)")
    private String ip;

    @ApiModelProperty("1为司机端出示 2为业务员端出示")
    private int clientType;
}
