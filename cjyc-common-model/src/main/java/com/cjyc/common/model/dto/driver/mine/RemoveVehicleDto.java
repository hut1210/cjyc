package com.cjyc.common.model.dto.driver.mine;

import com.cjyc.common.model.dto.driver.AppDriverDto;
import com.cjyc.common.model.dto.driver.BaseDriverDto;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import java.io.Serializable;
@Data
public class RemoveVehicleDto extends AppDriverDto {
    private static final long serialVersionUID = 8223311768840207579L;
    @ApiModelProperty(value = "司机id",required = true)
    @NotNull(message = "司机id不能为空")
    private Long driverId;

    @ApiModelProperty(value = "车辆id",required = true)
    @NotNull(message = "车辆id不能为空")
    private Long vehicleId;
}