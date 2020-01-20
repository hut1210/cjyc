package com.cjyc.common.model.vo.web.finance;

import com.cjyc.common.model.entity.CustomerInvoice;
import com.cjyc.common.model.serizlizer.BigDecimalSerizlizer;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

/**
 * @Author: Hut
 * @Date: 2019/12/9 17:18
 */
@Data
public class SettlementDetailVo implements Serializable {

    private static final long serialVersionUID = 1L;

    @JsonSerialize(using = BigDecimalSerizlizer.class)
    @ApiModelProperty(value = "应收总运费")
    private BigDecimal freightFee;

    @JsonSerialize(using = BigDecimalSerizlizer.class)
    @ApiModelProperty(value = "结算金额")
    private BigDecimal amount;

    @ApiModelProperty(value = "发票号")
    private String invoiceNo;

    @ApiModelProperty(value = "核销时间")
    private Long writeOffTime;

    private CustomerInvoice customerInvoice;

    private List<OrderCarDetailVo> orderCarDetailList;
}
