package com.cjyc.common.model.dto.web.mimeCarrier;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
@Data
public class ModifyMyCarDto implements Serializable {
    private static final long serialVersionUID = -5244422999448907742L;

    @ApiModelProperty("承运商id(carrierId)")
    @NotNull(message = "承运商id(carrierId)不能为空")
    private Long carrierId;

    @ApiModelProperty("车辆id(vehicleId)")
    @NotNull(message = "车辆id(vehicleId)不能为空")
    private Long vehicleId;

    @ApiModelProperty("司机id(driverId)")
    private Long driverId;

    @ApiModelProperty("车牌号")
    @NotBlank(message = "车牌号不能为空")
    private String plateNo;

    @ApiModelProperty("车位数")
    @NotNull(message = "车位数不能为空")
    private Integer defaultCarryNum;

}