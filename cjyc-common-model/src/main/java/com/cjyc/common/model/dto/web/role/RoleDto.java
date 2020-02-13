package com.cjyc.common.model.dto.web.role;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
@Data
public class RoleDto implements Serializable {
    private static final long serialVersionUID = 7558876788533668794L;

    @ApiModelProperty(value = "角色名称")
    private String roleName;
}