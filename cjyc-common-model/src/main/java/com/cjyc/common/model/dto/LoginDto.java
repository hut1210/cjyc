package com.cjyc.common.model.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import java.io.Serializable;
@Data
public class LoginDto implements Serializable {
    private static final long serialVersionUID = 3333146372464982794L;

    @ApiModelProperty("手机号")
    @NotBlank(message = "手机号不能为空")
    @Pattern(regexp = "1[3|4|5|7|8][0-9]\\d{8}",message = "电话号码格式不对")
    private String phone;

    @ApiModelProperty("验证码")
    @NotBlank(message = "验证码不能为空")
    @Pattern(regexp = "\\d{4}",message = "验证码为4位数字")
    private String code;
}