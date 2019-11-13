package com.cjyc.common.model.dto.web;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
@Data
public class VerifyCarrierDto implements Serializable {
    private static final long serialVersionUID = -2649304987458496460L;

    @ApiModelProperty("手机号")
    private String phone;

    @ApiModelProperty("身份证号")
    private String idCard;
}