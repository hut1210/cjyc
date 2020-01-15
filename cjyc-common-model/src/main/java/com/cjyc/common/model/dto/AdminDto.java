package com.cjyc.common.model.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
@Data
public class AdminDto implements Serializable {
    private static final long serialVersionUID = 4260332178119799044L;

    @ApiModelProperty(value = "角色id",required = true)
    private Long roleId;

    @ApiModelProperty(value = "登录用户id",required = true)
    private Long loginId;
}