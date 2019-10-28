package com.cjyc.common.model.vo.web.vehicle;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
@Data
public class VehicleVo implements Serializable {

    @ApiModelProperty(value = "运输车辆主键id")
    private String id;

    @ApiModelProperty(value = "车牌号")
    private String plateNo;

    @ApiModelProperty("车位数")
    private String defaultCarryNum;

    @ApiModelProperty("司机姓名")
    private String realName;

    @ApiModelProperty("司机手机号")
    private String phone;

    @ApiModelProperty("车辆类型 0 ：代驾 1：干线司机  2：拖车司机 4：全支持")
    private String mode;

    @ApiModelProperty("所有权：0韵车自营，1个人所有，2第三方物流公司")
    private String ownershipType;

    @ApiModelProperty(value = "状态：0待审核，2已审核，4已驳回，7已停用")
    private String state;

    @ApiModelProperty("最后操作时间")
    private String createTime;

    @ApiModelProperty("最后操作人")
    private String createName;
}