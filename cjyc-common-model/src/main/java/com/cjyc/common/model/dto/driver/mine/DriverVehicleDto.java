package com.cjyc.common.model.dto.driver.mine;

import com.cjyc.common.model.dto.driver.BaseDriverDto;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class DriverVehicleDto extends BaseDriverDto {
    private static final long serialVersionUID = 57636748974273863L;

    @ApiModelProperty("承运商类型：1个人承运商，2企业承运商")
    private Integer type;
}