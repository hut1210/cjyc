package com.cjyc.common.model.dto.salesman.order;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
@Data
public class SalesOrderDetailDto implements Serializable {
    private static final long serialVersionUID = 5621071994742064856L;
    @ApiModelProperty(value = "订单id",required = true)
    @NotNull(message = "订单id不能为空")
    private Long orderId;
}