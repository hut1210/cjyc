package com.cjyc.common.model.dto.customer.freightBill;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;
@Data
public class LineDto implements Serializable {

    private static final long serialVersionUID = -8242676495285093070L;
    @ApiModelProperty(value = "起始城市编码",required = true)
    @NotBlank(message = "起始城市编码不能为空")
    private String fromCode;

    @ApiModelProperty(value = "目的城市编码",required = true)
    @NotBlank(message = "目的城市编码不能为空")
    private String toCode;
}