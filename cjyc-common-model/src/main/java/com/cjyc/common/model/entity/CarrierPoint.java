package com.cjyc.common.model.entity;

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
 * 承运商分表
 * </p>
 *
 * @author JPG
 * @since 2019-09-29
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("d_carrier_point")
public class CarrierPoint implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * ID
     */
    @TableId(value = "id", type = IdType.ID_WORKER)
    private Long id;

    /**
     * 司机ID
     */
    @TableField("carrier_id")
    private Long carrierId;

    /**
     * 锁定积分
     */
    @TableField("protected_point")
    private Integer protectedPoint;

    /**
     * 可用积分
     */
    @TableField("available_point")
    private Integer availablePoint;

    /**
     * 状态：
     */
    @TableField("state")
    private Integer state;

    /**
     * 总收入积分
     */
    @TableField("total_income_point")
    private Integer totalIncomePoint;

    /**
     * 总花费积分
     */
    @TableField("total_expend_point")
    private Integer totalExpendPoint;

    /**
     * 创建时间
     */
    @TableField("create_time")
    private String createTime;

    /**
     * 更新时间
     */
    @TableField("update_time")
    private String updateTime;


}
