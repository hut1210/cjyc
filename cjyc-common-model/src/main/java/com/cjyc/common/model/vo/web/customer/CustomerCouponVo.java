package com.cjyc.common.model.vo.web.customer;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
public class CustomerCouponVo implements Serializable {

    @ApiModelProperty("优惠券编码")
    private String couponNo;

    @ApiModelProperty("优惠券类型 0：满减  1：直减  2：折扣")
    private Integer type;

    @ApiModelProperty("满减价")
    private BigDecimal fullAmount;

    @ApiModelProperty("减额值")
    private BigDecimal cutAmount;

    @ApiModelProperty("折扣")
    private BigDecimal discount;

    @ApiModelProperty("是否永久  0：否  1：是")
    private Integer isForever;

    @ApiModelProperty("有效期开始时间")
    private String startPeriodDate;

    @ApiModelProperty("有效期结束时间")
    private String endPeriodDate;

    @ApiModelProperty("领取时间")
    private String receiveTime;

}