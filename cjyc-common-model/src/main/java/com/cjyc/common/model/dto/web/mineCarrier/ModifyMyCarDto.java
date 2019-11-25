package com.cjyc.common.model.dto.web.mineCarrier;

import com.cjyc.common.model.constant.RegexConstant;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.io.Serializable;
@Data
public class ModifyMyCarDto implements Serializable {
    private static final long serialVersionUID = -5244422999448907742L;

    @ApiModelProperty(value = "承运商id(carrierId)",required = true)
    @NotNull(message = "承运商id(carrierId)不能为空")
    private Long carrierId;

    @ApiModelProperty(value = "车辆id(vehicleId)",required = true)
    @NotNull(message = "车辆id(vehicleId)不能为空")
    private Long vehicleId;

    @ApiModelProperty("司机id(driverId)")
    private Long driverId;

    @ApiModelProperty(value = "车牌号",required = true)
    @NotBlank(message = "车牌号不能为空")
    @Pattern(regexp = RegexConstant.PLATE_NO,message = "车牌号格式不对")
    private String plateNo;

    @ApiModelProperty(value = "车位数",required = true)
    @NotNull(message = "车位数不能为空")
    private Integer defaultCarryNum;

}