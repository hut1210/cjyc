package com.cjyc.common.model.vo.web.carrier;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
@Data
public class BaseDriverVo implements Serializable {

    @ApiModelProperty("司机id")
    private Long driverId;

    @ApiModelProperty("司机姓名")
    private String realName;

    @ApiModelProperty("司机姓名")
    private String phone;

    @ApiModelProperty("承运方式：0 ：代驾 1：干线司机  2：拖车司机 ")
    private Integer mode;

    @ApiModelProperty("营运状态：0营运中，1停运中")
    private Integer businessState;

    @ApiModelProperty("状态：0待审核，2已审核，4取消，7已驳回，9已停用（CommonStateEnum）")
    private Integer state;

    @ApiModelProperty("司机身份 0：普通司机 1：管理员")
    private Integer identity;

    @ApiModelProperty("运输车辆数")
    private Integer carNum;

    @ApiModelProperty("车牌号")
    private String plateNo;

    @ApiModelProperty("车位数")
    private Integer defaultCarryNum;

}