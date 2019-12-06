package com.cjyc.common.model.vo.web.admin;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class AdminPageVo {
    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;
    @ApiModelProperty(value = "状态：0待审核，2已审核，4取消，7已驳回，9已停用（CommonStateEnum）")
    private Integer state;
    @ApiModelProperty("用户名称")
    private String name;
    @ApiModelProperty("账号")
    private String account;
    @ApiModelProperty("角色列表描述")
    private String roles;
    @ApiModelProperty("业务城市")
    private String bizCity;
    @ApiModelProperty("业务中心")
    private String bizCenter;
    @ApiModelProperty("用户标识")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long userId;
    @ApiModelProperty(value = "业务范围描述信息")
    private String bizDesc;
    @ApiModelProperty(value = "创建时间")
    private Long createTime;
    @ApiModelProperty(value = "创建时间")
    private String createUser;

}
