package com.cjyc.common.model.dto.web.order;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class ChangeCarryTypeDto {

    @NotNull(message = "调度车辆不能为空")
    private Long orderCarId;
    @NotNull(message = "提车类型不能为空")
    @ApiModelProperty(value = "1 自送，2代驾上门，3拖车上门，4物流上门")
    private Integer pickType;
    @NotNull(message = "送车类型不能为空")
    @ApiModelProperty(value = "1 自送，2代驾上门，3拖车上门，4物流上门")
    private Integer backType;
}
