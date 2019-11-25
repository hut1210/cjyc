package com.cjyc.common.model.dto.driver.mine;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
@Data
public class BankCardDto implements Serializable {
    private static final long serialVersionUID = 5533588799316430410L;
    @ApiModelProperty(value = "承运商id",required = true)
    @NotNull(message = "承运商id不能为空")
    private Long carrierId;

    @ApiModelProperty(value = "司机姓名",required = true)
    @NotBlank(message = "司机姓名不能为空")
    private String realName;

    @ApiModelProperty(value = "银行卡号",required = true)
    @NotBlank(message = "银行卡号不能为空")
    private String cardNo;

    @ApiModelProperty(value = "银行卡名称",required = true)
    @NotBlank(message = "银行卡名称不能为空")
    private String bankName;

    @ApiModelProperty(value = "手机号",required = true)
    @NotBlank(message = "手机号不能为空")
    private String phone;

    @ApiModelProperty(value = "身份证号",required = true)
    @NotBlank(message = "身份证号不能为空")
    private String idCard;
}