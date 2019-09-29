package com.cjyc.common.model.entity;

import java.math.BigDecimal;
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
 * 班线管理
 * </p>
 *
 * @author JPG
 * @since 2019-09-29
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("s_line")
public class Line implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 线路ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 线路编号
     */
    @TableField("code")
    private String code;

    /**
     * 线路名称
     */
    @TableField("name")
    private String name;

    /**
     * 出发地行政区编码
     */
    @TableField("from_code")
    private String fromCode;

    /**
     * 目的地行政区编码
     */
    @TableField("to_code")
    private String toCode;

    /**
     * 级别：2市级，3区级
     */
    @TableField("level")
    private Boolean level;

    /**
     * 总理程(KM)
     */
    @TableField("kilometer")
    private BigDecimal kilometer;

    /**
     * 总耗时(天)
     */
    @TableField("days")
    private BigDecimal days;

    /**
     * 状态:1启用 2停用
     */
    @TableField("state")
    private BigDecimal state;

    /**
     * 默认物流费（上游），单位分
     */
    @TableField("default_wl_fee")
    private BigDecimal defaultWlFee;

    /**
     * 默认运费（下游），单位分
     */
    @TableField("default_freight_fee")
    private BigDecimal defaultFreightFee;

    /**
     * 备注
     */
    @TableField("remark")
    private String remark;

    /**
     * 创建时间
     */
    @TableField("create_time")
    private String createTime;

    /**
     * 创建人ID
     */
    @TableField("create_user_id")
    private Long createUserId;

    /**
     * 更新时间
     */
    @TableField("update_time")
    private String updateTime;

    /**
     * 更新人ID
     */
    @TableField("update_user_id")
    private Long updateUserId;


}
