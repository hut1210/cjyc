package com.cjyc.common.model.vo.web.finance;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * <p>待开票列表查询请求参数封装实体类Vo</p>
 *
 * @Author:RenPL
 * @Date:2020/3/20 15:57
 */
@Data
public class ReceiveSettlementNeedInvoiceVo implements Serializable {

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

}
