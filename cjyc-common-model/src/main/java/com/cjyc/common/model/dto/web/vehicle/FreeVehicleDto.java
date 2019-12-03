package com.cjyc.common.model.dto.web.vehicle;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
@Data
public class FreeVehicleDto implements Serializable {
    private static final long serialVersionUID = 1711088889830151998L;

    @ApiModelProperty(value = "承运商id",required = true)
    @NotNull(message = "承运商id不能为空")
    private Long carrierId;

    @ApiModelProperty(value = "车牌号",required = true)
    private String plateNo;
}