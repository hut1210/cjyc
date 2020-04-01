package com.cjyc.common.model.vo.web.vehicle;

import com.cjyc.common.model.serizlizer.SecondLongSerizlizer;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
@Data
public class VehicleVo implements Serializable {

    private static final long serialVersionUID = -2252247630872394365L;

    @ApiModelProperty(value = "车辆主键id(vehicleId)")
    @JsonSerialize(using= ToStringSerializer.class)
    private Long vehicleId;

    @ApiModelProperty(value = "司机id(driverId)")
    @JsonSerialize(using= ToStringSerializer.class)
    private Long driverId;

    @ApiModelProperty(value = "车牌号")
    private String plateNo;

    @ApiModelProperty("车位数")
    private Integer defaultCarryNum;

    @ApiModelProperty("司机姓名")
    private String realName;

    @ApiModelProperty("司机手机号")
    private String phone;

    @ApiModelProperty("最后操作时间")
    @JsonSerialize(using = SecondLongSerizlizer.class)
    private Long checkTime;

    @ApiModelProperty("最后操作人")
    private String checkName;
}