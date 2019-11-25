package com.cjyc.common.model.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
@Data
public class CommonDto extends BasePageDto implements Serializable {

    @ApiModelProperty(value = "登陆id(loginId)",required = true)
    @NotNull(message = "登陆人id(loginId)不能为空")
    private Long loginId;

}