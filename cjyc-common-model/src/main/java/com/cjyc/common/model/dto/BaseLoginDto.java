package com.cjyc.common.model.dto;

import com.cjyc.common.model.enums.UserTypeEnum;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class BaseLoginDto {
    @NotNull
    @ApiModelProperty("登录人ID")
    private Long loginId;
    @NotNull
    @ApiModelProperty(value = "1WEB管理后台, 2业务员APP, 4司机APP, 6用户端APP, 7用户端小程序", required = true)
    private int clientId;

    @ApiModelProperty(value = "登录人ID", hidden = true)
    private String loginName;
    @ApiModelProperty(value = "登录人手机号", hidden = true)
    private String loginPhone;
    @ApiModelProperty(value = "登录人手机号", hidden = true)
    private UserTypeEnum loginType;
}
