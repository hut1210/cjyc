package com.cjyc.common.model.vo.web.finance;

import com.cjyc.common.model.vo.customer.invoice.CustomerInvoiceVo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * <p>
 * 应收账款结算申请Vo
 * </p>
 *
 * @author RenPL
 * @since 2020-3-20
 */
@Data
@ApiModel(value = "ReceiveSettlementVo", description = "应收账款结算申请Vo")
public class ApplyReceiveSettlementVo {

    @ApiModelProperty(value = "应收运费")
    private BigDecimal totalReceivableFee;

    @ApiModelProperty(value = "开票金额")
    private BigDecimal totalInvoiceFee;

    @ApiModelProperty(value = "客户发票")
    private CustomerInvoiceVo customerInvoiceVo;

    @ApiModelProperty(value = "订单车辆id集合")
    private List<ReceiveSettlementDetailVo> receiveSettlementDetailList;

    @ApiModelProperty(value = "业务员Id", required = true)
    private Long customerId;

    @ApiModelProperty(value = "业务员名称")
    private String customerName;

    @ApiModelProperty(value = "是否需要开票：0：不需要 1：需要")
    private Integer needVoice;

}
