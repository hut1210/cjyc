package com.cjyc.common.model.dto.web.order;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotNull;

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel
public class AllotOrderDto {

    @NotNull
    @ApiModelProperty(value = "订单ID", required = true)
    private Long orderId;
    @NotNull
    @ApiModelProperty(value = "操作人ID", required = true)
    private Long userId;
    @ApiModelProperty("操作人名称")
    private String userName;

    @NotNull
    @ApiModelProperty(value = "被分配人ID", required = true)
    private Long toUserId;
    @ApiModelProperty(value = "被分配人名称")
    private String toUserName;
}
