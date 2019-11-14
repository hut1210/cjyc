package com.cjyc.common.model.dto.customer.freightBill;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
@Data
public class TransportDto implements Serializable {

    private static final long serialVersionUID = 291266326412877024L;
    @ApiModelProperty("用户id(lgoinId)")
    @NotNull(message = "登陆用户id(lgoinId)不能为空")
    private Long lgoinId;

    @ApiModelProperty("起始地code")
    @NotBlank(message = "起始地code不能为空")
    private String fromCode;

    @ApiModelProperty("起始城市")
    @NotBlank(message = "起始城市不能为空")
    private String fromCity;

    @ApiModelProperty("目的地code")
    @NotBlank(message = "目的地code不能为空")
    private String toCode;

    @ApiModelProperty("目的城市")
    @NotBlank(message = "目的城市不能为空")
    private String toCity;
}