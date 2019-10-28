package com.cjyc.common.model.dto.web.coupon;

import com.cjyc.common.model.dto.BasePageDto;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
@Data
public class SeleCouponDto extends BasePageDto implements Serializable {

    @ApiModelProperty("优惠券名称")
    private String couponName;

    @ApiModelProperty(value = "优惠券类型 0：满减  3：直减  5：折扣")
    private String couponType;

    @ApiModelProperty(value = "优惠券审核状态 0：待审核  3：审核通过  5：审核不通过  7：已作废")
    private String state;

    @ApiModelProperty(value = "创建开始时间")
    private String startTime;

    @ApiModelProperty(value = "创建结束时间")
    private String endTime;

    @ApiModelProperty(value = "创建人")
    private String createName;
}