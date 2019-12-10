package com.cjyc.common.model.vo.web.finance;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @Author:Hut
 * @Date:2019/11/25 18:36
 */
@Data
public class WaitInvoiceVo implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "结算流水号")
    private  String serialNumber;

    @ApiModelProperty(value = "应收运费")
    private BigDecimal freightReceivable;

    @ApiModelProperty(value = "结算金额（开票金额）")
    private BigDecimal amount;

    @ApiModelProperty(value = "差额")
    private BigDecimal difference;

    @ApiModelProperty(value = "发票类型")
    private Integer type;

    @ApiModelProperty(value = "客户名称")
    private String customerName;

    @ApiModelProperty(value = "纳税人识别号")
    private String taxPayerNumber;

    @ApiModelProperty(value = "地址")
    private String address;

    @ApiModelProperty(value = "电话")
    private String tel;

    @ApiModelProperty(value = "开户行名称")
    private String bankName;

    @ApiModelProperty(value = "银行账号")
    private String bankAccount;

    @ApiModelProperty(value = "收件人")
    private String pickUpPerson;

    @ApiModelProperty(value = "收件人电话")
    private String pickUpPhone;

    @ApiModelProperty(value = "收件地址")
    private String pickUpAddress;

    @ApiModelProperty(value = "申请结算时间")
    private Long applyTime;

    @ApiModelProperty(value = "申请结算时间")
    private String applyTimeStr;

    @ApiModelProperty(value = "申请人")
    private  String applicant;

    @ApiModelProperty(value = "确认开票时间")
    private Long confirmTime;

    @ApiModelProperty(value = "确认开票时间")
    private String confirmTimeStr;

    @ApiModelProperty(value = "确认人")
    private  String confirmMan;

    @ApiModelProperty(value = "核销时间")
    private Long writeOffTime;

    @ApiModelProperty(value = "核销人")
    private String writeOffMan;

}
