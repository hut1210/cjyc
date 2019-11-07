package com.cjyc.common.model.dto.web.vehicle;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
@Data
public class DriverVehicleConDto implements Serializable {
    private static final long serialVersionUID = 4811129462747451190L;

    @ApiModelProperty("车辆主键id")
    @NotNull(message = "车辆主键不能为空")
    private Long id;

    @ApiModelProperty("车牌号")
    @NotNull(message = "车牌号不能为空")
    private String plateNo;

    @ApiModelProperty("车位数")
    @NotNull(message = "车位数不能为空")
    private Integer defauleCarryNum;
}