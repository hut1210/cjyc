package com.cjyc.common.model.vo.web.driver;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
@Data
public class DispatchDriverVo implements Serializable {
    private static final long serialVersionUID = 6420417671963463704L;

    @ApiModelProperty(value = "ID")
    private Long carrierId;

    @ApiModelProperty(value = "公司名称")
    private String name;
    @ApiModelProperty(value = "承运方式")
    private String mode;

    @ApiModelProperty("司机id(driverId)")
    @JsonSerialize(using= ToStringSerializer.class)
    private Long driverId;

    @ApiModelProperty("司机姓名")
    private String driverName;

    @ApiModelProperty("手机号")
    private String driverPhone;

    @ApiModelProperty("车牌号")
    private String vehiclePlateNo;

    @ApiModelProperty("身份证号")
    private String idCard;

    @ApiModelProperty("承运数")
    private Integer carryCarNum;

    @ApiModelProperty("非空车位")
    private Integer occupiedCarNum;

    @ApiModelProperty("运行状态：0空闲，1在途 2繁忙")
    private Integer runningState;
}