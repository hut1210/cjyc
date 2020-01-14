package com.cjyc.common.model.dto.web.order;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class ChangeCarryTypeDto {

    @NotNull(message = "调度车辆不能为空")
    private Integer orderCarId;
    @NotNull(message = "调度类型不能为空")
    @ApiModelProperty(value = "调度类型 0：全部 1：提车调度 2：干线调度 3：送车调度")
    private Integer dispatchType;
    @NotNull(message = "承运类型不能为空")
    @ApiModelProperty(value = "1 自送，2代驾上门，3拖车上门，4物流上门")
    private Integer carryType;
}
