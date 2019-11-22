package com.cjyc.common.model.dto.driver.mine;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.Null;
import java.io.Serializable;
@Data
public class DeleteVehicleDto implements Serializable {
    private static final long serialVersionUID = 8223311768840207579L;
    @ApiModelProperty("司机id")
    @Null(message = "司机id不能为空")
    private Long loginId;

    @ApiModelProperty("车辆id")
    @Null(message = "车辆id不能为空")
    private Long vehicleId;
}