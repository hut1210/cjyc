package com.cjyc.common.model.dto.driver.mine;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
@Data
public class CarrierDriverNameDto implements Serializable {
    private static final long serialVersionUID = 3522955250907832783L;

    @ApiModelProperty(value = "承运商id",required = true)
    @NotNull(message = "该承运商id不能为空")
    private Long carrierId;

    @ApiModelProperty("司机姓名")
    private String realName;
}