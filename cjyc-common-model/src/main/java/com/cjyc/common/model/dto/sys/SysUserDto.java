package com.cjyc.common.model.dto.sys;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 系统用户
 *
 * @author cjwl56
 */
@Data
@TableName("sys_user")
public class SysUserDto implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 用户ID
	 */
	@TableId(type= IdType.ID_WORKER)
	@JsonSerialize(using= ToStringSerializer.class)
	private Long userId;
	/**
	 * 姓名
	 */
	private String name;
	/**
	 * 用户名
	 */
	private String username;

	/**
	 * 密码
	 */
//	@JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
	private String password;

	/**
	 * 盐
	 */
	@JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
	private String salt;

	/**
	 * 邮箱
	 */
	//@NotBlank(message = "邮箱不能为空", groups = { AddGroup.class, UpdateGroup.class })
	//@Email(message = "邮箱格式不正确", groups = { AddGroup.class, UpdateGroup.class })
	private String email;

	/**
	 * 手机号
	 */
	private String mobile;

	/**
	 * 状态 0：禁用 1：正常 2:停用
	 */
	private Integer status;

	/**
	 * 角色ID列表
	 */
	@TableField(exist = false)
	private List<String> roleIdList;

	/**
	 * 角色名称列表
	 */
	@TableField(exist = false)
	private List<SysRoleDto> roleIdName;
	/**
	 * 修改时间
	 */
	@TableField(value = "update_time", fill = FieldFill.INSERT_UPDATE)
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	private Date updateTime;
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
	 * 部门ID
	 */
	@JsonSerialize(using= ToStringSerializer.class)
	private Long deptId;

	/**
	 * 部门名称
	 */
	@TableField(exist = false)
	private String deptName;

	/**
	 * 删除用户ID列表
	 */
	@TableField(exist = false)
	private String  userIds;


	/**
	 * 最后一次登录时间
	 */
	@TableField(value = "last_login_time")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	private Date lastLoginTime;
	/**
	 * 登录个数
	 */
	private Long loginCount;
	/**
	 * 修改密码时间
	 */
	private Date updatePasswordTime;

}
