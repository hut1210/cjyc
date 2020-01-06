package com.cjyc.common.model.vo.web.finance;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @Author: Hut
 * @Date: 2020/01/06 10:09
 * 应付-申请结算
 **/
@Data
public class SettlementVo {

    @ApiModelProperty(value = "运单单号")
    private String no;

    @ApiModelProperty(value = "结算流水号")
    private String serialNumber;

    @ApiModelProperty(value = "应付运费")
    private BigDecimal freightFee;

    @ApiModelProperty(value = "状态")
    private String state;

    @ApiModelProperty(value = "发票号")
    private String invoiceNo;

    @ApiModelProperty(value = "申请结算时间")
    private Long applyTime;

    private Long applicantId;

    @ApiModelProperty(value = "申请人")
    private String applicant;

    @ApiModelProperty(value = "确认开票时间")
    private Long confirmTime;

    @ApiModelProperty(value = "确认人Id")
    private Long confirmId;

    @ApiModelProperty(value = "确认人")
    private String confirmMan;

    @ApiModelProperty(value = "核销时间")
    private Long writeOffTime;

    @ApiModelProperty(value = "核销人Id")
    private Long writeOffId;

    @ApiModelProperty(value = "核销人")
    private Long writeOffMan;

    @ApiModelProperty(value = "实付总运费")
    private BigDecimal totalFreightPay;
}
