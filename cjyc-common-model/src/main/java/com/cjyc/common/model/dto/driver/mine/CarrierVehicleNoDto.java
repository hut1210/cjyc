package com.cjyc.common.model.dto.driver.mine;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
@Data
public class CarrierVehicleNoDto implements Serializable {
    private static final long serialVersionUID = 802398463503664129L;

    @ApiModelProperty(value = "承运商id",required = true)
    @NotNull(message = "该承运商id不能为空")
    private Long carrierId;

    @ApiModelProperty("车牌号")
    private String plateNo;
}