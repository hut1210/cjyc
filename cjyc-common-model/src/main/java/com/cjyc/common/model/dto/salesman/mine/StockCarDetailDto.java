package com.cjyc.common.model.dto.salesman.mine;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
@Data
public class StockCarDetailDto implements Serializable {
    private static final long serialVersionUID = -1560969906609895793L;
    @ApiModelProperty(value = "车辆id",required = true)
    @NotNull(message = "车辆id不能为空")
    private Long orderCarId;
}