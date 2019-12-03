package com.cjyc.common.model.dto.driver;

import com.cjyc.common.model.dto.BasePageDto;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
@Data
public class BaseDriverDto extends BasePageDto {
    private static final long serialVersionUID = -640959385420785099L;
    @ApiModelProperty(value = "司机id",required = true)
    @NotNull(message = "司机id不能为空")
    private Long loginId;

    @ApiModelProperty(value = "角色id",required = true)
    @NotNull(message = "角色id不能为空")
    private Long roleId;
}