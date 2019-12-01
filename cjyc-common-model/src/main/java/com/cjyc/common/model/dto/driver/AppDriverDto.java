package com.cjyc.common.model.dto.driver;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
@Data
public class AppDriverDto implements Serializable {
    private static final long serialVersionUID = 4866938616628967004L;
    @ApiModelProperty(value = "司机id",required = true)
    @NotNull(message = "司机id不能为空")
    private Long loginId;

    @ApiModelProperty(value = "角色id",required = true)
    @NotNull(message = "角色id不能为空")
    private Integer roleId;
}