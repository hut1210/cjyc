package com.cjyc.common.model.vo.web.role;

import com.cjyc.common.model.util.DataLongSerizlizer;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
@ApiModel
public class SelectUserByRoleVo implements Serializable {
    private static final long serialVersionUID = 1L;
    @ApiModelProperty("用户状态")
    private Integer status;
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
    @ApiModelProperty(value = "创建人")
    private String createUser;
    @ApiModelProperty("创建时间")
    @JsonSerialize(using = DataLongSerizlizer.class)
    private Long createTime;

    private String bizDesc;
}
