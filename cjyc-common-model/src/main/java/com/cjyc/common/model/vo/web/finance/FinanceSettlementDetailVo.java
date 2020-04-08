package com.cjyc.common.model.vo.web.finance;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @Author: Hut
 * @Date: 2020/04/08 10:54
 **/
@Data
public class FinanceSettlementDetailVo implements Serializable {

    @ApiModelProperty(value = "车辆编号")
    private String no;

    @ApiModelProperty(value = "应收运费")
    private BigDecimal freightFee;

    @ApiModelProperty(value = "开票金额")
    private BigDecimal invoiceFee;

    @ApiModelProperty(value = "核销时间")
    private Long verificationTime;
}
