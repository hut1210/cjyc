package com.cjyc.common.model.vo.web.vehicle;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
@Data
public class VehicleVo implements Serializable {

    private static final long serialVersionUID = -2252247630872394365L;

    @ApiModelProperty(value = "运输车辆主键id")
    @JsonSerialize(using= ToStringSerializer.class)
    private Long id;

    @ApiModelProperty(value = "车牌号")
    private String plateNo;

    @ApiModelProperty("车位数")
    private Integer defaultCarryNum;

    @ApiModelProperty("司机姓名")
    private String realName;

    @ApiModelProperty("司机手机号")
    private String phone;

    @ApiModelProperty("最后操作时间")
    private String operateTime;

    @ApiModelProperty("最后操作人")
    private String operateName;
}