package com.cjyc.common.model.dto.web.coupon;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;

@Data
public class CouponDto implements Serializable {

    private static final long serialVersionUID = 14175756539931543L;

    @ApiModelProperty(value = "优惠券主键id")
    private Long couponId;

    @ApiModelProperty(value = "登陆用户id(loginId)")
    @NotNull(message = "登陆用户id(loginId)不能为空")
    private Long loginId;

    @ApiModelProperty(value = "优惠券名称")
    @NotBlank(message = "优惠券名称不能为空")
    private String name;

    @ApiModelProperty(value = "优惠券类型 0：满减  1：直减  2：折扣")
    @NotNull(message = "优惠券类型不能为空")
    private Integer type;

    @ApiModelProperty(value = "满额价")
    private BigDecimal fullAmount;

    @ApiModelProperty(value = "减额值")
    private BigDecimal cutAmount;

    @ApiModelProperty(value = "折扣")
    private BigDecimal discount;

    @ApiModelProperty(value = "发放张数")
    @NotNull(message = "发放张数不能为空")
    private Integer grantNum;

    @ApiModelProperty(value = "是否永久  0：否  1：是")
    @NotNull(message = "是否永久不能为空")
    private Integer isForever;

    @ApiModelProperty(value = "优惠券有效起始时间")
    private String startPeriodDate;

    @ApiModelProperty(value = "优惠券有效结束时间")
    private String endPeriodDate;
}