package com.cjyc.common.model.dto.sys;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 系统用户Token
 *
 * @author cjwl56
 */
@Data
@TableName("sys_user_token")
public class SysUserTokenDto implements Serializable {
	private static final long serialVersionUID = 1L;

	// 用户ID
	@TableId(type = IdType.INPUT)
	@JsonSerialize(using= ToStringSerializer.class)
	private Long userId;
	// token
	private String token;
	// 过期时间
	private Date expireTime;
	// 更新时间
	private Date updateTime;


}
