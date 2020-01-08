package com.cjyc.common.model.dto.customer.pingxx;

import com.cjyc.common.model.enums.UserTypeEnum;
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

    @ApiModelProperty(value = "用户端类型",hidden = true)
    private Integer clientId;
    @ApiModelProperty(value = "用户ID",required = true)
    private Long loginId;
    @ApiModelProperty(value = "用户名称",hidden = true)
    private String loginName;
    @ApiModelProperty(value = "用户类型",hidden = true)
    private UserTypeEnum loginType;
    @ApiModelProperty(hidden = true)
    private String ip;

    @ApiModelProperty(value = "任务Id")
    private Long taskId;
    @ApiModelProperty(value = "支付渠道")
    private String channel;
    @NotEmpty(message = "任务车辆ID")
    @ApiModelProperty(value = "任务车辆ID")
    private List<String> taskCarIdList;
    @ApiModelProperty(value = "4为司机端出示 2为业务员端出示", required = true)
    private int clientType;
}
