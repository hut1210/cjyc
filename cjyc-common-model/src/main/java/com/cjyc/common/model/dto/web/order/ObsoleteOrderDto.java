package com.cjyc.common.model.dto.web.order;

import com.cjyc.common.model.dto.BaseLoginDto;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class ObsoleteOrderDto extends BaseLoginDto {
    @NotNull
    @ApiModelProperty(value = "订单ID", required = true)
    private Long orderId;

    @ApiModelProperty(value = "原因", required = true)
    private String reason;

    @ApiModelProperty(value = "是否强制执行", required = true)
    private boolean isForce = false;

}
