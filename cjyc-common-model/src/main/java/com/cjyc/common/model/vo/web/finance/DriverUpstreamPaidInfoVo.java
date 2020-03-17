package com.cjyc.common.model.vo.web.finance;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @Author: RenPL
 * @Date: 2020/3/16 10:08
 */
@Data
public class DriverUpstreamPaidInfoVo implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "车辆编码")
    private String vno;

    @ApiModelProperty(value = "VIN码")
    private String vin;

    @ApiModelProperty(value = "订单编号")
    private String orderNo;

    @ApiModelProperty(value = "付款状态")
    private Integer payState;

    @ApiModelProperty(value = "付款时间")
    private Long payTime;

    @ApiModelProperty(value = "始发城市")
    private String startCity;

    @ApiModelProperty(value = "目的城市")
    private String endCity;

    @ApiModelProperty(value = "下单客户(账号)")
    private String customerAccount;

    @ApiModelProperty(value = "客户名称")
    private String customerName;

    @ApiModelProperty(value = "客户付款方式：0到付（默认），1预付，2账期")
    private Integer payType;

    @ApiModelProperty(value = "交付时间")
    private Long finishTime;

    @ApiModelProperty(value = "订单金额(元)")
    private BigDecimal totalFee;

}
