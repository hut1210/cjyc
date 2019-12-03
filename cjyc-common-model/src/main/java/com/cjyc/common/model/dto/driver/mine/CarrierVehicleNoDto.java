package com.cjyc.common.model.dto.driver.mine;

import com.cjyc.common.model.dto.driver.AppDriverDto;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class CarrierVehicleNoDto extends AppDriverDto {
    private static final long serialVersionUID = 802398463503664129L;

    @ApiModelProperty("车牌号")
    private String plateNo;
}