package com.cjyc.common.model.dto.web.order;

import com.cjyc.common.model.dto.web.BaseWebDto;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class RejectOrderDto extends BaseWebDto {
    @NotNull
    @ApiModelProperty(value = "loginId", required = true)
    private Long loginId;

    @ApiModelProperty(value = "操作人(不用传)")
    private String loginName;

    @NotNull
    @ApiModelProperty(value = "订单ID", required = true)
    private Long orderId;

    @NotNull
    @ApiModelProperty(value = "驳回原因", required = true)
    private String reason;
}
