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
 * 用户积分表
 * </p>
 *
 * @author JPG
 * @since 2019-09-29
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("c_customer_point")
public class CustomerPoint implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * ID
     */
    @TableId(value = "id", type = IdType.ID_WORKER)
    private Long id;

    /**
     * 用户ID
     */
    @TableField("customer_id")
    private Long customerId;

    /**
     * 锁定积分
     */
    @TableField("protected_points")
    private Integer protectedPoints;

    /**
     * 可用积分
     */
    @TableField("available_points")
    private Integer availablePoints;

    /**
     * 总收入积分
     */
    @TableField("total_income_points")
    private Integer totalIncomePoints;

    /**
     * 总花费积分
     */
    @TableField("total_expend_points")
    private Integer totalExpendPoints;

    /**
     * 状态：
     */
    @TableField("state")
    private Integer state;

    /**
     * 创建时间
     */
    @TableField("create_time")
    private Long createTime;

    /**
     * 更新时间
     */
    @TableField("update_time")
    private Long updateTime;


}
