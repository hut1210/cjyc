package com.cjyc.common.model.dto.driver.mine;

import com.cjyc.common.model.dto.salesman.sms.CaptchaSendDto;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
@Data
public class RemoveBankCardDto extends CaptchaSendDto {
    private static final long serialVersionUID = 2596164153020063347L;
    @ApiModelProperty(value = "司机id",required = true)
    @NotNull(message = "司机id不能为空")
    private Long loginId;

    @ApiModelProperty(value = "角色id",required = true)
    @NotNull(message = "角色id不能为空")
    private Long roleId;

    @ApiModelProperty(value = "银行卡id",required = true)
    @NotNull(message = "银行卡id不能为空")
    private Long cardId;

    @ApiModelProperty(value = "验证码",required = true)
    @NotBlank(message = "验证码不能为空")
    private String code;

}