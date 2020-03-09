package com.cjyc.common.model.vo.web.finance;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @Author: Hut
 * @Date: 2020/03/09 10:48
 **/
@Data
public class CooperatorPaidVo implements Serializable {
    private static final long serialVersionUID = 1L;
    @ApiModelProperty(value = "订单Id")
    private Long orderId;

    @ApiModelProperty(value = "订单编号")
    private String orderNo;

    @ApiModelProperty(value = "合伙人服务费")
    private BigDecimal serviceFee;

    @ApiModelProperty(value = "付款状态")
    private String state;

    @ApiModelProperty(value = "合伙人名称")
    private String cooperator;

    @ApiModelProperty(value = "联系电话")
    private String phone;

}
