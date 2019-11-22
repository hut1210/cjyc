package com.cjyc.common.model.dto.web.order;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class RejectOrderDto {
    @NotNull
    @ApiModelProperty(value = "loginId", required = true)
    private Long userId;

    @ApiModelProperty("loginName")
    private String userName;

    @NotNull
    @ApiModelProperty(value = "订单ID", required = true)
    private Long orderId;

    @NotNull
    @ApiModelProperty(value = "驳回原因", required = true)
    private String reason;
}
