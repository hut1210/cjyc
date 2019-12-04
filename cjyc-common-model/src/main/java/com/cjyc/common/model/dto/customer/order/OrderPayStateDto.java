package com.cjyc.common.model.dto.customer.order;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
public class OrderPayStateDto {
    @ApiModelProperty(value = "用户ID")
    private Long loginId;
    @ApiModelProperty(value = "用户名称（不用传）")
    private String loginName;
    private List<Long> orderCarId;
}
