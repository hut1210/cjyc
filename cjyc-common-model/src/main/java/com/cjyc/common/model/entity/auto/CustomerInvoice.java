package com.cjyc.common.model.entity.auto;

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
 * 发票信息表
 * </p>
 *
 * @author JPG
 * @since 2019-09-29
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("c_customer_invoice")
public class CustomerInvoice implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * ID
     */
    @TableId(value = "id", type = IdType.ID_WORKER)
    private Long id;

    /**
     * 客户ID
     */
    @TableField("customer_id")
    private Long customerId;

    /**
     * 发票类型 1-普通(个人) ，2-增值普票(企业) ，3-增值专用发票'
     */
    @TableField("type")
    private Integer type;

    /**
     * 发票抬头
     */
    @TableField("title")
    private String title;

    /**
     * 姓名
     */
    @TableField("name")
    private String name;

    /**
     * 纳税人识别号
     */
    @TableField("tax_code")
    private String taxCode;

    /**
     * 地址
     */
    @TableField("invoice_address")
    private String invoiceAddress;

    /**
     * 电话
     */
    @TableField("tel")
    private String tel;

    /**
     * 开户银行名称
     */
    @TableField("bank_name")
    private String bankName;

    /**
     * 开户行账号
     */
    @TableField("bank_account")
    private String bankAccount;

    /**
     * 默认标识
     */
    @TableField("default_flag")
    private Integer defaultFlag;


}
