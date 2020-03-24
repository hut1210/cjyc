package com.cjyc.common.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.math.BigDecimal;

/**
 * <p>
 * 应收账款结算表
 * </p>
 *
 * @author RenPL
 * @since 2020-3-20
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("s_settlement")
@ApiModel(value = "ReceiveSettlement", description = "应收账款结算表")
public class ReceiveSettlement {

    @ApiModelProperty(value = "id")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty(value = "结算流水号")
    private String serialNumber;

    @ApiModelProperty(value = "应收运费")
    private BigDecimal totalReceivableFee;

    @ApiModelProperty(value = "开票金额")
    private BigDecimal totalInvoiceFee;

    @ApiModelProperty(value = "结算申请时间")
    private Long applyTime;

    @ApiModelProperty(value = "申请人id")
    private Long applicantId;

    @ApiModelProperty(value = "申请人名称")
    private String applicantName;

    @ApiModelProperty(value = "确认收票时间")
    private Long confirmTime;

    @ApiModelProperty(value = "确认人id")
    private Long confirmId;

    @ApiModelProperty(value = "确认人名称")
    private String confirmName;

    @ApiModelProperty(value = "核销时间")
    private Long verificationTime;

    @ApiModelProperty(value = "核销人id")
    private Long verificationId;

    @ApiModelProperty(value = "核销人名称")
    private String verificationName;

    @ApiModelProperty(value = "状态: 0已申请 1已确认 2已核销")
    private Integer state;

    @ApiModelProperty(value = "发票id")
    private Long invoiceId;

    @ApiModelProperty(value = "发票号")
    private String invoiceNo;

}
