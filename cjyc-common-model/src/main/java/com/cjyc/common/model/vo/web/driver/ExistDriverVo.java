package com.cjyc.common.model.vo.web.driver;

import com.cjyc.common.model.util.DataLongSerizlizer;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
@Data
public class ExistDriverVo implements Serializable {
    private static final long serialVersionUID = 7479114035952937475L;

    @ApiModelProperty("主键id")
    @JsonSerialize(using= ToStringSerializer.class)
    private Long id;

    @ApiModelProperty("司机id")
    @JsonSerialize(using= ToStringSerializer.class)
    private Long driverId;

    @ApiModelProperty("司机姓名")
    private String name;

    @ApiModelProperty("手机号")
    private String phone;

    @ApiModelProperty("身份证号")
    private String idCard;

    @ApiModelProperty("已存在身份证号")
    private String existIdCard;

    @ApiModelProperty("创建时间")
    @JsonSerialize(using = DataLongSerizlizer.class)
    private Long createTime;
}