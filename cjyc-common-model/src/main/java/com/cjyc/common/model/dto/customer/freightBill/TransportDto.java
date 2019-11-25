package com.cjyc.common.model.dto.customer.freightBill;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
@Data
public class TransportDto implements Serializable {

    private static final long serialVersionUID = 291266326412877024L;
    @ApiModelProperty(value = "用户id(lgoinId)",required = true)
    @NotNull(message = "登陆用户id(lgoinId)不能为空")
    private Long lgoinId;

    @ApiModelProperty(value = "起始地code",required = true)
    @NotBlank(message = "起始地code不能为空")
    private String fromCode;

    @ApiModelProperty(value = "起始城市",required = true)
    @NotBlank(message = "起始城市不能为空")
    private String fromCity;

    @ApiModelProperty(value = "目的地code",required = true)
    @NotBlank(message = "目的地code不能为空")
    private String toCode;

    @ApiModelProperty("目的城市")
    @NotBlank(message = "目的城市不能为空")
    private String toCity;
}