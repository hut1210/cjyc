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
 * 部门管理
 *
 * @author cjwl56
 */
@Data
@TableName("sys_dept")
public class SysDeptEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 部门ID
	 */
	@TableId(type = IdType.ID_WORKER)
	@JsonSerialize(using= ToStringSerializer.class)
	private Long deptId;
	/**
	 * 上级部门ID，一级部门为0
	 */
	@JsonSerialize(using= ToStringSerializer.class)
	private Long parentId;
	/**
	 * 部门名称
	 */
	private String name;
	/**
	 * 上级部门名称
	 */
	@TableField(exist = false)
	private String parentName;
	private Integer orderNum;
	@TableLogic
	private Integer delFlag;
	/**
	 * ztree属性
	 */
	@TableField(exist = false)
	private Boolean open;
	@TableField(exist = false)
	private List<?> list;

	@TableField(exist = false)
	 private List<SysDeptEntity> children;
	  
	    // 非数据库字段
	@TableField(exist = false)
	 private Integer level;

	// 非数据库字段
	@TableField(exist = false)
	private String deptIds;

	@TableField(exist = false)
	private Long userId;

	/*
	 * 联系人
	 */
	private String deptPerson;

	/**
	 * 
	 */
	private String telephone;
	/**
	 * 公司地址
	 */
	private String companyAddress;
	/**
	 * 备注
	 */
	private String remake;
	/**
	 * 行业类别
	 */
	private String companyType;
	/**
	 * 代码
	 */
	private String companyCode;
	/**
	 * 
	 */
	private String registeredCapital;
	/**
	 * 
	 */
	private String registeredAddress;
	/**
	 * 成立日期
	 */
	private Date establishTime;
	/**
	 * 法人
	 */
	private String legalPerson;
	/**
	 * 法人身份证多个以,隔开
	 */
	private String idCard;

	/**
	 * 机构类型
	 */
	private String deptType;

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

	/**
	 * 存储父级机构
	 */
	@TableField(exist = false)
	private SysDeptEntity deptParent;

	@TableField(exist = false)
	List<SysDeptResourceEntity> sysDeptResourceEntityList;
}
