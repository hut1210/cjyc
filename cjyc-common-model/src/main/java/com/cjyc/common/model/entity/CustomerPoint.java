package com.cjyc.common.model.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
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
 * @since 2019-10-08
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
    @TableId(value = "id", type = IdType.INPUT)
    private Long id;

    /**
     * 用户ID
     */
    private Long customerId;

    /**
     * 锁定积分
     */
    private Integer protectedPoints;

    /**
     * 可用积分
     */
    private Integer availablePoints;

    /**
     * 总收入积分
     */
    private Integer totalIncomePoints;

    /**
     * 总花费积分
     */
    private Integer totalExpendPoints;

    /**
     * 状态：
     */
    private Integer state;

    /**
     * 创建时间
     */
    private Long createTime;

    /**
     * 更新时间
     */
    private Long updateTime;


}
