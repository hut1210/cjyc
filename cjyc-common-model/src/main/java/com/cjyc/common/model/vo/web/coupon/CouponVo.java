package com.cjyc.common.model.vo.web.coupon;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
@Data
public class CouponVo implements Serializable {

    @ApiModelProperty(value = "优惠券主键id")
    private String id;

    @ApiModelProperty(value = "优惠券名称")
    private String couponName;

    @ApiModelProperty(value = "优惠券类型 0：满减  3：直减  5：折扣")
    private String couponType;

    @ApiModelProperty(value = "满额价")
    private String fullAmount;

    @ApiModelProperty(value = "减额值")
    private String cutAmount;

    @ApiModelProperty(value = "折扣")
    private String discount;

    @ApiModelProperty(value = "发放张数")
    private String grantNum;

    @ApiModelProperty(value = "领取张数")
    private String receiveNum;

    @ApiModelProperty(value = "消耗张数")
    private String consumeNum;

    @ApiModelProperty(value = "到期作废张数")
    private String expireDeleNum;

    @ApiModelProperty(value = "剩余可用张数")
    private String surplusAvailNum;

    @ApiModelProperty(value = "是否永久  0：否  1：是")
    private String isForever;

    @ApiModelProperty(value = "优惠券有效起始时间")
    private String startPeriodDate;

    @ApiModelProperty(value = "优惠券有效结束时间")
    private String endPeriodDate;

    @ApiModelProperty(value = "优惠券审核状态 0：待审核  3：审核通过  5：审核不通过  7：已作废")
    private String state;

    @ApiModelProperty(value = "创建时间")
    private String createTime;
}