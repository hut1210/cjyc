package com.cjyc.common.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @Author:Hut
 * @Date:2019/11/25 15:44
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("f_invoice_apply")
@ApiModel(value="InvoiceReceipt对象", description="发票申请结算信息表")
public class InvoiceReceipt implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "ID")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty(value = "结算流水号")
    private String serialNumber;

    @ApiModelProperty(value = "车辆编码")
    private String orderCarNo;

    @ApiModelProperty(value = "客户ID")
    private Long customerId;

    @ApiModelProperty(value = "应收总运费")
    private BigDecimal freightFee;

    @ApiModelProperty(value = "结算金额")
    private BigDecimal amount;

    @ApiModelProperty(value = "是否开票")
    private String isInvoice;

    @ApiModelProperty(value = "发票信息ID")
    private Long invoiceId;

    @ApiModelProperty(value = "发票号")
    private String invoiceNo;

    @ApiModelProperty(value = "结算状态")
    private String state;

    @ApiModelProperty(value = "申请时间")
    private Long invoiceTime;

    @ApiModelProperty(value = "申请人")
    private String invoiceMan;

}
