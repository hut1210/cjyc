package com.cjyc.common.model.vo.driver.mine;

import com.cjyc.common.model.vo.FreeVehicleVo;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class CarrierVehicleVo implements Serializable {
    private static final long serialVersionUID = -3984368538478279058L;

    @ApiModelProperty("承运商下空闲车辆")
    private List<FreeVehicleVo> vehicleVo;
}