package com.cjyc.common.model.dto.driver.mine;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
@Data
public class DriverStateDto implements Serializable {
    private static final long serialVersionUID = -7570563259684223330L;
    @ApiModelProperty(value = "司机id",required = true)
    @NotNull(message = "司机id不能为空")
    private Long loginId;

    @ApiModelProperty("营运状态：0营运中(空闲)，1停运中(繁忙)")
    private Integer businessState;
}