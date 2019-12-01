package com.cjyc.common.model.dto.driver.mine;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
@Data
public class AppCarrierVehicleDto implements Serializable {
    private static final long serialVersionUID = -345793247315486853L;

    @ApiModelProperty(value = "承运商id",required = true)
    @NotNull(message = "承运商id不能为空")
    private Long carrierId;

    @ApiModelProperty("车牌id")
    private Long vehicleId;

    @ApiModelProperty(value = "车牌号",required = true)
    @NotBlank(message = "车牌号不能为空")
    private String plateNo;

    @ApiModelProperty(value = "车位数",required = true)
    @NotNull(message = "车位数不能为空")
    private Integer defaultCarryNum;

    @ApiModelProperty(value = "司机id",required = true)
    @NotNull(message = "司机id不能为空")
    private Long driverId;

    @ApiModelProperty(value = "司机姓名",required = true)
    @NotBlank(message = "司机姓名不能为空")
    private String realName;

    @ApiModelProperty(value = "司机i姓名",required = true)
    @NotBlank(message = "司机姓名不能为空")
    private String phone;
}