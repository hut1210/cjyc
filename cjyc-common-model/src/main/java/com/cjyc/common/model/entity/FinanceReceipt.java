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
 * 财务收款单表
 * </p>
 *
 * @author JPG
 * @since 2019-10-08
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("f_finance_receipt")
public class FinanceReceipt implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 客户ID
     */
    private Long customerId;

    /**
     * 客户名称
     */
    private String customerName;

    /**
     * 款项来源：1物流费，2损失赔付
     */
    private String source;

    /**
     * 类型：1物流费预付，2物流费全款到付，3物流费部分到付
     */
    private Integer type;

    /**
     * 支付渠道： 微信、支付宝、现金
     */
    private String channel;

    /**
     * 应收金额
     */
    private BigDecimal amount;

    /**
     * 实收金额
     */
    private BigDecimal realAmount;

    /**
     * 支付状态： 0待结算， 1已结算， 2未结算
     */
    private Integer payState;

    /**
     * 支付时间
     */
    private Long payTime;

    /**
     * 备注
     */
    private String remark;

    /**
     * 创建时间
     */
    private Long createTime;


}
