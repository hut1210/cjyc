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

    @ApiModelProperty("公司名称")
    private String name;

    @ApiModelProperty("承运方式：2 : 代驾  3 : 干线   4：拖车")
    private Integer mode;

    @ApiModelProperty("车牌号")
    private String plateNo;

    @ApiModelProperty("承运数")
    private Integer carryCarNum;

    @ApiModelProperty("非空车位")
    private Integer occupiedCarNum;
}