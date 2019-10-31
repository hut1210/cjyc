package com.cjyc.common.model.vo.web.coupon;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
public class ConsumeCouponVo implements Serializable {

    @ApiModelProperty("优惠券编码")
    private String couponNo;

    @ApiModelProperty("使用时间")
    private String useTime;

    @ApiModelProperty("客户名称")
    private String customerName;

    @ApiModelProperty("订单编号")
    private String orderNo;

    @ApiModelProperty("折扣金额")
    private BigDecimal couponOffsetFee;

    @ApiModelProperty("最终订单金额")
    private BigDecimal finalOrderAmount;

}