package com.cjyc.common.model.entity.auto;

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
 * 业务员推广表
 * </p>
 *
 * @author JPG
 * @since 2019-09-29
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("b_saleman_promote")
public class SalemanPromote implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * ID
     */
    @TableId(value = "id", type = IdType.ID_WORKER)
    private Long id;

    /**
     * user_id
     */
    @TableField("user_id")
    private Long userId;

    /**
     * 推广人数
     */
    @TableField("customer_num")
    private Integer customerNum;

    /**
     * 有效推广人数
     */
    @TableField("customer_num_valid")
    private Integer customerNumValid;

    /**
     * 总收益
     */
    @TableField("income_amount")
    private BigDecimal incomeAmount;

    /**
     * 结算中收益
     */
    @TableField("settling_amount")
    private BigDecimal settlingAmount;

    /**
     * 未结算收益
     */
    @TableField("unsettle_amount")
    private BigDecimal unsettleAmount;


}
