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
 * 业务员推广明细
 * </p>
 *
 * @author JPG
 * @since 2019-10-08
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("b_saleman_promote_detail")
public class SalemanPromoteDetail implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * ID
     */
    @TableId(value = "id", type = IdType.INPUT)
    private Long id;

    /**
     * 业务员ID
     */
    private Long userId;

    /**
     * 推广的用户ID
     */
    private Long customerId;

    /**
     * 状态：0用户未下单，1用户已下单
     */
    private Integer state;

    /**
     * 状态：0未结算，1结算中，2已结算
     */
    private Integer settleState;

    /**
     * 结算申请单号
     */
    private String settleBillNo;


}