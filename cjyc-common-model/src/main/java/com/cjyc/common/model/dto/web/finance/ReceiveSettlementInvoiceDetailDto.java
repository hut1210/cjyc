package com.cjyc.common.model.dto.web.finance;

import com.cjyc.common.model.entity.CustomerInvoice;
import com.cjyc.common.model.entity.ReceiveSettlement;
import com.cjyc.common.model.entity.ReceiveSettlementDetail;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @Author:RenPL
 * @Date:2020/3/21 15:57
 */
@Data
public class ReceiveSettlementInvoiceDetailDto implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "结算明细列表")
    private List<ReceiveSettlementDetail> receiveSettlementInvoiceDetailList;

    @ApiModelProperty(value = "发票信息")
    private CustomerInvoice invoice;

    @ApiModelProperty(value = "结算信息")
    private ReceiveSettlement receiveSettlement;

}
