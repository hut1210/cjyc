package com.cjyc.common.model.vo.web.mimeCarrier;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
@Data
public class MyFreeDriverVo implements Serializable {
    private static final long serialVersionUID = -1771517127364026372L;

    @ApiModelProperty("司机id(driverId)")
    private Long driverId;

    @ApiModelProperty("车辆编号")
    private String plateNo;

    @ApiModelProperty("司机手机号")
    private String phone;

    @ApiModelProperty("承运方式：2 : 代驾  3 : 干线   4：拖车")
    private Integer mode;

}