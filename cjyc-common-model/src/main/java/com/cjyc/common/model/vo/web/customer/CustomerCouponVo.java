package com.cjyc.common.model.vo.web.customer;

import com.cjyc.common.model.serizlizer.BigDecimalSerizlizer;
import com.cjyc.common.model.serizlizer.DateLongSerizlizer;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
public class CustomerCouponVo implements Serializable {

    @ApiModelProperty("客户优惠券id(sendId)")
    @JsonSerialize(using= ToStringSerializer.class)
    private Long sendId;

    @ApiModelProperty("优惠券编码")
    private String couponNo;

    @ApiModelProperty("优惠券类型 0：满减  1：直减  2：折扣")
    private Integer type;

    @ApiModelProperty("满减价")
    @JsonSerialize(using = BigDecimalSerizlizer.class)
    private BigDecimal fullAmount;

    @ApiModelProperty("减额值")
    @JsonSerialize(using = BigDecimalSerizlizer.class)
    private BigDecimal cutAmount;

    @ApiModelProperty("折扣")
    private BigDecimal discount;

    @ApiModelProperty("是否已使用 0：可用  1：不可用")
    private Integer isUse;

    @ApiModelProperty("是否永久  0：否  1：是")
    private Integer isForever;

    @ApiModelProperty("有效期开始时间")
    @JsonSerialize(using = DateLongSerizlizer.class)
    private Long startPeriodDate;

    @ApiModelProperty("有效期结束时间")
    @JsonSerialize(using = DateLongSerizlizer.class)
    private Long endPeriodDate;

    @ApiModelProperty("领取时间")
    @JsonSerialize(using = DateLongSerizlizer.class)
    private Long receiveTime;

}