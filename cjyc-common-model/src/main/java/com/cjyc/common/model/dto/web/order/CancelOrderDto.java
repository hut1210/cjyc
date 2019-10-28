package com.cjyc.common.model.dto.web.order;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.util.List;

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel
public class CancelOrderDto {

    @ApiModelProperty("userId")
    private Long userId;

    @ApiModelProperty("userName")
    private String userName;

    @ApiModelProperty("订单ID")
    private Long orderId;

    @ApiModelProperty("订单ID")
    private List<ChangePriceOrderCarDto> orderCarList;

    @ApiModelProperty("原因")
    private String reason;
}

