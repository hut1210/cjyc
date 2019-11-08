package com.cjyc.common.model.dto.web.vehicle;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
@Data
public class RemoveVehicleDto implements Serializable {
    private static final long serialVersionUID = -6810218590345922895L;

    @ApiModelProperty("车辆id(vehicleId)")
    private Long vehicleId;

    @ApiModelProperty("司机id(driverId)")
    private Long driverId;
}