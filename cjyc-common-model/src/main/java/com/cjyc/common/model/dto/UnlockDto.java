package com.cjyc.common.model.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import java.util.List;

@Data
public class UnlockDto {
    @ApiModelProperty(value = "订单编号或者车辆编号")
    @NotEmpty
    private List<String> nos;

}
