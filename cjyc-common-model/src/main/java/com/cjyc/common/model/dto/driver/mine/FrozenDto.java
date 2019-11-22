package com.cjyc.common.model.dto.driver.mine;

import com.cjyc.common.model.dto.driver.BaseDto;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
@Data
public class FrozenDto extends BaseDto implements Serializable {
    private static final long serialVersionUID = 6407613385776403501L;

    @ApiModelProperty("承运商id")
    @NotNull(message = "承运商id不能为空")
    private Long carrierId;

    @ApiModelProperty("司机id")
    @NotNull(message = "司机id不能为空")
    private Long driverId;
}