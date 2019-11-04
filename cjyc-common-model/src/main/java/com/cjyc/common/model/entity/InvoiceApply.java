package com.cjyc.common.model.entity;

import java.math.BigDecimal;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 发票申请信息表
 * </p>
 *
 * @author JPG
 * @since 2019-11-02
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("c_invoice_apply")
@ApiModel(value="InvoiceApply对象", description="发票申请信息表")
public class InvoiceApply implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty(value = "发票ID")
    private Long invoiceId;

    @ApiModelProperty(value = "客户ID")
    private Long customerId;

    @ApiModelProperty(value = "申请人名称")
    private String customerName;

    @ApiModelProperty(value = "申请时间")
    private Long applyTime;

    @ApiModelProperty(value = "开票时间")
    private Long invoiceTime;

    @ApiModelProperty(value = "开票金额")
    private BigDecimal amount;

    @ApiModelProperty(value = "发票号")
    private String invoiceNo;

    @ApiModelProperty(value = "开票人姓名")
    private String operationName;

    @ApiModelProperty(value = "开票状态 1-申请中，2-已开票")
    private Integer state;


}
