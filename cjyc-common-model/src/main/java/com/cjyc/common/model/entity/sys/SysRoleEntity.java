
package com.cjyc.common.model.entity.sys;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 角色
 *
 * @author cjwl56
 */
@Data
@TableName("sys_role")
public class SysRoleEntity implements Serializable {
	private static final long serialVersionUID = 1L;
	
	/**
	 * 角色ID
	 */
	@TableId(type= IdType.ID_WORKER)
	@JsonSerialize(using= ToStringSerializer.class)
	private Long roleId;

	/**
	 * 角色名称
	 */
	private String roleName;

	/**
	 * 备注
	 */
	private String remark;

	/**
	 * 部门ID
	 */
	@JsonSerialize(using= ToStringSerializer.class)
	private Long deptId;

	/**
	 * 角色类型 1：系统管理员 2：机构管理员 3：普通角色
	 */
	private Integer roleType;

	/**
	 * 部门名称
	 */
	@TableField(exist=false)
	private String deptName;

	@TableField(exist=false)
	private List<String> menuIdList;
	
	@TableField(exist=false)
	private List<Long> deptIdList;

	@TableField(exist = false)
	private String userName;

	/**
	 * 账号
	 */
	@TableField(exist = false)
	private String name;
	
	/**
	 * 创建时间
	 */
	@TableField(value = "create_time", fill = FieldFill.INSERT)
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	private Date createTime;
	/**
	 * 创建人
	 */
	private String createName;
	/**
	 * 修改时间
	 */
	@TableField(value = "update_time", fill = FieldFill.INSERT_UPDATE)
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	private Date updateTime;
	
	/*
	 * 删除角色
	 */
	@TableField(exist=false)
	private String roleIds;
	
}
