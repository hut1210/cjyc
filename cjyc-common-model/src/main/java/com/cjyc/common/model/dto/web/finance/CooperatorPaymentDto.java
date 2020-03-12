package com.cjyc.common.model.dto.web.finance;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @Author: Hut
 * @Date: 2020/03/12 15:05
 **/
@Data
public class CooperatorPaymentDto {

    @ApiModelProperty(value = "订单Id")
    private Long orderId;

    @ApiModelProperty(value = "登录人Id")
    private Long loginId;
}
