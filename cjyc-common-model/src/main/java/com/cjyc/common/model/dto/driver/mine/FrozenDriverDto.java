package com.cjyc.common.model.dto.driver.mine;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
@Data
public class FrozenDriverDto implements Serializable {
    private static final long serialVersionUID = 6407613385776403501L;
    @ApiModelProperty(value = "司机id",required = true)
    @NotNull(message = "司机id不能为空")
    private Long loginId;

    @ApiModelProperty(value = "承运商id",required = true)
    @NotNull(message = "承运商id不能为空")
    private Long carrierId;

    @ApiModelProperty(value = "司机id",required = true)
    @NotNull(message = "司机id不能为空")
    private Long driverId;
}