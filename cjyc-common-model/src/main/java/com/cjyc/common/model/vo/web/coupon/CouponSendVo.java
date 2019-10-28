package com.cjyc.common.model.vo.web.coupon;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
@Data
public class CouponSendVo implements Serializable {

    @ApiModelProperty(value = "发放优惠券主键id")
    private String id;

    @ApiModelProperty(value = "优惠券使用时间")
    private String useTime;

    @ApiModelProperty(value = "客户名称")
    private String customerName;

    @ApiModelProperty(value = "订单编号")
    private String orerNo;

    @ApiModelProperty(value = "折扣金额")
    private String disAmount;

    @ApiModelProperty(value = "最终订单金额")
    private String finOrdAmount;
}