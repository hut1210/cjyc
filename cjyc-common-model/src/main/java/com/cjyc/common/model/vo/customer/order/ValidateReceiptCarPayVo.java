package com.cjyc.common.model.vo.customer.order;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class ValidateReceiptCarPayVo {

    @ApiModelProperty(value = "是否需要支付：0否 ，1是")
    private Integer isNeedPay;
    @ApiModelProperty(value = "金额")
    private BigDecimal amount;
    @ApiModelProperty(value = "车辆编号列表")
    private List<String> orderCarNos;
    @ApiModelProperty(value = "订单编号")
    private String orderNo;
}
