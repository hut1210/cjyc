
package com.cjyc.common.model.entity.sys;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;

import java.io.Serializable;

/**
 * 角色与菜单对应关系
 *
 * @author cjwl56
 */
@Data
@TableName("sys_role_menu")
public class SysRoleMenuEntity implements Serializable {
	private static final long serialVersionUID = 1L;
	
	@TableId(type= IdType.ID_WORKER)
	@JsonSerialize(using= ToStringSerializer.class)
	private Long id;

	/**
	 * 角色ID
	 */
	@JsonSerialize(using= ToStringSerializer.class)
	private Long roleId;

	/**
	 * 菜单ID
	 */
	@JsonSerialize(using= ToStringSerializer.class)
	private Long menuId;

	/**
	 * 角色名称
	 */
	@TableField(exist = false)
	private String roleName;

	/**
	 * 系统名称
	 */
	@TableField(exist = false)
	private String appName;

	@TableField(exist = false)
	private Integer status;

	/**
	 * 系统版本号
	 */
	@TableField(exist = false)
	private String sysVersion;

	/**
	 * 系统标识
	 */
	@TableField(exist = false)
	private String appFlag;

}
