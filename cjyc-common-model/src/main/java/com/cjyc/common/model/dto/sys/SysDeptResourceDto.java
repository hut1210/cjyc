package com.cjyc.common.model.dto.sys;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;

import java.io.Serializable;

/**
 * 
 * 
 * @author cjwl56
 * @email cjwl56@gmail.com
 * @date 2019-04-16 09:54:04
 */
@Data
@TableName("sys_dept_resource")
public class SysDeptResourceDto implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 */
	@TableId(type = IdType.ID_WORKER)
	@JsonSerialize(using= ToStringSerializer.class)
	private Long id;
	/**
	 * 部门ID
	 */
	@JsonSerialize(using= ToStringSerializer.class)
	private Long deptId;
	/**
	 * 资源ID
	 */
	private Long resourceid;
	/**
	 * 资源路径
	 */
	private String resourcepath;
	/**
	 * 资源地址
	 */
	private String resourceurl;

	/**
	 * 文件内容
	 */
	@TableField(exist = false)
	private String fileContent;

}
