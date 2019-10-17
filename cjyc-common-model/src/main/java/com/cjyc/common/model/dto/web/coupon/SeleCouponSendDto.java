package com.cjyc.common.model.dto.web.coupon;

import com.cjyc.common.model.dto.BasePageDto;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
@Data
public class SeleCouponSendDto extends BasePageDto implements Serializable {

    @ApiModelProperty(value = "优惠券编号")
    private String couponId;

    @ApiModelProperty(value = "客户名称")
    private String customerName;

    @ApiModelProperty(value = "订单编号")
    private String orderNo;

    @ApiModelProperty(value = "使用开始时间")
    private String startDate;

    @ApiModelProperty(value = "使用结束时间")
    private String endDate;
}