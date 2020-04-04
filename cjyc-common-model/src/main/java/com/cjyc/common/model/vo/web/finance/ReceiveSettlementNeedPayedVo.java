package com.cjyc.common.model.vo.web.finance;

import com.cjyc.common.model.dto.BasePageDto;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * <p>待回款列表查询请求参数封装实体类Vo</p>
 *
 * @Author:RenPL
 * @Date:2020/3/20 15:57
 */
@Data
public class ReceiveSettlementNeedPayedVo extends BasePageDto implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "结算流水号")
    private String serialNumber;

    @ApiModelProperty(value = "纳税人识别号")
    private String taxCode;

    @ApiModelProperty(value = "客户账号")
    private String bankAccount;

    @ApiModelProperty(value = "申请结算开始时间")
    private Long startApplyTime;

    @ApiModelProperty(value = "申请结算结束时间")
    private Long endApplyTime;

    @ApiModelProperty(value = "申请人")
    private String applicantName;

    @ApiModelProperty(value = "发票类型")
    private Integer invoiceType;

    @ApiModelProperty(value = "客户名称")
    private String customerName;

    @ApiModelProperty(value = "确认开票开始时间")
    private Long startConfirmTime;

    @ApiModelProperty(value = "确认开票结束时间")
    private Long endConfirmTime;

    @ApiModelProperty(value = "确认开票人")
    private String confirmName;

    @ApiModelProperty(value = "核销开始时间")
    private Long startVerificationTime;

    @ApiModelProperty(value = "核销结束时间")
    private Long endVerificationTime;

    @ApiModelProperty(value = "核销人")
    private String verificationName;
}
