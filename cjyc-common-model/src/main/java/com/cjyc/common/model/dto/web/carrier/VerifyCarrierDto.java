package com.cjyc.common.model.dto.web.carrier;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
@Data
public class VerifyCarrierDto implements Serializable {

    private static final long serialVersionUID = -5041300734246269231L;
    @ApiModelProperty("承运商id(carrierId)")
    private Long carrierId;

    @ApiModelProperty("司机id(driverId)")
    private Long driverId;

    @ApiModelProperty("手机号")
    private String phone;

    @ApiModelProperty("身份证号")
    private String idCard;
}