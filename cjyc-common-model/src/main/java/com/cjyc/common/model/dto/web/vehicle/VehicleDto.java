package com.cjyc.common.model.dto.web.vehicle;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;
@Data
public class VehicleDto implements Serializable {

    public interface SaveVehicleDto {
    }

    public interface UpdateVehicleDto {
    }

    @ApiModelProperty("车辆信息id")
    @NotBlank(groups = {VehicleDto.UpdateVehicleDto.class},message = "车辆信息id不能为空")
    private String id;

    @ApiModelProperty("当前登录人ID")
    @NotBlank(groups = {VehicleDto.SaveVehicleDto.class},message = "当前登录人ID不能为空")
    @NotBlank(groups = {VehicleDto.UpdateVehicleDto.class},message = "当前登录人ID不能为空")
    private String userId;

    @ApiModelProperty("车牌号")
    @NotBlank(groups = {VehicleDto.SaveVehicleDto.class},message = "车牌号不能为空")
    @NotBlank(groups = {VehicleDto.UpdateVehicleDto.class},message = "车牌号不能为空")
    private String plateNo;

    @ApiModelProperty("车位数")
    @NotBlank(groups = {VehicleDto.SaveVehicleDto.class},message = "车位数不能为空")
    @NotBlank(groups = {VehicleDto.UpdateVehicleDto.class},message = "车位数不能为空")
    private String defaultCarryNum;

    @ApiModelProperty("司机ID")
    @NotBlank(groups = {VehicleDto.SaveVehicleDto.class},message = "司机ID不能为空")
    @NotBlank(groups = {VehicleDto.UpdateVehicleDto.class},message = "司机ID不能为空")
    private String driverId;

    @ApiModelProperty("所有权：0韵车自营，1个人所有，2第三方物流公司")
    private String ownershipType;
}