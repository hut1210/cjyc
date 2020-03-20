package com.cjyc.common.model.dto.web.finance;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * <p>待开票列表查询返回参数实体类Dto</p>
 *
 * @Author:RenPL
 * @Date:2020/3/20 15:57
 */
@Data
public class ReceiveSettlementNeedInvoiceDto {

    @ApiModelProperty(value = "应收运费")
    private BigDecimal receivableFee;

    @ApiModelProperty(value = "开票金额")
    private BigDecimal invoiceFee;

    @ApiModelProperty(value = "结算流水号")
    private String serialNumber;

    @ApiModelProperty(value = "申请时间")
    private Long applyTime;

    @ApiModelProperty(value = "申请人")
    private String applicantName;

    @ApiModelProperty(value = "确认时间")
    private Long confirmTime;

    @ApiModelProperty(value = "确认人")
    private String confirmName;

    @ApiModelProperty(value = "核销时间")
    private Long verificationTime;

    @ApiModelProperty(value = "核销人")
    private String verificationName;

    @ApiModelProperty(value = "发票类型 1-普通(个人) ，2-增值普票(企业) ，3-增值专用发票'")
    private String invoiceType;

    @ApiModelProperty(value = "客户名称")
    private String customerName;

    @ApiModelProperty(value = "纳税人识别号")
    private String taxCode;

    @ApiModelProperty(value = "地址")
    private String invoiceAddress;

    @ApiModelProperty(value = "电话")
    private String phone;

    @ApiModelProperty(value = "开户银行名称")
    private String bankName;

    @ApiModelProperty(value = "开户行账号")
    private String bankAccount;

    @ApiModelProperty(value = "收票人")
    private String pickupPerson;

    @ApiModelProperty(value = "收票电话")
    private String pickupPhone;

    @ApiModelProperty(value = "邮寄地址")
    private String pickupAddress;

    @ApiModelProperty(value = "发票号")
    private String title;

}
