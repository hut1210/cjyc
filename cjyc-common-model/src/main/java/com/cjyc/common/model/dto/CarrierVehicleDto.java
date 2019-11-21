package com.cjyc.common.model.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
@Data
public class CarrierVehicleDto implements Serializable {

    private static final long serialVersionUID = 992240700345836877L;

    @ApiModelProperty("登陆系统用户id(loginId)")
    @NotNull(message = "登陆系统用户id(loginId)不能为空")
    private Long loginId;

    @ApiModelProperty("承运商id")
    private Long carrierId;

    @ApiModelProperty("司机id(driverId)")
    private Long driverId;

    @ApiModelProperty("承运商管理员角色(d_carrier_driver_con)id")
    private Long roleId;

    @ApiModelProperty("车辆id")
    private Long vehicleId;

    @ApiModelProperty("车牌号")
    private String plateNo;

    @ApiModelProperty("车位数")
    private Integer defaultCarryNum;

    @ApiModelProperty("司机名称")
    private String realName;

    @ApiModelProperty("司机手机号")
    private String phone;

    @ApiModelProperty("承运方式：2 : 代驾  3 : 干线   4：拖车")
    private Integer mode;
}