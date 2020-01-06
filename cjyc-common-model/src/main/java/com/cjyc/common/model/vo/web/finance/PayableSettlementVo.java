package com.cjyc.common.model.vo.web.finance;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * @Author: Hut
 * @Date: 2020/01/06 16:26
 * 应付账款
 **/
@Data
public class PayableSettlementVo {

    @ApiModelProperty(value = "应付总运费")
    private BigDecimal totalFreightFee;

    @ApiModelProperty(value = "发票号")
    private String invoiceNo;

    private List<PayableTaskVo> payableTaskVo;

    @ApiModelProperty(value = "实付总运费")
    private BigDecimal totalFreightPaid;

    @ApiModelProperty(value = "核销时间")
    private Long writeOffTime;
}
