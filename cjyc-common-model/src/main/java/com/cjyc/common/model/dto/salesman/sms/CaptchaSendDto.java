package com.cjyc.common.model.dto.salesman.sms;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

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
public class CaptchaSendDto {

    @NotNull
    @ApiModelProperty(value = "手机号", required = true)
    private String phone;

    @NotNull
    @ApiModelProperty(value = "验证码类型：1登录，2忘记登录密码，3忘记安全密码，4修改银行卡，5收车码", required = true)
    private Integer type;

}
