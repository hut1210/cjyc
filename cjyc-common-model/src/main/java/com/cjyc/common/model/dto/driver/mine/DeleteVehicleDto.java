package com.cjyc.common.model.dto.driver.mine;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.Null;
import java.io.Serializable;
@Data
public class DeleteVehicleDto implements Serializable {
    private static final long serialVersionUID = 8223311768840207579L;
    @ApiModelProperty(value = "司机id",required = true)
    @Null(message = "司机id不能为空")
    private Long driverId;

    @ApiModelProperty(value = "车辆id",required = true)
    @Null(message = "车辆id不能为空")
    private Long vehicleId;
}