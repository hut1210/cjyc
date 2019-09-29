package com.cjyc.common.entity.atuo;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 操作日志
 * </p>
 *
 * @author JPG
 * @since 2019-09-29
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("r_operate_log")
public class OperateLog implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 用户ID
     */
    @TableField("user_id")
    private Long userId;

    /**
     * 用户名
     */
    @TableField("user_name")
    private String userName;

    /**
     * 用户操作
     */
    @TableField("operation")
    private String operation;

    /**
     * 操作方法
     */
    @TableField("method")
    private String method;

    /**
     * 操作参数
     */
    @TableField("params")
    private String params;

    /**
     * 操作时长
     */
    @TableField("duration")
    private Long duration;

    /**
     * IP地址
     */
    @TableField("ip")
    private String ip;

    /**
     * 创建时间
     */
    @TableField("create_time")
    private Long createTime;


}
