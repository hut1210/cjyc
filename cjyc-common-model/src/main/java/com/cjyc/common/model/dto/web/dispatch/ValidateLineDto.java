package com.cjyc.common.model.dto.web.dispatch;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class ValidateLineDto {
    @ApiModelProperty("车辆编号")
    @NotBlank(message = "车辆编号不能为空")
    private String orderCarNo;
    @ApiModelProperty("起始城市编号")
    @NotBlank(message = "起始城市编号不能为空")
    private String fromCode;
    @ApiModelProperty("目的城市编号")
    @NotBlank(message = "起始城市编号不能为空")
    private String toCode;
}
