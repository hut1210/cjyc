package com.cjyc.common.model.dto.salesman.sms;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * <p>
 * 入参对象
 * </p>
 * @author JPG
 * @since 2019-10-12
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel
public class CaptchaValidatedDto {

    @NotBlank(message = "手机号不能为空")
    @ApiModelProperty(value = "手机号", required = true)
    private String phone;

    @NotBlank(message = "验证码不能为空")
    @ApiModelProperty(value = "验证码", required = true)
    private String captcha;

    @NotBlank(message = "验证码类型不能为空")
    @ApiModelProperty(value = "验证码类型：1登录，2忘记登录密码，3忘记安全密码，4修改银行卡,5收车码", required = true)
    private Integer type;

}
