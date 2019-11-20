package com.cjyc.common.model.vo.web.role;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 系统资源树
 */
@Data
@ApiModel
public class MenuTreeVo implements Serializable {
    private static final long serialVersionUID = 1L;
    @ApiModelProperty(name = "menuId", value = "菜单id")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long menuId;
    @ApiModelProperty(name = "parentId", value = "父级菜单id")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long parentId;
    @ApiModelProperty(name = "name", value = "菜单名称")
    private String name;
    @ApiModelProperty(name = "url", value = "菜单url")
    private String url;
    @ApiModelProperty(name = "flag", value = "标识：如果资源为按钮，则为权限标识；如果资源为系统，则为系统标识")
    private String flag;
    @ApiModelProperty(name = "type", value = "资源类型 0：系统 1：菜单 2：按钮")
    private Integer type;
    @ApiModelProperty(name = "icon", value = "图标")
    private String icon;
    @ApiModelProperty(name = "sysVersion", value = "系统版本")
    private String sysVersion;
    @ApiModelProperty(name = "children", value = "子节点")
    private List<MenuTreeVo> children;
}
