package com.cjyc.common.model.vo.web.mimeCarrier;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
@Data
public class MyCarVo implements Serializable {
    private static final long serialVersionUID = -8085977393938838953L;

    @ApiModelProperty("车辆id(vehicleId)")
    @JsonSerialize(using= ToStringSerializer.class)
    private Long vehicleId;

    @ApiModelProperty("承运商id(carrierId)")
    @JsonSerialize(using= ToStringSerializer.class)
    private Long carrierId;

    @ApiModelProperty("司机id(driverId)")
    @JsonSerialize(using= ToStringSerializer.class)
    private Long driverId;

    @ApiModelProperty("车牌号")
    private String plateNo;

    @ApiModelProperty("车位数")
    private Integer defaultCarryNum;

    @ApiModelProperty("当前驾驶司机")
    private String realName;

    @ApiModelProperty("司机手机号")
    private String phone;

    @ApiModelProperty("承运方式：2 : 代驾  3 : 干线   4：拖车")
    private Integer mode;
}