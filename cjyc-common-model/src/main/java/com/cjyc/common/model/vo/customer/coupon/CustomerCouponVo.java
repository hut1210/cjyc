package com.cjyc.common.model.vo.customer.coupon;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
@Data
public class CustomerCouponVo implements Serializable {

    private static final long serialVersionUID = 5038946191538866849L;
    @ApiModelProperty("优惠券名称")
    private String name;

    @ApiModelProperty(value = "类型 0：满减  1：直减  2：折扣")
    private Integer type;

    @ApiModelProperty(value = "是否永久  0：否  1：是")
    private Integer isForever;

    @ApiModelProperty(value = "是否已使用 0：未使用  1：已使用")
    private Integer isUse;

    @ApiModelProperty(value = "是否过期 0：未过期  1：已过期")
    private Integer isExpire;

    @ApiModelProperty(value = "有效期截止时间")
    private String endPeriodDate;
}