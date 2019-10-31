package com.cjyc.common.model.vo.web.role;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 角色关联用户信息
 */
@Data
@ApiModel
public class AdminListVo implements Serializable {
    private static final long serialVersionUID = 1L;
    @ApiModelProperty(value = "用户状态")
    private Integer status;
    @ApiModelProperty(value = "用户名称")
    private String name;
    @ApiModelProperty(value = "账号")
    private String account;
    @ApiModelProperty(value = "角色列表描述")
    private String roles;
    @ApiModelProperty(value = "业务城市")
    private String bizCity;
    @ApiModelProperty(value = "业务中心")
    private String bizCenter;
}
