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
 * 发票信息表
 * </p>
 *
 * @author JPG
 * @since 2019-10-08
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
    @TableId(value = "id", type = IdType.INPUT)
    private Long id;

    /**
     * 客户ID
     */
    private Long customerId;

    /**
     * 发票类型 1-普通(个人) ，2-增值普票(企业) ，3-增值专用发票'
     */
    private Integer type;

    /**
     * 发票抬头
     */
    private String title;

    /**
     * 姓名
     */
    private String name;

    /**
     * 纳税人识别号
     */
    private String taxCode;

    /**
     * 地址
     */
    private String invoiceAddress;

    /**
     * 电话
     */
    private String tel;

    /**
     * 开户银行名称
     */
    private String bankName;

    /**
     * 开户行账号
     */
    private String bankAccount;

    /**
     * 默认标识
     */
    private Integer defaultFlag;


}
