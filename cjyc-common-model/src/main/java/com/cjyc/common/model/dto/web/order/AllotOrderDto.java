package com.cjyc.common.model.dto.web.order;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel
public class AllotOrderDto {

    @ApiModelProperty(value = "订单ID", required = true)
    private Long orderId;

    @ApiModelProperty(value = "操作人ID", required = true)
    private Long userId;
    @ApiModelProperty("操作人名称")
    private String userName;

    @ApiModelProperty(value = "被分配人ID",required = true)
    private Long toUserId;
    @ApiModelProperty(value = "被分配人名称")
    private String toUserName;
}
