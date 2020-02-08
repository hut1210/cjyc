package com.cjyc.common.model.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
@Data
public class CommonDto extends BasePageDto implements Serializable {

    @ApiModelProperty(value = "登录用户id",required = true)
    @NotNull(message = "登录用户id不能为空")
    private Long loginId;

    @ApiModelProperty(value = "客户手机号")
    private String customerPhone;

}