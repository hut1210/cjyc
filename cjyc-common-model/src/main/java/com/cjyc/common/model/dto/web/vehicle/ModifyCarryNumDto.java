package com.cjyc.common.model.dto.web.vehicle;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
@Data
public class ModifyCarryNumDto implements Serializable {
    private static final long serialVersionUID = 4811129462747451190L;

    @ApiModelProperty("车辆id(vehicleId)")
    @NotNull(message = "车辆id(vehicleId)不能为空")
    private Long vehicleId;

    @ApiModelProperty("车位数")
    @NotNull(message = "车位数不能为空")
    private Integer defauleCarryNum;
}