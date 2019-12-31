package com.cjyc.common.model.dto.salesman.mine;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
@Data
public class OrderCarDto implements Serializable {

    @ApiModelProperty(value = "订单车辆id")
    private Long orderCarId;
}