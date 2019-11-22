package com.cjyc.common.model.vo.driver.login;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
@Data
public class DriverLoginVo implements Serializable {
    private static final long serialVersionUID = 1212783695647256969L;

    @ApiModelProperty("司机id")
    @JsonSerialize(using= ToStringSerializer.class)
    private Long id;

    @ApiModelProperty("角色id")
    @JsonSerialize(using= ToStringSerializer.class)
    private Long roleId;

    @ApiModelProperty("司机userId")
    @JsonSerialize(using= ToStringSerializer.class)
    private Long userId;

    @ApiModelProperty("承运商类型：1个人承运商，2企业承运商")
    private Integer type;

    @ApiModelProperty("司机姓名")
    private String realName;

    @ApiModelProperty("手机号")
    private String phone;

    @ApiModelProperty("司机身份 0：普通司机 1：管理员")
    private Integer identity;

    @ApiModelProperty("营运状态：0营运中(空闲)，1停运中(繁忙)")
    private Integer businessState;

    @ApiModelProperty("公司名称")
    private String companyName;

    @ApiModelProperty("车牌号")
    private String plateNo;

    @ApiModelProperty("头像")
    private String photoImg;

    @ApiModelProperty("token值")
    private String accessToken;
}