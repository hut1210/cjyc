package com.cjyc.common.model.dto.customer.login;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import java.io.Serializable;
@Data
public class VerifyCodeDto implements Serializable {
    private static final long serialVersionUID = 7330948394277510224L;

    @ApiModelProperty("手机号")
    @NotBlank(message = "手机号不能为空")
    @Pattern(regexp = "1[3|4|5|7|8][0-9]\\d{8}",message = "电话号码格式不对")
    private String phone;
}