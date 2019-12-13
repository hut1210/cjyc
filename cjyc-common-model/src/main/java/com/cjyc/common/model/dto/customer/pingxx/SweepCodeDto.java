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

    @ApiModelProperty(value = "ip")
    private String ip;

    @ApiModelProperty(value = "验证码",required = true)
    @NotBlank(message = "验证码不能为空")
    private String code;

    @ApiModelProperty("1为司机端出示 2为业务员端出示")
    private String clientType;
}
