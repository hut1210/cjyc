
package com.cjyc.common.model.dto.sys;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;

import java.io.Serializable;
import java.util.Collections;
import java.util.Date;
import java.util.List;

/**
 * 菜单管理
 *
 * @author cjwl56
 */
@Data
@TableName("sys_menu")
public class SysMenuDto implements Serializable {
	private static final long serialVersionUID = 1L;
	
	/**
	 * 菜单ID
	 */
	@TableId(type= IdType.ID_WORKER)
	@JsonSerialize(using= ToStringSerializer.class)
	private Long menuId;

	/**
	 * 父菜单ID，一级菜单为0
	 */
	@JsonSerialize(using= ToStringSerializer.class)
	private Long parentId;
	
	/**
	 * 父菜单名称
	 */
	@TableField(exist=false)
	private String parentName;

    // 非数据库字段
    @TableField(exist=false)
    private Integer level;
    // 非数据库字段
    @TableField(exist=false)
    private List<SysMenuDto> children = Collections.emptyList();

    
	/**
	 * 菜单名称
	 */
	private String name;

	/**
	 * 菜单URL
	 */
	private String url;

	/**
	 * 授权(多个用逗号分隔，如：user:list,user:create)
	 */
	private String perms;

	/**
	 * 类型     0：目录   1：菜单   2：按钮
	 */
	private Integer type;

	/**
	 * 菜单图标
	 */
	private String icon;

	/**
	 * 排序
	 */
	private Integer orderNum;

	/**
	 * 目标连结， 0：当前窗口 1：弹出窗口
	 */
	private Integer linkTarget;

	/**
	 * 系统版本号
	 */
	private String sysVersion;
	
	/**
	 * ztree属性
	 */
	@TableField(exist=false)
	private Boolean open = Boolean.FALSE;

	@TableField(exist=false)
	private List<?> list = Collections.emptyList();

	private String createBy;

	/**
	 * 删除用菜单ID列表
	 */
	@TableField(exist = false)
	private String  menuIds = "";
	/**
	 * 创建时间
	 */
	@TableField(value = "create_time", fill = FieldFill.INSERT)
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	private Date createTime;

    private String lastUpdateBy;

    /**
	 * 修改时间
	 */
	@TableField(value = "update_time", fill = FieldFill.INSERT_UPDATE)
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	private Date updateTime;
    
    private Byte delFlag;

    private Long status;

	/**
	 * 角色ID
	 */
	@TableField(exist = false)
	private Long roleId;

	/**
	 * 菜单列表
	 */
	@TableField(exist = false)
	private List<SysMenuDto> menuList;
    
}
