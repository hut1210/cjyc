package com.cjyc.common.model.dto.customer.freightBill;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
@Data
public class TransportPriceDto implements Serializable {

    @ApiModelProperty("用户userId")
    private Long userId;

    @ApiModelProperty("起始地code")
    private String fromCode;

    @ApiModelProperty("目的地code")
    private String toCode;
}