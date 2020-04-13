package com.cjyc.common.model.vo.customer.order;

import com.cjyc.common.model.serizlizer.BigDecimalSerizlizer;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class ValidateReceiptCarPayVo {

    @ApiModelProperty(value = "是否需要支付：0否 ，1是")
    private Integer isNeedPay;

    private Integer payType;
    @ApiModelProperty(value = "金额")
    @JsonSerialize(using = BigDecimalSerizlizer.class)
    private BigDecimal amount;
    @ApiModelProperty(value = "车辆编号列表")
    private List<String> orderCarNos;
    @ApiModelProperty(value = "订单编号")
    private String orderNo;
}
