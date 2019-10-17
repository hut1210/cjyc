package com.cjyc.common.model.vo.customer.couponSend;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
@Data
public class CouponSendVo implements Serializable {

    @ApiModelProperty("优惠券名称")
    private String couponName;

    @ApiModelProperty(value = "优惠券类型 0：满减  3：直减  5：折扣")
    private String couponType;

    @ApiModelProperty(value = "是否永久  0：否  1：是")
    private String isForever;

    @ApiModelProperty(value = "是否已使用 0：领取未使用  1：已使用")
    private String isUse;

    @ApiModelProperty(value = "有效期截至时间")
    private String periodDate;
}