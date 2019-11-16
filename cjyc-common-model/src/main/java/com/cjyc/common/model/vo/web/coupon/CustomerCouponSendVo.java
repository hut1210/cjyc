package com.cjyc.common.model.vo.web.coupon;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
public class CustomerCouponSendVo implements Serializable {

    private static final long serialVersionUID = -4638706234324512258L;
    @ApiModelProperty("优惠券发放id")
    private Long couponSendId;

    @ApiModelProperty("优惠券名称")
    private String name;

    @ApiModelProperty("类型 0：满减  1：直减  2：折扣")
    private Integer type;

    @ApiModelProperty("满额价")
    private BigDecimal fullAmount;

    @ApiModelProperty("减额值")
    private BigDecimal cutAmount;

    @ApiModelProperty("折扣")
    private BigDecimal discount;

    @ApiModelProperty("有效结束时间")
    private Long endPeriodDate;
}