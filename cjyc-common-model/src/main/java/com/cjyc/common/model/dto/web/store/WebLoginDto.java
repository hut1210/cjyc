package com.cjyc.common.model.dto.web.store;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class WebLoginDto {
    @ApiModelProperty("登录人ID")
    private Long loginId;
    @ApiModelProperty("角色ID")
    private Long roleId;
}
