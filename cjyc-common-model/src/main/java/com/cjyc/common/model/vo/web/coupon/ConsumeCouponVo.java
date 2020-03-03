package com.cjyc.common.model.vo.web.coupon;

import com.cjyc.common.model.serizlizer.BigDecimalSerizlizer;
import com.cjyc.common.model.serizlizer.DateLongSerizlizer;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
public class ConsumeCouponVo implements Serializable {

    private static final long serialVersionUID = -4462744108072997446L;
    @ApiModelProperty("优惠券发放id(snedId)")
    @JsonSerialize(using= ToStringSerializer.class)
    private Long sendId;

    @ApiModelProperty("优惠券id(couponId)")
    @JsonSerialize(using= ToStringSerializer.class)
    private Long couponId;

    @ApiModelProperty("优惠券编码")
    private String couponNo;

    @ApiModelProperty("使用时间")
    @JsonSerialize(using = DateLongSerizlizer.class)
    private Long useTime;

    @ApiModelProperty("客户名称")
    private String customerName;

    @ApiModelProperty("订单编号")
    private String orderNo;

    @ApiModelProperty("折扣金额")
    @JsonSerialize(using = BigDecimalSerizlizer.class)
    private BigDecimal couponOffsetFee;

    @ApiModelProperty("最终订单金额")
    @JsonSerialize(using = BigDecimalSerizlizer.class)
    private BigDecimal finalOrderAmount;

}