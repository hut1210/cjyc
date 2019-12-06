package com.cjyc.common.model.dto.customer.order;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
public class CarPayStateDto {
    @ApiModelProperty(value = "用户ID",required = true)
    private Long loginId;
    @ApiModelProperty(value = "用户名称（不用传）")
    private String loginName;
    @ApiModelProperty(value = "订单车辆编号",required = true)
    private List<String> orderCarNos;
}
