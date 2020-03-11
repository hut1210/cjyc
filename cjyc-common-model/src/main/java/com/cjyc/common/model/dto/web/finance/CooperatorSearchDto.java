package com.cjyc.common.model.dto.web.finance;

import com.cjyc.common.model.dto.BasePageDto;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @Author: Hut
 * @Date: 2020/03/11 10:14
 **/
@Data
public class CooperatorSearchDto extends BasePageDto implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "订单编号")
    private String orderNo;

    @ApiModelProperty(value = "付款开始时间")
    private Long startPayTime;

    @ApiModelProperty(value = "付款结束时间")
    private Long endPayTime;

    @ApiModelProperty(value = "交付开始时间")
    private Long startCompleteTime;

    @ApiModelProperty(value = "交付结束时间")
    private Long endCompleteTime;

    @ApiModelProperty(value = "付款状态")
    private String state;

    @ApiModelProperty(value = "付款操作人")
    private String operator;

    @ApiModelProperty(value = "下单客户电话")
    private String phone;

    @ApiModelProperty(value = "客户名称")
    private String customerName;
}
