package com.cjyc.common.model.dto.customer.freightBill;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
@Data
public class TransportPriceDto implements Serializable {

    @ApiModelProperty("用户userId")
    @NotNull(message = "登陆用户userId不能为空")
    private Long userId;

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