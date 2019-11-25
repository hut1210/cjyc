package com.cjyc.common.model.dto;

import com.cjyc.common.model.constant.RegexConstant;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import java.io.Serializable;
@Data
public class VerifyCodeDto implements Serializable {
    private static final long serialVersionUID = 7330948394277510224L;

    @ApiModelProperty(value = "手机号",required = true)
    @NotBlank(message = "手机号不能为空")
    @Pattern(regexp = RegexConstant.REGEX_MOBILE_EXACT_LATEST,message = "电话号码格式不对")
    private String phone;
}