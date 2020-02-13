package com.cjyc.common.model.dto.salesman.login;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * 请求角色对应的按钮列表
 */
@Data
@ApiModel
public class RoleBtnReqDto {
    @NotNull(message = "登录标识不能为空")
    @ApiModelProperty(value = "登录标识", required = true)
    private Long loginId;
}
