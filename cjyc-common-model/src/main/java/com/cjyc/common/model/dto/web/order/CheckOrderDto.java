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
public class CheckOrderDto {

    @NotNull
    @ApiModelProperty(value = "loginId", required = true)
    private Long userId;

    @ApiModelProperty("loginName")
    private String userName;

    @NotNull
    @ApiModelProperty(value = "订单ID", required = true)
    private Long orderId;

}
