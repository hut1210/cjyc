package com.cjyc.common.model.dto.web.order;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class ListOrderChangeLogDto {
    @ApiModelProperty("订单ID")
    private Long orderId;
    @ApiModelProperty("类型:1 订单改价，4取消订单")
    private Integer type;
}
