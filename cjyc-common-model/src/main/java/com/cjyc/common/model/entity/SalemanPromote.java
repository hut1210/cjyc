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
 * 业务员推广表
 * </p>
 *
 * @author JPG
 * @since 2019-10-08
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
    @TableId(value = "id", type = IdType.INPUT)
    private Long id;

    /**
     * user_id
     */
    private Long userId;

    /**
     * 推广人数
     */
    private Integer customerNum;

    /**
     * 有效推广人数
     */
    private Integer customerNumValid;

    /**
     * 总收益
     */
    private BigDecimal incomeAmount;

    /**
     * 结算中收益
     */
    private BigDecimal settlingAmount;

    /**
     * 未结算收益
     */
    private BigDecimal unsettleAmount;


}