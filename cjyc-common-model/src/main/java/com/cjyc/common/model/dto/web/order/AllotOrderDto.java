package com.cjyc.common.model.dto.web.order;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotNull;

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class AllotOrderDto {

    @NotNull
    @ApiModelProperty(value = "订单ID", required = true)
    private Long orderId;
    @NotNull
    @ApiModelProperty(value = "操作人ID", required = true)
    private Long loginId;
    @ApiModelProperty(value = "操作人(不用传)")
    private String loginName;

    @NotNull
    @ApiModelProperty(value = "被分配人ID", required = true)
    private Long toAdminId;
    @ApiModelProperty(value = "被分配人名称")
    private String toAdminName;
}
