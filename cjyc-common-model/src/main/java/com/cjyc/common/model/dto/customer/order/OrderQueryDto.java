package com.cjyc.common.model.dto.customer.order;

import com.cjyc.common.model.dto.BasePageDto;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Data
public class OrderQueryDto extends BasePageDto {
    private static final long serialVersionUID = 399318965603713438L;
    @ApiModelProperty("登录id")
    @NotNull(message = "登录id不能为空" )
    private Long loginId;

    @ApiModelProperty("订单状态 0:待确认,1:运输中,2:已交付,3:全部")
    @Pattern(regexp = "[0|1|2|3]",message = "订单状态只能是0,1,2,3中的一位数")
    private String state;

    @ApiModelProperty("订单号")
    private String orderNo;

    @ApiModelProperty("车系")
    private String model;

    @ApiModelProperty("下单开始时间")
    private Long startDate;

    @ApiModelProperty("下单结束时间")
    private Long endDate;

    @ApiModelProperty("始发地编码")
    private String startCityCode;

    @ApiModelProperty("目的地编码")
    private String endCityCode;
}