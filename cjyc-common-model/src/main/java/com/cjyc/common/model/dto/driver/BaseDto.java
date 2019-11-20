package com.cjyc.common.model.dto.driver;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
@Data
public class BaseDto implements Serializable {
    private static final long serialVersionUID = -1680169213070049919L;

    @ApiModelProperty("司机id")
    @NotNull(message = "司机id不能为空")
    private Long loginId;
}