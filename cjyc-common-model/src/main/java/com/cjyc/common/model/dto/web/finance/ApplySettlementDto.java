package com.cjyc.common.model.dto.web.finance;

import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @Author:Hut
 * @Date:2019/11/22 15:57
 */
@Data
public class ApplySettlementDto implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "车辆编号(多个之间用，分割)")
    private String no;

    @ApiModelProperty(value = "客户Id")
    private Long customerId;

    @ApiModelProperty(value = "应收总运费")
    private BigDecimal freightFee;

    @ApiModelProperty(value = "结算金额（开票金额）")
    private BigDecimal amount;

    @ApiModelProperty(value = "是否开票 0不开 1开票")
    private String isInvoice;

    @ApiModelProperty(value = "发票类型")
    private Integer type;

    @ApiModelProperty(value = "客户名称")
    private String customerName;

    @ApiModelProperty(value = "纳税人识别号")
    private String taxPayerNumber;

    @ApiModelProperty(value = "地址")
    private String address;

    @ApiModelProperty(value = "电话")
    private String phone;

    @ApiModelProperty(value = "开户行名称")
    private String bankName;

    @ApiModelProperty(value = "银行账号")
    private String bankAccout;

    @ApiModelProperty(value = "收件人")
    private String addressee;

    @ApiModelProperty(value = "收件人电话")
    private String addresseePhone;

    @ApiModelProperty(value = "邮寄地址")
    private String mailAddress;

}
