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
 * 财务付款单表
 * </p>
 *
 * @author JPG
 * @since 2019-09-29
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("f_finance_payment")
public class FinancePayment implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 收款用户ID
     */
    @TableField("user_id")
    private Long userId;

    /**
     * 收款用户名称
     */
    @TableField("user_name")
    private String userName;

    /**
     * 类型：1运费，2中介费，3推广费
     */
    @TableField("source")
    private Integer source;

    /**
     * 应付账款
     */
    @TableField("amount")
    private BigDecimal amount;

    /**
     * 实付账款
     */
    @TableField("real_amount")
    private BigDecimal realAmount;

    /**
     * 收款卡号
     */
    @TableField("bank_card_no")
    private String bankCardNo;

    /**
     * 收款银行名称
     */
    @TableField("bank_name")
    private String bankName;

    /**
     * 结算方式：0时付，1账期
     */
    @TableField("pay_type")
    private Integer payType;

    /**
     * 支付状态： 0待结算， 1结算中，2已结算
     */
    @TableField("pay_state")
    private Integer payState;

    /**
     * 备注
     */
    @TableField("remark")
    private String remark;

    /**
     * 创建时间
     */
    @TableField("create_time")
    private Long createTime;

    /**
     * 结算编号
     */
    @TableField("trade_bill_no")
    private String tradeBillNo;

    /**
     * 结算时间
     */
    @TableField("pay_time")
    private Long payTime;


}
