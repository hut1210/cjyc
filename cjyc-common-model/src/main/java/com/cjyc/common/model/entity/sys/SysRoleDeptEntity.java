
package com.cjyc.common.model.entity.sys;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;

import java.io.Serializable;

/**
 * 角色与部门对应关系
 *
 * @author cjwl56
 */
@Data
@TableName("sys_role_dept")
public class SysRoleDeptEntity implements Serializable {
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
	 * 部门ID
	 */
	@JsonSerialize(using= ToStringSerializer.class)
	private Long deptId;


	
}
