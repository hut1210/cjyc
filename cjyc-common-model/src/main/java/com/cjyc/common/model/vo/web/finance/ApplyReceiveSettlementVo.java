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

    @ApiModelProperty(value = "应收运费", required = true)
    private BigDecimal totalReceivableFee;

    @ApiModelProperty(value = "开票金额", required = true)
    private BigDecimal totalInvoiceFee;

    @ApiModelProperty(value = "客户发票", required = false)
    private CustomerInvoiceVo customerInvoiceVo;

    @ApiModelProperty(value = "订单车辆集合", required = true)
    private List<ReceiveSettlementDetailVo> receiveSettlementDetailList;

    @ApiModelProperty(value = "当前登陆人Id", required = true)
    private Long loginId;

    @ApiModelProperty(value = "当前登陆人名称", required = false)
    private String loginName;

    @ApiModelProperty(value = "是否需要开票：0：不需要 1：需要", required = true)
    private Integer needVoice;

}
