package com.cjyc.common.model.dto.web.vehicle;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
@Data
public class FreeVehicleDto implements Serializable {

    @ApiModelProperty("承运商id(carrierId)")
    private Long carrierId;

    @ApiModelProperty("车牌号")
    private String plateNo;
}