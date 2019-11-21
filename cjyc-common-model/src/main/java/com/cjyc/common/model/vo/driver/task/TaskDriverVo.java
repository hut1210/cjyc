package com.cjyc.common.model.vo.driver.task;

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

    @ApiModelProperty(value = "营运状态：0营运中，1停运中")
    private Integer businessState;

    @ApiModelProperty(value = "车牌号")
    private String plateNo;

    @ApiModelProperty(value = "车位数")
    private Integer defaultCarryNum;

    @ApiModelProperty(value = "正在使用车位数")
    private Integer useCarryNum;

    public Integer getDefaultCarryNum() {
        return defaultCarryNum == null ? 0 : defaultCarryNum;
    }

    public Integer getUseCarryNum() {
        return useCarryNum == null ? 0 : useCarryNum;
    }
}
