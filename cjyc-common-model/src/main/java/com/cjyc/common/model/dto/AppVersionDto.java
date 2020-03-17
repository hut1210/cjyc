package com.cjyc.common.model.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
@Data
public class AppVersionDto implements Serializable {
    private static final long serialVersionUID = -3715721835243378149L;

    @ApiModelProperty(value = "系统类型 0：Android  1：IOS")
    @NotNull(message = "系统类型不能为空")
    private Integer systemType;

    @ApiModelProperty(value = "app类型 0：用户端  1 : 司机端  2：业务员端")
    @NotNull(message = "app类型不能为空")
    private Integer appType;

    @ApiModelProperty(value = "系统当前版本号")
    @NotBlank(message = "当前系统版本号不能为空")
    private String version;

}