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
 * 财务付款单表
 * </p>
 *
 * @author JPG
 * @since 2019-10-08
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
    private Long userId;

    /**
     * 收款用户名称
     */
    private String userName;

    /**
     * 类型：1运费，2中介费，3推广费
     */
    private Integer source;

    /**
     * 应付账款
     */
    private BigDecimal amount;

    /**
     * 实付账款
     */
    private BigDecimal realAmount;

    /**
     * 收款卡号
     */
    private String bankCardNo;

    /**
     * 收款银行名称
     */
    private String bankName;

    /**
     * 结算方式：0时付，1账期
     */
    private Integer payType;

    /**
     * 支付状态： 0待结算， 1结算中，2已结算
     */
    private Integer payState;

    /**
     * 备注
     */
    private String remark;

    /**
     * 创建时间
     */
    private Long createTime;

    /**
     * 结算编号
     */
    private String tradeBillNo;

    /**
     * 结算时间
     */
    private Long payTime;


}
