package com.cjyc.common.model.vo.driver.task;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @Description 司机信息VO
 * @Author Liu Xing Xiang
 * @Date 2019/11/20 17:23
 **/
@Data
public class TaskDriverVo implements Serializable {
    private static final long serialVersionUID = -2393389933825537440L;
    @ApiModelProperty(value = "手机号")
    private String phone;

    @ApiModelProperty(value = "身份证号")
    private String idCard;

    @ApiModelProperty(value = "真实姓名")
    private String realName;

    @ApiModelProperty(value = "运行状态：0空闲，1在途 2繁忙")
    private Integer runningState;

    @ApiModelProperty(value = "车牌号")
    private String plateNo;

    @ApiModelProperty(value = "总车位数")
    private Integer carryCarNum;

    @ApiModelProperty(value = "正在使用车位数")
    private Integer occupiedCarNum;

    @ApiModelProperty(value = "司机ID")
    @JsonSerialize(using= ToStringSerializer.class)
    private Long driverId;

    @ApiModelProperty(value = "司机登录ID")
    @JsonSerialize(using= ToStringSerializer.class)
    private Long loginId;

    @ApiModelProperty(value = "司机角色：0个人司机，1下属司机，2管理员，3超级管理员")
    private Integer role;

    @ApiModelProperty(value = "营运状态：0营运中(空闲)，1停运中(繁忙)")
    private Integer businessState;

    public String getPhone() {
        return phone == null ? "" : phone;
    }
    public String getIdCard() {
        return idCard == null ? "" : idCard;
    }
    public String getRealName() {
        return realName == null ? "" : realName;
    }
    public Integer getRunningState() {
        return runningState == null ? -1 : runningState;
    }
    public String getPlateNo() {
        return plateNo == null ? "" : plateNo;
    }
    public Integer getCarryCarNum() {
        return carryCarNum == null ? 0 : carryCarNum;
    }
    public Long getDriverId() {
        return driverId == null ? 0 : driverId;
    }
    public Long getLoginId() {
        return loginId == null ? 0 : loginId;
    }
    public Integer getRole() {
        return role == null ? 0 : role;
    }
    public Integer getOccupiedCarNum() {
        return occupiedCarNum == null ? 0 : occupiedCarNum;
    }
    public Integer getBusinessState() {
        return businessState == null ? -1 : businessState;
    }
}
