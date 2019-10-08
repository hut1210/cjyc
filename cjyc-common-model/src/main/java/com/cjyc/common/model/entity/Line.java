package com.cjyc.common.model.entity;

import java.math.BigDecimal;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
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
 * @since 2019-10-08
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
    private String code;

    /**
     * 线路名称
     */
    private String name;

    /**
     * 出发地行政区编码
     */
    private String fromCode;

    /**
     * 目的地行政区编码
     */
    private String toCode;

    /**
     * 级别：2市级，3区级
     */
    private Boolean level;

    /**
     * 总理程(KM)
     */
    private BigDecimal kilometer;

    /**
     * 总耗时(天)
     */
    private BigDecimal days;

    /**
     * 状态:1启用 2停用
     */
    private BigDecimal state;

    /**
     * 默认物流费（上游），单位分
     */
    private BigDecimal defaultWlFee;

    /**
     * 默认运费（下游），单位分
     */
    private BigDecimal defaultFreightFee;

    /**
     * 备注
     */
    private String remark;

    /**
     * 创建时间
     */
    private String createTime;

    /**
     * 创建人ID
     */
    private Long createUserId;

    /**
     * 更新时间
     */
    private String updateTime;

    /**
     * 更新人ID
     */
    private Long updateUserId;


}
