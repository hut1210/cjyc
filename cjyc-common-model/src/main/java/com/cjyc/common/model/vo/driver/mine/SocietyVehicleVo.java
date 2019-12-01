package com.cjyc.common.model.vo.driver.mine;

import com.cjyc.common.model.vo.FreeVehicleVo;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class SocietyVehicleVo implements Serializable {
    private static final long serialVersionUID = 7485978793138259973L;
    @ApiModelProperty("社会车辆")
    private List<FreeVehicleVo> vehicleVo;
}