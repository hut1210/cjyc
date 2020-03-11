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

    @ApiModelProperty(value = "付款失败原因")
    private String description;

    @ApiModelProperty(value = "付款时间")
    private Long payTime;

    @ApiModelProperty(value = "结算类型")
    private String settleType;

    @ApiModelProperty(value = "始发城市")
    private String startCity;

    @ApiModelProperty(value = "目的城市")
    private String endCity;

    @ApiModelProperty(value = "客户Id")
    private Long customerId;

    @ApiModelProperty(value = "客户名称")
    private String customerName;

    @ApiModelProperty(value = "客户支付方式 0到付（默认），1预付，2账期")
    private String customerPayType;

    @ApiModelProperty(value = "客户支付时间")
    private Long wlPayTime;

    @ApiModelProperty(value = "客户交付时间")
    private Long completeTime;

    @ApiModelProperty(value = "订单金额")
    private BigDecimal totalFee;

    @ApiModelProperty(value = "付款类型（公户，个户）")
    private String cardType;

    @ApiModelProperty(value = "对公开户名称")
    private String publicAccount;

    @ApiModelProperty(value = "开户银行账号")
    private String cardNo;

    @ApiModelProperty(value = "开户行名称")
    private String bankName;

    @ApiModelProperty(value = "持卡人姓名")
    private String cardName;

    @ApiModelProperty(value = "持卡人身份证号")
    private String IDCard;

}
