package com.cjyc.common.model.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
@Data
public class FreeVehicleDto implements Serializable {

    private static final long serialVersionUID = 5883906486927136906L;
    @ApiModelProperty("登陆用户id(loginId)")
    @NotNull(message = "登陆用户id不能为空")
    private Long loginId;

    @ApiModelProperty("用户角色id")
    @NotNull(message = "用户角色id不能为空")
    private Long roleId;

    @ApiModelProperty("车牌号")
    private String plateNo;
}