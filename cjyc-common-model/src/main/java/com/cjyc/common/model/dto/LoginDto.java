package com.cjyc.common.model.dto;

import com.cjyc.common.model.constant.RegexConstant;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import java.io.Serializable;
@Data
public class LoginDto implements Serializable {
    private static final long serialVersionUID = 3333146372464982794L;

    @ApiModelProperty(value = "手机号",required = true)
    @NotBlank(message = "手机号不能为空")
    @Pattern(regexp = RegexConstant.REGEX_MOBILE_EXACT_LATEST,message = "电话号码格式不对")
    private String phone;

    @ApiModelProperty(value = "验证码",required = true)
    @NotBlank(message = "验证码不能为空")
    @Pattern(regexp = RegexConstant.VERIFY_CODE,message = "验证码为4位数字")
    private String code;
}