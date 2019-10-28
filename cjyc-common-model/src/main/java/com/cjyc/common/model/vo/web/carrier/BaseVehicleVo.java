package com.cjyc.common.model.vo.web.carrier;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
@Data
public class BaseVehicleVo implements Serializable {

    @ApiModelProperty("车辆id")
    private Long id;

    @ApiModelProperty("车牌号")
    private String plateNo;

    @ApiModelProperty("车位数")
    private String defaultCarryNum;

    @ApiModelProperty("司机姓名")
    private String realName;

    @ApiModelProperty("司机手机号")
    private String phone;

    @ApiModelProperty("承运方式：0 ：代驾 1：干线司机  2：拖车司机 4全支持")
    private String mode;
}