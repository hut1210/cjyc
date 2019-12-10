package com.cjyc.common.model.vo.salesman.mine;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class StockCarVo extends BaseStockCarVo {
    private static final long serialVersionUID = -3225636078426967419L;
    @ApiModelProperty(value = "订单车辆id")
    @JsonSerialize(using= ToStringSerializer.class)
    private Long orderCarId;
}