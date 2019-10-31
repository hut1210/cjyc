package com.cjyc.common.model.dto.web.coupon;

import com.cjyc.common.model.dto.BasePageDto;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
@Data
public class ConsumeCouponDto extends BasePageDto implements Serializable {

    @ApiModelProperty("优惠券主键id")
    @NotNull(message = "优惠券主键id不能为空")
    private Long id;

    @ApiModelProperty("优惠券编码")
    private String couponNo;

    @ApiModelProperty("客户名称")
    private String customerName;

    @ApiModelProperty("订单编号")
    private String orderNo;

    @ApiModelProperty("开始使用时间")
    private Long startUseDate;

    @ApiModelProperty("结束使用时间")
    private Long endUseDate;
}