package com.cjyc.common.model.dto.customer.pingxx;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import java.util.List;

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

    @NotEmpty(message = "任务车辆ID")
    @ApiModelProperty(value = "任务车辆ID")
    private List<String> taskCarIdList;

    @ApiModelProperty(hidden = true)
    private String ip;

    @ApiModelProperty(value = "2为司机端出示 3为业务员端出示", hidden = true)
    private int clientType;
}
