package com.cjyc.common.model.dto.salesman.account;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotEmpty;

@Data
@Accessors(chain = true)
@ApiModel
@Validated
public class ForgetPwdDto {
    @NotEmpty(message = "手机号不能为空")
    @ApiModelProperty(value = "手机号", required = true)
    private String phone;
    @NotEmpty(message = "验证码不能为空")
    @ApiModelProperty(value = "验证码", required = true)
    private String captcha;
    @NotEmpty(message = "新密码不能为空")
    @ApiModelProperty(value = "新密码", required = true)
    private String newPwd;
}
