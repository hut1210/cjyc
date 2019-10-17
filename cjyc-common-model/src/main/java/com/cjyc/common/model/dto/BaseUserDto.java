package com.cjyc.common.model.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 基础用户信息Dto
 * @author JPG
 */
@Data
public class BaseUserDto {
    @ApiModelProperty("用户名")
    private String userName;

    @ApiModelProperty("用户userId")
    private Long UserId;
}
