package com.cjyc.common.model.dto.web.order;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class OrderCarNoDto {
    @NotBlank(message = "订单车辆编号不能为空")
    @ApiModelProperty(value = "订单车辆编号")
    private String orderCarNo;
}
