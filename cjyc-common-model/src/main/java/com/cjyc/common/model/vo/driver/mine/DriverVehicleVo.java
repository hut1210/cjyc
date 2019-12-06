package com.cjyc.common.model.vo.driver.mine;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
@Data
public class DriverVehicleVo implements Serializable {
    private static final long serialVersionUID = 6358499631834729670L;
    @ApiModelProperty("司机id")
    @JsonSerialize(using= ToStringSerializer.class)
    private Long driverId;

    @ApiModelProperty("车辆id")
    @JsonSerialize(using= ToStringSerializer.class)
    private Long vehicleId;

    @ApiModelProperty("司机姓名")
    private String realName;

    @ApiModelProperty("手机号")
    private String phone;

    @ApiModelProperty("车牌号")
    private String plateNo;

    @ApiModelProperty("承运数")
    private Integer carryCarNum;

    @ApiModelProperty("非空车位")
    private Integer occupiedCarNum;
    public Long getVehicleId()  { return vehicleId == null ? 0 : vehicleId; }
    public String getPlateNo()  { return plateNo == null ? "" : plateNo; }
    public Integer getCarryCarNum()  { return carryCarNum == null ? 0 : carryCarNum; }
    public Integer getOccupiedCarNum()  { return occupiedCarNum == null ? 0 : occupiedCarNum; }
}