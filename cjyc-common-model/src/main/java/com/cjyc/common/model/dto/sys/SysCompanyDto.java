package com.cjyc.common.model.dto.sys;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 
 * 
 * @author cjwl56
 * @email cjwl56@gmail.com
 * @date 2019-03-25 13:09:36
 */
@Data
@TableName("sys_company")
public class SysCompanyDto implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 */
	@TableId
	@JsonSerialize(using= ToStringSerializer.class)
	private Long companyId;
	/**
	 * 机构名称
	 */
	private String companyname;
	/**
	 * 社会信用代码
	 */
	private String companycode;
	/**
	 * 成立日期
	 */
	private Date establishTime;
	/**
	 * 法人
	 */
	private String legalPerson;
	/**
	 * 联系电话
	 */
	private String telephone;
	/**
	 * 注册资本
	 */
	private String registeredCapital;
	/**
	 * 注册地址
	 */
	private String registeredAddress;
	/**
	 * 行业类别
	 */
	private String companytype;
	/**
	 * 上级机构ID，一级机构为0
	 */
	private Long parentId;
	
	/**
	 * 上级机构名称
	 */
	@TableField(exist=false)
	private String parentName;
	
	/**
	 * 法人身份证多个以,隔开
	 */
	private String idCard;
	/**
	 * 备注
	 */
	private String remake;
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
	 * ztree属性
	 */
	@TableField(exist=false)
	private Boolean open;
	@TableField(exist=false)
	private List<?> list;
	
}
