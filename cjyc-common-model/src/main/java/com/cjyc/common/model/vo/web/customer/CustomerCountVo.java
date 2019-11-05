package com.cjyc.common.model.vo.web.customer;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
public class CustomerCountVo implements Serializable {

    @ApiModelProperty("总单量")
    private Integer totalOrder;

    @ApiModelProperty("总车量数")
    private Integer totalCar;

    @ApiModelProperty("订单总金额")
    private BigDecimal totalAmount;

}