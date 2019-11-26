package com.cjyc.common.model.dto.driver.mine;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
@Data
public class RemoveBankCardDto implements Serializable {
    private static final long serialVersionUID = 2596164153020063347L;
    @ApiModelProperty(value = "银行卡id",required = true)
    @NotNull(message = "银行卡id")
    private Long cardId;

    @ApiModelProperty(value = "手机号",required = true)
    @NotBlank(message = "手机号不能为空")
    private String phone;

    @ApiModelProperty(value = "验证码",required = true)
    @NotBlank(message = "验证码不能为空")
    private String code;
}